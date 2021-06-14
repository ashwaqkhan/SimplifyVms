import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IJobDetails, getJobDetailsIdentifier } from '../job-details.model';

export type EntityResponseType = HttpResponse<IJobDetails>;
export type EntityArrayResponseType = HttpResponse<IJobDetails[]>;

@Injectable({ providedIn: 'root' })
export class JobDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/job-details');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/job-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(jobDetails: IJobDetails): Observable<EntityResponseType> {
    return this.http.post<IJobDetails>(this.resourceUrl, jobDetails, { observe: 'response' });
  }

  update(jobDetails: IJobDetails): Observable<EntityResponseType> {
    return this.http.put<IJobDetails>(`${this.resourceUrl}/${getJobDetailsIdentifier(jobDetails) as number}`, jobDetails, {
      observe: 'response',
    });
  }

  partialUpdate(jobDetails: IJobDetails): Observable<EntityResponseType> {
    return this.http.patch<IJobDetails>(`${this.resourceUrl}/${getJobDetailsIdentifier(jobDetails) as number}`, jobDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addJobDetailsToCollectionIfMissing(
    jobDetailsCollection: IJobDetails[],
    ...jobDetailsToCheck: (IJobDetails | null | undefined)[]
  ): IJobDetails[] {
    const jobDetails: IJobDetails[] = jobDetailsToCheck.filter(isPresent);
    if (jobDetails.length > 0) {
      const jobDetailsCollectionIdentifiers = jobDetailsCollection.map(jobDetailsItem => getJobDetailsIdentifier(jobDetailsItem)!);
      const jobDetailsToAdd = jobDetails.filter(jobDetailsItem => {
        const jobDetailsIdentifier = getJobDetailsIdentifier(jobDetailsItem);
        if (jobDetailsIdentifier == null || jobDetailsCollectionIdentifiers.includes(jobDetailsIdentifier)) {
          return false;
        }
        jobDetailsCollectionIdentifiers.push(jobDetailsIdentifier);
        return true;
      });
      return [...jobDetailsToAdd, ...jobDetailsCollection];
    }
    return jobDetailsCollection;
  }
}
