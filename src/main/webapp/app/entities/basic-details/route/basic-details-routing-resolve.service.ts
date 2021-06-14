import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBasicDetails, BasicDetails } from '../basic-details.model';
import { BasicDetailsService } from '../service/basic-details.service';

@Injectable({ providedIn: 'root' })
export class BasicDetailsRoutingResolveService implements Resolve<IBasicDetails> {
  constructor(protected service: BasicDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBasicDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((basicDetails: HttpResponse<BasicDetails>) => {
          if (basicDetails.body) {
            return of(basicDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BasicDetails());
  }
}
