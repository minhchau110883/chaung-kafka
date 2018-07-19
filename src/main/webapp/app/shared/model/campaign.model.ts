export const enum UserType {
    CUSTOMER = 'CUSTOMER',
    MERCHANT = 'MERCHANT'
}

export interface ICampaign {
    id?: number;
    name?: string;
    minAmount?: number;
    maxAmount?: number;
    point?: number;
    type?: UserType;
}

export class Campaign implements ICampaign {
    constructor(
        public id?: number,
        public name?: string,
        public minAmount?: number,
        public maxAmount?: number,
        public point?: number,
        public type?: UserType
    ) {}
}
