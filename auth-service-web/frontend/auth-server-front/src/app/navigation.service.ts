import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root",
})
export class NavigationService {
  constructor(private router: Router) {}

  /** Navigate to using Router*/
  public navigateTo(nt: NavType): void {
    this.router.navigate([nt]);
  }
}

/** Navigation Type (Where we are navigating to) */
export enum NavType {
  LOGIN_PAGE = "login-page",
  MANAGE_USER = "manage-user",
  ACCESS_LOG = "access-log",
  CHANGE_PASSWORD = "change-password",
  OPERATE_HISTORY = "operate-history",
  MANAGE_TASKS = "manage-task",
}
