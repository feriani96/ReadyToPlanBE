import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManualBusinessPlan } from '../manual-business-plan.model';

@Component({
  selector: 'jhi-manual-business-plan-detail',
  templateUrl: './manual-business-plan-detail.component.html',
})
export class ManualBusinessPlanDetailComponent implements OnInit {
  manualBusinessPlan: IManualBusinessPlan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualBusinessPlan }) => {
      this.manualBusinessPlan = manualBusinessPlan;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
