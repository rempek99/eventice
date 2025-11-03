import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private jwtToken: String | null = null;

  private http = inject(HttpClient);

  register(email: string, username: string, password: string): void {
    const data = { email, username, password };
    console.log('Register data:', data);
    this.http
      .post<String>('http://localhost:8080/api/register', data)
      .pipe(catchError(this.handleError))
      .subscribe((response) => {
        console.log('Register response:', response);
      });
    // todo - register with not unique credentials returns 500 error - fix it
  }

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
    // todo 201 code is handled as error - fix it
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

  logout() {
    this.jwtToken = null;
  }
}
