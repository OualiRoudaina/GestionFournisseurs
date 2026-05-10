import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

declare var bootstrap: any;

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private routerSubscription?: Subscription;

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Fermer les dropdowns lors de la navigation
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.closeAllDropdowns();
      });
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  closeAllDropdowns(): void {
    // Fermer tous les dropdowns Bootstrap
    const dropdowns = document.querySelectorAll('.dropdown-menu.show');
    dropdowns.forEach(dropdown => {
      const bsDropdown = bootstrap.Dropdown.getInstance(dropdown.previousElementSibling);
      if (bsDropdown) {
        bsDropdown.hide();
      }
    });
  }

  navigateTo(route: string, event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }
    console.log('Navigating to:', route);
    this.closeAllDropdowns();
    this.router.navigate([route]).then(
      (success) => {
        console.log('Navigation successful to:', route, success);
      },
      (error) => {
        console.error('Navigation error to', route, ':', error);
      }
    );
  }

  isActive(route: string): boolean {
    return this.router.url === route || this.router.url.startsWith(route + '/');
  }
}