jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobDetailsService } from '../service/job-details.service';

import { JobDetailsComponent } from './job-details.component';

describe('Component Tests', () => {
  describe('JobDetails Management Component', () => {
    let comp: JobDetailsComponent;
    let fixture: ComponentFixture<JobDetailsComponent>;
    let service: JobDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JobDetailsComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(JobDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(JobDetailsService);

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
      expect(comp.jobDetails?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
