import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import {
  APP_BASE_HREF,
  HashLocationStrategy,
  LocationStrategy,
} from "@angular/common";
import { LoginComponent } from "./login/login.component";
import { ManagerUserComponent } from "./manager-user/manager-user.component";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { NavComponent } from "./nav/nav.component";
import { AccessLogComponent } from "./access-log/access-log.component";
import { RespInterceptor } from "./interceptors/resp-interceptor";
import { ErrorInterceptor } from "./interceptors/error-interceptor";
import { ChangePasswordComponent } from "./change-password/change-password.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from "@angular/material/table";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { ConfirmDialogComponent } from "./dialog/confirm/confirm-dialog.component";
import { OperateHistoryComponent } from "./operate-history/operate-history.component";
import { ManageTasksComponent } from "./manage-tasks/manage-tasks.component";
import { RegisterComponent } from "./register/register.component";
import { EventComponent } from "./event/event.component";
import { TaskHistoryComponent } from "./task-history/task-history.component";

@NgModule({
  exports: [],
  declarations: [
    AppComponent,
    LoginComponent,
    ManagerUserComponent,
    NavComponent,
    AccessLogComponent,
    ChangePasswordComponent,
    ConfirmDialogComponent,
    OperateHistoryComponent,
    ManageTasksComponent,
    RegisterComponent,
    EventComponent,
    TaskHistoryComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatTableModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
  ],
  entryComponents: [ConfirmDialogComponent],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: APP_BASE_HREF, useValue: "/" },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: RespInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
