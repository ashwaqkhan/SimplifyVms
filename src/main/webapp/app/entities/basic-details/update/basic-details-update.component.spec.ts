jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BasicDetailsService } from '../service/basic-details.service';
import { IBasicDetails, BasicDetails } from '../basic-details.model';
import { IJobDetails } from 'app/entities/job-details/job-details.model';
import { JobDetailsService } from 'app/entities/job-details/service/job-details.service';
import { IApply } from 'app/entities/apply/apply.model';
import { ApplyService } from 'app/entities/apply/service/apply.service';

import { BasicDetailsUpdateComponent } from './basic-details-update.component';

describe('Component Tests', () => {
  describe('BasicDetails Management Update Component', () => {
    let comp: BasicDetailsUpdateComponent;
    let fixture: ComponentFixture<BasicDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let basicDetailsService: BasicDetailsService;
    let jobDetailsService: JobDetailsService;
    let applyService: ApplyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BasicDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BasicDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BasicDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      basicDetailsService = TestBed.inject(BasicDetailsService);
      jobDetailsService = TestBed.inject(JobDetailsService);
      applyService = TestBed.inject(ApplyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call jobDetails query and add missing value', () => {
        const basicDetails: IBasicDetails = { id: 456 };
        const jobDetails: IJobDetails = { id: 69084 };
        basicDetails.jobDetails = jobDetails;

        const jobDetailsCollection: IJobDetails[] = [{ id: 76865 }];
        spyOn(jobDetailsService, 'query').and.returnValue(of(new HttpResponse({ body: jobDetailsCollection })));
        const expectedCollection: IJobDetails[] = [jobDetails, ...jobDetailsCollection];
        spyOn(jobDetailsService, 'addJobDetailsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        expect(jobDetailsService.query).toHaveBeenCalled();
        expect(jobDetailsService.addJobDetailsToCollectionIfMissing).toHaveBeenCalledWith(jobDetailsCollection, jobDetails);
        expect(comp.jobDetailsCollection).toEqual(expectedCollection);
      });

      it('Should call Apply query and add missing value', () => {
        const basicDetails: IBasicDetails = { id: 456 };
        const apply: IApply = { id: 27308 };
        basicDetails.apply = apply;

        const applyCollection: IApply[] = [{ id: 12334 }];
        spyOn(applyService, 'query').and.returnValue(of(new HttpResponse({ body: applyCollection })));
        const additionalApplies = [apply];
        const expectedCollection: IApply[] = [...additionalApplies, ...applyCollection];
        spyOn(applyService, 'addApplyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        expect(applyService.query).toHaveBeenCalled();
        expect(applyService.addApplyToCollectionIfMissing).toHaveBeenCalledWith(applyCollection, ...additionalApplies);
        expect(comp.appliesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const basicDetails: IBasicDetails = { id: 456 };
        const jobDetails: IJobDetails = { id: 48701 };
        basicDetails.jobDetails = jobDetails;
        const apply: IApply = { id: 52230 };
        basicDetails.apply = apply;

        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(basicDetails));
        expect(comp.jobDetailsCollection).toContain(jobDetails);
        expect(comp.appliesSharedCollection).toContain(apply);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const basicDetails = { id: 123 };
        spyOn(basicDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: basicDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(basicDetailsService.update).toHaveBeenCalledWith(basicDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const basicDetails = new BasicDetails();
        spyOn(basicDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: basicDetails }));
        saveSubject.complete();

        // THEN
        expect(basicDetailsService.create).toHaveBeenCalledWith(basicDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const basicDetails = { id: 123 };
        spyOn(basicDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(basicDetailsService.update).toHaveBeenCalledWith(basicDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackJobDetailsById', () => {
        it('Should return tracked JobDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackJobDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackApplyById', () => {
        it('Should return tracked Apply primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
