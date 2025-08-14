import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrainingCourseComponent } from '../list/training-course.component';
import { TrainingCourseDetailComponent } from '../detail/training-course-detail.component';
import { TrainingCourseUpdateComponent } from '../update/training-course-update.component';
import { TrainingCourseRoutingResolveService } from './training-course-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const trainingCourseRoute: Routes = [
  {
    path: '',
    component: TrainingCourseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrainingCourseDetailComponent,
    resolve: {
      trainingCourse: TrainingCourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrainingCourseUpdateComponent,
    resolve: {
      trainingCourse: TrainingCourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrainingCourseUpdateComponent,
    resolve: {
      trainingCourse: TrainingCourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trainingCourseRoute)],
  exports: [RouterModule],
})
export class TrainingCourseRoutingModule {}
