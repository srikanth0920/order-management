import { Component, OnInit } from '@angular/core';
import { VirtualTimeScheduler } from 'rxjs';
import { Order } from '../../model/order';
import { OrderService } from '../../services/order-service.service';

@Component({
  selector: 'app-order-details-component',
  templateUrl: './order-details-component.component.html',
  styleUrls: ['./order-details-component.component.css']
})
export class OrderDetailsComponentComponent implements OnInit {

  private orders: Order[] = Array<Order>();

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.orderService.getOrderdetails().subscribe(data => this.orders = data);
  }

  updateOrder(index) {

    if (index !== null && index !== '') {
      let order = this.orders[index];
      this.orderService.updateOrder(order.orderId, order).subscribe(data => {
      }
      );
    }

  }

  deleteOrder(index) {

    if (index !== null && index !== '') {
      let order = this.orders[index];
      this.orderService.deleteOrder(order.orderId).subscribe(data => {
        //console.log(data);
        this.orders.splice(index,1);
      }
      );
    }

  }
}
