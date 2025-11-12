import { Component, computed, InjectionToken, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { inject } from '@angular/core';

export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
  providedIn: 'root',
  factory: () => localStorage,
});

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrls: ['./header.scss'],
})
export class Header {
  public localStorage = inject(BROWSER_STORAGE);

  constructor() {
    if (
      this.localStorage['theme'] === 'dark' ||
      (!('theme' in this.localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)
    ) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }

  toggleTheme() {
    this.localStorage['theme'] = this.localStorage['theme'] === 'dark' ? 'light' : 'dark';
    document.documentElement.classList.toggle('dark');
    this.isDarkMode.set(document.documentElement.classList.contains('dark'));
  }

  isDarkMode = signal(document.documentElement.classList.contains('dark'));

  isDarkModeSign = computed(() => {
    return this.isDarkMode() ? '💡' : '🌙';
  });
}
