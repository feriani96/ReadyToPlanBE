import dayjs from 'dayjs/esm';
import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';

export interface IFinancialForecast {
  id: string;
  startDate?: dayjs.Dayjs | null;
  durationInMonths?: number | null;
  manualBusinessPlan?: Pick<IManualBusinessPlan, 'id' | 'name'> | null;
}

export type NewFinancialForecast = Omit<IFinancialForecast, 'id'> & { id: null };
