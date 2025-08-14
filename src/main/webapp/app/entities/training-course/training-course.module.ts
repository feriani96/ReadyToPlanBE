import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrainingCourseComponent } from './list/training-course.component';
import { TrainingCourseDetailComponent } from './detail/training-course-detail.component';
import { TrainingCourseUpdateComponent } from './update/training-course-update.component';
import { TrainingCourseDeleteDialogComponent } from './delete/training-course-delete-dialog.component';
import { TrainingCourseRoutingModule } from './route/training-course-routing.module';

@NgModule({
  imports: [SharedModule, TrainingCourseRoutingModule],
  declarations: [
    TrainingCourseComponent,
    TrainingCourseDetailComponent,
    TrainingCourseUpdateComponent,
    TrainingCourseDeleteDialogComponent,
  ],
})
export class TrainingCourseModule {}
