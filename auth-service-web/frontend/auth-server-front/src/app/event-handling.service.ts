import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {
  FindEventHandlingByPageReqVo,
  FindEventHandlingByPageRespVo,
  HandleEventReqVo,
} from "src/models/event";
import { Resp } from "src/models/resp";
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
export class EventHandlingService {
  constructor(private http: HttpClient) {}

  /**
   * Find records in pages
   */
  public findByPage(
    param: FindEventHandlingByPageReqVo
  ): Observable<Resp<FindEventHandlingByPageRespVo>> {
    return this.http.post<Resp<FindEventHandlingByPageRespVo>>(
      buildApiPath("/event/list"),
      param,
      headers
    );
  }

  /**
   * Handle an event, only admin can do so
   */
  public handle(param: HandleEventReqVo): Observable<Resp<void>> {
    return this.http.post<Resp<void>>(
      buildApiPath("/event/handle"),
      param,
      headers
    );
  }
}
