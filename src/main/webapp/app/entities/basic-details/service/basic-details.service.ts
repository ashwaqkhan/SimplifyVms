import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IBasicDetails, getBasicDetailsIdentifier } from '../basic-details.model';

export type EntityResponseType = HttpResponse<IBasicDetails>;
export type EntityArrayResponseType = HttpResponse<IBasicDetails[]>;

@Injectable({ providedIn: 'root' })
export class BasicDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/basic-details');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/basic-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(basicDetails: IBasicDetails): Observable<EntityResponseType> {
    return this.http.post<IBasicDetails>(this.resourceUrl, basicDetails, { observe: 'response' });
  }

  update(basicDetails: IBasicDetails): Observable<EntityResponseType> {
    return this.http.put<IBasicDetails>(`${this.resourceUrl}/${getBasicDetailsIdentifier(basicDetails) as number}`, basicDetails, {
      observe: 'response',
    });
  }

  partialUpdate(basicDetails: IBasicDetails): Observable<EntityResponseType> {
    return this.http.patch<IBasicDetails>(`${this.resourceUrl}/${getBasicDetailsIdentifier(basicDetails) as number}`, basicDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBasicDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBasicDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBasicDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addBasicDetailsToCollectionIfMissing(
    basicDetailsCollection: IBasicDetails[],
    ...basicDetailsToCheck: (IBasicDetails | null | undefined)[]
  ): IBasicDetails[] {
    const basicDetails: IBasicDetails[] = basicDetailsToCheck.filter(isPresent);
    if (basicDetails.length > 0) {
      const basicDetailsCollectionIdentifiers = basicDetailsCollection.map(
        basicDetailsItem => getBasicDetailsIdentifier(basicDetailsItem)!
      );
      const basicDetailsToAdd = basicDetails.filter(basicDetailsItem => {
        const basicDetailsIdentifier = getBasicDetailsIdentifier(basicDetailsItem);
        if (basicDetailsIdentifier == null || basicDetailsCollectionIdentifiers.includes(basicDetailsIdentifier)) {
          return false;
        }
        basicDetailsCollectionIdentifiers.push(basicDetailsIdentifier);
        return true;
      });
      return [...basicDetailsToAdd, ...basicDetailsCollection];
    }
    return basicDetailsCollection;
  }
}
