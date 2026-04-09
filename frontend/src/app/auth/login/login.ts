import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth-service';
import { NotificationService } from '../../util/notification-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  private router = inject(Router);

  private authService = inject(AuthService);

  private notify = inject(NotificationService);

  onSubmit(): void {
    this.authService
      .login({
        username: this.loginForm.controls.username.value || '',
        password: this.loginForm.controls.password.value || '',
      })
      .subscribe((logged) => {
        if (logged) {
          this.router.navigateByUrl('/');
        }
      });
  }
}
