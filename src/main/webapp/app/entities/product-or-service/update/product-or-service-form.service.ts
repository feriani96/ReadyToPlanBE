import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductOrService, NewProductOrService } from '../product-or-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductOrService for edit and NewProductOrServiceFormGroupInput for create.
 */
type ProductOrServiceFormGroupInput = IProductOrService | PartialWithRequiredKeyOf<NewProductOrService>;

type ProductOrServiceFormDefaults = Pick<NewProductOrService, 'id'>;

type ProductOrServiceFormGroupContent = {
  id: FormControl<IProductOrService['id'] | NewProductOrService['id']>;
  nameProductOrService: FormControl<IProductOrService['nameProductOrService']>;
  productDescription: FormControl<IProductOrService['productDescription']>;
  unitPrice: FormControl<IProductOrService['unitPrice']>;
  estimatedMonthlySales: FormControl<IProductOrService['estimatedMonthlySales']>;
  durationInMonths: FormControl<IProductOrService['durationInMonths']>;
  manualBusinessPlan: FormControl<IProductOrService['manualBusinessPlan']>;
};

export type ProductOrServiceFormGroup = FormGroup<ProductOrServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductOrServiceFormService {
  createProductOrServiceFormGroup(productOrService: ProductOrServiceFormGroupInput = { id: null }): ProductOrServiceFormGroup {
    const productOrServiceRawValue = {
      ...this.getFormDefaults(),
      ...productOrService,
    };
    return new FormGroup<ProductOrServiceFormGroupContent>({
      id: new FormControl(
        { value: productOrServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nameProductOrService: new FormControl(productOrServiceRawValue.nameProductOrService, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      productDescription: new FormControl(productOrServiceRawValue.productDescription, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      unitPrice: new FormControl(productOrServiceRawValue.unitPrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      estimatedMonthlySales: new FormControl(productOrServiceRawValue.estimatedMonthlySales),
      durationInMonths: new FormControl(productOrServiceRawValue.durationInMonths, {
        validators: [Validators.required, Validators.min(1)],
      }),
      manualBusinessPlan: new FormControl(productOrServiceRawValue.manualBusinessPlan),
    });
  }

  getProductOrService(form: ProductOrServiceFormGroup): IProductOrService | NewProductOrService {
    return form.getRawValue() as IProductOrService | NewProductOrService;
  }

  resetForm(form: ProductOrServiceFormGroup, productOrService: ProductOrServiceFormGroupInput): void {
    const productOrServiceRawValue = { ...this.getFormDefaults(), ...productOrService };
    form.reset(
      {
        ...productOrServiceRawValue,
        id: { value: productOrServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductOrServiceFormDefaults {
    return {
      id: null,
    };
  }
}
