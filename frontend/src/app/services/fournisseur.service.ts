import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Fournisseur } from '../models/fournisseur.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FournisseurService {
  private apiUrl = `${environment.apiUrl}/fournisseurs`;
  
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';
    
    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      errorMessage = `Code d'erreur: ${error.status}\nMessage: ${error.message}`;
    }
    
    console.error('Erreur HTTP:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  getAllFournisseurs(): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(this.apiUrl, this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  getFournisseurById(id: number): Observable<Fournisseur> {
    return this.http.get<Fournisseur>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  createFournisseur(fournisseur: Fournisseur): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(this.apiUrl, fournisseur, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateFournisseur(id: number, fournisseur: Fournisseur): Observable<Fournisseur> {
    return this.http.put<Fournisseur>(`${this.apiUrl}/${id}`, fournisseur, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteFournisseur(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  searchFournisseurs(nom: string): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(`${this.apiUrl}/search?nom=${encodeURIComponent(nom)}`, this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  getTopSuppliers(): Observable<Fournisseur[]> {
    return this.http.get<Fournisseur[]>(`${this.apiUrl}/top`, this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  getAverageNote(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/average-note`, this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  evaluateSupplier(id: number): Observable<Fournisseur> {
    return this.http.post<Fournisseur>(`${this.apiUrl}/${id}/evaluate`, {}, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }
}
