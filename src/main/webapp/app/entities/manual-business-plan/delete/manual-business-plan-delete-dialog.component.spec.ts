jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ManualBusinessPlanService } from '../service/manual-business-plan.service';

import { ManualBusinessPlanDeleteDialogComponent } from './manual-business-plan-delete-dialog.component';

describe('ManualBusinessPlan Management Delete Component', () => {
  let comp: ManualBusinessPlanDeleteDialogComponent;
  let fixture: ComponentFixture<ManualBusinessPlanDeleteDialogComponent>;
  let service: ManualBusinessPlanService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ManualBusinessPlanDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ManualBusinessPlanDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManualBusinessPlanDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ManualBusinessPlanService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
