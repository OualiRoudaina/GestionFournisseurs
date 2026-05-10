import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HistoriqueAchatsService } from '../../services/historique-achats.service';
import { FournisseurService } from '../../services/fournisseur.service';
import { HistoriqueAchats } from '../../models/historique-achats.model';
import { Fournisseur } from '../../models/fournisseur.model';

@Component({
  selector: 'app-historique-list',
  templateUrl: './historique-list.component.html',
  styleUrls: ['./historique-list.component.css']
})
export class HistoriqueListComponent implements OnInit {

  historiqueAchats: HistoriqueAchats[] = [];
  fournisseurs: Fournisseur[] = [];
  
  // Filtres
  selectedFournisseur: number | null = null;
  searchProduit: string = '';
  dateDebut: string = '';
  dateFin: string = '';
  
  // Statistiques
  stats = {
    totalAchats: 0,
    delaiMoyen: 0,
    montantTotal: 0
  };

  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private historiqueService: HistoriqueAchatsService,
    private fournisseurService: FournisseurService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadFournisseurs();
    this.loadHistorique();
  }

  loadFournisseurs(): void {
    this.fournisseurService.getAllFournisseurs().subscribe({
      next: (data) => {
        this.fournisseurs = data;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des fournisseurs:', error);
      }
    });
  }

  loadHistorique(): void {
    this.loading = true;
    this.errorMessage = '';

    if (this.selectedFournisseur) {
      this.historiqueService.getHistoriqueByFournisseurId(this.selectedFournisseur).subscribe({
        next: (data) => {
          this.historiqueAchats = data;
          this.calculateStats();
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors du chargement de l\'historique';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
    } else if (this.dateDebut && this.dateFin) {
      this.historiqueService.getHistoriqueByDateRange(this.dateDebut, this.dateFin).subscribe({
        next: (data) => {
          this.historiqueAchats = data;
          this.calculateStats();
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors du chargement de l\'historique';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
    } else {
      this.historiqueService.getAllHistoriqueAchats().subscribe({
        next: (data) => {
          this.historiqueAchats = data;
          this.applyFilters();
          this.calculateStats();
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors du chargement de l\'historique';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
    }
  }

  applyFilters(): void {
    let filtered = [...this.historiqueAchats];

    if (this.searchProduit.trim()) {
      filtered = filtered.filter(h => 
        h.produit.toLowerCase().includes(this.searchProduit.toLowerCase())
      );
    }

    this.historiqueAchats = filtered;
  }

  calculateStats(): void {
    if (this.historiqueAchats.length === 0) {
      this.stats = { totalAchats: 0, delaiMoyen: 0, montantTotal: 0 };
      return;
    }

    this.stats.totalAchats = this.historiqueAchats.length;
    
    const totalDelai = this.historiqueAchats.reduce((sum, h) => sum + h.delaiLivraison, 0);
    this.stats.delaiMoyen = Math.round(totalDelai / this.historiqueAchats.length);
  }

  onFilterChange(): void {
    this.loadHistorique();
  }

  onResetFilters(): void {
    this.selectedFournisseur = null;
    this.searchProduit = '';
    this.dateDebut = '';
    this.dateFin = '';
    this.loadHistorique();
  }

  onEdit(historique: HistoriqueAchats): void {
    if (historique.id) {
      this.router.navigate(['/historique/edit', historique.id]);
    }
  }

  onDelete(historique: HistoriqueAchats): void {
    if (historique.id && confirm('Êtes-vous sûr de vouloir supprimer cet achat de l\'historique ?')) {
      this.historiqueService.deleteHistoriqueAchat(historique.id).subscribe({
        next: () => {
          this.loadHistorique();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la suppression';
          console.error('Erreur:', error);
        }
      });
    }
  }

  getDelaiBadgeClass(delai: number): string {
    if (delai < 7) return 'badge bg-success';
    if (delai < 14) return 'badge bg-warning';
    return 'badge bg-danger';
  }

  formatDate(date: string | undefined): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString('fr-FR');
  }

  getFournisseurName(historique: HistoriqueAchats): string {
    return historique.fournisseur?.nom || 'N/A';
  }
}