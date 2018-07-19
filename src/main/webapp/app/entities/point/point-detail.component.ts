import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Point } from './point.model';
import { PointService } from './point.service';

@Component({
    selector: 'jhi-point-detail',
    templateUrl: './point-detail.component.html'
})
export class PointDetailComponent implements OnInit, OnDestroy {

    point: Point;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pointService: PointService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPoints();
    }

    load(id) {
        this.pointService.find(id)
            .subscribe((pointResponse: HttpResponse<Point>) => {
                this.point = pointResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPoints() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pointListModification',
            (response) => this.load(this.point.id)
        );
    }
}
