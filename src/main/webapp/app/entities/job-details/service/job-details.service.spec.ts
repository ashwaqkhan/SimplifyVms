import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SkillReq } from 'app/entities/enumerations/skill-req.model';
import { ReqEnglish } from 'app/entities/enumerations/req-english.model';
import { DepositCharged } from 'app/entities/enumerations/deposit-charged.model';
import { IJobDetails, JobDetails } from '../job-details.model';

import { JobDetailsService } from './job-details.service';

describe('Service Tests', () => {
  describe('JobDetails Service', () => {
    let service: JobDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IJobDetails;
    let expectedResult: IJobDetails | IJobDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(JobDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        requiredSkills: SkillReq.Computer_or_Laptop_Ownersip,
        english: ReqEnglish.NOEnglish,
        jobDescription: 'AAAAAAA',
        securityDepositCharged: DepositCharged.Yes,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a JobDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new JobDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a JobDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            requiredSkills: 'BBBBBB',
            english: 'BBBBBB',
            jobDescription: 'BBBBBB',
            securityDepositCharged: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a JobDetails', () => {
        const patchObject = Object.assign(
          {
            requiredSkills: 'BBBBBB',
            jobDescription: 'BBBBBB',
          },
          new JobDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of JobDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            requiredSkills: 'BBBBBB',
            english: 'BBBBBB',
            jobDescription: 'BBBBBB',
            securityDepositCharged: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a JobDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addJobDetailsToCollectionIfMissing', () => {
        it('should add a JobDetails to an empty array', () => {
          const jobDetails: IJobDetails = { id: 123 };
          expectedResult = service.addJobDetailsToCollectionIfMissing([], jobDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jobDetails);
        });

        it('should not add a JobDetails to an array that contains it', () => {
          const jobDetails: IJobDetails = { id: 123 };
          const jobDetailsCollection: IJobDetails[] = [
            {
              ...jobDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addJobDetailsToCollectionIfMissing(jobDetailsCollection, jobDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a JobDetails to an array that doesn't contain it", () => {
          const jobDetails: IJobDetails = { id: 123 };
          const jobDetailsCollection: IJobDetails[] = [{ id: 456 }];
          expectedResult = service.addJobDetailsToCollectionIfMissing(jobDetailsCollection, jobDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jobDetails);
        });

        it('should add only unique JobDetails to an array', () => {
          const jobDetailsArray: IJobDetails[] = [{ id: 123 }, { id: 456 }, { id: 81223 }];
          const jobDetailsCollection: IJobDetails[] = [{ id: 123 }];
          expectedResult = service.addJobDetailsToCollectionIfMissing(jobDetailsCollection, ...jobDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const jobDetails: IJobDetails = { id: 123 };
          const jobDetails2: IJobDetails = { id: 456 };
          expectedResult = service.addJobDetailsToCollectionIfMissing([], jobDetails, jobDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jobDetails);
          expect(expectedResult).toContain(jobDetails2);
        });

        it('should accept null and undefined values', () => {
          const jobDetails: IJobDetails = { id: 123 };
          expectedResult = service.addJobDetailsToCollectionIfMissing([], null, jobDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jobDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
