import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IManualBusinessPlan } from '../manual-business-plan.model';
import { ManualBusinessPlanService } from '../service/manual-business-plan.service';

import { ManualBusinessPlanRoutingResolveService } from './manual-business-plan-routing-resolve.service';

describe('ManualBusinessPlan routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ManualBusinessPlanRoutingResolveService;
  let service: ManualBusinessPlanService;
  let resultManualBusinessPlan: IManualBusinessPlan | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ManualBusinessPlanRoutingResolveService);
    service = TestBed.inject(ManualBusinessPlanService);
    resultManualBusinessPlan = undefined;
  });

  describe('resolve', () => {
    it('should return IManualBusinessPlan returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualBusinessPlan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultManualBusinessPlan).toEqual({ id: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualBusinessPlan = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultManualBusinessPlan).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IManualBusinessPlan>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultManualBusinessPlan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultManualBusinessPlan).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
