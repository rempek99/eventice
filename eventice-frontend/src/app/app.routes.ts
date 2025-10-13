import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Login } from './auth/login/login';

export const routes: Routes = [
    { 
        path: '',
        component: Home,
        title: 'Home'
    },
    {
        path: 'login',
        component: Login,
        title: 'Login'
    },
    {
        path: 'register',
        loadComponent: () => import('./auth/register/register').then(m => m.Register),
        title: 'Register'
    }
];