import { Injectable } from '@angular/core';
import { Order } from '../model/order';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl: string ='http://localhost:4300/orders';

  private orders: Array<Order>;

  private headers = {
    'Authorization': 'Basic '+ btoa('admin:admin5')
  }

  constructor(private http: HttpClient) {
  }

  getOrderdetails(pageNumber: number = 1): Observable<Order[]> {

    let params = {
      '_page': '' + pageNumber
    };

    
    //return this.http.get("http://localhost:4300/orders", { params: params }).map(data => data as Order[]);
    return this.http.get(this.baseUrl).map(data => data as Order[]);

  }

  findOrder(orderId: string): Observable<Order> {
    return this.http.get(this.baseUrl+"/"+orderId).map(data => data as Order);
  }

  placeOrder(order: Order): Observable<Order> {
    return this.http.post(this.baseUrl, order).map(data => data as Order);
  }

  updateOrder(orderId: string, order: Order): Observable<Order> {
    return this.http.put(this.baseUrl+"/"+orderId, order).map(data => data as Order);
  }

  deleteOrder(orderId: string): Observable<string> {
    return this.http.delete(this.baseUrl+"/"+orderId, {responseType: 'text'} ).map(data =>  data as string);
  }


}
