import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Point } from './point.model';
import { PointPopupService } from './point-popup.service';
import { PointService } from './point.service';
import { Campaign, CampaignService } from '../campaign';

@Component({
    selector: 'jhi-point-dialog',
    templateUrl: './point-dialog.component.html'
})
export class PointDialogComponent implements OnInit {

    point: Point;
    isSaving: boolean;

    campaigns: Campaign[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pointService: PointService,
        private campaignService: CampaignService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.campaignService.query()
            .subscribe((res: HttpResponse<Campaign[]>) => { this.campaigns = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.point.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pointService.update(this.point));
        } else {
            this.subscribeToSaveResponse(
                this.pointService.create(this.point));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Point>>) {
        result.subscribe((res: HttpResponse<Point>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Point) {
        this.eventManager.broadcast({ name: 'pointListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCampaignById(index: number, item: Campaign) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-point-popup',
    template: ''
})
export class PointPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pointPopupService: PointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pointPopupService
                    .open(PointDialogComponent as Component, params['id']);
            } else {
                this.pointPopupService
                    .open(PointDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
