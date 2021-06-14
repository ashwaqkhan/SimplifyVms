import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IJobDetails, JobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';
import { IInterviewInformation } from 'app/entities/interview-information/interview-information.model';
import { InterviewInformationService } from 'app/entities/interview-information/service/interview-information.service';

@Component({
  selector: 'jhi-job-details-update',
  templateUrl: './job-details-update.component.html',
})
export class JobDetailsUpdateComponent implements OnInit {
  isSaving = false;

  interviewInformationsCollection: IInterviewInformation[] = [];

  editForm = this.fb.group({
    id: [],
    requiredSkills: [null, [Validators.required]],
    english: [null, [Validators.required]],
    jobDescription: [null, [Validators.required]],
    securityDepositCharged: [null, [Validators.required]],
    interviewInformation: [],
  });

  constructor(
    protected jobDetailsService: JobDetailsService,
    protected interviewInformationService: InterviewInformationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobDetails }) => {
      this.updateForm(jobDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobDetails = this.createFromForm();
    if (jobDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.jobDetailsService.update(jobDetails));
    } else {
      this.subscribeToSaveResponse(this.jobDetailsService.create(jobDetails));
    }
  }

  trackInterviewInformationById(index: number, item: IInterviewInformation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobDetails>>): void {
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

  protected updateForm(jobDetails: IJobDetails): void {
    this.editForm.patchValue({
      id: jobDetails.id,
      requiredSkills: jobDetails.requiredSkills,
      english: jobDetails.english,
      jobDescription: jobDetails.jobDescription,
      securityDepositCharged: jobDetails.securityDepositCharged,
      interviewInformation: jobDetails.interviewInformation,
    });

    this.interviewInformationsCollection = this.interviewInformationService.addInterviewInformationToCollectionIfMissing(
      this.interviewInformationsCollection,
      jobDetails.interviewInformation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.interviewInformationService
      .query({ filter: 'jobdetails-is-null' })
      .pipe(map((res: HttpResponse<IInterviewInformation[]>) => res.body ?? []))
      .pipe(
        map((interviewInformations: IInterviewInformation[]) =>
          this.interviewInformationService.addInterviewInformationToCollectionIfMissing(
            interviewInformations,
            this.editForm.get('interviewInformation')!.value
          )
        )
      )
      .subscribe((interviewInformations: IInterviewInformation[]) => (this.interviewInformationsCollection = interviewInformations));
  }

  protected createFromForm(): IJobDetails {
    return {
      ...new JobDetails(),
      id: this.editForm.get(['id'])!.value,
      requiredSkills: this.editForm.get(['requiredSkills'])!.value,
      english: this.editForm.get(['english'])!.value,
      jobDescription: this.editForm.get(['jobDescription'])!.value,
      securityDepositCharged: this.editForm.get(['securityDepositCharged'])!.value,
      interviewInformation: this.editForm.get(['interviewInformation'])!.value,
    };
  }
}
