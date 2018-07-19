/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ChaungKafkaTestModule } from '../../../test.module';
import { PointComponent } from '../../../../../../main/webapp/app/entities/point/point.component';
import { PointService } from '../../../../../../main/webapp/app/entities/point/point.service';
import { Point } from '../../../../../../main/webapp/app/entities/point/point.model';

describe('Component Tests', () => {

    describe('Point Management Component', () => {
        let comp: PointComponent;
        let fixture: ComponentFixture<PointComponent>;
        let service: PointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChaungKafkaTestModule],
                declarations: [PointComponent],
                providers: [
                    PointService
                ]
            })
            .overrideTemplate(PointComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PointComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Point(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.points[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
