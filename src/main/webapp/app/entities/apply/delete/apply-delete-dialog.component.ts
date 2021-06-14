import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApply } from '../apply.model';
import { ApplyService } from '../service/apply.service';

@Component({
  templateUrl: './apply-delete-dialog.component.html',
})
export class ApplyDeleteDialogComponent {
  apply?: IApply;

  constructor(protected applyService: ApplyService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
