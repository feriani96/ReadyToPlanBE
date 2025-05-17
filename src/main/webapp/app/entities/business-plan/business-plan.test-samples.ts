import dayjs from 'dayjs/esm';

import { Country } from 'app/entities/enumerations/country.model';
import { Languages } from 'app/entities/enumerations/languages.model';
import { Currency } from 'app/entities/enumerations/currency.model';

import { IBusinessPlan, NewBusinessPlan } from './business-plan.model';

export const sampleWithRequiredData: IBusinessPlan = {
  id: 'c07d3233-24bf-4616-a533-95d6029c8abc',
  companyName: 'standardization',
  companyStartDate: dayjs('2025-05-17'),
  country: Country['CANADA'],
  languages: Languages['ENGLISH'],
  companyDescription: 'pink Parks',
  anticipatedProjectSize: 74144,
  currency: Currency['USD'],
};

export const sampleWithPartialData: IBusinessPlan = {
  id: '20d948c0-1593-4bfc-a647-9ea4473f3c81',
  companyName: 'Small support SQL',
  companyStartDate: dayjs('2025-05-17'),
  country: Country['GERMANY'],
  languages: Languages['FRENCH'],
  companyDescription: 'SDD payment blue',
  anticipatedProjectSize: 74721,
  currency: Currency['TND'],
};

export const sampleWithFullData: IBusinessPlan = {
  id: 'dd32995a-e33b-48b2-a67c-5787ff6fe17e',
  companyName: 'Cocos Forward Fantastic',
  companyStartDate: dayjs('2025-05-17'),
  country: Country['SAUDI_ARABIA'],
  languages: Languages['FRENCH'],
  companyDescription: 'Accountability',
  anticipatedProjectSize: 41950,
  currency: Currency['USD'],
};

export const sampleWithNewData: NewBusinessPlan = {
  companyName: 'program Taka bandwidth',
  companyStartDate: dayjs('2025-05-17'),
  country: Country['MEXICO'],
  languages: Languages['ENGLISH'],
  companyDescription: 'Branding Clothing',
  anticipatedProjectSize: 49102,
  currency: Currency['TND'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
