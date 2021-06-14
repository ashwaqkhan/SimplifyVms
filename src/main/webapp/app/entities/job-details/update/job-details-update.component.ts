import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IJobDetails, JobDetails } from '../job-details.model';
import { JobDetailsService } from '../service/job-details.service';

@Component({
  selector: 'jhi-job-details-update',
  templateUrl: './job-details-update.component.html',
})
export class JobDetailsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    requiredSkills: [null, [Validators.required]],
    english: [null, [Validators.required]],
    jobDescription: [null, [Validators.required]],
    securityDepositCharged: [null, [Validators.required]],
  });

  constructor(protected jobDetailsService: JobDetailsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobDetails }) => {
      this.updateForm(jobDetails);
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
    });
  }

  protected createFromForm(): IJobDetails {
    return {
      ...new JobDetails(),
      id: this.editForm.get(['id'])!.value,
      requiredSkills: this.editForm.get(['requiredSkills'])!.value,
      english: this.editForm.get(['english'])!.value,
      jobDescription: this.editForm.get(['jobDescription'])!.value,
      securityDepositCharged: this.editForm.get(['securityDepositCharged'])!.value,
    };
  }
}
