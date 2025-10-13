import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-register',
  imports: [],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {

onSubmit() {
  console.log('Form submitted');
}

}
