import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EnterpriseFormService } from './enterprise-form.service';
import { EnterpriseService } from '../service/enterprise.service';
import { IEnterprise } from '../enterprise.model';

import { EnterpriseUpdateComponent } from './enterprise-update.component';

describe('Enterprise Management Update Component', () => {
  let comp: EnterpriseUpdateComponent;
  let fixture: ComponentFixture<EnterpriseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enterpriseFormService: EnterpriseFormService;
  let enterpriseService: EnterpriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EnterpriseUpdateComponent],
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
      .overrideTemplate(EnterpriseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnterpriseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enterpriseFormService = TestBed.inject(EnterpriseFormService);
    enterpriseService = TestBed.inject(EnterpriseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const enterprise: IEnterprise = { id: 'CBA' };

      activatedRoute.data = of({ enterprise });
      comp.ngOnInit();

      expect(comp.enterprise).toEqual(enterprise);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnterprise>>();
      const enterprise = { id: 'ABC' };
      jest.spyOn(enterpriseFormService, 'getEnterprise').mockReturnValue(enterprise);
      jest.spyOn(enterpriseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enterprise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enterprise }));
      saveSubject.complete();

      // THEN
      expect(enterpriseFormService.getEnterprise).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enterpriseService.update).toHaveBeenCalledWith(expect.objectContaining(enterprise));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnterprise>>();
      const enterprise = { id: 'ABC' };
      jest.spyOn(enterpriseFormService, 'getEnterprise').mockReturnValue({ id: null });
      jest.spyOn(enterpriseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enterprise: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enterprise }));
      saveSubject.complete();

      // THEN
      expect(enterpriseFormService.getEnterprise).toHaveBeenCalled();
      expect(enterpriseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnterprise>>();
      const enterprise = { id: 'ABC' };
      jest.spyOn(enterpriseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enterprise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enterpriseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
