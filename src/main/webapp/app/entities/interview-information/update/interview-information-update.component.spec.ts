jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InterviewInformationService } from '../service/interview-information.service';
import { IInterviewInformation, InterviewInformation } from '../interview-information.model';

import { InterviewInformationUpdateComponent } from './interview-information-update.component';

describe('Component Tests', () => {
  describe('InterviewInformation Management Update Component', () => {
    let comp: InterviewInformationUpdateComponent;
    let fixture: ComponentFixture<InterviewInformationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let interviewInformationService: InterviewInformationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InterviewInformationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InterviewInformationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewInformationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      interviewInformationService = TestBed.inject(InterviewInformationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const interviewInformation: IInterviewInformation = { id: 456 };

        activatedRoute.data = of({ interviewInformation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(interviewInformation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const interviewInformation = { id: 123 };
        spyOn(interviewInformationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ interviewInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: interviewInformation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(interviewInformationService.update).toHaveBeenCalledWith(interviewInformation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const interviewInformation = new InterviewInformation();
        spyOn(interviewInformationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ interviewInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: interviewInformation }));
        saveSubject.complete();

        // THEN
        expect(interviewInformationService.create).toHaveBeenCalledWith(interviewInformation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const interviewInformation = { id: 123 };
        spyOn(interviewInformationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ interviewInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(interviewInformationService.update).toHaveBeenCalledWith(interviewInformation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
