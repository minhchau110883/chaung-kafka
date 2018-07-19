import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChaungKafkaSharedModule } from '../../shared';
import {
    CampaignService,
    CampaignPopupService,
    CampaignComponent,
    CampaignDetailComponent,
    CampaignDialogComponent,
    CampaignPopupComponent,
    CampaignDeletePopupComponent,
    CampaignDeleteDialogComponent,
    campaignRoute,
    campaignPopupRoute,
    CampaignResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...campaignRoute,
    ...campaignPopupRoute,
];

@NgModule({
    imports: [
        ChaungKafkaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CampaignComponent,
        CampaignDetailComponent,
        CampaignDialogComponent,
        CampaignDeleteDialogComponent,
        CampaignPopupComponent,
        CampaignDeletePopupComponent,
    ],
    entryComponents: [
        CampaignComponent,
        CampaignDialogComponent,
        CampaignPopupComponent,
        CampaignDeleteDialogComponent,
        CampaignDeletePopupComponent,
    ],
    providers: [
        CampaignService,
        CampaignPopupService,
        CampaignResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChaungKafkaCampaignModule {}
