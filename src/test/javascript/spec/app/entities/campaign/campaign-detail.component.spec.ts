/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ChaungKafkaTestModule } from '../../../test.module';
import { CampaignDetailComponent } from '../../../../../../main/webapp/app/entities/campaign/campaign-detail.component';
import { CampaignService } from '../../../../../../main/webapp/app/entities/campaign/campaign.service';
import { Campaign } from '../../../../../../main/webapp/app/entities/campaign/campaign.model';

describe('Component Tests', () => {

    describe('Campaign Management Detail Component', () => {
        let comp: CampaignDetailComponent;
        let fixture: ComponentFixture<CampaignDetailComponent>;
        let service: CampaignService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChaungKafkaTestModule],
                declarations: [CampaignDetailComponent],
                providers: [
                    CampaignService
                ]
            })
            .overrideTemplate(CampaignDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CampaignDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CampaignService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Campaign(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.campaign).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
