jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplyService } from '../service/apply.service';

import { ApplyComponent } from './apply.component';

describe('Component Tests', () => {
  describe('Apply Management Component', () => {
    let comp: ApplyComponent;
    let fixture: ComponentFixture<ApplyComponent>;
    let service: ApplyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplyComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ApplyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplyComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ApplyService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.applies?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
