import { IInterviewInformation } from 'app/entities/interview-information/interview-information.model';
import { SkillReq } from 'app/entities/enumerations/skill-req.model';
import { ReqEnglish } from 'app/entities/enumerations/req-english.model';
import { DepositCharged } from 'app/entities/enumerations/deposit-charged.model';

export interface IJobDetails {
  id?: number;
  requiredSkills?: SkillReq;
  english?: ReqEnglish;
  jobDescription?: string;
  securityDepositCharged?: DepositCharged;
  interviewInformation?: IInterviewInformation | null;
}

export class JobDetails implements IJobDetails {
  constructor(
    public id?: number,
    public requiredSkills?: SkillReq,
    public english?: ReqEnglish,
    public jobDescription?: string,
    public securityDepositCharged?: DepositCharged,
    public interviewInformation?: IInterviewInformation | null
  ) {}
}

export function getJobDetailsIdentifier(jobDetails: IJobDetails): number | undefined {
  return jobDetails.id;
}
