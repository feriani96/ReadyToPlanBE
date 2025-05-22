import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManualBusinessPlanComponent } from '../list/manual-business-plan.component';
import { ManualBusinessPlanDetailComponent } from '../detail/manual-business-plan-detail.component';
import { ManualBusinessPlanUpdateComponent } from '../update/manual-business-plan-update.component';
import { ManualBusinessPlanRoutingResolveService } from './manual-business-plan-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const manualBusinessPlanRoute: Routes = [
  {
    path: '',
    component: ManualBusinessPlanComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManualBusinessPlanDetailComponent,
    resolve: {
      manualBusinessPlan: ManualBusinessPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManualBusinessPlanUpdateComponent,
    resolve: {
      manualBusinessPlan: ManualBusinessPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManualBusinessPlanUpdateComponent,
    resolve: {
      manualBusinessPlan: ManualBusinessPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manualBusinessPlanRoute)],
  exports: [RouterModule],
})
export class ManualBusinessPlanRoutingModule {}
