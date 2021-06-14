import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInterviewInformation, InterviewInformation } from '../interview-information.model';
import { InterviewInformationService } from '../service/interview-information.service';

@Component({
  selector: 'jhi-interview-information-update',
  templateUrl: './interview-information-update.component.html',
})
export class InterviewInformationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    companyName: [null, [Validators.required]],
    recruitersName: [],
    hRwhatsappNumber: [],
    contactEmail: [],
    buildingName: [],
    city: [],
    area: [],
    recieveApplicationsFrom: [],
  });

  constructor(
    protected interviewInformationService: InterviewInformationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewInformation }) => {
      this.updateForm(interviewInformation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interviewInformation = this.createFromForm();
    if (interviewInformation.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewInformationService.update(interviewInformation));
    } else {
      this.subscribeToSaveResponse(this.interviewInformationService.create(interviewInformation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewInformation>>): void {
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

  protected updateForm(interviewInformation: IInterviewInformation): void {
    this.editForm.patchValue({
      id: interviewInformation.id,
      companyName: interviewInformation.companyName,
      recruitersName: interviewInformation.recruitersName,
      hRwhatsappNumber: interviewInformation.hRwhatsappNumber,
      contactEmail: interviewInformation.contactEmail,
      buildingName: interviewInformation.buildingName,
      city: interviewInformation.city,
      area: interviewInformation.area,
      recieveApplicationsFrom: interviewInformation.recieveApplicationsFrom,
    });
  }

  protected createFromForm(): IInterviewInformation {
    return {
      ...new InterviewInformation(),
      id: this.editForm.get(['id'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      recruitersName: this.editForm.get(['recruitersName'])!.value,
      hRwhatsappNumber: this.editForm.get(['hRwhatsappNumber'])!.value,
      contactEmail: this.editForm.get(['contactEmail'])!.value,
      buildingName: this.editForm.get(['buildingName'])!.value,
      city: this.editForm.get(['city'])!.value,
      area: this.editForm.get(['area'])!.value,
      recieveApplicationsFrom: this.editForm.get(['recieveApplicationsFrom'])!.value,
    };
  }
}
