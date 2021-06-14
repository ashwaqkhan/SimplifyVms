jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBasicDetails, BasicDetails } from '../basic-details.model';
import { BasicDetailsService } from '../service/basic-details.service';

import { BasicDetailsRoutingResolveService } from './basic-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('BasicDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BasicDetailsRoutingResolveService;
    let service: BasicDetailsService;
    let resultBasicDetails: IBasicDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BasicDetailsRoutingResolveService);
      service = TestBed.inject(BasicDetailsService);
      resultBasicDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IBasicDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBasicDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBasicDetails).toEqual({ id: 123 });
      });

      it('should return new IBasicDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBasicDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBasicDetails).toEqual(new BasicDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBasicDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBasicDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
