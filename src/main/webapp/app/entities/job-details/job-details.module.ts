import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { JobDetailsComponent } from './list/job-details.component';
import { JobDetailsDetailComponent } from './detail/job-details-detail.component';
import { JobDetailsUpdateComponent } from './update/job-details-update.component';
import { JobDetailsDeleteDialogComponent } from './delete/job-details-delete-dialog.component';
import { JobDetailsRoutingModule } from './route/job-details-routing.module';

@NgModule({
  imports: [SharedModule, JobDetailsRoutingModule],
  declarations: [JobDetailsComponent, JobDetailsDetailComponent, JobDetailsUpdateComponent, JobDetailsDeleteDialogComponent],
  entryComponents: [JobDetailsDeleteDialogComponent],
})
export class JobDetailsModule {}
