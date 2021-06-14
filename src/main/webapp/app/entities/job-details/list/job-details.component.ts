import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';
import { JobDetailsDeleteDialogComponent } from '../delete/job-details-delete-dialog.component';

@Component({
  selector: 'jhi-job-details',
  templateUrl: './job-details.component.html',
})
export class JobDetailsComponent implements OnInit {
  jobDetails?: IJobDetails[];
  isLoading = false;
  currentSearch: string;

  constructor(protected jobDetailsService: JobDetailsService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.jobDetailsService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IJobDetails[]>) => {
            this.isLoading = false;
            this.jobDetails = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.jobDetailsService.query().subscribe(
      (res: HttpResponse<IJobDetails[]>) => {
        this.isLoading = false;
        this.jobDetails = res.body ?? [];
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

  trackId(index: number, item: IJobDetails): number {
    return item.id!;
  }

  delete(jobDetails: IJobDetails): void {
    const modalRef = this.modalService.open(JobDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobDetails = jobDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
