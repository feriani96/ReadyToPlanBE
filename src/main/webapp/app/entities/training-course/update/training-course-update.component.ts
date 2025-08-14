import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TrainingCourseFormService, TrainingCourseFormGroup } from './training-course-form.service';
import { ITrainingCourse } from '../training-course.model';
import { TrainingCourseService } from '../service/training-course.service';
import { TargetAudience } from 'app/entities/enumerations/target-audience.model';
import { StudyClass } from 'app/entities/enumerations/study-class.model';
import { Level } from 'app/entities/enumerations/level.model';
import { LocationType } from 'app/entities/enumerations/location-type.model';
import { Languages } from 'app/entities/enumerations/languages.model';

@Component({
  selector: 'jhi-training-course-update',
  templateUrl: './training-course-update.component.html',
})
export class TrainingCourseUpdateComponent implements OnInit {
  isSaving = false;
  trainingCourse: ITrainingCourse | null = null;
  targetAudienceValues = Object.keys(TargetAudience);
  studyClassValues = Object.keys(StudyClass);
  levelValues = Object.keys(Level);
  locationTypeValues = Object.keys(LocationType);
  languagesValues = Object.keys(Languages);

  editForm: TrainingCourseFormGroup = this.trainingCourseFormService.createTrainingCourseFormGroup();

  constructor(
    protected trainingCourseService: TrainingCourseService,
    protected trainingCourseFormService: TrainingCourseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingCourse }) => {
      this.trainingCourse = trainingCourse;
      if (trainingCourse) {
        this.updateForm(trainingCourse);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainingCourse = this.trainingCourseFormService.getTrainingCourse(this.editForm);
    if (trainingCourse.id !== null) {
      this.subscribeToSaveResponse(this.trainingCourseService.update(trainingCourse));
    } else {
      this.subscribeToSaveResponse(this.trainingCourseService.create(trainingCourse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingCourse>>): void {
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

  protected updateForm(trainingCourse: ITrainingCourse): void {
    this.trainingCourse = trainingCourse;
    this.trainingCourseFormService.resetForm(this.editForm, trainingCourse);
  }
}
