import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { PointComponent } from './point.component';
import { PointDetailComponent } from './point-detail.component';
import { PointPopupComponent } from './point-dialog.component';
import { PointDeletePopupComponent } from './point-delete-dialog.component';

@Injectable()
export class PointResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const pointRoute: Routes = [
    {
        path: 'point',
        component: PointComponent,
        resolve: {
            'pagingParams': PointResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'point/:id',
        component: PointDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pointPopupRoute: Routes = [
    {
        path: 'point-new',
        component: PointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'point/:id/edit',
        component: PointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'point/:id/delete',
        component: PointDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
