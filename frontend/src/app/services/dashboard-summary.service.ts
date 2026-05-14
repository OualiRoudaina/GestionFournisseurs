import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface MontantMois {
  mois: string;
  montantTotal: number;
  nombreCommandes: number;
}

export interface TopFournisseurMontant {
  fournisseurId: number;
  nom: string;
  montantTotal: number;
  nombreCommandes: number;
}

export interface PrixPoint {
  date: string;
  prixUnitaire: number;
  fournisseurNom: string;
}

export interface EvolutionPrixProduit {
  produit: string;
  points: PrixPoint[];
}

export interface RecentCommande {
  id: number;
  fournisseurNom: string;
  date: string;
  statut: string;
  montant: number;
}

export interface DashboardSummary {
  periodeDebut: string;
  periodeFin: string;
  totalFournisseurs: number;
  totalCommandesPeriode: number;
  montantTotalPeriode: number;
  totalAchatsHistoriquePeriode: number;
  moyenneDelaiLivraison: number;
  commandesEnAttente: number;
  commandesLivree: number;
  commandesAutres: number;
  montantsParMois: MontantMois[];
  topFournisseurs: TopFournisseurMontant[];
  evolutionPrixParProduit: EvolutionPrixProduit[];
  recentCommandes: RecentCommande[];
}

@Injectable({ providedIn: 'root' })
export class DashboardSummaryService {
  private readonly url = `${environment.apiUrl}/dashboard/summary`;

  constructor(private http: HttpClient) {}

  getSummary(from?: string, to?: string): Observable<DashboardSummary> {
    let params = new HttpParams();
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return this.http.get<DashboardSummary>(this.url, { params });
  }
}
