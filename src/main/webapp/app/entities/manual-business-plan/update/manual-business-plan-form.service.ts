import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IManualBusinessPlan, NewManualBusinessPlan } from '../manual-business-plan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IManualBusinessPlan for edit and NewManualBusinessPlanFormGroupInput for create.
 */
type ManualBusinessPlanFormGroupInput = IManualBusinessPlan | PartialWithRequiredKeyOf<NewManualBusinessPlan>;

type ManualBusinessPlanFormDefaults = Pick<NewManualBusinessPlan, 'id'>;

type ManualBusinessPlanFormGroupContent = {
  id: FormControl<IManualBusinessPlan['id'] | NewManualBusinessPlan['id']>;
  name: FormControl<IManualBusinessPlan['name']>;
  description: FormControl<IManualBusinessPlan['description']>;
  creationDate: FormControl<IManualBusinessPlan['creationDate']>;
  entrepreneurName: FormControl<IManualBusinessPlan['entrepreneurName']>;
};

export type ManualBusinessPlanFormGroup = FormGroup<ManualBusinessPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ManualBusinessPlanFormService {
  createManualBusinessPlanFormGroup(manualBusinessPlan: ManualBusinessPlanFormGroupInput = { id: null }): ManualBusinessPlanFormGroup {
    const manualBusinessPlanRawValue = {
      ...this.getFormDefaults(),
      ...manualBusinessPlan,
    };
    return new FormGroup<ManualBusinessPlanFormGroupContent>({
      id: new FormControl(
        { value: manualBusinessPlanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(manualBusinessPlanRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(manualBusinessPlanRawValue.description, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      creationDate: new FormControl(manualBusinessPlanRawValue.creationDate, {
        validators: [Validators.required],
      }),
      entrepreneurName: new FormControl(manualBusinessPlanRawValue.entrepreneurName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getManualBusinessPlan(form: ManualBusinessPlanFormGroup): IManualBusinessPlan | NewManualBusinessPlan {
    return form.getRawValue() as IManualBusinessPlan | NewManualBusinessPlan;
  }

  resetForm(form: ManualBusinessPlanFormGroup, manualBusinessPlan: ManualBusinessPlanFormGroupInput): void {
    const manualBusinessPlanRawValue = { ...this.getFormDefaults(), ...manualBusinessPlan };
    form.reset(
      {
        ...manualBusinessPlanRawValue,
        id: { value: manualBusinessPlanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ManualBusinessPlanFormDefaults {
    return {
      id: null,
    };
  }
}
