import { Component } from '@angular/core';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  readonly swaggerDocsUrl = environment.production
    ? '/swagger-ui.html'
    : environment.apiUrl.replace(/\/api\/?$/, '') + '/swagger-ui.html';

  constructor(private auth: AuthService) {}

  get displayName(): string | null {
    return this.auth.getUsername();
  }

  logout(): void {
    this.auth.logout();
  }
}
