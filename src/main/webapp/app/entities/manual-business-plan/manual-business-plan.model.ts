import dayjs from 'dayjs/esm';

export interface IManualBusinessPlan {
  id: string;
  name?: string | null;
  description?: string | null;
  creationDate?: dayjs.Dayjs | null;
  entrepreneurName?: string | null;
}

export type NewManualBusinessPlan = Omit<IManualBusinessPlan, 'id'> & { id: null };
