import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InterviewInformationDetailComponent } from './interview-information-detail.component';

describe('Component Tests', () => {
  describe('InterviewInformation Management Detail Component', () => {
    let comp: InterviewInformationDetailComponent;
    let fixture: ComponentFixture<InterviewInformationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InterviewInformationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ interviewInformation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InterviewInformationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterviewInformationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interviewInformation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interviewInformation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
