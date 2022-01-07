import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthService {

  constructor(private http: HttpClient,private router: Router) { }

  public authenticate(username: string, password: string): Observable<any> {

    let token = btoa(username + ':' + password);
    let headers = {
      'Authorization': 'Basic ' + token
    }
    return this.http.get('http://localhost:4300/login', { headers }).pipe(
      map(
        userData => {
          this.storeInSession(token);
          return userData;
        }
      )

    );

  }


  storeInSession(token: string) {
    sessionStorage.setItem('TOKEN', token);
  }


  removeFromSession() {
    sessionStorage.removeItem('TOKEN');
  }

  public isUserLoggedIn(): boolean {
    let userToken = sessionStorage.getItem('TOKEN');
    console.log("userToken::"+userToken);
    if (userToken === null) {
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

