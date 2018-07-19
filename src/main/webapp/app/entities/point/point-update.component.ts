import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPoint } from 'app/shared/model/point.model';
import { PointService } from './point.service';
import { ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from 'app/entities/campaign';

@Component({
    selector: 'jhi-point-update',
    templateUrl: './point-update.component.html'
})
export class PointUpdateComponent implements OnInit {
    private _point: IPoint;
    isSaving: boolean;

    campaigns: ICampaign[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private pointService: PointService,
        private campaignService: CampaignService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ point }) => {
            this.point = point;
        });
        this.campaignService.query().subscribe(
            (res: HttpResponse<ICampaign[]>) => {
                this.campaigns = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.point.id !== undefined) {
            this.subscribeToSaveResponse(this.pointService.update(this.point));
        } else {
            this.subscribeToSaveResponse(this.pointService.create(this.point));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPoint>>) {
        result.subscribe((res: HttpResponse<IPoint>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCampaignById(index: number, item: ICampaign) {
        return item.id;
    }
    get point() {
        return this._point;
    }

    set point(point: IPoint) {
        this._point = point;
    }
}
