import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  onSubmit(): void {
    // Handle login logic here
    console.log('Login form submitted');
  }
}
