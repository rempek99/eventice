import { Component, inject } from '@angular/core';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarLabel,
  MatSnackBarAction,
  MatSnackBarRef,
} from '@angular/material/snack-bar';

@Component({
  selector: 'app-custom-snackbar',
  imports: [MatSnackBarLabel, MatSnackBarAction],
  templateUrl: './custom-snackbar.html',
  styleUrl: './custom-snackbar.scss',
})
export class CustomSnackbar {
  public data = inject(MAT_SNACK_BAR_DATA);
  snackBarRef = inject(MatSnackBarRef);
}
