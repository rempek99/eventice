import { Component, computed, InjectionToken } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../../auth/services/auth-service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ThemeService } from '../../util/theme-service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';

export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
  providedIn: 'root',
  factory: () => localStorage,
});

interface NavItem {
  label: string;
  route: string;
  icon: string;
}

@Component({
  selector: 'app-header',
  imports: [
    RouterLink,
    RouterLinkActive,
    MatSlideToggleModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
  ],
  templateUrl: './toolbar.html',
  styleUrls: ['./toolbar.scss'],
})
export class Header {
  public localStorage = inject(BROWSER_STORAGE);
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  private router = inject(Router);

  navItems = computed<NavItem[]>(() => {
    const loggedIn = this.authService.loggedIn();
    const items: NavItem[] = [{ label: 'Home', route: '/', icon: 'home' }];
    if (!loggedIn) {
      items.push({ label: 'Login', route: '/login', icon: 'login' });
      items.push({ label: 'Register', route: '/register', icon: 'person_add' });
    }
    if (loggedIn && this.authService.isAdmin()) {
      items.push({ label: 'Admin Panel', route: '/admin', icon: 'admin_panel_settings' });
    }
    return items;
  });

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }
}
