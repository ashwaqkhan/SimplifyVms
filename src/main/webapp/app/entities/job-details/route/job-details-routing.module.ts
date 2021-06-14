import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobDetailsComponent } from '../list/job-details.component';
import { JobDetailsDetailComponent } from '../detail/job-details-detail.component';
import { JobDetailsUpdateComponent } from '../update/job-details-update.component';
import { JobDetailsRoutingResolveService } from './job-details-routing-resolve.service';

const jobDetailsRoute: Routes = [
  {
    path: '',
    component: JobDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobDetailsDetailComponent,
    resolve: {
      jobDetails: JobDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobDetailsUpdateComponent,
    resolve: {
      jobDetails: JobDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobDetailsUpdateComponent,
    resolve: {
      jobDetails: JobDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobDetailsRoute)],
  exports: [RouterModule],
})
export class JobDetailsRoutingModule {}
