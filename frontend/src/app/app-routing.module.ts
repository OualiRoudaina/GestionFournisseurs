import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FournisseurListComponent } from './fournisseur/fournisseur-list/fournisseur-list.component';
import { FournisseurFormComponent } from './fournisseur/fournisseur-form/fournisseur-form.component';
import { CommandeListComponent } from './commande/commande-list/commande-list.component';
import { CommandeFormComponent } from './commande/commande-form/commande-form.component';
import { LigneCommandeListComponent } from './ligne-commande/ligne-commande-list/ligne-commande-list.component';
import { HistoriqueListComponent } from './historique/historique-list/historique-list.component';
import { HistoriqueFormComponent } from './historique/historique-form/historique-form.component';
import { ComparaisonOffresComponent } from './comparaison-offres/comparaison-offres.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'fournisseurs', component: FournisseurListComponent },
  { path: 'fournisseurs/new', component: FournisseurFormComponent },
  { path: 'fournisseurs/edit/:id', component: FournisseurFormComponent },
  { path: 'commandes', component: CommandeListComponent },
  { path: 'commandes/new', component: CommandeFormComponent },
  { path: 'commandes/edit/:id', component: CommandeFormComponent },
  { path: 'lignes-commande', component: LigneCommandeListComponent },
  { path: 'historique', component: HistoriqueListComponent },
  { path: 'historique/new', component: HistoriqueFormComponent },
  { path: 'historique/edit/:id', component: HistoriqueFormComponent },
  { path: 'comparaison-offres', component: ComparaisonOffresComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
