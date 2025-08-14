import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../training-course.test-samples';

import { TrainingCourseFormService } from './training-course-form.service';

describe('TrainingCourse Form Service', () => {
  let service: TrainingCourseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrainingCourseFormService);
  });

  describe('Service methods', () => {
    describe('createTrainingCourseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrainingCourseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            summary: expect.any(Object),
            targetAudience: expect.any(Object),
            instructor: expect.any(Object),
            studyClass: expect.any(Object),
            level: expect.any(Object),
            locationType: expect.any(Object),
            duration: expect.any(Object),
            languages: expect.any(Object),
          })
        );
      });

      it('passing ITrainingCourse should create a new form with FormGroup', () => {
        const formGroup = service.createTrainingCourseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            summary: expect.any(Object),
            targetAudience: expect.any(Object),
            instructor: expect.any(Object),
            studyClass: expect.any(Object),
            level: expect.any(Object),
            locationType: expect.any(Object),
            duration: expect.any(Object),
            languages: expect.any(Object),
          })
        );
      });
    });

    describe('getTrainingCourse', () => {
      it('should return NewTrainingCourse for default TrainingCourse initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrainingCourseFormGroup(sampleWithNewData);

        const trainingCourse = service.getTrainingCourse(formGroup) as any;

        expect(trainingCourse).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrainingCourse for empty TrainingCourse initial value', () => {
        const formGroup = service.createTrainingCourseFormGroup();

        const trainingCourse = service.getTrainingCourse(formGroup) as any;

        expect(trainingCourse).toMatchObject({});
      });

      it('should return ITrainingCourse', () => {
        const formGroup = service.createTrainingCourseFormGroup(sampleWithRequiredData);

        const trainingCourse = service.getTrainingCourse(formGroup) as any;

        expect(trainingCourse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrainingCourse should not enable id FormControl', () => {
        const formGroup = service.createTrainingCourseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrainingCourse should disable id FormControl', () => {
        const formGroup = service.createTrainingCourseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
