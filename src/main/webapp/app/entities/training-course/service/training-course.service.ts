import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrainingCourse, NewTrainingCourse } from '../training-course.model';

export type PartialUpdateTrainingCourse = Partial<ITrainingCourse> & Pick<ITrainingCourse, 'id'>;

export type EntityResponseType = HttpResponse<ITrainingCourse>;
export type EntityArrayResponseType = HttpResponse<ITrainingCourse[]>;

@Injectable({ providedIn: 'root' })
export class TrainingCourseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/training-courses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trainingCourse: NewTrainingCourse): Observable<EntityResponseType> {
    return this.http.post<ITrainingCourse>(this.resourceUrl, trainingCourse, { observe: 'response' });
  }

  update(trainingCourse: ITrainingCourse): Observable<EntityResponseType> {
    return this.http.put<ITrainingCourse>(`${this.resourceUrl}/${this.getTrainingCourseIdentifier(trainingCourse)}`, trainingCourse, {
      observe: 'response',
    });
  }

  partialUpdate(trainingCourse: PartialUpdateTrainingCourse): Observable<EntityResponseType> {
    return this.http.patch<ITrainingCourse>(`${this.resourceUrl}/${this.getTrainingCourseIdentifier(trainingCourse)}`, trainingCourse, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ITrainingCourse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrainingCourse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrainingCourseIdentifier(trainingCourse: Pick<ITrainingCourse, 'id'>): string {
    return trainingCourse.id;
  }

  compareTrainingCourse(o1: Pick<ITrainingCourse, 'id'> | null, o2: Pick<ITrainingCourse, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrainingCourseIdentifier(o1) === this.getTrainingCourseIdentifier(o2) : o1 === o2;
  }

  addTrainingCourseToCollectionIfMissing<Type extends Pick<ITrainingCourse, 'id'>>(
    trainingCourseCollection: Type[],
    ...trainingCoursesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trainingCourses: Type[] = trainingCoursesToCheck.filter(isPresent);
    if (trainingCourses.length > 0) {
      const trainingCourseCollectionIdentifiers = trainingCourseCollection.map(
        trainingCourseItem => this.getTrainingCourseIdentifier(trainingCourseItem)!
      );
      const trainingCoursesToAdd = trainingCourses.filter(trainingCourseItem => {
        const trainingCourseIdentifier = this.getTrainingCourseIdentifier(trainingCourseItem);
        if (trainingCourseCollectionIdentifiers.includes(trainingCourseIdentifier)) {
          return false;
        }
        trainingCourseCollectionIdentifiers.push(trainingCourseIdentifier);
        return true;
      });
      return [...trainingCoursesToAdd, ...trainingCourseCollection];
    }
    return trainingCourseCollection;
  }
}
