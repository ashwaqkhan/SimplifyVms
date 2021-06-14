import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplyComponent } from '../list/apply.component';
import { ApplyDetailComponent } from '../detail/apply-detail.component';
import { ApplyUpdateComponent } from '../update/apply-update.component';
import { ApplyRoutingResolveService } from './apply-routing-resolve.service';

const applyRoute: Routes = [
  {
    path: '',
    component: ApplyComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplyDetailComponent,
    resolve: {
      apply: ApplyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplyUpdateComponent,
    resolve: {
      apply: ApplyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplyUpdateComponent,
    resolve: {
      apply: ApplyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applyRoute)],
  exports: [RouterModule],
})
export class ApplyRoutingModule {}
