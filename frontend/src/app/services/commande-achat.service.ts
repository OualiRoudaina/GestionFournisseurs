import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommandeAchat } from '../models/commande-achat.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommandeAchatService {
  private apiUrl = `${environment.apiUrl}/commandes`;

  constructor(private http: HttpClient) { }

  getAllCommandes(): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(this.apiUrl);
  }

  getCommandeById(id: number): Observable<CommandeAchat> {
    return this.http.get<CommandeAchat>(`${this.apiUrl}/${id}`);
  }

  createCommande(commande: CommandeAchat): Observable<CommandeAchat> {
    return this.http.post<CommandeAchat>(this.apiUrl, commande);
  }

  updateCommande(id: number, commande: CommandeAchat): Observable<CommandeAchat> {
    return this.http.put<CommandeAchat>(`${this.apiUrl}/${id}`, commande);
  }

  deleteCommande(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getCommandesByFournisseur(fournisseurId: number): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(`${this.apiUrl}/fournisseur/${fournisseurId}`);
  }

  getCommandesByStatut(statut: string): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(`${this.apiUrl}/statut/${statut}`);
  }

  getCommandesByDateRange(dateDebut: string, dateFin: string): Observable<CommandeAchat[]> {
    return this.http.get<CommandeAchat[]>(`${this.apiUrl}/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  updateStatut(id: number, nouveauStatut: string): Observable<CommandeAchat> {
    return this.http.put<CommandeAchat>(`${this.apiUrl}/${id}/statut?nouveauStatut=${nouveauStatut}`, {});
  }
}
