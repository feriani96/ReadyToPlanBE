import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';

export interface IProductOrService {
  id: string;
  nameProductOrService?: string | null;
  productDescription?: string | null;
  unitPrice?: number | null;
  estimatedMonthlySales?: number | null;
  durationInMonths?: number | null;
  manualBusinessPlan?: Pick<IManualBusinessPlan, 'id' | 'name'> | null;
}

export type NewProductOrService = Omit<IProductOrService, 'id'> & { id: null };
