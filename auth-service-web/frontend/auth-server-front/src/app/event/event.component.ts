import { Component, OnInit } from "@angular/core";
import { PageEvent } from "@angular/material/paginator";
import { animateElementExpanding } from "src/animate/animate-util";
import {
  EventHandling,
  EventHandlingStatus,
  EventHandlingType,
  HandleResult,
} from "src/models/event";
import { PagingController } from "src/models/paging";
import { EventHandlingService } from "../event-handling.service";

@Component({
  selector: "app-event",
  templateUrl: "./event.component.html",
  styleUrls: ["./event.component.css"],
  animations: [animateElementExpanding()],
})
export class EventComponent implements OnInit {
  readonly REGISTRATION_EVENT = EventHandlingType.REGISTRATION;
  readonly NO_NEED_TO_HANDLE_STATUS = EventHandlingStatus.NO_NEED_TO_HANDLE;
  readonly TO_BE_HANDLED_STATUS = EventHandlingStatus.TO_BE_HANDLED;
  readonly HANDLED_STATUS = EventHandlingStatus.HANDLED;
  readonly COLUMNS_TO_BE_DISPLAYED = [
    "id",
    "type",
    "status",
    "description",
    "handlerId",
    "handleTime",
  ];
  readonly ACCEPT_RESULT = HandleResult.ACCEPT;
  readonly REJECT_RESULT = HandleResult.REJECT;

  events: EventHandling[] = [];
  pagingController: PagingController = new PagingController();
  searchStatus: number;
  searchType: number;
  expandedElement: EventHandling;

  constructor(private eventHandlingService: EventHandlingService) {}

  ngOnInit() {
    this.fetchEventList();
  }

  fetchEventList() {
    this.eventHandlingService
      .findByPage({
        type: this.searchType,
        status: this.searchStatus,
        pagingVo: this.pagingController.paging,
      })
      .subscribe({
        next: (resp) => {
          this.expandedElement = null;
          this.events = resp.data.list;
          this.pagingController.updatePages(resp.data.pagingVo.total);
        },
      });
  }

  handle(e: PageEvent): void {
    this.pagingController.handle(e);
    this.fetchEventList();
  }

  idEquals(tl: EventHandling, tr: EventHandling): boolean {
    if (tl == null || tr == null) return false;
    return tl.id === tr.id;
  }

  setExpandedElement(row: EventHandling) {
    if (this.idEquals(row, this.expandedElement)) {
      this.expandedElement = null;
      return;
    }
    this.expandedElement = this.copy(row);
  }

  copy(obj: EventHandling): EventHandling {
    if (obj == null) return null;
    return { ...obj };
  }

  handleEvent(e: EventHandling, res: HandleResult): void {
    this.eventHandlingService
      .handle({
        id: e.id,
        result: res,
      })
      .subscribe({
        complete: () => {
          this.expandedElement = null;
          this.fetchEventList();
        },
      });
  }
}
