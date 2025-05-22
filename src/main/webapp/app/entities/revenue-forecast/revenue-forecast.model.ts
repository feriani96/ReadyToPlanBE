import { IProductOrService } from 'app/entities/product-or-service/product-or-service.model';
import { IFinancialForecast } from 'app/entities/financial-forecast/financial-forecast.model';
import { Month } from 'app/entities/enumerations/month.model';

export interface IRevenueForecast {
  id: string;
  month?: Month | null;
  year?: number | null;
  unitsSold?: number | null;
  totalRevenue?: number | null;
  productOrService?: Pick<IProductOrService, 'id' | 'nameProductOrService'> | null;
  financialForecast?: Pick<IFinancialForecast, 'id' | 'startDate'> | null;
}

export type NewRevenueForecast = Omit<IRevenueForecast, 'id'> & { id: null };
