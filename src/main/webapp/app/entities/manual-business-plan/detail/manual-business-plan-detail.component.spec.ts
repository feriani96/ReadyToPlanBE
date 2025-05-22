import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManualBusinessPlanDetailComponent } from './manual-business-plan-detail.component';

describe('ManualBusinessPlan Management Detail Component', () => {
  let comp: ManualBusinessPlanDetailComponent;
  let fixture: ComponentFixture<ManualBusinessPlanDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManualBusinessPlanDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ manualBusinessPlan: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ManualBusinessPlanDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManualBusinessPlanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manualBusinessPlan on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.manualBusinessPlan).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
