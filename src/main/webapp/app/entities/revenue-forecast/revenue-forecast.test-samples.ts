import { Month } from 'app/entities/enumerations/month.model';

import { IRevenueForecast, NewRevenueForecast } from './revenue-forecast.model';

export const sampleWithRequiredData: IRevenueForecast = {
  id: '441bdb7e-6d81-4cc4-bde9-2e2c7a5f99fe',
  month: Month['AUGUST'],
  year: 1945,
  unitsSold: 86302,
  totalRevenue: 99177,
};

export const sampleWithPartialData: IRevenueForecast = {
  id: 'f255dd5c-4219-422c-b62e-5e3613666d01',
  month: Month['JUNE'],
  year: 2082,
  unitsSold: 92993,
  totalRevenue: 58896,
};

export const sampleWithFullData: IRevenueForecast = {
  id: 'c2a6587f-45e2-45fc-bddc-c385e2a4d4a7',
  month: Month['MAY'],
  year: 1953,
  unitsSold: 5390,
  totalRevenue: 26981,
};

export const sampleWithNewData: NewRevenueForecast = {
  month: Month['APRIL'],
  year: 2040,
  unitsSold: 88021,
  totalRevenue: 76916,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
