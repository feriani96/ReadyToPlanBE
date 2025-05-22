import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ManualBusinessPlanFormService } from './manual-business-plan-form.service';
import { ManualBusinessPlanService } from '../service/manual-business-plan.service';
import { IManualBusinessPlan } from '../manual-business-plan.model';

import { ManualBusinessPlanUpdateComponent } from './manual-business-plan-update.component';

describe('ManualBusinessPlan Management Update Component', () => {
  let comp: ManualBusinessPlanUpdateComponent;
  let fixture: ComponentFixture<ManualBusinessPlanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manualBusinessPlanFormService: ManualBusinessPlanFormService;
  let manualBusinessPlanService: ManualBusinessPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ManualBusinessPlanUpdateComponent],
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
      .overrideTemplate(ManualBusinessPlanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManualBusinessPlanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualBusinessPlanFormService = TestBed.inject(ManualBusinessPlanFormService);
    manualBusinessPlanService = TestBed.inject(ManualBusinessPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const manualBusinessPlan: IManualBusinessPlan = { id: 'CBA' };

      activatedRoute.data = of({ manualBusinessPlan });
      comp.ngOnInit();

      expect(comp.manualBusinessPlan).toEqual(manualBusinessPlan);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualBusinessPlan>>();
      const manualBusinessPlan = { id: 'ABC' };
      jest.spyOn(manualBusinessPlanFormService, 'getManualBusinessPlan').mockReturnValue(manualBusinessPlan);
      jest.spyOn(manualBusinessPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualBusinessPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualBusinessPlan }));
      saveSubject.complete();

      // THEN
      expect(manualBusinessPlanFormService.getManualBusinessPlan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualBusinessPlanService.update).toHaveBeenCalledWith(expect.objectContaining(manualBusinessPlan));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualBusinessPlan>>();
      const manualBusinessPlan = { id: 'ABC' };
      jest.spyOn(manualBusinessPlanFormService, 'getManualBusinessPlan').mockReturnValue({ id: null });
      jest.spyOn(manualBusinessPlanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualBusinessPlan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualBusinessPlan }));
      saveSubject.complete();

      // THEN
      expect(manualBusinessPlanFormService.getManualBusinessPlan).toHaveBeenCalled();
      expect(manualBusinessPlanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualBusinessPlan>>();
      const manualBusinessPlan = { id: 'ABC' };
      jest.spyOn(manualBusinessPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualBusinessPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualBusinessPlanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
