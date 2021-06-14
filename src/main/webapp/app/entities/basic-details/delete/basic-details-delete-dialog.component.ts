import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBasicDetails } from '../basic-details.model';
import { BasicDetailsService } from '../service/basic-details.service';

@Component({
  templateUrl: './basic-details-delete-dialog.component.html',
})
export class BasicDetailsDeleteDialogComponent {
  basicDetails?: IBasicDetails;

  constructor(protected basicDetailsService: BasicDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.basicDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
