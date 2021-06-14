import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApply, Apply } from '../apply.model';
import { ApplyService } from '../service/apply.service';

@Injectable({ providedIn: 'root' })
export class ApplyRoutingResolveService implements Resolve<IApply> {
  constructor(protected service: ApplyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApply> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apply: HttpResponse<Apply>) => {
          if (apply.body) {
            return of(apply.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Apply());
  }
}
