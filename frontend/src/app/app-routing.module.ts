import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FournisseurListComponent } from './fournisseur/fournisseur-list/fournisseur-list.component';
import { FournisseurFormComponent } from './fournisseur/fournisseur-form/fournisseur-form.component';
import { CommandeListComponent } from './commande/commande-list/commande-list.component';
import { CommandeFormComponent } from './commande/commande-form/commande-form.component';
import { LigneCommandeListComponent } from './ligne-commande/ligne-commande-list/ligne-commande-list.component';
import { LigneCommandeFormComponent } from './ligne-commande/ligne-commande-form/ligne-commande-form.component';
import { HistoriqueListComponent } from './historique/historique-list/historique-list.component';
import { HistoriqueFormComponent } from './historique/historique-form/historique-form.component';
import { ComparaisonOffresComponent } from './comparaison-offres/comparaison-offres.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'fournisseurs', component: FournisseurListComponent, canActivate: [AuthGuard] },
  { path: 'fournisseurs/new', component: FournisseurFormComponent, canActivate: [AuthGuard] },
  { path: 'fournisseurs/edit/:id', component: FournisseurFormComponent, canActivate: [AuthGuard] },
  { path: 'commandes', component: CommandeListComponent, canActivate: [AuthGuard] },
  { path: 'commandes/new', component: CommandeFormComponent, canActivate: [AuthGuard] },
  { path: 'commandes/edit/:id', component: CommandeFormComponent, canActivate: [AuthGuard] },
  { path: 'lignes-commande/new', component: LigneCommandeFormComponent, canActivate: [AuthGuard] },
  { path: 'lignes-commande/edit/:id', component: LigneCommandeFormComponent, canActivate: [AuthGuard] },
  { path: 'lignes-commande', component: LigneCommandeListComponent, canActivate: [AuthGuard] },
  { path: 'historique', component: HistoriqueListComponent, canActivate: [AuthGuard] },
  { path: 'historique/new', component: HistoriqueFormComponent, canActivate: [AuthGuard] },
  { path: 'historique/edit/:id', component: HistoriqueFormComponent, canActivate: [AuthGuard] },
  { path: 'comparaison-offres', component: ComparaisonOffresComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
