import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChaungKafkaSharedModule } from '../../shared';
import {
    PointService,
    PointPopupService,
    PointComponent,
    PointDetailComponent,
    PointDialogComponent,
    PointPopupComponent,
    PointDeletePopupComponent,
    PointDeleteDialogComponent,
    pointRoute,
    pointPopupRoute,
    PointResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...pointRoute,
    ...pointPopupRoute,
];

@NgModule({
    imports: [
        ChaungKafkaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PointComponent,
        PointDetailComponent,
        PointDialogComponent,
        PointDeleteDialogComponent,
        PointPopupComponent,
        PointDeletePopupComponent,
    ],
    entryComponents: [
        PointComponent,
        PointDialogComponent,
        PointPopupComponent,
        PointDeleteDialogComponent,
        PointDeletePopupComponent,
    ],
    providers: [
        PointService,
        PointPopupService,
        PointResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChaungKafkaPointModule {}
