jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IApply, Apply } from '../apply.model';
import { ApplyService } from '../service/apply.service';

import { ApplyRoutingResolveService } from './apply-routing-resolve.service';

describe('Service Tests', () => {
  describe('Apply routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ApplyRoutingResolveService;
    let service: ApplyService;
    let resultApply: IApply | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ApplyRoutingResolveService);
      service = TestBed.inject(ApplyService);
      resultApply = undefined;
    });

    describe('resolve', () => {
      it('should return IApply returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApply = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApply).toEqual({ id: 123 });
      });

      it('should return new IApply if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApply = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultApply).toEqual(new Apply());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApply = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApply).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
