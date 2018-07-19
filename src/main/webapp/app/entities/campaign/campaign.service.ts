import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Campaign } from './campaign.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Campaign>;

@Injectable()
export class CampaignService {

    private resourceUrl =  SERVER_API_URL + 'api/campaigns';

    constructor(private http: HttpClient) { }

    create(campaign: Campaign): Observable<EntityResponseType> {
        const copy = this.convert(campaign);
        return this.http.post<Campaign>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(campaign: Campaign): Observable<EntityResponseType> {
        const copy = this.convert(campaign);
        return this.http.put<Campaign>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Campaign>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Campaign[]>> {
        const options = createRequestOption(req);
        return this.http.get<Campaign[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Campaign[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Campaign = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Campaign[]>): HttpResponse<Campaign[]> {
        const jsonResponse: Campaign[] = res.body;
        const body: Campaign[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Campaign.
     */
    private convertItemFromServer(campaign: Campaign): Campaign {
        const copy: Campaign = Object.assign({}, campaign);
        return copy;
    }

    /**
     * Convert a Campaign to a JSON which can be sent to the server.
     */
    private convert(campaign: Campaign): Campaign {
        const copy: Campaign = Object.assign({}, campaign);
        return copy;
    }
}
