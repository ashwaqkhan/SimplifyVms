import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InterviewInformationComponent } from './list/interview-information.component';
import { InterviewInformationDetailComponent } from './detail/interview-information-detail.component';
import { InterviewInformationUpdateComponent } from './update/interview-information-update.component';
import { InterviewInformationDeleteDialogComponent } from './delete/interview-information-delete-dialog.component';
import { InterviewInformationRoutingModule } from './route/interview-information-routing.module';

@NgModule({
  imports: [SharedModule, InterviewInformationRoutingModule],
  declarations: [
    InterviewInformationComponent,
    InterviewInformationDetailComponent,
    InterviewInformationUpdateComponent,
    InterviewInformationDeleteDialogComponent,
  ],
  entryComponents: [InterviewInformationDeleteDialogComponent],
})
export class InterviewInformationModule {}
