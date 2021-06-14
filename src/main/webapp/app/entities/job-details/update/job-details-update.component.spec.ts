jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JobDetailsService } from '../service/job-details.service';
import { IJobDetails, JobDetails } from '../job-details.model';

import { JobDetailsUpdateComponent } from './job-details-update.component';

describe('Component Tests', () => {
  describe('JobDetails Management Update Component', () => {
    let comp: JobDetailsUpdateComponent;
    let fixture: ComponentFixture<JobDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let jobDetailsService: JobDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JobDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JobDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      jobDetailsService = TestBed.inject(JobDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const jobDetails: IJobDetails = { id: 456 };

        activatedRoute.data = of({ jobDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(jobDetails));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobDetails = { id: 123 };
        spyOn(jobDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jobDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(jobDetailsService.update).toHaveBeenCalledWith(jobDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobDetails = new JobDetails();
        spyOn(jobDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jobDetails }));
        saveSubject.complete();

        // THEN
        expect(jobDetailsService.create).toHaveBeenCalledWith(jobDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobDetails = { id: 123 };
        spyOn(jobDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(jobDetailsService.update).toHaveBeenCalledWith(jobDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
