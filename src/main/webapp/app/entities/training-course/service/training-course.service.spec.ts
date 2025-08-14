import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrainingCourse } from '../training-course.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../training-course.test-samples';

import { TrainingCourseService } from './training-course.service';

const requireRestSample: ITrainingCourse = {
  ...sampleWithRequiredData,
};

describe('TrainingCourse Service', () => {
  let service: TrainingCourseService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrainingCourse | ITrainingCourse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrainingCourseService);
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

    it('should create a TrainingCourse', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const trainingCourse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trainingCourse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrainingCourse', () => {
      const trainingCourse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trainingCourse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrainingCourse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrainingCourse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrainingCourse', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrainingCourseToCollectionIfMissing', () => {
      it('should add a TrainingCourse to an empty array', () => {
        const trainingCourse: ITrainingCourse = sampleWithRequiredData;
        expectedResult = service.addTrainingCourseToCollectionIfMissing([], trainingCourse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingCourse);
      });

      it('should not add a TrainingCourse to an array that contains it', () => {
        const trainingCourse: ITrainingCourse = sampleWithRequiredData;
        const trainingCourseCollection: ITrainingCourse[] = [
          {
            ...trainingCourse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrainingCourseToCollectionIfMissing(trainingCourseCollection, trainingCourse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrainingCourse to an array that doesn't contain it", () => {
        const trainingCourse: ITrainingCourse = sampleWithRequiredData;
        const trainingCourseCollection: ITrainingCourse[] = [sampleWithPartialData];
        expectedResult = service.addTrainingCourseToCollectionIfMissing(trainingCourseCollection, trainingCourse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingCourse);
      });

      it('should add only unique TrainingCourse to an array', () => {
        const trainingCourseArray: ITrainingCourse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trainingCourseCollection: ITrainingCourse[] = [sampleWithRequiredData];
        expectedResult = service.addTrainingCourseToCollectionIfMissing(trainingCourseCollection, ...trainingCourseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trainingCourse: ITrainingCourse = sampleWithRequiredData;
        const trainingCourse2: ITrainingCourse = sampleWithPartialData;
        expectedResult = service.addTrainingCourseToCollectionIfMissing([], trainingCourse, trainingCourse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingCourse);
        expect(expectedResult).toContain(trainingCourse2);
      });

      it('should accept null and undefined values', () => {
        const trainingCourse: ITrainingCourse = sampleWithRequiredData;
        expectedResult = service.addTrainingCourseToCollectionIfMissing([], null, trainingCourse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingCourse);
      });

      it('should return initial array if no TrainingCourse is added', () => {
        const trainingCourseCollection: ITrainingCourse[] = [sampleWithRequiredData];
        expectedResult = service.addTrainingCourseToCollectionIfMissing(trainingCourseCollection, undefined, null);
        expect(expectedResult).toEqual(trainingCourseCollection);
      });
    });

    describe('compareTrainingCourse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrainingCourse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareTrainingCourse(entity1, entity2);
        const compareResult2 = service.compareTrainingCourse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareTrainingCourse(entity1, entity2);
        const compareResult2 = service.compareTrainingCourse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareTrainingCourse(entity1, entity2);
        const compareResult2 = service.compareTrainingCourse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
