import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { NotificationService } from '../../util/notification-service';
import { jwtDecode } from 'jwt-decode';

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

  getToken(): string | null {
    return localStorage.getItem('id_token');
  }

  getUsername(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const payload: any = jwtDecode(token);
    return payload.username;
  }
}
