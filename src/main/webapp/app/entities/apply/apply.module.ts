import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApplyComponent } from './list/apply.component';
import { ApplyDetailComponent } from './detail/apply-detail.component';
import { ApplyUpdateComponent } from './update/apply-update.component';
import { ApplyDeleteDialogComponent } from './delete/apply-delete-dialog.component';
import { ApplyRoutingModule } from './route/apply-routing.module';

@NgModule({
  imports: [SharedModule, ApplyRoutingModule],
  declarations: [ApplyComponent, ApplyDetailComponent, ApplyUpdateComponent, ApplyDeleteDialogComponent],
  entryComponents: [ApplyDeleteDialogComponent],
})
export class ApplyModule {}
