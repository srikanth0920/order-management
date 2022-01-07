import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { JwtAuthService } from './jwt-auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private jwtAuthService: JwtAuthService, private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Intercepted request' + request.url);

    if (this.jwtAuthService.isUserLoggedIn()) {
      console.log('User logged in ::' + this.jwtAuthService.getAccessToken());
      let accessToken = this.jwtAuthService.getAccessToken();
      const clonedReq = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + accessToken)
      });
      return next.handle(clonedReq).pipe(
        catchError((error) => {
          console.log('error in intercept')
          console.error(error);
          if (error.status == 401) {
            this.jwtAuthService.logout();
            this.router.navigateByUrl("/login");
          }
          return throwError(error);
        })
      )
    }else{
      return next.handle(request).pipe(
        catchError((error) => {
          console.log('error in intercept2'+error)
          return throwError(error);
        })
      )
    }

    return next.handle(request);
  }
}
