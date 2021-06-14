jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BasicDetailsService } from '../service/basic-details.service';

import { BasicDetailsComponent } from './basic-details.component';

describe('Component Tests', () => {
  describe('BasicDetails Management Component', () => {
    let comp: BasicDetailsComponent;
    let fixture: ComponentFixture<BasicDetailsComponent>;
    let service: BasicDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BasicDetailsComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(BasicDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BasicDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BasicDetailsService);

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
      expect(comp.basicDetails?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
