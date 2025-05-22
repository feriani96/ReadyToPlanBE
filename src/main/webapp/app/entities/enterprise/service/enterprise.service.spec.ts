import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnterprise } from '../enterprise.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../enterprise.test-samples';

import { EnterpriseService } from './enterprise.service';

const requireRestSample: IEnterprise = {
  ...sampleWithRequiredData,
};

describe('Enterprise Service', () => {
  let service: EnterpriseService;
  let httpMock: HttpTestingController;
  let expectedResult: IEnterprise | IEnterprise[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EnterpriseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Enterprise', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const enterprise = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(enterprise).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Enterprise', () => {
      const enterprise = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(enterprise).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Enterprise', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Enterprise', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Enterprise', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEnterpriseToCollectionIfMissing', () => {
      it('should add a Enterprise to an empty array', () => {
        const enterprise: IEnterprise = sampleWithRequiredData;
        expectedResult = service.addEnterpriseToCollectionIfMissing([], enterprise);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enterprise);
      });

      it('should not add a Enterprise to an array that contains it', () => {
        const enterprise: IEnterprise = sampleWithRequiredData;
        const enterpriseCollection: IEnterprise[] = [
          {
            ...enterprise,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEnterpriseToCollectionIfMissing(enterpriseCollection, enterprise);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Enterprise to an array that doesn't contain it", () => {
        const enterprise: IEnterprise = sampleWithRequiredData;
        const enterpriseCollection: IEnterprise[] = [sampleWithPartialData];
        expectedResult = service.addEnterpriseToCollectionIfMissing(enterpriseCollection, enterprise);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enterprise);
      });

      it('should add only unique Enterprise to an array', () => {
        const enterpriseArray: IEnterprise[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const enterpriseCollection: IEnterprise[] = [sampleWithRequiredData];
        expectedResult = service.addEnterpriseToCollectionIfMissing(enterpriseCollection, ...enterpriseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const enterprise: IEnterprise = sampleWithRequiredData;
        const enterprise2: IEnterprise = sampleWithPartialData;
        expectedResult = service.addEnterpriseToCollectionIfMissing([], enterprise, enterprise2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enterprise);
        expect(expectedResult).toContain(enterprise2);
      });

      it('should accept null and undefined values', () => {
        const enterprise: IEnterprise = sampleWithRequiredData;
        expectedResult = service.addEnterpriseToCollectionIfMissing([], null, enterprise, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enterprise);
      });

      it('should return initial array if no Enterprise is added', () => {
        const enterpriseCollection: IEnterprise[] = [sampleWithRequiredData];
        expectedResult = service.addEnterpriseToCollectionIfMissing(enterpriseCollection, undefined, null);
        expect(expectedResult).toEqual(enterpriseCollection);
      });
    });

    describe('compareEnterprise', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEnterprise(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareEnterprise(entity1, entity2);
        const compareResult2 = service.compareEnterprise(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareEnterprise(entity1, entity2);
        const compareResult2 = service.compareEnterprise(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareEnterprise(entity1, entity2);
        const compareResult2 = service.compareEnterprise(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
