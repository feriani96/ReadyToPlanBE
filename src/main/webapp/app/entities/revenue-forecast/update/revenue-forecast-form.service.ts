import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRevenueForecast, NewRevenueForecast } from '../revenue-forecast.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRevenueForecast for edit and NewRevenueForecastFormGroupInput for create.
 */
type RevenueForecastFormGroupInput = IRevenueForecast | PartialWithRequiredKeyOf<NewRevenueForecast>;

type RevenueForecastFormDefaults = Pick<NewRevenueForecast, 'id'>;

type RevenueForecastFormGroupContent = {
  id: FormControl<IRevenueForecast['id'] | NewRevenueForecast['id']>;
  month: FormControl<IRevenueForecast['month']>;
  year: FormControl<IRevenueForecast['year']>;
  unitsSold: FormControl<IRevenueForecast['unitsSold']>;
  totalRevenue: FormControl<IRevenueForecast['totalRevenue']>;
  productOrService: FormControl<IRevenueForecast['productOrService']>;
  financialForecast: FormControl<IRevenueForecast['financialForecast']>;
};

export type RevenueForecastFormGroup = FormGroup<RevenueForecastFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RevenueForecastFormService {
  createRevenueForecastFormGroup(revenueForecast: RevenueForecastFormGroupInput = { id: null }): RevenueForecastFormGroup {
    const revenueForecastRawValue = {
      ...this.getFormDefaults(),
      ...revenueForecast,
    };
    return new FormGroup<RevenueForecastFormGroupContent>({
      id: new FormControl(
        { value: revenueForecastRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(revenueForecastRawValue.month, {
        validators: [Validators.required],
      }),
      year: new FormControl(revenueForecastRawValue.year, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      unitsSold: new FormControl(revenueForecastRawValue.unitsSold, {
        validators: [Validators.required, Validators.min(0)],
      }),
      totalRevenue: new FormControl(revenueForecastRawValue.totalRevenue, {
        validators: [Validators.required, Validators.min(0)],
      }),
      productOrService: new FormControl(revenueForecastRawValue.productOrService),
      financialForecast: new FormControl(revenueForecastRawValue.financialForecast),
    });
  }

  getRevenueForecast(form: RevenueForecastFormGroup): IRevenueForecast | NewRevenueForecast {
    return form.getRawValue() as IRevenueForecast | NewRevenueForecast;
  }

  resetForm(form: RevenueForecastFormGroup, revenueForecast: RevenueForecastFormGroupInput): void {
    const revenueForecastRawValue = { ...this.getFormDefaults(), ...revenueForecast };
    form.reset(
      {
        ...revenueForecastRawValue,
        id: { value: revenueForecastRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RevenueForecastFormDefaults {
    return {
      id: null,
    };
  }
}
