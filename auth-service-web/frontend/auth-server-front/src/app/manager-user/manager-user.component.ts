import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import { Component, OnInit } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { animateElementExpanding } from "src/animate/animate-util";
import { PagingConst, PagingController } from "src/models/paging";
import {
  emptyFetchUserInfoParam,
  FetchUserInfoParam,
  UpdateUserInfoParam,
  UserInfo,
  UserIsDisabledEnum,
  UserRoleEnum,
  USER_IS_DISABLED_OPTIONS,
  USER_ROLE_OPTIONS,
} from "src/models/user-info";
import { NotificationService } from "../notification.service";
import { UserService } from "../user.service";

@Component({
  selector: "app-manager-user",
  templateUrl: "./manager-user.component.html",
  styleUrls: ["./manager-user.component.css"],
  animations: [animateElementExpanding()],
})
export class ManagerUserComponent implements OnInit {
  readonly USER_IS_NORMAL = UserIsDisabledEnum.NORMAL;
  readonly USER_IS_DISABLED = UserIsDisabledEnum.IS_DISABLED;
  readonly COLUMNS_TO_BE_DISPLAYED = [
    "id",
    "name",
    "role",
    "status",
    "createBy",
    "createTime",
    "updateBy",
    "updateTime",
  ];
  readonly USER_IS_DISABLED_OPTS = USER_IS_DISABLED_OPTIONS;
  readonly USER_ROLE_OPTS = USER_ROLE_OPTIONS;

  usernameToBeAdded: string = null;
  passswordToBeAdded: string = null;
  userRoleOfAddedUser: string = UserRoleEnum.GUEST;
  userInfoList: UserInfo[] = [];
  addUserPanelDisplayed: boolean = false;
  expandedElement: UserInfo = null;
  searchParam: FetchUserInfoParam = emptyFetchUserInfoParam();
  pagingController: PagingController = new PagingController();

  constructor(
    private userService: UserService,
    private notifi: NotificationService
  ) {}

  ngOnInit() {
    this.fetchUserInfoList();
  }

  /**
   * add user (only admin is allowed)
   */
  addUser(): void {
    if (!this.usernameToBeAdded || !this.passswordToBeAdded) {
      this.notifi.toast("Please enter username and password");
      return;
    }
    this.userService
      .addUser(
        this.usernameToBeAdded,
        this.passswordToBeAdded,
        this.userRoleOfAddedUser
      )
      .subscribe({
        next: (resp) => {
          console.log("Successfully added guest:", this.usernameToBeAdded);
        },
        complete: () => {
          this.userRoleOfAddedUser = UserRoleEnum.GUEST;
          this.usernameToBeAdded = null;
          this.passswordToBeAdded = null;
          this.addUserPanelDisplayed = false;
          this.fetchUserInfoList();
        },
      });
  }

  fetchUserInfoList(): void {
    this.searchParam.pagingVo = this.pagingController.paging;
    this.userService.fetchUserList(this.searchParam).subscribe({
      next: (resp) => {
        this.userInfoList = resp.data.list;
        this.pagingController.updatePages(resp.data.pagingVo.total);
      },
    });
  }

  searchNameInputKeyPressed(event: any): void {
    if (event.key === "Enter") {
      this.fetchUserInfoList();
    }
  }

  resetSearchParam(): void {
    this.searchParam.isDisabled = null;
    this.searchParam.role = null;
  }

  handle(e: PageEvent): void {
    this.pagingController.handle(e);
    this.fetchUserInfoList();
  }

  /**
   * Update user info (only admin is allowed)
   */
  updateUserInfo(): void {
    this.userService
      .updateUserInfo({
        id: this.expandedElement.id,
        role: this.expandedElement.role,
        isDisabled: this.expandedElement.isDisabled,
      })
      .subscribe({
        complete: () => {
          this.fetchUserInfoList();
        },
      });
  }
}
