import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnterprise } from '../enterprise.model';
import { EnterpriseService } from '../service/enterprise.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './enterprise-delete-dialog.component.html',
})
export class EnterpriseDeleteDialogComponent {
  enterprise?: IEnterprise;

  constructor(protected enterpriseService: EnterpriseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.enterpriseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
