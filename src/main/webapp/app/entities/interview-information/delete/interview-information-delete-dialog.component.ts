import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewInformation } from '../interview-information.model';
import { InterviewInformationService } from '../service/interview-information.service';

@Component({
  templateUrl: './interview-information-delete-dialog.component.html',
})
export class InterviewInformationDeleteDialogComponent {
  interviewInformation?: IInterviewInformation;

  constructor(protected interviewInformationService: InterviewInformationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewInformationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
