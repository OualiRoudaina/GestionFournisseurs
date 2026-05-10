import { Component, OnInit } from '@angular/core';
import { FournisseurService } from '../../services/fournisseur.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-fournisseur-list',
  templateUrl: './fournisseur-list.component.html',
  styleUrls: ['./fournisseur-list.component.css']
})
export class FournisseurListComponent implements OnInit {

  fournisseurs: any[] = [];
  searchTerm: string = '';

  constructor(
    private fournisseurService: FournisseurService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadFournisseurs();
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

  onSearch(): void {
    if (this.searchTerm.trim()) {
      this.fournisseurService.searchFournisseurs(this.searchTerm).subscribe({
        next: (data) => {
          this.fournisseurs = data;
        },
        error: (error) => {
          console.error('Erreur lors de la recherche:', error);
        }
      });
    } else {
      this.loadFournisseurs();
    }
  }

  onEdit(fournisseur: any): void {
    this.router.navigate(['/fournisseurs/edit', fournisseur.id]);
  }

  onDelete(fournisseur: any): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce fournisseur ?')) {
      this.fournisseurService.deleteFournisseur(fournisseur.id).subscribe({
        next: () => {
          this.loadFournisseurs();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
        }
      });
    }
  }

  onEvaluate(fournisseur: any): void {
    this.fournisseurService.evaluateSupplier(fournisseur.id).subscribe({
      next: (updatedFournisseur) => {
        this.loadFournisseurs();
      },
      error: (error) => {
        console.error('Erreur lors de l\'évaluation:', error);
      }
    });
  }

  getStatusBadgeClass(note: number): string {
    if (note >= 8) return 'badge bg-success';
    if (note >= 6) return 'badge bg-warning';
    return 'badge bg-danger';
  }
}