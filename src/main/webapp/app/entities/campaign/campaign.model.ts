import { BaseEntity } from './../../shared';

export const enum UserType {
    'CUSTOMER',
    'MERCHANT'
}

export class Campaign implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public minAmount?: number,
        public maxAmount?: number,
        public point?: number,
        public type?: UserType,
    ) {
    }
}
