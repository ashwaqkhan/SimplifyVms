import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Application } from 'app/entities/enumerations/application.model';
import { IInterviewInformation, InterviewInformation } from '../interview-information.model';

import { InterviewInformationService } from './interview-information.service';

describe('Service Tests', () => {
  describe('InterviewInformation Service', () => {
    let service: InterviewInformationService;
    let httpMock: HttpTestingController;
    let elemDefault: IInterviewInformation;
    let expectedResult: IInterviewInformation | IInterviewInformation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InterviewInformationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        companyName: 'AAAAAAA',
        recruitersName: 'AAAAAAA',
        hRwhatsappNumber: 0,
        contactEmail: 'AAAAAAA',
        buildingName: 'AAAAAAA',
        city: 'AAAAAAA',
        area: 'AAAAAAA',
        recieveApplicationsFrom: Application.EntireCity,
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

      it('should create a InterviewInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new InterviewInformation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a InterviewInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            companyName: 'BBBBBB',
            recruitersName: 'BBBBBB',
            hRwhatsappNumber: 1,
            contactEmail: 'BBBBBB',
            buildingName: 'BBBBBB',
            city: 'BBBBBB',
            area: 'BBBBBB',
            recieveApplicationsFrom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a InterviewInformation', () => {
        const patchObject = Object.assign(
          {
            buildingName: 'BBBBBB',
            area: 'BBBBBB',
          },
          new InterviewInformation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of InterviewInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            companyName: 'BBBBBB',
            recruitersName: 'BBBBBB',
            hRwhatsappNumber: 1,
            contactEmail: 'BBBBBB',
            buildingName: 'BBBBBB',
            city: 'BBBBBB',
            area: 'BBBBBB',
            recieveApplicationsFrom: 'BBBBBB',
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

      it('should delete a InterviewInformation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInterviewInformationToCollectionIfMissing', () => {
        it('should add a InterviewInformation to an empty array', () => {
          const interviewInformation: IInterviewInformation = { id: 123 };
          expectedResult = service.addInterviewInformationToCollectionIfMissing([], interviewInformation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(interviewInformation);
        });

        it('should not add a InterviewInformation to an array that contains it', () => {
          const interviewInformation: IInterviewInformation = { id: 123 };
          const interviewInformationCollection: IInterviewInformation[] = [
            {
              ...interviewInformation,
            },
            { id: 456 },
          ];
          expectedResult = service.addInterviewInformationToCollectionIfMissing(interviewInformationCollection, interviewInformation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a InterviewInformation to an array that doesn't contain it", () => {
          const interviewInformation: IInterviewInformation = { id: 123 };
          const interviewInformationCollection: IInterviewInformation[] = [{ id: 456 }];
          expectedResult = service.addInterviewInformationToCollectionIfMissing(interviewInformationCollection, interviewInformation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(interviewInformation);
        });

        it('should add only unique InterviewInformation to an array', () => {
          const interviewInformationArray: IInterviewInformation[] = [{ id: 123 }, { id: 456 }, { id: 89170 }];
          const interviewInformationCollection: IInterviewInformation[] = [{ id: 123 }];
          expectedResult = service.addInterviewInformationToCollectionIfMissing(
            interviewInformationCollection,
            ...interviewInformationArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const interviewInformation: IInterviewInformation = { id: 123 };
          const interviewInformation2: IInterviewInformation = { id: 456 };
          expectedResult = service.addInterviewInformationToCollectionIfMissing([], interviewInformation, interviewInformation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(interviewInformation);
          expect(expectedResult).toContain(interviewInformation2);
        });

        it('should accept null and undefined values', () => {
          const interviewInformation: IInterviewInformation = { id: 123 };
          expectedResult = service.addInterviewInformationToCollectionIfMissing([], null, interviewInformation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(interviewInformation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
