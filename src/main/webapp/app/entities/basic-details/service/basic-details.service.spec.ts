import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { JobType } from 'app/entities/enumerations/job-type.model';
import { Qualification } from 'app/entities/enumerations/qualification.model';
import { RequiredExp } from 'app/entities/enumerations/required-exp.model';
import { GenderReq } from 'app/entities/enumerations/gender-req.model';
import { IBasicDetails, BasicDetails } from '../basic-details.model';

import { BasicDetailsService } from './basic-details.service';

describe('Service Tests', () => {
  describe('BasicDetails Service', () => {
    let service: BasicDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IBasicDetails;
    let expectedResult: IBasicDetails | IBasicDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BasicDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        jobRole: 'AAAAAAA',
        workFromHome: false,
        type: JobType.PartTime,
        minSalary: 0,
        maxSalRY: 0,
        openings: 0,
        workingDays: 'AAAAAAA',
        workTimings: 'AAAAAAA',
        minEducation: Qualification.BelowTenth,
        experience: RequiredExp.Fresher,
        gender: GenderReq.Male,
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

      it('should create a BasicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BasicDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BasicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            jobRole: 'BBBBBB',
            workFromHome: true,
            type: 'BBBBBB',
            minSalary: 1,
            maxSalRY: 1,
            openings: 1,
            workingDays: 'BBBBBB',
            workTimings: 'BBBBBB',
            minEducation: 'BBBBBB',
            experience: 'BBBBBB',
            gender: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BasicDetails', () => {
        const patchObject = Object.assign(
          {
            jobRole: 'BBBBBB',
            type: 'BBBBBB',
            minSalary: 1,
            maxSalRY: 1,
            openings: 1,
            workingDays: 'BBBBBB',
            minEducation: 'BBBBBB',
            gender: 'BBBBBB',
          },
          new BasicDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BasicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            jobRole: 'BBBBBB',
            workFromHome: true,
            type: 'BBBBBB',
            minSalary: 1,
            maxSalRY: 1,
            openings: 1,
            workingDays: 'BBBBBB',
            workTimings: 'BBBBBB',
            minEducation: 'BBBBBB',
            experience: 'BBBBBB',
            gender: 'BBBBBB',
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

      it('should delete a BasicDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBasicDetailsToCollectionIfMissing', () => {
        it('should add a BasicDetails to an empty array', () => {
          const basicDetails: IBasicDetails = { id: 123 };
          expectedResult = service.addBasicDetailsToCollectionIfMissing([], basicDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(basicDetails);
        });

        it('should not add a BasicDetails to an array that contains it', () => {
          const basicDetails: IBasicDetails = { id: 123 };
          const basicDetailsCollection: IBasicDetails[] = [
            {
              ...basicDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addBasicDetailsToCollectionIfMissing(basicDetailsCollection, basicDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BasicDetails to an array that doesn't contain it", () => {
          const basicDetails: IBasicDetails = { id: 123 };
          const basicDetailsCollection: IBasicDetails[] = [{ id: 456 }];
          expectedResult = service.addBasicDetailsToCollectionIfMissing(basicDetailsCollection, basicDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(basicDetails);
        });

        it('should add only unique BasicDetails to an array', () => {
          const basicDetailsArray: IBasicDetails[] = [{ id: 123 }, { id: 456 }, { id: 5130 }];
          const basicDetailsCollection: IBasicDetails[] = [{ id: 123 }];
          expectedResult = service.addBasicDetailsToCollectionIfMissing(basicDetailsCollection, ...basicDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const basicDetails: IBasicDetails = { id: 123 };
          const basicDetails2: IBasicDetails = { id: 456 };
          expectedResult = service.addBasicDetailsToCollectionIfMissing([], basicDetails, basicDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(basicDetails);
          expect(expectedResult).toContain(basicDetails2);
        });

        it('should accept null and undefined values', () => {
          const basicDetails: IBasicDetails = { id: 123 };
          expectedResult = service.addBasicDetailsToCollectionIfMissing([], null, basicDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(basicDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
