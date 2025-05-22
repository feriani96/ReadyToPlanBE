import { Country } from 'app/entities/enumerations/country.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IEnterprise {
  id: string;
  enterpriseName?: string | null;
  country?: Country | null;
  phoneNumber?: number | null;
  description?: string | null;
  amount?: number | null;
  currency?: Currency | null;
}

export type NewEnterprise = Omit<IEnterprise, 'id'> & { id: null };
