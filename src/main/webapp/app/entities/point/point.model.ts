import { BaseEntity } from './../../shared';

export class Point implements BaseEntity {
    constructor(
        public id?: number,
        public outletId?: number,
        public terminalId?: number,
        public merchantId?: number,
        public userId?: number,
        public campaignId?: number,
        public paymentTransaction?: number,
        public paymentType?: string,
        public amount?: number,
        public point?: number,
    ) {
    }
}
