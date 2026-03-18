import { Component, computed, InjectionToken, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../../auth/services/auth-service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ThemeService } from '../../util/theme-service';

export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
  providedIn: 'root',
  factory: () => localStorage,
});

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive, MatSlideToggleModule],
  templateUrl: './header.html',
  styleUrls: ['./header.scss'],
})
export class Header {
  public localStorage = inject(BROWSER_STORAGE);
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  constructor() {}
}
