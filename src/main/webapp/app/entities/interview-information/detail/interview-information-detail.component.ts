import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterviewInformation } from '../interview-information.model';

@Component({
  selector: 'jhi-interview-information-detail',
  templateUrl: './interview-information-detail.component.html',
})
export class InterviewInformationDetailComponent implements OnInit {
  interviewInformation: IInterviewInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewInformation }) => {
      this.interviewInformation = interviewInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
