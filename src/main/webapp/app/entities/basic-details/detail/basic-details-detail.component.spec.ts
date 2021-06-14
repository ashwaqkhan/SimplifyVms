import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BasicDetailsDetailComponent } from './basic-details-detail.component';

describe('Component Tests', () => {
  describe('BasicDetails Management Detail Component', () => {
    let comp: BasicDetailsDetailComponent;
    let fixture: ComponentFixture<BasicDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BasicDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ basicDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BasicDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BasicDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load basicDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.basicDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
