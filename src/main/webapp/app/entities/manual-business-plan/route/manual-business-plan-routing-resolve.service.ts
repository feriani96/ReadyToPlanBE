import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManualBusinessPlan } from '../manual-business-plan.model';
import { ManualBusinessPlanService } from '../service/manual-business-plan.service';

@Injectable({ providedIn: 'root' })
export class ManualBusinessPlanRoutingResolveService implements Resolve<IManualBusinessPlan | null> {
  constructor(protected service: ManualBusinessPlanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManualBusinessPlan | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manualBusinessPlan: HttpResponse<IManualBusinessPlan>) => {
          if (manualBusinessPlan.body) {
            return of(manualBusinessPlan.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
