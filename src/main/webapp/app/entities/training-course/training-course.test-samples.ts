import { TargetAudience } from 'app/entities/enumerations/target-audience.model';
import { StudyClass } from 'app/entities/enumerations/study-class.model';
import { Level } from 'app/entities/enumerations/level.model';
import { LocationType } from 'app/entities/enumerations/location-type.model';
import { Languages } from 'app/entities/enumerations/languages.model';

import { ITrainingCourse, NewTrainingCourse } from './training-course.model';

export const sampleWithRequiredData: ITrainingCourse = {
  id: '404b1334-761b-4b37-aed2-3eb1b3a046c0',
  title: 'magenta',
  targetAudience: TargetAudience['TEACHERS'],
  studyClass: StudyClass['PRIMARY'],
  level: Level['BEGINNER'],
  locationType: LocationType['UNIVERSITY'],
  languages: Languages['FRENCH'],
};

export const sampleWithPartialData: ITrainingCourse = {
  id: 'cdf54dec-b42e-450e-815e-4c4070acc6c0',
  title: 'Facilitator Loan Dollar',
  targetAudience: TargetAudience['TEACHERS'],
  instructor: 'Pizza',
  studyClass: StudyClass['TRAINING'],
  level: Level['BEGINNER'],
  locationType: LocationType['CENTER'],
  languages: Languages['FRENCH'],
};

export const sampleWithFullData: ITrainingCourse = {
  id: '20720489-d356-4c31-ace9-48396c7f909f',
  title: 'Awesome',
  summary: 'Handmade',
  targetAudience: TargetAudience['STUDENTS'],
  instructor: 'Architect SSL',
  studyClass: StudyClass['HIGH_SCHOOL'],
  level: Level['INTERMEDIATE'],
  locationType: LocationType['CENTER'],
  duration: 'throughput Corporate Gorgeous',
  languages: Languages['ENGLISH'],
};

export const sampleWithNewData: NewTrainingCourse = {
  title: 'interfaces Health',
  targetAudience: TargetAudience['PROFESSIONALS'],
  studyClass: StudyClass['BACHELOR1'],
  level: Level['INTERMEDIATE'],
  locationType: LocationType['UNIVERSITY'],
  languages: Languages['FRENCH'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
