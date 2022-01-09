import { Component, OnInit } from '@angular/core';
import { Subscription, VirtualTimeScheduler } from 'rxjs';
import { Order } from '../../model/order';
import { OrderService } from '../../services/order-service.service';

@Component({
  selector: 'app-order-details-component',
  templateUrl: './order-details-component.component.html',
  styleUrls: ['./order-details-component.component.css']
})
export class OrderDetailsComponentComponent implements OnInit {

  private orders: Order[] = Array<Order>();
  private subscription: Subscription;

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    console.log('Order details init');

    this.subscription = this.orderService.getOrderdetails().subscribe(data => this.orders = data);
  }

  updateOrder(index) {

    if (index !== null && index !== '') {
      let order = this.orders[index];
      this.subscription = this.orderService.updateOrder(order.orderId, order).subscribe(data => {
      }
      );
    }

  }

  deleteOrder(index) {

    if (index !== null && index !== '') {
      let order = this.orders[index];
      this.subscription = this.orderService.deleteOrder(order.orderId).subscribe(data => {
        //console.log(data);
        this.orders.splice(index, 1);
      }
      );
    }

  }

  ngOnDestroy() {
    if (this.subscription != null) {
      console.log('Order details destroy');
      this.subscription.unsubscribe();
    }
  }
}
