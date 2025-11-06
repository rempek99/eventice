import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth-service';
import { NotificationService } from '../../util/notification-service';

const HASH_SIGNS = ['*', '#', '$', '%', '&', '!'];

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  applyForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
    email: new FormControl(''),
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
    this.authService
      .register(
        this.applyForm.value.email || '',
        this.applyForm.value.username || '',
        this.applyForm.value.password || ''
      )
      .subscribe({
        next: (response) => {
          console.log(response);
          this.notify.show('Registration successful! Please log in.', this.notify.MESSAGE);
        },
        error: (err) => {
          this.notify.show(err.message, this.notify.WARNING);
        },
      });
  }
}
