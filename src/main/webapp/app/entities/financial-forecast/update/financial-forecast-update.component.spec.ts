import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FinancialForecastFormService } from './financial-forecast-form.service';
import { FinancialForecastService } from '../service/financial-forecast.service';
import { IFinancialForecast } from '../financial-forecast.model';
import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';
import { ManualBusinessPlanService } from 'app/entities/manual-business-plan/service/manual-business-plan.service';

import { FinancialForecastUpdateComponent } from './financial-forecast-update.component';

describe('FinancialForecast Management Update Component', () => {
  let comp: FinancialForecastUpdateComponent;
  let fixture: ComponentFixture<FinancialForecastUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financialForecastFormService: FinancialForecastFormService;
  let financialForecastService: FinancialForecastService;
  let manualBusinessPlanService: ManualBusinessPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FinancialForecastUpdateComponent],
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
      .overrideTemplate(FinancialForecastUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinancialForecastUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financialForecastFormService = TestBed.inject(FinancialForecastFormService);
    financialForecastService = TestBed.inject(FinancialForecastService);
    manualBusinessPlanService = TestBed.inject(ManualBusinessPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ManualBusinessPlan query and add missing value', () => {
      const financialForecast: IFinancialForecast = { id: 'CBA' };
      const manualBusinessPlan: IManualBusinessPlan = { id: 'bfad4ae9-41c2-43dc-9f9e-d8fcc0e60a4e' };
      financialForecast.manualBusinessPlan = manualBusinessPlan;

      const manualBusinessPlanCollection: IManualBusinessPlan[] = [{ id: 'b59b18fd-94b1-4b65-a04a-e9bbf9fc0cb0' }];
      jest.spyOn(manualBusinessPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: manualBusinessPlanCollection })));
      const additionalManualBusinessPlans = [manualBusinessPlan];
      const expectedCollection: IManualBusinessPlan[] = [...additionalManualBusinessPlans, ...manualBusinessPlanCollection];
      jest.spyOn(manualBusinessPlanService, 'addManualBusinessPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financialForecast });
      comp.ngOnInit();

      expect(manualBusinessPlanService.query).toHaveBeenCalled();
      expect(manualBusinessPlanService.addManualBusinessPlanToCollectionIfMissing).toHaveBeenCalledWith(
        manualBusinessPlanCollection,
        ...additionalManualBusinessPlans.map(expect.objectContaining)
      );
      expect(comp.manualBusinessPlansSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const financialForecast: IFinancialForecast = { id: 'CBA' };
      const manualBusinessPlan: IManualBusinessPlan = { id: '9973c42c-b3fa-415c-9180-f772b4f9e479' };
      financialForecast.manualBusinessPlan = manualBusinessPlan;

      activatedRoute.data = of({ financialForecast });
      comp.ngOnInit();

      expect(comp.manualBusinessPlansSharedCollection).toContain(manualBusinessPlan);
      expect(comp.financialForecast).toEqual(financialForecast);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialForecast>>();
      const financialForecast = { id: 'ABC' };
      jest.spyOn(financialForecastFormService, 'getFinancialForecast').mockReturnValue(financialForecast);
      jest.spyOn(financialForecastService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialForecast });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialForecast }));
      saveSubject.complete();

      // THEN
      expect(financialForecastFormService.getFinancialForecast).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financialForecastService.update).toHaveBeenCalledWith(expect.objectContaining(financialForecast));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialForecast>>();
      const financialForecast = { id: 'ABC' };
      jest.spyOn(financialForecastFormService, 'getFinancialForecast').mockReturnValue({ id: null });
      jest.spyOn(financialForecastService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialForecast: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialForecast }));
      saveSubject.complete();

      // THEN
      expect(financialForecastFormService.getFinancialForecast).toHaveBeenCalled();
      expect(financialForecastService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialForecast>>();
      const financialForecast = { id: 'ABC' };
      jest.spyOn(financialForecastService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financialForecast });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financialForecastService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareManualBusinessPlan', () => {
      it('Should forward to manualBusinessPlanService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(manualBusinessPlanService, 'compareManualBusinessPlan');
        comp.compareManualBusinessPlan(entity, entity2);
        expect(manualBusinessPlanService.compareManualBusinessPlan).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
