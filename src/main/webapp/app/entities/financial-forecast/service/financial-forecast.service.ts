import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinancialForecast, NewFinancialForecast } from '../financial-forecast.model';

export type PartialUpdateFinancialForecast = Partial<IFinancialForecast> & Pick<IFinancialForecast, 'id'>;

type RestOf<T extends IFinancialForecast | NewFinancialForecast> = Omit<T, 'startDate'> & {
  startDate?: string | null;
};

export type RestFinancialForecast = RestOf<IFinancialForecast>;

export type NewRestFinancialForecast = RestOf<NewFinancialForecast>;

export type PartialUpdateRestFinancialForecast = RestOf<PartialUpdateFinancialForecast>;

export type EntityResponseType = HttpResponse<IFinancialForecast>;
export type EntityArrayResponseType = HttpResponse<IFinancialForecast[]>;

@Injectable({ providedIn: 'root' })
export class FinancialForecastService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financial-forecasts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(financialForecast: NewFinancialForecast): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialForecast);
    return this.http
      .post<RestFinancialForecast>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(financialForecast: IFinancialForecast): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialForecast);
    return this.http
      .put<RestFinancialForecast>(`${this.resourceUrl}/${this.getFinancialForecastIdentifier(financialForecast)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(financialForecast: PartialUpdateFinancialForecast): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialForecast);
    return this.http
      .patch<RestFinancialForecast>(`${this.resourceUrl}/${this.getFinancialForecastIdentifier(financialForecast)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestFinancialForecast>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFinancialForecast[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinancialForecastIdentifier(financialForecast: Pick<IFinancialForecast, 'id'>): string {
    return financialForecast.id;
  }

  compareFinancialForecast(o1: Pick<IFinancialForecast, 'id'> | null, o2: Pick<IFinancialForecast, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinancialForecastIdentifier(o1) === this.getFinancialForecastIdentifier(o2) : o1 === o2;
  }

  addFinancialForecastToCollectionIfMissing<Type extends Pick<IFinancialForecast, 'id'>>(
    financialForecastCollection: Type[],
    ...financialForecastsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financialForecasts: Type[] = financialForecastsToCheck.filter(isPresent);
    if (financialForecasts.length > 0) {
      const financialForecastCollectionIdentifiers = financialForecastCollection.map(
        financialForecastItem => this.getFinancialForecastIdentifier(financialForecastItem)!
      );
      const financialForecastsToAdd = financialForecasts.filter(financialForecastItem => {
        const financialForecastIdentifier = this.getFinancialForecastIdentifier(financialForecastItem);
        if (financialForecastCollectionIdentifiers.includes(financialForecastIdentifier)) {
          return false;
        }
        financialForecastCollectionIdentifiers.push(financialForecastIdentifier);
        return true;
      });
      return [...financialForecastsToAdd, ...financialForecastCollection];
    }
    return financialForecastCollection;
  }

  protected convertDateFromClient<T extends IFinancialForecast | NewFinancialForecast | PartialUpdateFinancialForecast>(
    financialForecast: T
  ): RestOf<T> {
    return {
      ...financialForecast,
      startDate: financialForecast.startDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFinancialForecast: RestFinancialForecast): IFinancialForecast {
    return {
      ...restFinancialForecast,
      startDate: restFinancialForecast.startDate ? dayjs(restFinancialForecast.startDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFinancialForecast>): HttpResponse<IFinancialForecast> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFinancialForecast[]>): HttpResponse<IFinancialForecast[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
