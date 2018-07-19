export interface IPoint {
    id?: number;
    outletId?: number;
    terminalId?: number;
    merchantId?: number;
    userId?: number;
    campaignId?: number;
    paymentTransaction?: number;
    paymentType?: string;
    amount?: number;
    point?: number;
    campaignId?: number;
}

export class Point implements IPoint {
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
        public campaignId?: number
    ) {}
}
