jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInterviewInformation, InterviewInformation } from '../interview-information.model';
import { InterviewInformationService } from '../service/interview-information.service';

import { InterviewInformationRoutingResolveService } from './interview-information-routing-resolve.service';

describe('Service Tests', () => {
  describe('InterviewInformation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InterviewInformationRoutingResolveService;
    let service: InterviewInformationService;
    let resultInterviewInformation: IInterviewInformation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InterviewInformationRoutingResolveService);
      service = TestBed.inject(InterviewInformationService);
      resultInterviewInformation = undefined;
    });

    describe('resolve', () => {
      it('should return IInterviewInformation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterviewInformation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInterviewInformation).toEqual({ id: 123 });
      });

      it('should return new IInterviewInformation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterviewInformation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInterviewInformation).toEqual(new InterviewInformation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInterviewInformation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInterviewInformation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
