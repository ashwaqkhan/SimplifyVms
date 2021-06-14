import { IJobDetails } from 'app/entities/job-details/job-details.model';
import { IApply } from 'app/entities/apply/apply.model';
import { JobType } from 'app/entities/enumerations/job-type.model';
import { Jobshift } from 'app/entities/enumerations/jobshift.model';
import { Qualification } from 'app/entities/enumerations/qualification.model';
import { RequiredExp } from 'app/entities/enumerations/required-exp.model';
import { GenderReq } from 'app/entities/enumerations/gender-req.model';

export interface IBasicDetails {
  id?: number;
  jobRole?: string;
  workFromHome?: boolean;
  type?: JobType;
  shift?: Jobshift;
  minSalary?: number | null;
  maxSalRY?: number | null;
  openings?: number;
  workingDays?: string;
  workTimings?: string;
  minEducation?: Qualification;
  experience?: RequiredExp;
  gender?: GenderReq;
  jobDetails?: IJobDetails | null;
  apply?: IApply | null;
}

export class BasicDetails implements IBasicDetails {
  constructor(
    public id?: number,
    public jobRole?: string,
    public workFromHome?: boolean,
    public type?: JobType,
    public shift?: Jobshift,
    public minSalary?: number | null,
    public maxSalRY?: number | null,
    public openings?: number,
    public workingDays?: string,
    public workTimings?: string,
    public minEducation?: Qualification,
    public experience?: RequiredExp,
    public gender?: GenderReq,
    public jobDetails?: IJobDetails | null,
    public apply?: IApply | null
  ) {
    this.workFromHome = this.workFromHome ?? false;
  }
}

export function getBasicDetailsIdentifier(basicDetails: IBasicDetails): number | undefined {
  return basicDetails.id;
}
