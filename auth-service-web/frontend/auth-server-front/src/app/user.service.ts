import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { Resp } from "src/models/resp";
import {
  ChangeUserRoleReqVo,
  FetchUserInfoParam,
  FetchUserInfoResp as FetchUserInfoResp,
  UserInfo,
} from "src/models/user-info";
import { NavigationService, NavType } from "./navigation.service";
import { NotificationService } from "./notification.service";
import { buildApiPath } from "./util/api-util";

const headers = {
  headers: new HttpHeaders({
    "Content-Type": "application/json",
  }),
  withCredentials: true,
};

@Injectable({
  providedIn: "root",
})
export class UserService {
  private userInfo: UserInfo = null;
  private roleSubject = new Subject<string>();
  private isLoggedInSubject = new Subject<boolean>();

  roleObservable: Observable<string> = this.roleSubject.asObservable();
  isLoggedInObservable: Observable<boolean> =
    this.isLoggedInSubject.asObservable();

  constructor(
    private http: HttpClient,
    private nav: NavigationService,
    private notifi: NotificationService
  ) {}

  /**
   * Attempt to signin
   * @param username
   * @param password
   */
  public login(username: string, password: string): Observable<Resp<any>> {
    let formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);

    return this.http.post<Resp<any>>(buildApiPath("/login"), formData, {
      withCredentials: true,
    });
  }

  /**
   * Logout current user
   */
  public logout(): Observable<any> {
    this.setLogout();
    return this.http.get<void>(buildApiPath("/logout"), {
      withCredentials: true,
    });
  }

  /**
   * Set user being logged out
   */
  public setLogout(): void {
    this.userInfo = null;
    this.notifyLoginStatus(false);
    this.nav.navigateTo(NavType.LOGIN_PAGE);
  }

  /**
   * Add user, only admin is allowed to add user
   * @param username
   * @param password
   * @returns
   */
  public addUser(
    username: string,
    password: string,
    userRole: string
  ): Observable<Resp<any>> {
    return this.http.post<Resp<any>>(
      buildApiPath("/user/register"),
      { username, password, userRole },
      headers
    );
  }

  /**
   * Register user
   * @param username
   * @param password
   * @returns
   */
  public register(username: string, password: string): Observable<Resp<any>> {
    return this.http.post<Resp<any>>(
      buildApiPath("/user/register/request"),
      { username, password },
      headers
    );
  }

  /**
   * Fetch user info
   */
  public fetchUserInfo(): void {
    this.http
      .get<Resp<UserInfo>>(buildApiPath("/user/info"), {
        withCredentials: true,
      })
      .subscribe({
        next: (resp) => {
          if (resp.data != null) {
            this.userInfo = resp.data;
            this.notifyRole(this.userInfo.role);
            this.notifyLoginStatus(true);
          } else {
            this.notifi.toast("Please login first");
            this.nav.navigateTo(NavType.LOGIN_PAGE);
            this.notifyLoginStatus(false);
          }
        },
      });
  }

  /** Notify the role of the user via observable */
  private notifyRole(role: string): void {
    this.roleSubject.next(role);
  }

  /** Notify the login status of the user via observable */
  private notifyLoginStatus(isLoggedIn: boolean): void {
    this.isLoggedInSubject.next(isLoggedIn);
  }

  /**
   * Get user info that is previously fetched
   */
  public getUserInfo(): UserInfo {
    return this.userInfo;
  }

  /**
   * Check if the service has the user info already
   */
  public hasUserInfo(): boolean {
    return this.userInfo != null;
  }

  /**
   * Fetch list of user infos (only admin is allowed)
   */
  public fetchUserList(
    param: FetchUserInfoParam
  ): Observable<Resp<FetchUserInfoResp>> {
    return this.http.post<Resp<FetchUserInfoResp>>(
      buildApiPath("/user/list"),
      param,
      headers
    );
  }

  /**
   * Disable user by id (only admin is allowed)
   * @param id
   */
  public disableUserById(id: number): Observable<Resp<any>> {
    return this.http.post<Resp<any>>(
      buildApiPath("/user/disable"),
      {
        id: id,
      },
      headers
    );
  }

  /**
   * Enable user by id (only admin is allowed)
   * @param id
   */
  public enableUserById(id: number): Observable<Resp<any>> {
    return this.http.post<Resp<any>>(
      buildApiPath("/user/enable"),
      {
        id: id,
      },
      headers
    );
  }

  /**
   * Change user's role (only admin is allowed)
   */
  public changeUserRole(param: ChangeUserRoleReqVo): Observable<Resp<void>> {
    return this.http.post<Resp<void>>(
      buildApiPath("/user/role/change"),
      param,
      headers
    );
  }
}
