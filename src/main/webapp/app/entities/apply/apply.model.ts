import { IBasicDetails } from 'app/entities/basic-details/basic-details.model';

export interface IApply {
  id?: number;
  name?: string;
  mobileNo?: number;
  basicDetails?: IBasicDetails[] | null;
}

export class Apply implements IApply {
  constructor(public id?: number, public name?: string, public mobileNo?: number, public basicDetails?: IBasicDetails[] | null) {}
}

export function getApplyIdentifier(apply: IApply): number | undefined {
  return apply.id;
}
