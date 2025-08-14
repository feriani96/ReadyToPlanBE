import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrainingCourseFormService } from './training-course-form.service';
import { TrainingCourseService } from '../service/training-course.service';
import { ITrainingCourse } from '../training-course.model';

import { TrainingCourseUpdateComponent } from './training-course-update.component';

describe('TrainingCourse Management Update Component', () => {
  let comp: TrainingCourseUpdateComponent;
  let fixture: ComponentFixture<TrainingCourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trainingCourseFormService: TrainingCourseFormService;
  let trainingCourseService: TrainingCourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrainingCourseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TrainingCourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainingCourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trainingCourseFormService = TestBed.inject(TrainingCourseFormService);
    trainingCourseService = TestBed.inject(TrainingCourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trainingCourse: ITrainingCourse = { id: 'CBA' };

      activatedRoute.data = of({ trainingCourse });
      comp.ngOnInit();

      expect(comp.trainingCourse).toEqual(trainingCourse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainingCourse>>();
      const trainingCourse = { id: 'ABC' };
      jest.spyOn(trainingCourseFormService, 'getTrainingCourse').mockReturnValue(trainingCourse);
      jest.spyOn(trainingCourseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingCourse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingCourse }));
      saveSubject.complete();

      // THEN
      expect(trainingCourseFormService.getTrainingCourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trainingCourseService.update).toHaveBeenCalledWith(expect.objectContaining(trainingCourse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainingCourse>>();
      const trainingCourse = { id: 'ABC' };
      jest.spyOn(trainingCourseFormService, 'getTrainingCourse').mockReturnValue({ id: null });
      jest.spyOn(trainingCourseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingCourse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingCourse }));
      saveSubject.complete();

      // THEN
      expect(trainingCourseFormService.getTrainingCourse).toHaveBeenCalled();
      expect(trainingCourseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainingCourse>>();
      const trainingCourse = { id: 'ABC' };
      jest.spyOn(trainingCourseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingCourse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trainingCourseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
