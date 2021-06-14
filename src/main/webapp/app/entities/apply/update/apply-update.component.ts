import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApply, Apply } from '../apply.model';
import { ApplyService } from '../service/apply.service';

@Component({
  selector: 'jhi-apply-update',
  templateUrl: './apply-update.component.html',
})
export class ApplyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    mobileNo: [null, [Validators.required]],
  });

  constructor(protected applyService: ApplyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apply }) => {
      this.updateForm(apply);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apply = this.createFromForm();
    if (apply.id !== undefined) {
      this.subscribeToSaveResponse(this.applyService.update(apply));
    } else {
      this.subscribeToSaveResponse(this.applyService.create(apply));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApply>>): void {
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

  protected updateForm(apply: IApply): void {
    this.editForm.patchValue({
      id: apply.id,
      name: apply.name,
      mobileNo: apply.mobileNo,
    });
  }

  protected createFromForm(): IApply {
    return {
      ...new Apply(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      mobileNo: this.editForm.get(['mobileNo'])!.value,
    };
  }
}
