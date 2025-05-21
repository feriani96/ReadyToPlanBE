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
  id: '0d948c01-593b-4fce-a479-ea4473f3c81b',
  companyName: 'Handcrafted',
  companyStartDate: dayjs('2025-05-16'),
  country: Country['TUNISIA'],
  languages: Languages['FRENCH'],
  companyDescription: 'lime',
  anticipatedProjectSize: 86087,
  currency: Currency['TND'],
};

export const sampleWithFullData: IBusinessPlan = {
  id: 'e10a8b31-6bbd-4d32-995a-e33b8b2267c5',
  companyName: 'Berkshire Administrator',
  companyStartDate: dayjs('2025-05-16'),
  country: Country['AUSTRALIA'],
  languages: Languages['ENGLISH'],
  companyDescription: 'connecting Sausages blue',
  anticipatedProjectSize: 96117,
  currency: Currency['EUR'],
  generatedPresentation: 'Account SSL',
};

export const sampleWithNewData: NewBusinessPlan = {
  companyName: 'Producer',
  companyStartDate: dayjs('2025-05-17'),
  country: Country['SOUTH_AFRICA'],
  languages: Languages['ENGLISH'],
  companyDescription: 'bandwidth Macedonia',
  anticipatedProjectSize: 53249,
  currency: Currency['TND'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
