import { IProductOrService, NewProductOrService } from './product-or-service.model';

export const sampleWithRequiredData: IProductOrService = {
  id: '8f1a263e-f631-4bcc-9fde-0d7f0ddd4d1e',
  nameProductOrService: 'dot-com wireless Louisiana',
  productDescription: 'hack',
  unitPrice: 37331,
  durationInMonths: 49202,
};

export const sampleWithPartialData: IProductOrService = {
  id: '40551feb-dfbe-4d3b-b492-32d6e3ddf6cd',
  nameProductOrService: 'partnerships wireless',
  productDescription: 'Investment relationships',
  unitPrice: 76979,
  estimatedMonthlySales: 35315,
  durationInMonths: 10887,
};

export const sampleWithFullData: IProductOrService = {
  id: 'de7bf943-e021-4b4f-9e93-634a39f461e1',
  nameProductOrService: 'bus hour',
  productDescription: 'Quality-focused Virginia',
  unitPrice: 24586,
  estimatedMonthlySales: 56970,
  durationInMonths: 57545,
};

export const sampleWithNewData: NewProductOrService = {
  nameProductOrService: 'facilitate Rue',
  productDescription: 'bypassing Well Account',
  unitPrice: 9281,
  durationInMonths: 53216,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
