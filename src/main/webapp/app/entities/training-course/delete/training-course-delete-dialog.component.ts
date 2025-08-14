import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrainingCourse } from '../training-course.model';
import { TrainingCourseService } from '../service/training-course.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './training-course-delete-dialog.component.html',
})
export class TrainingCourseDeleteDialogComponent {
  trainingCourse?: ITrainingCourse;

  constructor(protected trainingCourseService: TrainingCourseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.trainingCourseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
