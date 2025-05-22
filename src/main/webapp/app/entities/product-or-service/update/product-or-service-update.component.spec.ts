import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductOrServiceFormService } from './product-or-service-form.service';
import { ProductOrServiceService } from '../service/product-or-service.service';
import { IProductOrService } from '../product-or-service.model';
import { IManualBusinessPlan } from 'app/entities/manual-business-plan/manual-business-plan.model';
import { ManualBusinessPlanService } from 'app/entities/manual-business-plan/service/manual-business-plan.service';

import { ProductOrServiceUpdateComponent } from './product-or-service-update.component';

describe('ProductOrService Management Update Component', () => {
  let comp: ProductOrServiceUpdateComponent;
  let fixture: ComponentFixture<ProductOrServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productOrServiceFormService: ProductOrServiceFormService;
  let productOrServiceService: ProductOrServiceService;
  let manualBusinessPlanService: ManualBusinessPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductOrServiceUpdateComponent],
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
      .overrideTemplate(ProductOrServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductOrServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productOrServiceFormService = TestBed.inject(ProductOrServiceFormService);
    productOrServiceService = TestBed.inject(ProductOrServiceService);
    manualBusinessPlanService = TestBed.inject(ManualBusinessPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ManualBusinessPlan query and add missing value', () => {
      const productOrService: IProductOrService = { id: 'CBA' };
      const manualBusinessPlan: IManualBusinessPlan = { id: 'e77dc8ac-db5f-4b43-bb68-a7c77bf0c1c8' };
      productOrService.manualBusinessPlan = manualBusinessPlan;

      const manualBusinessPlanCollection: IManualBusinessPlan[] = [{ id: '0efc3807-8788-4dc1-8818-23122b967900' }];
      jest.spyOn(manualBusinessPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: manualBusinessPlanCollection })));
      const additionalManualBusinessPlans = [manualBusinessPlan];
      const expectedCollection: IManualBusinessPlan[] = [...additionalManualBusinessPlans, ...manualBusinessPlanCollection];
      jest.spyOn(manualBusinessPlanService, 'addManualBusinessPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrService });
      comp.ngOnInit();

      expect(manualBusinessPlanService.query).toHaveBeenCalled();
      expect(manualBusinessPlanService.addManualBusinessPlanToCollectionIfMissing).toHaveBeenCalledWith(
        manualBusinessPlanCollection,
        ...additionalManualBusinessPlans.map(expect.objectContaining)
      );
      expect(comp.manualBusinessPlansSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productOrService: IProductOrService = { id: 'CBA' };
      const manualBusinessPlan: IManualBusinessPlan = { id: '5a46650d-b869-4a74-96d7-14516574f7f3' };
      productOrService.manualBusinessPlan = manualBusinessPlan;

      activatedRoute.data = of({ productOrService });
      comp.ngOnInit();

      expect(comp.manualBusinessPlansSharedCollection).toContain(manualBusinessPlan);
      expect(comp.productOrService).toEqual(productOrService);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrService>>();
      const productOrService = { id: 'ABC' };
      jest.spyOn(productOrServiceFormService, 'getProductOrService').mockReturnValue(productOrService);
      jest.spyOn(productOrServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrService }));
      saveSubject.complete();

      // THEN
      expect(productOrServiceFormService.getProductOrService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productOrServiceService.update).toHaveBeenCalledWith(expect.objectContaining(productOrService));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrService>>();
      const productOrService = { id: 'ABC' };
      jest.spyOn(productOrServiceFormService, 'getProductOrService').mockReturnValue({ id: null });
      jest.spyOn(productOrServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrService }));
      saveSubject.complete();

      // THEN
      expect(productOrServiceFormService.getProductOrService).toHaveBeenCalled();
      expect(productOrServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrService>>();
      const productOrService = { id: 'ABC' };
      jest.spyOn(productOrServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productOrServiceService.update).toHaveBeenCalled();
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
