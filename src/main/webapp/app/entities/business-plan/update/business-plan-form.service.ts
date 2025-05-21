import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessPlan, NewBusinessPlan } from '../business-plan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessPlan for edit and NewBusinessPlanFormGroupInput for create.
 */
type BusinessPlanFormGroupInput = IBusinessPlan | PartialWithRequiredKeyOf<NewBusinessPlan>;

type BusinessPlanFormDefaults = Pick<NewBusinessPlan, 'id'>;

type BusinessPlanFormGroupContent = {
  id: FormControl<IBusinessPlan['id'] | NewBusinessPlan['id']>;
  companyName: FormControl<IBusinessPlan['companyName']>;
  companyStartDate: FormControl<IBusinessPlan['companyStartDate']>;
  country: FormControl<IBusinessPlan['country']>;
  languages: FormControl<IBusinessPlan['languages']>;
  companyDescription: FormControl<IBusinessPlan['companyDescription']>;
  anticipatedProjectSize: FormControl<IBusinessPlan['anticipatedProjectSize']>;
  currency: FormControl<IBusinessPlan['currency']>;
  generatedPresentation: FormControl<IBusinessPlan['generatedPresentation']>;
};

export type BusinessPlanFormGroup = FormGroup<BusinessPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessPlanFormService {
  createBusinessPlanFormGroup(businessPlan: BusinessPlanFormGroupInput = { id: null }): BusinessPlanFormGroup {
    const businessPlanRawValue = {
      ...this.getFormDefaults(),
      ...businessPlan,
    };
    return new FormGroup<BusinessPlanFormGroupContent>({
      id: new FormControl(
        { value: businessPlanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      companyName: new FormControl(businessPlanRawValue.companyName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      companyStartDate: new FormControl(businessPlanRawValue.companyStartDate, {
        validators: [Validators.required],
      }),
      country: new FormControl(businessPlanRawValue.country, {
        validators: [Validators.required],
      }),
      languages: new FormControl(businessPlanRawValue.languages, {
        validators: [Validators.required],
      }),
      companyDescription: new FormControl(businessPlanRawValue.companyDescription, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      anticipatedProjectSize: new FormControl(businessPlanRawValue.anticipatedProjectSize, {
        validators: [Validators.required],
      }),
      currency: new FormControl(businessPlanRawValue.currency, {
        validators: [Validators.required],
      }),
      generatedPresentation: new FormControl(businessPlanRawValue.generatedPresentation),
    });
  }

  getBusinessPlan(form: BusinessPlanFormGroup): IBusinessPlan | NewBusinessPlan {
    return form.getRawValue() as IBusinessPlan | NewBusinessPlan;
  }

  resetForm(form: BusinessPlanFormGroup, businessPlan: BusinessPlanFormGroupInput): void {
    const businessPlanRawValue = { ...this.getFormDefaults(), ...businessPlan };
    form.reset(
      {
        ...businessPlanRawValue,
        id: { value: businessPlanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BusinessPlanFormDefaults {
    return {
      id: null,
    };
  }
}
