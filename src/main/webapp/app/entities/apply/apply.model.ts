export interface IApply {
  id?: number;
  name?: string;
  mobileNo?: number;
}

export class Apply implements IApply {
  constructor(public id?: number, public name?: string, public mobileNo?: number) {}
}

export function getApplyIdentifier(apply: IApply): number | undefined {
  return apply.id;
}
