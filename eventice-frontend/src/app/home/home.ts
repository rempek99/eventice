import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { catchError, map } from 'rxjs';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  title = signal('Eventice Frontend');
  private http: HttpClient = inject(HttpClient);

  testUser(): void {
    this.http.get<any>('http://localhost:8080/test').subscribe({
      error: (err) => console.log('Error: ' + err.message),
      next: (res) => console.log('Value: ' + res.message),
      complete: () => console.log('Completed!'),
    });
  }
}
