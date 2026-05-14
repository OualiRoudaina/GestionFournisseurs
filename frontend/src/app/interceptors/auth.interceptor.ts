import { Injectable } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.auth.getToken();
    const url = req.url;
    const isOurApi = this.isAppApiUrl(url);
    const isLogin = url.includes('/auth/login');

    let authReq = req;
    if (token && isOurApi && !isLogin) {
      authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }

    return next.handle(authReq).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401 && this.auth.getToken() && isOurApi && !isLogin) {
          this.auth.logout();
        }
        return throwError(() => err);
      })
    );
  }

  private isAppApiUrl(url: string): boolean {
    if (url.startsWith(environment.apiUrl)) {
      return true;
    }
    try {
      const path = new URL(url, typeof window !== 'undefined' ? window.location.origin : 'http://localhost').pathname;
      return path.startsWith('/api');
    } catch {
      return url.startsWith('/api');
    }
  }
}
