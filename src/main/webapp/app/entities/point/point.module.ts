import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChaungKafkaSharedModule } from 'app/shared';
import {
    PointComponent,
    PointDetailComponent,
    PointUpdateComponent,
    PointDeletePopupComponent,
    PointDeleteDialogComponent,
    pointRoute,
    pointPopupRoute
} from './';

const ENTITY_STATES = [...pointRoute, ...pointPopupRoute];

@NgModule({
    imports: [ChaungKafkaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PointComponent, PointDetailComponent, PointUpdateComponent, PointDeleteDialogComponent, PointDeletePopupComponent],
    entryComponents: [PointComponent, PointUpdateComponent, PointDeleteDialogComponent, PointDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChaungKafkaPointModule {}
