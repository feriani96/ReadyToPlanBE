import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../manual-business-plan.test-samples';

import { ManualBusinessPlanFormService } from './manual-business-plan-form.service';

describe('ManualBusinessPlan Form Service', () => {
  let service: ManualBusinessPlanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManualBusinessPlanFormService);
  });

  describe('Service methods', () => {
    describe('createManualBusinessPlanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createManualBusinessPlanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            creationDate: expect.any(Object),
            entrepreneurName: expect.any(Object),
          })
        );
      });

      it('passing IManualBusinessPlan should create a new form with FormGroup', () => {
        const formGroup = service.createManualBusinessPlanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            creationDate: expect.any(Object),
            entrepreneurName: expect.any(Object),
          })
        );
      });
    });

    describe('getManualBusinessPlan', () => {
      it('should return NewManualBusinessPlan for default ManualBusinessPlan initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createManualBusinessPlanFormGroup(sampleWithNewData);

        const manualBusinessPlan = service.getManualBusinessPlan(formGroup) as any;

        expect(manualBusinessPlan).toMatchObject(sampleWithNewData);
      });

      it('should return NewManualBusinessPlan for empty ManualBusinessPlan initial value', () => {
        const formGroup = service.createManualBusinessPlanFormGroup();

        const manualBusinessPlan = service.getManualBusinessPlan(formGroup) as any;

        expect(manualBusinessPlan).toMatchObject({});
      });

      it('should return IManualBusinessPlan', () => {
        const formGroup = service.createManualBusinessPlanFormGroup(sampleWithRequiredData);

        const manualBusinessPlan = service.getManualBusinessPlan(formGroup) as any;

        expect(manualBusinessPlan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IManualBusinessPlan should not enable id FormControl', () => {
        const formGroup = service.createManualBusinessPlanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewManualBusinessPlan should disable id FormControl', () => {
        const formGroup = service.createManualBusinessPlanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
