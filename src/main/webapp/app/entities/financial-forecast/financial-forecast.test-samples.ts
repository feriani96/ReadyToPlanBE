import dayjs from 'dayjs/esm';

import { IFinancialForecast, NewFinancialForecast } from './financial-forecast.model';

export const sampleWithRequiredData: IFinancialForecast = {
  id: 'fced6e0b-eaad-4729-9639-7d7fcef87324',
  startDate: dayjs('2025-05-21'),
};

export const sampleWithPartialData: IFinancialForecast = {
  id: '10fafa1c-05a4-40c0-b7ba-11549cfa3b46',
  startDate: dayjs('2025-05-21'),
  durationInMonths: 68799,
};

export const sampleWithFullData: IFinancialForecast = {
  id: '4ae3969b-0747-41e2-860a-00ba7904b6e9',
  startDate: dayjs('2025-05-21'),
  durationInMonths: 94226,
};

export const sampleWithNewData: NewFinancialForecast = {
  startDate: dayjs('2025-05-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
