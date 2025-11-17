import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { NotificationService } from '../../util/notification-service';
import { jwtDecode, JwtPayload } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);

  private notify = inject(NotificationService);

  register(email: string, username: string, password: string): Observable<string> {
    const data = { email, username, password };

    const result = this.http.post<string>('http://localhost:8080/register', data).pipe(
      map((response) => response as string),
      catchError((error) => this.handleError(error))
    );

    return result;
  }

  login(data: { username: string; password: string }): Observable<boolean> {
    return this.http.post<any>('http://localhost:8080/login', data).pipe(
      map((response) => {
        this.handleAuth(response.token, response.username);
        this.notify.show('Logged Succesfully', this.notify.SUCCESS);
        return true;
      }),
      catchError((error) => this.handleError(error))
    );
  }

  private handleAuth(token: string, username: string) {
    localStorage.setItem('id_token', token);
  }

  private handleError(error: HttpErrorResponse) {
    const errorMessage = `${error.error?.message || 'Error occured.'}`;
    this.notify.show(errorMessage, this.notify.WARNING);
    return EMPTY;
  }

  private invalidateToken(): void {
    localStorage.removeItem('id_token');
  }

  getToken(): any | null {
    const token = localStorage.getItem('id_token');
    if (!token) {
      return null;
    }
    const payload: JwtPayload = jwtDecode(token);
    const expireAt: Date = new Date((payload.exp || 0) * 1000);
    const now: Date = new Date();
    if (now > expireAt) {
      this.invalidateToken();
      return null;
    }
    return payload;
  }

  getUsername(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    return token.username;
  }
}
