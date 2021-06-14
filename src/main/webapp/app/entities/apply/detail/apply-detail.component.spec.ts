import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplyDetailComponent } from './apply-detail.component';

describe('Component Tests', () => {
  describe('Apply Management Detail Component', () => {
    let comp: ApplyDetailComponent;
    let fixture: ComponentFixture<ApplyDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApplyDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ apply: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApplyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load apply on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.apply).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
