import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductOrServiceFormService, ProductOrServiceFormGroup } from './product-or-service-form.service';
import { IProductOrService } from '../product-or-service.model';
import { ProductOrServiceService } from '../service/product-or-service.service';
import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';
import { ManualBusinessPlanService } from 'app/entities/manual-business-plan/service/manual-business-plan.service';

@Component({
  selector: 'jhi-product-or-service-update',
  templateUrl: './product-or-service-update.component.html',
})
export class ProductOrServiceUpdateComponent implements OnInit {
  isSaving = false;
  productOrService: IProductOrService | null = null;

  manualBusinessPlansSharedCollection: IManualBusinessPlan[] = [];

  editForm: ProductOrServiceFormGroup = this.productOrServiceFormService.createProductOrServiceFormGroup();

  constructor(
    protected productOrServiceService: ProductOrServiceService,
    protected productOrServiceFormService: ProductOrServiceFormService,
    protected manualBusinessPlanService: ManualBusinessPlanService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareManualBusinessPlan = (o1: IManualBusinessPlan | null, o2: IManualBusinessPlan | null): boolean =>
    this.manualBusinessPlanService.compareManualBusinessPlan(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productOrService }) => {
      this.productOrService = productOrService;
      if (productOrService) {
        this.updateForm(productOrService);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productOrService = this.productOrServiceFormService.getProductOrService(this.editForm);
    if (productOrService.id !== null) {
      this.subscribeToSaveResponse(this.productOrServiceService.update(productOrService));
    } else {
      this.subscribeToSaveResponse(this.productOrServiceService.create(productOrService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrService>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productOrService: IProductOrService): void {
    this.productOrService = productOrService;
    this.productOrServiceFormService.resetForm(this.editForm, productOrService);

    this.manualBusinessPlansSharedCollection =
      this.manualBusinessPlanService.addManualBusinessPlanToCollectionIfMissing<IManualBusinessPlan>(
        this.manualBusinessPlansSharedCollection,
        productOrService.manualBusinessPlan
      );
  }

  protected loadRelationshipsOptions(): void {
    this.manualBusinessPlanService
      .query()
      .pipe(map((res: HttpResponse<IManualBusinessPlan[]>) => res.body ?? []))
      .pipe(
        map((manualBusinessPlans: IManualBusinessPlan[]) =>
          this.manualBusinessPlanService.addManualBusinessPlanToCollectionIfMissing<IManualBusinessPlan>(
            manualBusinessPlans,
            this.productOrService?.manualBusinessPlan
          )
        )
      )
      .subscribe((manualBusinessPlans: IManualBusinessPlan[]) => (this.manualBusinessPlansSharedCollection = manualBusinessPlans));
  }
}
