import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistoriqueAchats } from '../models/historique-achats.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HistoriqueAchatsService {
  private apiUrl = `${environment.apiUrl}/historique-achats`;

  constructor(private http: HttpClient) { }

  getAllHistoriqueAchats(): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(this.apiUrl);
  }

  getHistoriqueAchatById(id: number): Observable<HistoriqueAchats> {
    return this.http.get<HistoriqueAchats>(`${this.apiUrl}/${id}`);
  }

  createHistoriqueAchat(historiqueAchat: HistoriqueAchats): Observable<HistoriqueAchats> {
    return this.http.post<HistoriqueAchats>(this.apiUrl, historiqueAchat);
  }

  updateHistoriqueAchat(id: number, historiqueAchat: HistoriqueAchats): Observable<HistoriqueAchats> {
    return this.http.put<HistoriqueAchats>(`${this.apiUrl}/${id}`, historiqueAchat);
  }

  deleteHistoriqueAchat(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getHistoriqueByFournisseurId(fournisseurId: number): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.apiUrl}/fournisseur/${fournisseurId}`);
  }

  getHistoriqueByProduit(produit: string): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.apiUrl}/produit/${produit}`);
  }

  getHistoriqueByDateRange(dateDebut: string, dateFin: string): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.apiUrl}/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  getAverageDelaiLivraisonByFournisseur(fournisseurId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/fournisseur/${fournisseurId}/stats/average-delai`);
  }

  getFastestDeliveryForProduct(produit: string): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.apiUrl}/fastest-delivery/${produit}`);
  }

  getTotalAchatsByFournisseur(fournisseurId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/fournisseur/${fournisseurId}/stats/total-achats`);
  }
}
