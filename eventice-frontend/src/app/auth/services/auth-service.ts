import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { from, Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private jwtToken: String | null = null;

  private http = inject(HttpClient);

  register(email: string, username: string, password: string): Observable<String> {
    const data = { email, username, password };

    const result = from(
      this.http.post<String>('http://localhost:8080/register', data).pipe(
        map((response) => response as String),
        catchError(this.handleError)
      )
    );

    return result;
  }

  // todo - register with not unique credentials returns 500 error - fix it

  login(data: { email: string; password: string }): void {
    const token = this.http
      .post<String>('localhost:8080/api/login', data)
      .pipe(catchError(this.handleError));
    token.subscribe((token) => {
      this.handleAuth(token as string);
    });
  }

  private handleAuth(token: string) {
    this.jwtToken = token;
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => new Error(`${error.error?.message || 'Error occured.'}`));
  }

  logout() {
    this.jwtToken = null;
  }
}
