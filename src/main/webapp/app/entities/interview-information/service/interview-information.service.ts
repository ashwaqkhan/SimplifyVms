import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IInterviewInformation, getInterviewInformationIdentifier } from '../interview-information.model';

export type EntityResponseType = HttpResponse<IInterviewInformation>;
export type EntityArrayResponseType = HttpResponse<IInterviewInformation[]>;

@Injectable({ providedIn: 'root' })
export class InterviewInformationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/interview-informations');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/interview-informations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(interviewInformation: IInterviewInformation): Observable<EntityResponseType> {
    return this.http.post<IInterviewInformation>(this.resourceUrl, interviewInformation, { observe: 'response' });
  }

  update(interviewInformation: IInterviewInformation): Observable<EntityResponseType> {
    return this.http.put<IInterviewInformation>(
      `${this.resourceUrl}/${getInterviewInformationIdentifier(interviewInformation) as number}`,
      interviewInformation,
      { observe: 'response' }
    );
  }

  partialUpdate(interviewInformation: IInterviewInformation): Observable<EntityResponseType> {
    return this.http.patch<IInterviewInformation>(
      `${this.resourceUrl}/${getInterviewInformationIdentifier(interviewInformation) as number}`,
      interviewInformation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterviewInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewInformation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewInformation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addInterviewInformationToCollectionIfMissing(
    interviewInformationCollection: IInterviewInformation[],
    ...interviewInformationsToCheck: (IInterviewInformation | null | undefined)[]
  ): IInterviewInformation[] {
    const interviewInformations: IInterviewInformation[] = interviewInformationsToCheck.filter(isPresent);
    if (interviewInformations.length > 0) {
      const interviewInformationCollectionIdentifiers = interviewInformationCollection.map(
        interviewInformationItem => getInterviewInformationIdentifier(interviewInformationItem)!
      );
      const interviewInformationsToAdd = interviewInformations.filter(interviewInformationItem => {
        const interviewInformationIdentifier = getInterviewInformationIdentifier(interviewInformationItem);
        if (interviewInformationIdentifier == null || interviewInformationCollectionIdentifiers.includes(interviewInformationIdentifier)) {
          return false;
        }
        interviewInformationCollectionIdentifiers.push(interviewInformationIdentifier);
        return true;
      });
      return [...interviewInformationsToAdd, ...interviewInformationCollection];
    }
    return interviewInformationCollection;
  }
}
