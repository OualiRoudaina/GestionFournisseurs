import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LigneCommandeAchat } from '../models/ligne-commande-achat.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LigneCommandeAchatService {
  private apiUrl = `${environment.apiUrl}/lignes-commande`;

  constructor(private http: HttpClient) { }

  getAllLignesCommande(): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(this.apiUrl);
  }

  getLigneCommandeById(id: number): Observable<LigneCommandeAchat> {
    return this.http.get<LigneCommandeAchat>(`${this.apiUrl}/${id}`);
  }

  createLigneCommande(ligneCommande: LigneCommandeAchat): Observable<LigneCommandeAchat> {
    return this.http.post<LigneCommandeAchat>(this.apiUrl, ligneCommande);
  }

  updateLigneCommande(id: number, ligneCommande: LigneCommandeAchat): Observable<LigneCommandeAchat> {
    return this.http.put<LigneCommandeAchat>(`${this.apiUrl}/${id}`, ligneCommande);
  }

  deleteLigneCommande(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getLignesByCommandeId(commandeId: number): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(`${this.apiUrl}/commande/${commandeId}`);
  }

  getLignesByProduit(produit: string): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(`${this.apiUrl}/produit/${produit}`);
  }

  getLignesByFournisseurId(fournisseurId: number): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(`${this.apiUrl}/fournisseur/${fournisseurId}`);
  }

  getAllProducts(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/products`);
  }

  getBestPricesForProduct(produit: string): Observable<LigneCommandeAchat[]> {
    return this.http.get<LigneCommandeAchat[]>(`${this.apiUrl}/best-prices/${produit}`);
  }

  calculateTotalCommande(commandeId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/commande/${commandeId}/total`);
  }
}
