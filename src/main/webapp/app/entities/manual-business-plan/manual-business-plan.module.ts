import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ManualBusinessPlanComponent } from './list/manual-business-plan.component';
import { ManualBusinessPlanDetailComponent } from './detail/manual-business-plan-detail.component';
import { ManualBusinessPlanUpdateComponent } from './update/manual-business-plan-update.component';
import { ManualBusinessPlanDeleteDialogComponent } from './delete/manual-business-plan-delete-dialog.component';
import { ManualBusinessPlanRoutingModule } from './route/manual-business-plan-routing.module';

@NgModule({
  imports: [SharedModule, ManualBusinessPlanRoutingModule],
  declarations: [
    ManualBusinessPlanComponent,
    ManualBusinessPlanDetailComponent,
    ManualBusinessPlanUpdateComponent,
    ManualBusinessPlanDeleteDialogComponent,
  ],
})
export class ManualBusinessPlanModule {}
