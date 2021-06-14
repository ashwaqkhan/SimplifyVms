import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBasicDetails } from '../basic-details.model';
import { BasicDetailsService } from '../service/basic-details.service';
import { BasicDetailsDeleteDialogComponent } from '../delete/basic-details-delete-dialog.component';

@Component({
  selector: 'jhi-basic-details',
  templateUrl: './basic-details.component.html',
})
export class BasicDetailsComponent implements OnInit {
  basicDetails?: IBasicDetails[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected basicDetailsService: BasicDetailsService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.basicDetailsService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IBasicDetails[]>) => {
            this.isLoading = false;
            this.basicDetails = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.basicDetailsService.query().subscribe(
      (res: HttpResponse<IBasicDetails[]>) => {
        this.isLoading = false;
        this.basicDetails = res.body ?? [];
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

  trackId(index: number, item: IBasicDetails): number {
    return item.id!;
  }

  delete(basicDetails: IBasicDetails): void {
    const modalRef = this.modalService.open(BasicDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.basicDetails = basicDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
