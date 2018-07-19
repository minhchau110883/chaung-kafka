import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Point } from './point.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Point>;

@Injectable()
export class PointService {

    private resourceUrl =  SERVER_API_URL + 'api/points';

    constructor(private http: HttpClient) { }

    create(point: Point): Observable<EntityResponseType> {
        const copy = this.convert(point);
        return this.http.post<Point>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(point: Point): Observable<EntityResponseType> {
        const copy = this.convert(point);
        return this.http.put<Point>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Point>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Point[]>> {
        const options = createRequestOption(req);
        return this.http.get<Point[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Point[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Point = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Point[]>): HttpResponse<Point[]> {
        const jsonResponse: Point[] = res.body;
        const body: Point[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Point.
     */
    private convertItemFromServer(point: Point): Point {
        const copy: Point = Object.assign({}, point);
        return copy;
    }

    /**
     * Convert a Point to a JSON which can be sent to the server.
     */
    private convert(point: Point): Point {
        const copy: Point = Object.assign({}, point);
        return copy;
    }
}
