import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApply, Apply } from '../apply.model';

import { ApplyService } from './apply.service';

describe('Service Tests', () => {
  describe('Apply Service', () => {
    let service: ApplyService;
    let httpMock: HttpTestingController;
    let elemDefault: IApply;
    let expectedResult: IApply | IApply[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApplyService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        mobileNo: 0,
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

      it('should create a Apply', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Apply()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Apply', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            mobileNo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Apply', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Apply()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Apply', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            mobileNo: 1,
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

      it('should delete a Apply', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApplyToCollectionIfMissing', () => {
        it('should add a Apply to an empty array', () => {
          const apply: IApply = { id: 123 };
          expectedResult = service.addApplyToCollectionIfMissing([], apply);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(apply);
        });

        it('should not add a Apply to an array that contains it', () => {
          const apply: IApply = { id: 123 };
          const applyCollection: IApply[] = [
            {
              ...apply,
            },
            { id: 456 },
          ];
          expectedResult = service.addApplyToCollectionIfMissing(applyCollection, apply);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Apply to an array that doesn't contain it", () => {
          const apply: IApply = { id: 123 };
          const applyCollection: IApply[] = [{ id: 456 }];
          expectedResult = service.addApplyToCollectionIfMissing(applyCollection, apply);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(apply);
        });

        it('should add only unique Apply to an array', () => {
          const applyArray: IApply[] = [{ id: 123 }, { id: 456 }, { id: 82338 }];
          const applyCollection: IApply[] = [{ id: 123 }];
          expectedResult = service.addApplyToCollectionIfMissing(applyCollection, ...applyArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const apply: IApply = { id: 123 };
          const apply2: IApply = { id: 456 };
          expectedResult = service.addApplyToCollectionIfMissing([], apply, apply2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(apply);
          expect(expectedResult).toContain(apply2);
        });

        it('should accept null and undefined values', () => {
          const apply: IApply = { id: 123 };
          expectedResult = service.addApplyToCollectionIfMissing([], null, apply, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(apply);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
