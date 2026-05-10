import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
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
    ComparaisonOffresComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
