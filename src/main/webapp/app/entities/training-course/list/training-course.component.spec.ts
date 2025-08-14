import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrainingCourseService } from '../service/training-course.service';

import { TrainingCourseComponent } from './training-course.component';

describe('TrainingCourse Management Component', () => {
  let comp: TrainingCourseComponent;
  let fixture: ComponentFixture<TrainingCourseComponent>;
  let service: TrainingCourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'training-course', component: TrainingCourseComponent }]), HttpClientTestingModule],
      declarations: [TrainingCourseComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(TrainingCourseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainingCourseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TrainingCourseService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.trainingCourses?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to trainingCourseService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getTrainingCourseIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTrainingCourseIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
