import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobDetails, JobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';

@Injectable({ providedIn: 'root' })
export class JobDetailsRoutingResolveService implements Resolve<IJobDetails> {
  constructor(protected service: JobDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jobDetails: HttpResponse<JobDetails>) => {
          if (jobDetails.body) {
            return of(jobDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobDetails());
  }
}
