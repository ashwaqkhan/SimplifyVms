import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBasicDetails, BasicDetails } from '../basic-details.model';
import { BasicDetailsService } from '../service/basic-details.service';
import { IJobDetails } from 'app/entities/job-details/job-details.model';
import { JobDetailsService } from 'app/entities/job-details/service/job-details.service';
import { IApply } from 'app/entities/apply/apply.model';
import { ApplyService } from 'app/entities/apply/service/apply.service';

@Component({
  selector: 'jhi-basic-details-update',
  templateUrl: './basic-details-update.component.html',
})
export class BasicDetailsUpdateComponent implements OnInit {
  isSaving = false;

  jobDetailsCollection: IJobDetails[] = [];
  appliesSharedCollection: IApply[] = [];

  editForm = this.fb.group({
    id: [],
    jobRole: [null, [Validators.required]],
    workFromHome: [null, [Validators.required]],
    type: [null, [Validators.required]],
    shift: [null, [Validators.required]],
    minSalary: [],
    maxSalRY: [],
    openings: [null, [Validators.required]],
    workingDays: [null, [Validators.required]],
    workTimings: [null, [Validators.required]],
    minEducation: [null, [Validators.required]],
    experience: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    jobDetails: [],
    apply: [],
  });

  constructor(
    protected basicDetailsService: BasicDetailsService,
    protected jobDetailsService: JobDetailsService,
    protected applyService: ApplyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ basicDetails }) => {
      this.updateForm(basicDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const basicDetails = this.createFromForm();
    if (basicDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.basicDetailsService.update(basicDetails));
    } else {
      this.subscribeToSaveResponse(this.basicDetailsService.create(basicDetails));
    }
  }

  trackJobDetailsById(index: number, item: IJobDetails): number {
    return item.id!;
  }

  trackApplyById(index: number, item: IApply): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBasicDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(basicDetails: IBasicDetails): void {
    this.editForm.patchValue({
      id: basicDetails.id,
      jobRole: basicDetails.jobRole,
      workFromHome: basicDetails.workFromHome,
      type: basicDetails.type,
      shift: basicDetails.shift,
      minSalary: basicDetails.minSalary,
      maxSalRY: basicDetails.maxSalRY,
      openings: basicDetails.openings,
      workingDays: basicDetails.workingDays,
      workTimings: basicDetails.workTimings,
      minEducation: basicDetails.minEducation,
      experience: basicDetails.experience,
      gender: basicDetails.gender,
      jobDetails: basicDetails.jobDetails,
      apply: basicDetails.apply,
    });

    this.jobDetailsCollection = this.jobDetailsService.addJobDetailsToCollectionIfMissing(
      this.jobDetailsCollection,
      basicDetails.jobDetails
    );
    this.appliesSharedCollection = this.applyService.addApplyToCollectionIfMissing(this.appliesSharedCollection, basicDetails.apply);
  }

  protected loadRelationshipsOptions(): void {
    this.jobDetailsService
      .query({ filter: 'basicdetails-is-null' })
      .pipe(map((res: HttpResponse<IJobDetails[]>) => res.body ?? []))
      .pipe(
        map((jobDetails: IJobDetails[]) =>
          this.jobDetailsService.addJobDetailsToCollectionIfMissing(jobDetails, this.editForm.get('jobDetails')!.value)
        )
      )
      .subscribe((jobDetails: IJobDetails[]) => (this.jobDetailsCollection = jobDetails));

    this.applyService
      .query()
      .pipe(map((res: HttpResponse<IApply[]>) => res.body ?? []))
      .pipe(map((applies: IApply[]) => this.applyService.addApplyToCollectionIfMissing(applies, this.editForm.get('apply')!.value)))
      .subscribe((applies: IApply[]) => (this.appliesSharedCollection = applies));
  }

  protected createFromForm(): IBasicDetails {
    return {
      ...new BasicDetails(),
      id: this.editForm.get(['id'])!.value,
      jobRole: this.editForm.get(['jobRole'])!.value,
      workFromHome: this.editForm.get(['workFromHome'])!.value,
      type: this.editForm.get(['type'])!.value,
      shift: this.editForm.get(['shift'])!.value,
      minSalary: this.editForm.get(['minSalary'])!.value,
      maxSalRY: this.editForm.get(['maxSalRY'])!.value,
      openings: this.editForm.get(['openings'])!.value,
      workingDays: this.editForm.get(['workingDays'])!.value,
      workTimings: this.editForm.get(['workTimings'])!.value,
      minEducation: this.editForm.get(['minEducation'])!.value,
      experience: this.editForm.get(['experience'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      jobDetails: this.editForm.get(['jobDetails'])!.value,
      apply: this.editForm.get(['apply'])!.value,
    };
  }
}
