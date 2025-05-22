import { Country } from 'app/entities/enumerations/country.model';
import { Currency } from 'app/entities/enumerations/currency.model';

import { IEnterprise, NewEnterprise } from './enterprise.model';

export const sampleWithRequiredData: IEnterprise = {
  id: 'decf5457-3f25-487f-a91e-8014b59378b4',
  enterpriseName: 'Gloves array',
  country: Country['SOUTH_AFRICA'],
  phoneNumber: 23893,
  description: 'Polarised next-generation Buckinghamshire',
  amount: 92491,
  currency: Currency['USD'],
};

export const sampleWithPartialData: IEnterprise = {
  id: '0b40b86c-84f2-427a-8c2a-7ed46c381788',
  enterpriseName: 'Niue deposit',
  country: Country['AUSTRALIA'],
  phoneNumber: 63359,
  description: 'Tools',
  amount: 70731,
  currency: Currency['EUR'],
};

export const sampleWithFullData: IEnterprise = {
  id: '55d91e66-2bc5-41aa-bee2-0d7735d5c71e',
  enterpriseName: 'Persistent',
  country: Country['RUSSIA'],
  phoneNumber: 15278,
  description: 'pixel PCI',
  amount: 91388,
  currency: Currency['EUR'],
};

export const sampleWithNewData: NewEnterprise = {
  enterpriseName: 'Integration Integration',
  country: Country['SOUTH_KOREA'],
  phoneNumber: 66784,
  description: 'Granite executive',
  amount: 44927,
  currency: Currency['TND'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
