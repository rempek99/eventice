import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomSnackbar } from './custom-snackbar/custom-snackbar';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private snackbar: MatSnackBar) {}

  public readonly MESSAGE = 0;
  public readonly WARNING = 1;
  public readonly ERROR = 2;
  private readonly DISMISS = 'Dismiss';

  show = (message: string, type: number): void => {
    if (type === this.WARNING) {
      this.snackbar.openFromComponent(CustomSnackbar, {
        data: { message, action: this.DISMISS },
        panelClass: ['snackbar-warning'],
      });
    } else if (type === this.ERROR) {
      this.snackbar.openFromComponent(CustomSnackbar, {
        data: { message, action: this.DISMISS },
        panelClass: ['snackbar-error'],
      });
    } else {
      this.snackbar.openFromComponent(CustomSnackbar, {
        data: { message: message, action: this.DISMISS },
      });
    }
  };
}
