import { NgModule } from '@angular/core';

import { ChaungKafkaSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ChaungKafkaSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ChaungKafkaSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ChaungKafkaSharedCommonModule {}
