import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
}

const TOKEN_KEY = 'gf_auth_token';
const USER_KEY = 'gf_auth_username';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly loginUrl = `${environment.apiUrl}/auth/login`;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.loginUrl, { username, password }).pipe(
      tap((res) => {
        localStorage.setItem(TOKEN_KEY, res.token);
        localStorage.setItem(USER_KEY, res.username);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getUsername(): string | null {
    return localStorage.getItem(USER_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
