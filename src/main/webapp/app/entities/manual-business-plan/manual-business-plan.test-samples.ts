import dayjs from 'dayjs/esm';

import { IManualBusinessPlan, NewManualBusinessPlan } from './manual-business-plan.model';

export const sampleWithRequiredData: IManualBusinessPlan = {
  id: '3a77388b-b454-4806-9b82-a482ba5bb3fe',
  name: 'SMS Handcrafted',
  description: 'benchmark Center Steel',
  creationDate: dayjs('2025-05-21'),
  entrepreneurName: 'Specialist Loan',
};

export const sampleWithPartialData: IManualBusinessPlan = {
  id: '59584bae-896d-41cb-a39c-1e60954a7a3a',
  name: 'input Nepalese',
  description: 'Fantastic',
  creationDate: dayjs('2025-05-21'),
  entrepreneurName: 'embrace Frozen',
};

export const sampleWithFullData: IManualBusinessPlan = {
  id: '121b963c-2233-4f7f-b5ad-717cea08a20c',
  name: 'attitude Fresh circuit',
  description: 'Florida',
  creationDate: dayjs('2025-05-21'),
  entrepreneurName: 'Secured Operative',
};

export const sampleWithNewData: NewManualBusinessPlan = {
  name: 'Missouri neural deploy',
  description: 'Caicos',
  creationDate: dayjs('2025-05-21'),
  entrepreneurName: 'Jewelery Account',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
