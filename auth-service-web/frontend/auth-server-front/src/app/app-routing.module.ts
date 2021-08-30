import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AccessLogComponent } from "./access-log/access-log.component";
import { ChangePasswordComponent } from "./change-password/change-password.component";
import { EventComponent } from "./event/event.component";
import { LoginComponent } from "./login/login.component";
import { ManageTasksComponent } from "./manage-tasks/manage-tasks.component";
import { ManagerUserComponent } from "./manager-user/manager-user.component";
import { OperateHistoryComponent } from "./operate-history/operate-history.component";
import { RegisterComponent } from "./register/register.component";

const routes: Routes = [
  {
    path: "login-page",
    component: LoginComponent,
  },
  {
    path: "manage-user",
    component: ManagerUserComponent,
  },
  {
    path: "access-log",
    component: AccessLogComponent,
  },
  {
    path: "change-password",
    component: ChangePasswordComponent,
  },
  {
    path: "operate-history",
    component: OperateHistoryComponent,
  },
  {
    path: "manage-task",
    component: ManageTasksComponent,
  },
  {
    path: "event",
    component: EventComponent,
  },
  {
    path: "register",
    component: RegisterComponent,
  },
  { path: "**", redirectTo: "/login-page" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
