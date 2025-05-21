import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessPlan, NewBusinessPlan } from '../business-plan.model';

export type PartialUpdateBusinessPlan = Partial<IBusinessPlan> & Pick<IBusinessPlan, 'id'>;

type RestOf<T extends IBusinessPlan | NewBusinessPlan> = Omit<T, 'companyStartDate'> & {
  companyStartDate?: string | null;
};

export type RestBusinessPlan = RestOf<IBusinessPlan>;

export type NewRestBusinessPlan = RestOf<NewBusinessPlan>;

export type PartialUpdateRestBusinessPlan = RestOf<PartialUpdateBusinessPlan>;

export type EntityResponseType = HttpResponse<IBusinessPlan>;
export type EntityArrayResponseType = HttpResponse<IBusinessPlan[]>;

@Injectable({ providedIn: 'root' })
export class BusinessPlanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-plans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(businessPlan: NewBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessPlan);
    return this.http
      .post<RestBusinessPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(businessPlan: IBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessPlan);
    return this.http
      .put<RestBusinessPlan>(`${this.resourceUrl}/${this.getBusinessPlanIdentifier(businessPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(businessPlan: PartialUpdateBusinessPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessPlan);
    return this.http
      .patch<RestBusinessPlan>(`${this.resourceUrl}/${this.getBusinessPlanIdentifier(businessPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestBusinessPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBusinessPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessPlanIdentifier(businessPlan: Pick<IBusinessPlan, 'id'>): string {
    return businessPlan.id;
  }

  compareBusinessPlan(o1: Pick<IBusinessPlan, 'id'> | null, o2: Pick<IBusinessPlan, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessPlanIdentifier(o1) === this.getBusinessPlanIdentifier(o2) : o1 === o2;
  }

  addBusinessPlanToCollectionIfMissing<Type extends Pick<IBusinessPlan, 'id'>>(
    businessPlanCollection: Type[],
    ...businessPlansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businessPlans: Type[] = businessPlansToCheck.filter(isPresent);
    if (businessPlans.length > 0) {
      const businessPlanCollectionIdentifiers = businessPlanCollection.map(
        businessPlanItem => this.getBusinessPlanIdentifier(businessPlanItem)!
      );
      const businessPlansToAdd = businessPlans.filter(businessPlanItem => {
        const businessPlanIdentifier = this.getBusinessPlanIdentifier(businessPlanItem);
        if (businessPlanCollectionIdentifiers.includes(businessPlanIdentifier)) {
          return false;
        }
        businessPlanCollectionIdentifiers.push(businessPlanIdentifier);
        return true;
      });
      return [...businessPlansToAdd, ...businessPlanCollection];
    }
    return businessPlanCollection;
  }

  protected convertDateFromClient<T extends IBusinessPlan | NewBusinessPlan | PartialUpdateBusinessPlan>(businessPlan: T): RestOf<T> {
    return {
      ...businessPlan,
      companyStartDate: businessPlan.companyStartDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBusinessPlan: RestBusinessPlan): IBusinessPlan {
    return {
      ...restBusinessPlan,
      companyStartDate: restBusinessPlan.companyStartDate ? dayjs(restBusinessPlan.companyStartDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBusinessPlan>): HttpResponse<IBusinessPlan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBusinessPlan[]>): HttpResponse<IBusinessPlan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  generatePresentation(id: string): Observable<string> {
    return this.http.get<string>(`${this.resourceUrl}/${id}/generate-presentation`);
  }

}
