import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'basic-details',
        data: { pageTitle: 'simplifyVmsApp.basicDetails.home.title' },
        loadChildren: () => import('./basic-details/basic-details.module').then(m => m.BasicDetailsModule),
      },
      {
        path: 'job-details',
        data: { pageTitle: 'simplifyVmsApp.jobDetails.home.title' },
        loadChildren: () => import('./job-details/job-details.module').then(m => m.JobDetailsModule),
      },
      {
        path: 'interview-information',
        data: { pageTitle: 'simplifyVmsApp.interviewInformation.home.title' },
        loadChildren: () => import('./interview-information/interview-information.module').then(m => m.InterviewInformationModule),
      },
      {
        path: 'apply',
        data: { pageTitle: 'simplifyVmsApp.apply.home.title' },
        loadChildren: () => import('./apply/apply.module').then(m => m.ApplyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
