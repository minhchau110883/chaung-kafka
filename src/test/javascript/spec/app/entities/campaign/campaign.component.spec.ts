/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ChaungKafkaTestModule } from '../../../test.module';
import { CampaignComponent } from '../../../../../../main/webapp/app/entities/campaign/campaign.component';
import { CampaignService } from '../../../../../../main/webapp/app/entities/campaign/campaign.service';
import { Campaign } from '../../../../../../main/webapp/app/entities/campaign/campaign.model';

describe('Component Tests', () => {

    describe('Campaign Management Component', () => {
        let comp: CampaignComponent;
        let fixture: ComponentFixture<CampaignComponent>;
        let service: CampaignService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChaungKafkaTestModule],
                declarations: [CampaignComponent],
                providers: [
                    CampaignService
                ]
            })
            .overrideTemplate(CampaignComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CampaignComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CampaignService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Campaign(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.campaigns[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
