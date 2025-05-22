import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnterprise, NewEnterprise } from '../enterprise.model';

export type PartialUpdateEnterprise = Partial<IEnterprise> & Pick<IEnterprise, 'id'>;

export type EntityResponseType = HttpResponse<IEnterprise>;
export type EntityArrayResponseType = HttpResponse<IEnterprise[]>;

@Injectable({ providedIn: 'root' })
export class EnterpriseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enterprises');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(enterprise: NewEnterprise): Observable<EntityResponseType> {
    return this.http.post<IEnterprise>(this.resourceUrl, enterprise, { observe: 'response' });
  }

  update(enterprise: IEnterprise): Observable<EntityResponseType> {
    return this.http.put<IEnterprise>(`${this.resourceUrl}/${this.getEnterpriseIdentifier(enterprise)}`, enterprise, {
      observe: 'response',
    });
  }

  partialUpdate(enterprise: PartialUpdateEnterprise): Observable<EntityResponseType> {
    return this.http.patch<IEnterprise>(`${this.resourceUrl}/${this.getEnterpriseIdentifier(enterprise)}`, enterprise, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IEnterprise>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnterprise[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnterpriseIdentifier(enterprise: Pick<IEnterprise, 'id'>): string {
    return enterprise.id;
  }

  compareEnterprise(o1: Pick<IEnterprise, 'id'> | null, o2: Pick<IEnterprise, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnterpriseIdentifier(o1) === this.getEnterpriseIdentifier(o2) : o1 === o2;
  }

  addEnterpriseToCollectionIfMissing<Type extends Pick<IEnterprise, 'id'>>(
    enterpriseCollection: Type[],
    ...enterprisesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const enterprises: Type[] = enterprisesToCheck.filter(isPresent);
    if (enterprises.length > 0) {
      const enterpriseCollectionIdentifiers = enterpriseCollection.map(enterpriseItem => this.getEnterpriseIdentifier(enterpriseItem)!);
      const enterprisesToAdd = enterprises.filter(enterpriseItem => {
        const enterpriseIdentifier = this.getEnterpriseIdentifier(enterpriseItem);
        if (enterpriseCollectionIdentifiers.includes(enterpriseIdentifier)) {
          return false;
        }
        enterpriseCollectionIdentifiers.push(enterpriseIdentifier);
        return true;
      });
      return [...enterprisesToAdd, ...enterpriseCollection];
    }
    return enterpriseCollection;
  }
}
