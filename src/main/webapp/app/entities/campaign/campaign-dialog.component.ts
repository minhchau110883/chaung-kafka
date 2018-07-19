import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Campaign } from './campaign.model';
import { CampaignPopupService } from './campaign-popup.service';
import { CampaignService } from './campaign.service';

@Component({
    selector: 'jhi-campaign-dialog',
    templateUrl: './campaign-dialog.component.html'
})
export class CampaignDialogComponent implements OnInit {

    campaign: Campaign;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private campaignService: CampaignService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.campaign.id !== undefined) {
            this.subscribeToSaveResponse(
                this.campaignService.update(this.campaign));
        } else {
            this.subscribeToSaveResponse(
                this.campaignService.create(this.campaign));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Campaign>>) {
        result.subscribe((res: HttpResponse<Campaign>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Campaign) {
        this.eventManager.broadcast({ name: 'campaignListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-campaign-popup',
    template: ''
})
export class CampaignPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private campaignPopupService: CampaignPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.campaignPopupService
                    .open(CampaignDialogComponent as Component, params['id']);
            } else {
                this.campaignPopupService
                    .open(CampaignDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
