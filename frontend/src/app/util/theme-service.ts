import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private darkMode = signal(this.getStoredTheme());

  constructor() {
    this.applyTheme();
  }

  private getStoredTheme(): boolean {
    const stored = localStorage.getItem('isDarkMode');
    if (stored !== null) {
      return JSON.parse(stored);
    }
    // Check system preference
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  }

  private applyTheme(): void {
    const isDark = this.darkMode();
    if (isDark) {
      document.documentElement.classList.add('dark-mode');
    } else {
      document.documentElement.classList.remove('dark-mode');
    }
    localStorage.setItem('isDarkMode', JSON.stringify(isDark));
  }

  isDarkMode = this.darkMode; // Expose the signal for reactive updates

  toggleTheme() {
    this.darkMode.update((mode) => !mode);
    this.applyTheme();
  }
}
