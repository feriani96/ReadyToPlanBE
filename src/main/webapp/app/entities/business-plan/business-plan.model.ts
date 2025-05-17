import dayjs from 'dayjs/esm';
import { Country } from 'app/entities/enumerations/country.model';
import { Languages } from 'app/entities/enumerations/languages.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IBusinessPlan {
  id: string;
  companyName?: string | null;
  companyStartDate?: dayjs.Dayjs | null;
  country?: Country | null;
  languages?: Languages | null;
  companyDescription?: string | null;
  anticipatedProjectSize?: number | null;
  currency?: Currency | null;
}

export type NewBusinessPlan = Omit<IBusinessPlan, 'id'> & { id: null };
