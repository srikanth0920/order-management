import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Order } from 'src/app/model/order';
import { OrderService } from 'src/app/services/order-service.service';

@Component({
  selector: 'app-find-order',
  templateUrl: './find-order.component.html',
  styleUrls: ['./find-order.component.css']
})
export class FindOrderComponent implements OnInit {

  private orderId: string;

  private subscription: Subscription;

  private order: Order = new Order();

  constructor(private orderService: OrderService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    this.getOrderId();
    this.findOrder(this.orderId);

  }

  findOrder(orderId: string) {

    if (this.orderId !== null && this.orderId !== '') {
      this.subscription = this.orderService.findOrder(orderId).subscribe(data => {
        this.order = data
      },
        error => {
          this.order = null;
        }
      );
    }

  }


  getOrderId() {
    this.activatedRoute.params.subscribe(data => {
      this.orderId = data['orderId'];
    })
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
