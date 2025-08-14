import { TargetAudience } from 'app/entities/enumerations/target-audience.model';
import { StudyClass } from 'app/entities/enumerations/study-class.model';
import { Level } from 'app/entities/enumerations/level.model';
import { LocationType } from 'app/entities/enumerations/location-type.model';
import { Languages } from 'app/entities/enumerations/languages.model';

export interface ITrainingCourse {
  id: string;
  title?: string | null;
  summary?: string | null;
  targetAudience?: TargetAudience | null;
  instructor?: string | null;
  studyClass?: StudyClass | null;
  level?: Level | null;
  locationType?: LocationType | null;
  duration?: string | null;
  languages?: Languages | null;
  presentation?: string | null;
}

export type NewTrainingCourse = Omit<ITrainingCourse, 'id'> & { id: null };
