import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BasicDetailsComponent } from '../list/basic-details.component';
import { BasicDetailsDetailComponent } from '../detail/basic-details-detail.component';
import { BasicDetailsUpdateComponent } from '../update/basic-details-update.component';
import { BasicDetailsRoutingResolveService } from './basic-details-routing-resolve.service';

const basicDetailsRoute: Routes = [
  {
    path: '',
    component: BasicDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BasicDetailsDetailComponent,
    resolve: {
      basicDetails: BasicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BasicDetailsUpdateComponent,
    resolve: {
      basicDetails: BasicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BasicDetailsUpdateComponent,
    resolve: {
      basicDetails: BasicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(basicDetailsRoute)],
  exports: [RouterModule],
})
export class BasicDetailsRoutingModule {}
