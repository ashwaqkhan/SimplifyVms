jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplyService } from '../service/apply.service';
import { IApply, Apply } from '../apply.model';

import { ApplyUpdateComponent } from './apply-update.component';

describe('Component Tests', () => {
  describe('Apply Management Update Component', () => {
    let comp: ApplyUpdateComponent;
    let fixture: ComponentFixture<ApplyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let applyService: ApplyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApplyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      applyService = TestBed.inject(ApplyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const apply: IApply = { id: 456 };

        activatedRoute.data = of({ apply });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(apply));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const apply = { id: 123 };
        spyOn(applyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ apply });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: apply }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(applyService.update).toHaveBeenCalledWith(apply);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const apply = new Apply();
        spyOn(applyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ apply });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: apply }));
        saveSubject.complete();

        // THEN
        expect(applyService.create).toHaveBeenCalledWith(apply);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const apply = { id: 123 };
        spyOn(applyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ apply });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(applyService.update).toHaveBeenCalledWith(apply);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
