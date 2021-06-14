jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BasicDetailsService } from '../service/basic-details.service';
import { IBasicDetails, BasicDetails } from '../basic-details.model';

import { BasicDetailsUpdateComponent } from './basic-details-update.component';

describe('Component Tests', () => {
  describe('BasicDetails Management Update Component', () => {
    let comp: BasicDetailsUpdateComponent;
    let fixture: ComponentFixture<BasicDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let basicDetailsService: BasicDetailsService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const basicDetails: IBasicDetails = { id: 456 };

        activatedRoute.data = of({ basicDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(basicDetails));
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
  });
});
