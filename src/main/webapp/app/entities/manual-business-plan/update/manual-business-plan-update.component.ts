import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ManualBusinessPlanFormService, ManualBusinessPlanFormGroup } from './manual-business-plan-form.service';
import { IManualBusinessPlan } from '../manual-business-plan.model';
import { ManualBusinessPlanService } from '../service/manual-business-plan.service';

@Component({
  selector: 'jhi-manual-business-plan-update',
  templateUrl: './manual-business-plan-update.component.html',
})
export class ManualBusinessPlanUpdateComponent implements OnInit {
  isSaving = false;
  manualBusinessPlan: IManualBusinessPlan | null = null;

  editForm: ManualBusinessPlanFormGroup = this.manualBusinessPlanFormService.createManualBusinessPlanFormGroup();

  constructor(
    protected manualBusinessPlanService: ManualBusinessPlanService,
    protected manualBusinessPlanFormService: ManualBusinessPlanFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualBusinessPlan }) => {
      this.manualBusinessPlan = manualBusinessPlan;
      if (manualBusinessPlan) {
        this.updateForm(manualBusinessPlan);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manualBusinessPlan = this.manualBusinessPlanFormService.getManualBusinessPlan(this.editForm);
    if (manualBusinessPlan.id !== null) {
      this.subscribeToSaveResponse(this.manualBusinessPlanService.update(manualBusinessPlan));
    } else {
      this.subscribeToSaveResponse(this.manualBusinessPlanService.create(manualBusinessPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManualBusinessPlan>>): void {
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

  protected updateForm(manualBusinessPlan: IManualBusinessPlan): void {
    this.manualBusinessPlan = manualBusinessPlan;
    this.manualBusinessPlanFormService.resetForm(this.editForm, manualBusinessPlan);
  }
}
