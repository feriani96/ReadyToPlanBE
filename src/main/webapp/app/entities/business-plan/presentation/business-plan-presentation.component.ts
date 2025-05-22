import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BusinessPlanService } from '../service/business-plan.service';
import { IBusinessPlan } from '../business-plan.model';

@Component({
  selector: 'jhi-business-plan-presentation',
  templateUrl: './business-plan-presentation.component.html',
  styleUrls: ['./business-plan-presentation.component.scss']
})
export class BusinessPlanPresentationComponent implements OnInit {
 businessPlan?: IBusinessPlan;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private businessPlanService: BusinessPlanService
  ) {}

  ngOnInit(): void {
    this.loadBusinessPlan();
  }

  loadBusinessPlan(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isLoading = true;
      this.businessPlanService.find(id).subscribe({
        next: res => {
          this.businessPlan = res.body!;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
    }
  }

}
