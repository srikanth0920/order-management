import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtResponse } from '../model/jwt-response';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class JwtAuthService {

  private jwtResponse: JwtResponse;
  constructor(private http: HttpClient, private router: Router) { }

  authenticate(username: string, password: string): Observable<any> {


    return this.http.post('http://localhost:4300/authenticate', {
      'username': username,
      'password': password
    }).pipe(
      map(
        userData => {
          this.jwtResponse = userData as JwtResponse;
          this.storeJwtInSession(this.jwtResponse.accessToken);
          return userData;
        }
      )

    );
  }


  storeJwtInSession(accessToken: string) {
    sessionStorage.setItem('ACCESS_TOKEN', accessToken);
    //sessionStorage.setItem('REFRESH_TOKEN', accessToken);
  }

  removeFromSession() {
    sessionStorage.removeItem('ACCESS_TOKEN');
  }

  getAccessToken(): string {
    return sessionStorage.getItem('ACCESS_TOKEN');
  }

  public isUserLoggedIn(): boolean {
    let accessToken = sessionStorage.getItem('ACCESS_TOKEN');
    console.log("ACCESS_TOKEN::" + accessToken);
    if (accessToken === null) {
      return false;
    } else {
      return true;
    }
  }

  public logout() {
    this.removeFromSession();
    this.router.navigate(['/login']);
  }
}
