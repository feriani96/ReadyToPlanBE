import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManualBusinessPlan, NewManualBusinessPlan } from '../manual-business-plan.model';

export type PartialUpdateManualBusinessPlan = Partial<IManualBusinessPlan> & Pick<IManualBusinessPlan, 'id'>;

type RestOf<T extends IManualBusinessPlan | NewManualBusinessPlan> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

export type RestManualBusinessPlan = RestOf<IManualBusinessPlan>;

export type NewRestManualBusinessPlan = RestOf<NewManualBusinessPlan>;

export type PartialUpdateRestManualBusinessPlan = RestOf<PartialUpdateManualBusinessPlan>;

export type EntityResponseType = HttpResponse<IManualBusinessPlan>;
export type EntityArrayResponseType = HttpResponse<IManualBusinessPlan[]>;

@Injectable({ providedIn: 'root' })
export class ManualBusinessPlanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manual-business-plans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(manualBusinessPlan: NewManualBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualBusinessPlan);
    return this.http
      .post<RestManualBusinessPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(manualBusinessPlan: IManualBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualBusinessPlan);
    return this.http
      .put<RestManualBusinessPlan>(`${this.resourceUrl}/${this.getManualBusinessPlanIdentifier(manualBusinessPlan)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(manualBusinessPlan: PartialUpdateManualBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualBusinessPlan);
    return this.http
      .patch<RestManualBusinessPlan>(`${this.resourceUrl}/${this.getManualBusinessPlanIdentifier(manualBusinessPlan)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestManualBusinessPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestManualBusinessPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getManualBusinessPlanIdentifier(manualBusinessPlan: Pick<IManualBusinessPlan, 'id'>): string {
    return manualBusinessPlan.id;
  }

  compareManualBusinessPlan(o1: Pick<IManualBusinessPlan, 'id'> | null, o2: Pick<IManualBusinessPlan, 'id'> | null): boolean {
    return o1 && o2 ? this.getManualBusinessPlanIdentifier(o1) === this.getManualBusinessPlanIdentifier(o2) : o1 === o2;
  }

  addManualBusinessPlanToCollectionIfMissing<Type extends Pick<IManualBusinessPlan, 'id'>>(
    manualBusinessPlanCollection: Type[],
    ...manualBusinessPlansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const manualBusinessPlans: Type[] = manualBusinessPlansToCheck.filter(isPresent);
    if (manualBusinessPlans.length > 0) {
      const manualBusinessPlanCollectionIdentifiers = manualBusinessPlanCollection.map(
        manualBusinessPlanItem => this.getManualBusinessPlanIdentifier(manualBusinessPlanItem)!
      );
      const manualBusinessPlansToAdd = manualBusinessPlans.filter(manualBusinessPlanItem => {
        const manualBusinessPlanIdentifier = this.getManualBusinessPlanIdentifier(manualBusinessPlanItem);
        if (manualBusinessPlanCollectionIdentifiers.includes(manualBusinessPlanIdentifier)) {
          return false;
        }
        manualBusinessPlanCollectionIdentifiers.push(manualBusinessPlanIdentifier);
        return true;
      });
      return [...manualBusinessPlansToAdd, ...manualBusinessPlanCollection];
    }
    return manualBusinessPlanCollection;
  }

  protected convertDateFromClient<T extends IManualBusinessPlan | NewManualBusinessPlan | PartialUpdateManualBusinessPlan>(
    manualBusinessPlan: T
  ): RestOf<T> {
    return {
      ...manualBusinessPlan,
      creationDate: manualBusinessPlan.creationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restManualBusinessPlan: RestManualBusinessPlan): IManualBusinessPlan {
    return {
      ...restManualBusinessPlan,
      creationDate: restManualBusinessPlan.creationDate ? dayjs(restManualBusinessPlan.creationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestManualBusinessPlan>): HttpResponse<IManualBusinessPlan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestManualBusinessPlan[]>): HttpResponse<IManualBusinessPlan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
