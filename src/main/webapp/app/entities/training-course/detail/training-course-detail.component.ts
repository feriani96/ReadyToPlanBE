import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ITrainingCourse } from '../training-course.model';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';

@Component({
  selector: 'jhi-training-course-detail',
  templateUrl: './training-course-detail.component.html',
})
export class TrainingCourseDetailComponent implements OnInit {
  trainingCourse: ITrainingCourse | null = null;
  presentationHtml: SafeHtml = '';  // <-- changer en SafeHtml

  constructor(protected activatedRoute: ActivatedRoute, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingCourse }) => {
      this.trainingCourse = trainingCourse;

      if (trainingCourse?.presentation) {
        const html = marked(trainingCourse.presentation);
        console.log('HTML généré:', html);
        this.presentationHtml = this.sanitizer.bypassSecurityTrustHtml(html);
      }

    });
  }

  previousState(): void {
    window.history.back();
  }
}
