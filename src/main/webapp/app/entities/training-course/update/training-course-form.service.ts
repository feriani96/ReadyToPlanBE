import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrainingCourse, NewTrainingCourse } from '../training-course.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrainingCourse for edit and NewTrainingCourseFormGroupInput for create.
 */
type TrainingCourseFormGroupInput = ITrainingCourse | PartialWithRequiredKeyOf<NewTrainingCourse>;

type TrainingCourseFormDefaults = Pick<NewTrainingCourse, 'id'>;

type TrainingCourseFormGroupContent = {
  id: FormControl<ITrainingCourse['id'] | NewTrainingCourse['id']>;
  title: FormControl<ITrainingCourse['title']>;
  summary: FormControl<ITrainingCourse['summary']>;
  targetAudience: FormControl<ITrainingCourse['targetAudience']>;
  instructor: FormControl<ITrainingCourse['instructor']>;
  studyClass: FormControl<ITrainingCourse['studyClass']>;
  level: FormControl<ITrainingCourse['level']>;
  locationType: FormControl<ITrainingCourse['locationType']>;
  duration: FormControl<ITrainingCourse['duration']>;
  languages: FormControl<ITrainingCourse['languages']>;
};

export type TrainingCourseFormGroup = FormGroup<TrainingCourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrainingCourseFormService {
  createTrainingCourseFormGroup(trainingCourse: TrainingCourseFormGroupInput = { id: null }): TrainingCourseFormGroup {
    const trainingCourseRawValue = {
      ...this.getFormDefaults(),
      ...trainingCourse,
    };
    return new FormGroup<TrainingCourseFormGroupContent>({
      id: new FormControl(
        { value: trainingCourseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(trainingCourseRawValue.title, {
        validators: [Validators.required],
      }),
      summary: new FormControl(trainingCourseRawValue.summary),
      targetAudience: new FormControl(trainingCourseRawValue.targetAudience, {
        validators: [Validators.required],
      }),
      instructor: new FormControl(trainingCourseRawValue.instructor),
      studyClass: new FormControl(trainingCourseRawValue.studyClass, {
        validators: [Validators.required],
      }),
      level: new FormControl(trainingCourseRawValue.level, {
        validators: [Validators.required],
      }),
      locationType: new FormControl(trainingCourseRawValue.locationType, {
        validators: [Validators.required],
      }),
      duration: new FormControl(trainingCourseRawValue.duration),
      languages: new FormControl(trainingCourseRawValue.languages, {
        validators: [Validators.required],
      }),
    });
  }

  getTrainingCourse(form: TrainingCourseFormGroup): ITrainingCourse | NewTrainingCourse {
    return form.getRawValue() as ITrainingCourse | NewTrainingCourse;
  }

  resetForm(form: TrainingCourseFormGroup, trainingCourse: TrainingCourseFormGroupInput): void {
    const trainingCourseRawValue = { ...this.getFormDefaults(), ...trainingCourse };
    form.reset(
      {
        ...trainingCourseRawValue,
        id: { value: trainingCourseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrainingCourseFormDefaults {
    return {
      id: null,
    };
  }
}
