import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LigneCommandeAchat } from '../models/ligne-commande-achat.model';
import { HistoriqueAchats } from '../models/historique-achats.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ComparaisonOffresService {
  private apiUrl = `${environment.apiUrl}/comparaison-offres`;

  constructor(private http: HttpClient) { }

  /**
   * Compare prices for a specific product
   */
  comparerPrixProduit(produit: string): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(`${this.apiUrl}/produit/${encodeURIComponent(produit)}/prix`);
  }

  /**
   * Compare delivery delays for a specific product
   */
  comparerDelaisLivraison(produit: string): Observable<HistoriqueAchats[]> {
    return this.http.get<HistoriqueAchats[]>(`${this.apiUrl}/produit/${encodeURIComponent(produit)}/livraison`);
  }

  /**
   * Get complete comparison for a product (prices + delivery delays)
   */
  comparaisonCompleteProduit(produit: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/produit/${encodeURIComponent(produit)}/complet`);
  }

  /**
   * Compare suppliers for a specific product
   */
  comparerFournisseursPourProduit(produit: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/fournisseurs/${encodeURIComponent(produit)}`);
  }

  /**
   * Calculate global scores for suppliers for a product
   * Scores based on: price (40%), delivery time (30%), and service quality (30%)
   */
  calculerScoreGlobalParProduit(produit: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/scores/${encodeURIComponent(produit)}`);
  }

  /**
   * Get list of available products
   */
  getAvailableProducts(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/produits`);
  }
}
