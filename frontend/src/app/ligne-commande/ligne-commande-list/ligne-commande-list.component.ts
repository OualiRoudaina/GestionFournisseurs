import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LigneCommandeAchat } from '../../models/ligne-commande-achat.model';
import { LigneCommandeAchatService } from '../../services/ligne-commande-achat.service';

@Component({
  selector: 'app-ligne-commande-list',
  templateUrl: './ligne-commande-list.component.html',
  styleUrls: ['./ligne-commande-list.component.css']
})
export class LigneCommandeListComponent implements OnInit {
  lignesCommande: LigneCommandeAchat[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private ligneCommandeService: LigneCommandeAchatService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadLignesCommande();
  }

  loadLignesCommande(): void {
    this.loading = true;
    this.errorMessage = '';

    this.ligneCommandeService.getAllLignesCommande().subscribe({
      next: (data) => {
        this.lignesCommande = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des lignes de commande:', error);
        this.errorMessage = 'Erreur lors du chargement des lignes de commande';
        this.loading = false;
      }
    });
  }

  onNewLigne(): void {
    this.router.navigate(['/lignes-commande/new']);
  }

  onEdit(ligne: LigneCommandeAchat): void {
    this.router.navigate(['/lignes-commande/edit', ligne.id]);
  }

  onDelete(ligne: LigneCommandeAchat): void {
    if (!ligne.id || !confirm(`Supprimer la ligne "${ligne.produit}" ?`)) {
      return;
    }

    this.ligneCommandeService.deleteLigneCommande(ligne.id).subscribe({
      next: () => this.loadLignesCommande(),
      error: (error) => {
        console.error('Erreur lors de la suppression de la ligne:', error);
        this.errorMessage = 'Erreur lors de la suppression de la ligne de commande';
      }
    });
  }

  getTotal(ligne: LigneCommandeAchat): number {
    return (ligne.quantite || 0) * (ligne.prixUnitaire || 0);
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(value || 0);
  }
}