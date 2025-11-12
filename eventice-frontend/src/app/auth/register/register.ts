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
      const password = this.applyForm.value.password;
      if (!password) {
        return '';
      }
      if (h.length < password.length) {
        return h + HASH_SIGNS[Math.floor(Math.random() * HASH_SIGNS.length)];
      } else {
        return h.substring(0, password.length);
      }
    });
  }

  constructor(private notify: NotificationService) {
    this.applyForm.valueChanges.subscribe(() => {
      this.handlePasswordChanged();
    });
  }

  onSubmit() {
    if (this.applyForm.invalid === false) {
      this.applyForm.markAllAsTouched();
      return;
    }
    this.authService
      .register(
        this.applyForm.value.email!,
        this.applyForm.value.username!,
        this.applyForm.value.password!
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
