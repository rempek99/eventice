import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { EMPTY, from, Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { NotificationService } from '../../util/notification-service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private jwtToken: string | null = null;

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

  // todo - register with not unique credentials returns 500 error - fix it

  login(data: { username: string; password: string }): Observable<boolean> {
    return this.http.post<any>('http://localhost:8080/login', data).pipe(
      map((response) => {
        this.handleAuth(response.token);
        return true;
      }),
      catchError((error) => this.handleError(error))
    );
  }

  private handleAuth(token: string) {
    this.jwtToken = token;
    localStorage.setItem('id_token', token);
    console.log(this.jwtToken);
  }

  private handleError(error: HttpErrorResponse) {
    const errorMessage = `${error.error?.message || 'Error occured.'}`;
    this.notify.show(errorMessage, this.notify.WARNING);
    return EMPTY;
  }

  logout() {
    this.jwtToken = null;
  }
}
