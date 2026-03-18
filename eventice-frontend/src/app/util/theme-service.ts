import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
// IDK it is not working :(
export class ThemeService {
  private darkMode = signal(this.getStoredTheme());

  constructor() {
    this.applyTheme();
  }

  private getStoredTheme(): boolean {
    const stored = localStorage.getItem('isDarkMode');
    return stored ? JSON.parse(stored) : document.documentElement.classList.contains('dark-mode');
  }

  private applyTheme(): void {
    if (this.isDarkMode()) {
      document.documentElement.classList.add('dark-mode');
    } else {
      document.documentElement.classList.remove('dark-mode');
    }
    console.log('Dark mode applied:', document.documentElement.classList.contains('dark-mode'));
  }

  isDarkMode(): boolean {
    return this.darkMode();
  }

  toggleTheme() {
    this.darkMode.update((mode) => !mode);
    localStorage.setItem('isDarkMode', JSON.stringify(this.darkMode()));
    this.applyTheme();
  }
}
