import { IExpenseForecast, NewExpenseForecast } from './expense-forecast.model';

export const sampleWithRequiredData: IExpenseForecast = {
  id: '9b774019-9ad9-4ebd-85ec-1893a15c8590',
  label: 'Profit-focused',
  monthlyAmount: 3703,
};

export const sampleWithPartialData: IExpenseForecast = {
  id: '198cc34d-f648-420e-afa6-741d14f56b09',
  label: 'AGP Chicken',
  monthlyAmount: 30414,
};

export const sampleWithFullData: IExpenseForecast = {
  id: '2ca9ac47-0667-4353-b2f5-ff439257bb11',
  label: 'compress Technician',
  monthlyAmount: 80527,
};

export const sampleWithNewData: NewExpenseForecast = {
  label: 'Loan Jewelery',
  monthlyAmount: 60317,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
