import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Point } from './point.model';
import { PointPopupService } from './point-popup.service';
import { PointService } from './point.service';

@Component({
    selector: 'jhi-point-delete-dialog',
    templateUrl: './point-delete-dialog.component.html'
})
export class PointDeleteDialogComponent {

    point: Point;

    constructor(
        private pointService: PointService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pointService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pointListModification',
                content: 'Deleted an point'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-point-delete-popup',
    template: ''
})
export class PointDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pointPopupService: PointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pointPopupService
                .open(PointDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
