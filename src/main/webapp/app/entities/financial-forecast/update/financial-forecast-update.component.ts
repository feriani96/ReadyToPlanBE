import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FinancialForecastFormService, FinancialForecastFormGroup } from './financial-forecast-form.service';
import { IFinancialForecast } from '../financial-forecast.model';
import { FinancialForecastService } from '../service/financial-forecast.service';
import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';
import { ManualBusinessPlanService } from 'app/entities/manual-business-plan/service/manual-business-plan.service';

@Component({
  selector: 'jhi-financial-forecast-update',
  templateUrl: './financial-forecast-update.component.html',
})
export class FinancialForecastUpdateComponent implements OnInit {
  isSaving = false;
  financialForecast: IFinancialForecast | null = null;

  manualBusinessPlansSharedCollection: IManualBusinessPlan[] = [];

  editForm: FinancialForecastFormGroup = this.financialForecastFormService.createFinancialForecastFormGroup();

  constructor(
    protected financialForecastService: FinancialForecastService,
    protected financialForecastFormService: FinancialForecastFormService,
    protected manualBusinessPlanService: ManualBusinessPlanService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareManualBusinessPlan = (o1: IManualBusinessPlan | null, o2: IManualBusinessPlan | null): boolean =>
    this.manualBusinessPlanService.compareManualBusinessPlan(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialForecast }) => {
      this.financialForecast = financialForecast;
      if (financialForecast) {
        this.updateForm(financialForecast);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financialForecast = this.financialForecastFormService.getFinancialForecast(this.editForm);
    if (financialForecast.id !== null) {
      this.subscribeToSaveResponse(this.financialForecastService.update(financialForecast));
    } else {
      this.subscribeToSaveResponse(this.financialForecastService.create(financialForecast));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinancialForecast>>): void {
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

  protected updateForm(financialForecast: IFinancialForecast): void {
    this.financialForecast = financialForecast;
    this.financialForecastFormService.resetForm(this.editForm, financialForecast);

    this.manualBusinessPlansSharedCollection =
      this.manualBusinessPlanService.addManualBusinessPlanToCollectionIfMissing<IManualBusinessPlan>(
        this.manualBusinessPlansSharedCollection,
        financialForecast.manualBusinessPlan
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
            this.financialForecast?.manualBusinessPlan
          )
        )
      )
      .subscribe((manualBusinessPlans: IManualBusinessPlan[]) => (this.manualBusinessPlansSharedCollection = manualBusinessPlans));
  }
}
