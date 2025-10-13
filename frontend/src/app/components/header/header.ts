import { Component, computed, signal } from '@angular/core';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class Header {

handleLoginClicked = () => {
  console.log('Login clicked');
    this.isUserLoggedIn.update(state => !state);
}

  isUserLoggedIn = signal(false);
  loginBtnText = computed(() => this.isUserLoggedIn() ? 'Logout' : 'Login');

    followingMessage= computed(() => this.isUserLoggedIn() ? ' Welcome, User!' : 'Please log in.');
  
  }
