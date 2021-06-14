import { Application } from 'app/entities/enumerations/application.model';

export interface IInterviewInformation {
  id?: number;
  companyName?: string;
  recruitersName?: string | null;
  hRwhatsappNumber?: number | null;
  contactEmail?: string | null;
  buildingName?: string | null;
  city?: string | null;
  area?: string | null;
  recieveApplicationsFrom?: Application | null;
}

export class InterviewInformation implements IInterviewInformation {
  constructor(
    public id?: number,
    public companyName?: string,
    public recruitersName?: string | null,
    public hRwhatsappNumber?: number | null,
    public contactEmail?: string | null,
    public buildingName?: string | null,
    public city?: string | null,
    public area?: string | null,
    public recieveApplicationsFrom?: Application | null
  ) {}
}

export function getInterviewInformationIdentifier(interviewInformation: IInterviewInformation): number | undefined {
  return interviewInformation.id;
}
