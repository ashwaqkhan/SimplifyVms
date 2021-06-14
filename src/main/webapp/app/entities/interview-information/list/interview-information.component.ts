import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewInformation } from '../interview-information.model';
import { InterviewInformationService } from '../service/interview-information.service';
import { InterviewInformationDeleteDialogComponent } from '../delete/interview-information-delete-dialog.component';

@Component({
  selector: 'jhi-interview-information',
  templateUrl: './interview-information.component.html',
})
export class InterviewInformationComponent implements OnInit {
  interviewInformations?: IInterviewInformation[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected interviewInformationService: InterviewInformationService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.interviewInformationService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IInterviewInformation[]>) => {
            this.isLoading = false;
            this.interviewInformations = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.interviewInformationService.query().subscribe(
      (res: HttpResponse<IInterviewInformation[]>) => {
        this.isLoading = false;
        this.interviewInformations = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInterviewInformation): number {
    return item.id!;
  }

  delete(interviewInformation: IInterviewInformation): void {
    const modalRef = this.modalService.open(InterviewInformationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interviewInformation = interviewInformation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
