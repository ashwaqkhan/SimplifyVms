import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobDetailsDetailComponent } from './job-details-detail.component';

describe('Component Tests', () => {
  describe('JobDetails Management Detail Component', () => {
    let comp: JobDetailsDetailComponent;
    let fixture: ComponentFixture<JobDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [JobDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ jobDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(JobDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
