import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { NotificationService } from '../util/notification-service';

interface TestResponse {
  message: string;
}
@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  title = signal('Eventice Frontend');
  private http: HttpClient = inject(HttpClient);
  private notify = inject(NotificationService);

  testUser(): void {
    this.http.get<TestResponse>('http://localhost:8080/test').subscribe({
      error: (err) => {
        console.log('Error: ' + err.message);
        this.notify.show(err.message, this.notify.ERROR);
      },
      next: (res) => {
        console.log('Value: ' + res.message);
        this.notify.show('User test successful!', this.notify.SUCCESS);
      },
      complete: () => console.log('Completed!'),
    });
  }
}
