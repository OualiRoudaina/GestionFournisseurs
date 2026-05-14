import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FournisseurListComponent } from './fournisseur/fournisseur-list/fournisseur-list.component';
import { FournisseurFormComponent } from './fournisseur/fournisseur-form/fournisseur-form.component';
import { CommandeListComponent } from './commande/commande-list/commande-list.component';
import { CommandeFormComponent } from './commande/commande-form/commande-form.component';
import { LigneCommandeListComponent } from './ligne-commande/ligne-commande-list/ligne-commande-list.component';
import { LigneCommandeFormComponent } from './ligne-commande/ligne-commande-form/ligne-commande-form.component';
import { HistoriqueListComponent } from './historique/historique-list/historique-list.component';
import { HistoriqueFormComponent } from './historique/historique-form/historique-form.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ComparaisonOffresComponent } from './comparaison-offres/comparaison-offres.component';
import { LoginComponent } from './login/login.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FournisseurListComponent,
    FournisseurFormComponent,
    CommandeListComponent,
    CommandeFormComponent,
    LigneCommandeListComponent,
    LigneCommandeFormComponent,
    HistoriqueListComponent,
    HistoriqueFormComponent,
    DashboardComponent,
    ComparaisonOffresComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
