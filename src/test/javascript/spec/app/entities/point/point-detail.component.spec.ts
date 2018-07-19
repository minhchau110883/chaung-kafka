/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ChaungKafkaTestModule } from '../../../test.module';
import { PointDetailComponent } from '../../../../../../main/webapp/app/entities/point/point-detail.component';
import { PointService } from '../../../../../../main/webapp/app/entities/point/point.service';
import { Point } from '../../../../../../main/webapp/app/entities/point/point.model';

describe('Component Tests', () => {

    describe('Point Management Detail Component', () => {
        let comp: PointDetailComponent;
        let fixture: ComponentFixture<PointDetailComponent>;
        let service: PointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChaungKafkaTestModule],
                declarations: [PointDetailComponent],
                providers: [
                    PointService
                ]
            })
            .overrideTemplate(PointDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PointDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Point(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.point).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
