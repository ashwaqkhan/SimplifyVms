import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterviewInformation, InterviewInformation } from '../interview-information.model';
import { InterviewInformationService } from '../service/interview-information.service';

@Injectable({ providedIn: 'root' })
export class InterviewInformationRoutingResolveService implements Resolve<IInterviewInformation> {
  constructor(protected service: InterviewInformationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterviewInformation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((interviewInformation: HttpResponse<InterviewInformation>) => {
          if (interviewInformation.body) {
            return of(interviewInformation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InterviewInformation());
  }
}
