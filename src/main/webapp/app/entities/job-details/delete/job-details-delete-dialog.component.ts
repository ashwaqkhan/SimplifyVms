import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';

@Component({
  templateUrl: './job-details-delete-dialog.component.html',
})
export class JobDetailsDeleteDialogComponent {
  jobDetails?: IJobDetails;

  constructor(protected jobDetailsService: JobDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
