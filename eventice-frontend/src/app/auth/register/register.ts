import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../services/auth-service';
import { NotificationService } from '../../util/notification-service';
import { CommonModule } from '@angular/common';

const HASH_SIGNS = ['*', '#', '$', '%', '&', '!'];

// @NotNull
// @Size(min = 3, max = 16)
// private String username;

// @NotNull
// @Email
// private String email;

// @NotNull
// @Size(min = 6, max = 32)
// private String password;

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  applyForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(16),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,32}$'),
    ]),
    email: new FormControl('', [Validators.email, Validators.required]),
  });

  private authService = inject(AuthService);

  passwordStr = signal('');

  passwordHash = signal('');

  handlePasswordChanged() {
    this.passwordHash.update((h) => {
      if (
        this.applyForm == null ||
        this.applyForm.value == null ||
        this.applyForm.value.password == null
      ) {
        return '';
      }
      if (h.length < this.applyForm.value.password.length) {
        return h + HASH_SIGNS[Math.floor(Math.random() * HASH_SIGNS.length)];
      } else {
        console.log(h.substring(0, this.applyForm.value.password.length));
        return h.substring(0, this.applyForm.value.password.length);
      }
    });
  }

  constructor(private notify: NotificationService) {
    this.applyForm.valueChanges.subscribe(() => {
      this.handlePasswordChanged();
    });
  }

  onSubmit() {
    if (this.applyForm.valid === false) {
      let error = null;
      error = this.applyForm.controls.username.errors;
      if (error != null) {
        console.log('username', error);
        return;
      }
      error = this.applyForm.controls.email.errors;
      if (error != null) {
        console.log('email', error);
        return;
      }
      error = this.applyForm.controls.password.errors;
      if (error != null) {
        console.log('password', error);
        return;
      }
    }
    this.authService
      .register(
        this.applyForm.value.email || '',
        this.applyForm.value.username || '',
        this.applyForm.value.password || ''
      )
      .subscribe({
        next: (response) => {
          console.log(response);
          this.notify.show('Registration successful! Please log in.', this.notify.SUCCESS);
        },
        error: (err) => {
          this.notify.show(err.message, this.notify.WARNING);
        },
      });
  }
}
