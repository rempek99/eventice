import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth-service';

const HASH_SIGNS = ['*', '#', '$', '%', '&', '!'];

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
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

  constructor() {
    this.applyForm.valueChanges.subscribe(() => {
      this.handlePasswordChanged();
    });
  }

  onSubmit() {
    console.log('Form submitted', this.applyForm.value);
    this.authService.register(
      this.applyForm.value.email || '',
      this.applyForm.value.username || '',
      this.applyForm.value.password || ''
    );
    // .subscribe({
    //   next: (response) => {
    //     console.log('Registration successful:', response);
    //   },
    //   error: (error) => {
    //     console.error('Registration failed:', error);
    //   },
    // });
  }
}
