import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnterprise, NewEnterprise } from '../enterprise.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnterprise for edit and NewEnterpriseFormGroupInput for create.
 */
type EnterpriseFormGroupInput = IEnterprise | PartialWithRequiredKeyOf<NewEnterprise>;

type EnterpriseFormDefaults = Pick<NewEnterprise, 'id'>;

type EnterpriseFormGroupContent = {
  id: FormControl<IEnterprise['id'] | NewEnterprise['id']>;
  enterpriseName: FormControl<IEnterprise['enterpriseName']>;
  country: FormControl<IEnterprise['country']>;
  phoneNumber: FormControl<IEnterprise['phoneNumber']>;
  description: FormControl<IEnterprise['description']>;
  amount: FormControl<IEnterprise['amount']>;
  currency: FormControl<IEnterprise['currency']>;
};

export type EnterpriseFormGroup = FormGroup<EnterpriseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnterpriseFormService {
  createEnterpriseFormGroup(enterprise: EnterpriseFormGroupInput = { id: null }): EnterpriseFormGroup {
    const enterpriseRawValue = {
      ...this.getFormDefaults(),
      ...enterprise,
    };
    return new FormGroup<EnterpriseFormGroupContent>({
      id: new FormControl(
        { value: enterpriseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      enterpriseName: new FormControl(enterpriseRawValue.enterpriseName, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      country: new FormControl(enterpriseRawValue.country, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(enterpriseRawValue.phoneNumber, {
        validators: [Validators.required],
      }),
      description: new FormControl(enterpriseRawValue.description, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      amount: new FormControl(enterpriseRawValue.amount, {
        validators: [Validators.required],
      }),
      currency: new FormControl(enterpriseRawValue.currency, {
        validators: [Validators.required],
      }),
    });
  }

  getEnterprise(form: EnterpriseFormGroup): IEnterprise | NewEnterprise {
    return form.getRawValue() as IEnterprise | NewEnterprise;
  }

  resetForm(form: EnterpriseFormGroup, enterprise: EnterpriseFormGroupInput): void {
    const enterpriseRawValue = { ...this.getFormDefaults(), ...enterprise };
    form.reset(
      {
        ...enterpriseRawValue,
        id: { value: enterpriseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EnterpriseFormDefaults {
    return {
      id: null,
    };
  }
}
