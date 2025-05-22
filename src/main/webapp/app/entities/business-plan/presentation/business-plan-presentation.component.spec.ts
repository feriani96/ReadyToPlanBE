import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessPlanPresentationComponent } from './business-plan-presentation.component';

describe('BusinessPlanPresentationComponent', () => {
  let component: BusinessPlanPresentationComponent;
  let fixture: ComponentFixture<BusinessPlanPresentationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BusinessPlanPresentationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BusinessPlanPresentationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
