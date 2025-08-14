import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrainingCourseDetailComponent } from './training-course-detail.component';

describe('TrainingCourse Management Detail Component', () => {
  let comp: TrainingCourseDetailComponent;
  let fixture: ComponentFixture<TrainingCourseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrainingCourseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trainingCourse: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(TrainingCourseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrainingCourseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trainingCourse on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trainingCourse).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
