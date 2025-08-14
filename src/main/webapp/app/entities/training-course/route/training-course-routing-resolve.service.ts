import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrainingCourse } from '../training-course.model';
import { TrainingCourseService } from '../service/training-course.service';

@Injectable({ providedIn: 'root' })
export class TrainingCourseRoutingResolveService implements Resolve<ITrainingCourse | null> {
  constructor(protected service: TrainingCourseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrainingCourse | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trainingCourse: HttpResponse<ITrainingCourse>) => {
          if (trainingCourse.body) {
            return of(trainingCourse.body);
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
