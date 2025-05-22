import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnterprise } from '../enterprise.model';
import { EnterpriseService } from '../service/enterprise.service';

@Injectable({ providedIn: 'root' })
export class EnterpriseRoutingResolveService implements Resolve<IEnterprise | null> {
  constructor(protected service: EnterpriseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnterprise | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((enterprise: HttpResponse<IEnterprise>) => {
          if (enterprise.body) {
            return of(enterprise.body);
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
