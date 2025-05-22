import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IManualBusinessPlan } from '../manual-business-plan.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../manual-business-plan.test-samples';

import { ManualBusinessPlanService, RestManualBusinessPlan } from './manual-business-plan.service';

const requireRestSample: RestManualBusinessPlan = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.format(DATE_FORMAT),
};

describe('ManualBusinessPlan Service', () => {
  let service: ManualBusinessPlanService;
  let httpMock: HttpTestingController;
  let expectedResult: IManualBusinessPlan | IManualBusinessPlan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManualBusinessPlanService);
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

    it('should create a ManualBusinessPlan', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const manualBusinessPlan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(manualBusinessPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManualBusinessPlan', () => {
      const manualBusinessPlan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(manualBusinessPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManualBusinessPlan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManualBusinessPlan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ManualBusinessPlan', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addManualBusinessPlanToCollectionIfMissing', () => {
      it('should add a ManualBusinessPlan to an empty array', () => {
        const manualBusinessPlan: IManualBusinessPlan = sampleWithRequiredData;
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing([], manualBusinessPlan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualBusinessPlan);
      });

      it('should not add a ManualBusinessPlan to an array that contains it', () => {
        const manualBusinessPlan: IManualBusinessPlan = sampleWithRequiredData;
        const manualBusinessPlanCollection: IManualBusinessPlan[] = [
          {
            ...manualBusinessPlan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing(manualBusinessPlanCollection, manualBusinessPlan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManualBusinessPlan to an array that doesn't contain it", () => {
        const manualBusinessPlan: IManualBusinessPlan = sampleWithRequiredData;
        const manualBusinessPlanCollection: IManualBusinessPlan[] = [sampleWithPartialData];
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing(manualBusinessPlanCollection, manualBusinessPlan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualBusinessPlan);
      });

      it('should add only unique ManualBusinessPlan to an array', () => {
        const manualBusinessPlanArray: IManualBusinessPlan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const manualBusinessPlanCollection: IManualBusinessPlan[] = [sampleWithRequiredData];
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing(manualBusinessPlanCollection, ...manualBusinessPlanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manualBusinessPlan: IManualBusinessPlan = sampleWithRequiredData;
        const manualBusinessPlan2: IManualBusinessPlan = sampleWithPartialData;
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing([], manualBusinessPlan, manualBusinessPlan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualBusinessPlan);
        expect(expectedResult).toContain(manualBusinessPlan2);
      });

      it('should accept null and undefined values', () => {
        const manualBusinessPlan: IManualBusinessPlan = sampleWithRequiredData;
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing([], null, manualBusinessPlan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualBusinessPlan);
      });

      it('should return initial array if no ManualBusinessPlan is added', () => {
        const manualBusinessPlanCollection: IManualBusinessPlan[] = [sampleWithRequiredData];
        expectedResult = service.addManualBusinessPlanToCollectionIfMissing(manualBusinessPlanCollection, undefined, null);
        expect(expectedResult).toEqual(manualBusinessPlanCollection);
      });
    });

    describe('compareManualBusinessPlan', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareManualBusinessPlan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareManualBusinessPlan(entity1, entity2);
        const compareResult2 = service.compareManualBusinessPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareManualBusinessPlan(entity1, entity2);
        const compareResult2 = service.compareManualBusinessPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareManualBusinessPlan(entity1, entity2);
        const compareResult2 = service.compareManualBusinessPlan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
