jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IJobDetails, JobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';

import { JobDetailsRoutingResolveService } from './job-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('JobDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: JobDetailsRoutingResolveService;
    let service: JobDetailsService;
    let resultJobDetails: IJobDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(JobDetailsRoutingResolveService);
      service = TestBed.inject(JobDetailsService);
      resultJobDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IJobDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJobDetails).toEqual({ id: 123 });
      });

      it('should return new IJobDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultJobDetails).toEqual(new JobDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJobDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
