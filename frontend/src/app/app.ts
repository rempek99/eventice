import { Component, signal } from '@angular/core';
import { Header } from './components/header/header';
import { Home } from './home/home';
@Component({
  selector: 'app-root',
  imports: [Header, Home],
  template: `
    <app-header></app-header>
    <app-home></app-home>
  `,
  styles: [],
})
export class App {
}
