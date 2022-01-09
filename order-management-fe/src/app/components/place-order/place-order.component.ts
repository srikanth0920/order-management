import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Order } from 'src/app/model/order';
import { OrderService } from 'src/app/services/order-service.service';

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.css']
})
export class PlaceOrderComponent implements OnInit {

  private orderPlaced: boolean = false;

  private errorMessage: string;
  private subscription: Subscription;

  constructor(private orderService: OrderService) {
  }

  private order: Order = new Order();

  ngOnInit() {
    this.errorMessage = null;
  }



  placeOrder(order: Order) {
    this.orderService.placeOrder(order).subscribe(data => {
      this.order = data;
      this.orderPlaced = true;
    }, error => {

      if (error.status == 403) {
        this.errorMessage = "Unauthorized";
      }
    });
  }


}
