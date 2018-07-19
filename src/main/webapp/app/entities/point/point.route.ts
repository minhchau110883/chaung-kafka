import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Point } from 'app/shared/model/point.model';
import { PointService } from './point.service';
import { PointComponent } from './point.component';
import { PointDetailComponent } from './point-detail.component';
import { PointUpdateComponent } from './point-update.component';
import { PointDeletePopupComponent } from './point-delete-dialog.component';
import { IPoint } from 'app/shared/model/point.model';

@Injectable({ providedIn: 'root' })
export class PointResolve implements Resolve<IPoint> {
    constructor(private service: PointService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((point: HttpResponse<Point>) => point.body));
        }
        return of(new Point());
    }
}

export const pointRoute: Routes = [
    {
        path: 'point',
        component: PointComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point/:id/view',
        component: PointDetailComponent,
        resolve: {
            point: PointResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point/new',
        component: PointUpdateComponent,
        resolve: {
            point: PointResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point/:id/edit',
        component: PointUpdateComponent,
        resolve: {
            point: PointResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pointPopupRoute: Routes = [
    {
        path: 'point/:id/delete',
        component: PointDeletePopupComponent,
        resolve: {
            point: PointResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Points'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
