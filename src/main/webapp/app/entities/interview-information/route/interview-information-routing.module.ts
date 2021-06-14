import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterviewInformationComponent } from '../list/interview-information.component';
import { InterviewInformationDetailComponent } from '../detail/interview-information-detail.component';
import { InterviewInformationUpdateComponent } from '../update/interview-information-update.component';
import { InterviewInformationRoutingResolveService } from './interview-information-routing-resolve.service';

const interviewInformationRoute: Routes = [
  {
    path: '',
    component: InterviewInformationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterviewInformationDetailComponent,
    resolve: {
      interviewInformation: InterviewInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterviewInformationUpdateComponent,
    resolve: {
      interviewInformation: InterviewInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterviewInformationUpdateComponent,
    resolve: {
      interviewInformation: InterviewInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interviewInformationRoute)],
  exports: [RouterModule],
})
export class InterviewInformationRoutingModule {}
