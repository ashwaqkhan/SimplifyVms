import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IApply, getApplyIdentifier } from '../apply.model';

export type EntityResponseType = HttpResponse<IApply>;
export type EntityArrayResponseType = HttpResponse<IApply[]>;

@Injectable({ providedIn: 'root' })
export class ApplyService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/applies');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/applies');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(apply: IApply): Observable<EntityResponseType> {
    return this.http.post<IApply>(this.resourceUrl, apply, { observe: 'response' });
  }

  update(apply: IApply): Observable<EntityResponseType> {
    return this.http.put<IApply>(`${this.resourceUrl}/${getApplyIdentifier(apply) as number}`, apply, { observe: 'response' });
  }

  partialUpdate(apply: IApply): Observable<EntityResponseType> {
    return this.http.patch<IApply>(`${this.resourceUrl}/${getApplyIdentifier(apply) as number}`, apply, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApply>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApply[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApply[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addApplyToCollectionIfMissing(applyCollection: IApply[], ...appliesToCheck: (IApply | null | undefined)[]): IApply[] {
    const applies: IApply[] = appliesToCheck.filter(isPresent);
    if (applies.length > 0) {
      const applyCollectionIdentifiers = applyCollection.map(applyItem => getApplyIdentifier(applyItem)!);
      const appliesToAdd = applies.filter(applyItem => {
        const applyIdentifier = getApplyIdentifier(applyItem);
        if (applyIdentifier == null || applyCollectionIdentifiers.includes(applyIdentifier)) {
          return false;
        }
        applyCollectionIdentifiers.push(applyIdentifier);
        return true;
      });
      return [...appliesToAdd, ...applyCollection];
    }
    return applyCollection;
  }
}
