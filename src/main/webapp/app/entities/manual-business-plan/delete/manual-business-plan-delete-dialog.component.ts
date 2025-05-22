import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManualBusinessPlan } from '../manual-business-plan.model';
import { ManualBusinessPlanService } from '../service/manual-business-plan.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './manual-business-plan-delete-dialog.component.html',
})
export class ManualBusinessPlanDeleteDialogComponent {
  manualBusinessPlan?: IManualBusinessPlan;

  constructor(protected manualBusinessPlanService: ManualBusinessPlanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.manualBusinessPlanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
