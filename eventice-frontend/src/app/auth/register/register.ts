import { Component, computed, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

const HASH_SIGNS = ['*', '#', '$', '%', '&', '!', ];

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  username = new FormControl('');
  password = new FormControl('');
  email = new FormControl('');

  passwordStr = signal('');

  passwordHash = signal('');

  handlePasswordChanged() {
 this.passwordHash.update((h) => {
        if(this.password.value === null) {
          return '';
        }
        if (h.length < this.password.value.length) {
          return h + HASH_SIGNS[Math.floor(Math.random() * HASH_SIGNS.length)];
        } else {
          console.log(h.substring(0, this.password.value.length));
          return h.substring(0, this.password.value.length);
        }
    });
  }

  constructor() {
    this.password.valueChanges.subscribe(() => {
     this.handlePasswordChanged();
  });
}

onSubmit() {
  console.log('Form submitted', this.password.value);
}

}
