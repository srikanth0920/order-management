import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JwtAuthService } from 'src/app/services/jwt-auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private authService: JwtAuthService, private router: Router) { }

  ngOnInit() {

    let isUserLoggedIn = this.authService.isUserLoggedIn();
    console.log('data from header::' + isUserLoggedIn);
    if (isUserLoggedIn === false) {
      this.router.navigate(['/login']);
    }
  }


}
