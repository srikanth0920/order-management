import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { BasicAuthService } from 'src/app/services/basicauthservice.service';
import { JwtAuthService } from 'src/app/services/jwt-auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private username: string;
  private password: string;
  private errorMessage: string;
  private subscription: Subscription;

  constructor(private authService: JwtAuthService, private router: Router) { }

  ngOnInit() {
    let isUserLoggedIn = this.authService.isUserLoggedIn();
    console.log('data from login:: ' + isUserLoggedIn);
    this.errorMessage = null;
    if (isUserLoggedIn === true) {
      this.router.navigate(['/home']);
    }

  }

  login() {
    this.subscription = this.authService.authenticate(this.username, this.password).subscribe(data => {
      console.log('User Logged in');
      this.router.navigate(['/home']);
    }, errorResponse => {
      console.log('User login failed');
      this.errorMessage = errorResponse.error.errorMessage
    })
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
