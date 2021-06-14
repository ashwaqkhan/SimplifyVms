import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBasicDetails } from '../basic-details.model';

@Component({
  selector: 'jhi-basic-details-detail',
  templateUrl: './basic-details-detail.component.html',
})
export class BasicDetailsDetailComponent implements OnInit {
  basicDetails: IBasicDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ basicDetails }) => {
      this.basicDetails = basicDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
