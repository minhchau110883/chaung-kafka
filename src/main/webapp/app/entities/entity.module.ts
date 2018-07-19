import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ChaungKafkaCampaignModule } from './campaign/campaign.module';
import { ChaungKafkaPointModule } from './point/point.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ChaungKafkaCampaignModule,
        ChaungKafkaPointModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChaungKafkaEntityModule {}
