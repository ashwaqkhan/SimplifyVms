import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApply } from '../apply.model';
import { ApplyService } from '../service/apply.service';
import { ApplyDeleteDialogComponent } from '../delete/apply-delete-dialog.component';

@Component({
  selector: 'jhi-apply',
  templateUrl: './apply.component.html',
})
export class ApplyComponent implements OnInit {
  applies?: IApply[];
  isLoading = false;
  currentSearch: string;

  constructor(protected applyService: ApplyService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.applyService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IApply[]>) => {
            this.isLoading = false;
            this.applies = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.applyService.query().subscribe(
      (res: HttpResponse<IApply[]>) => {
        this.isLoading = false;
        this.applies = res.body ?? [];
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

  trackId(index: number, item: IApply): number {
    return item.id!;
  }

  delete(apply: IApply): void {
    const modalRef = this.modalService.open(ApplyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apply = apply;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
