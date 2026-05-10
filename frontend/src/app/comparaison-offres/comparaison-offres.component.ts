import { Component, OnInit } from '@angular/core';
import { ComparaisonOffresService } from '../services/comparaison-offres.service';

@Component({
  selector: 'app-comparaison-offres',
  templateUrl: './comparaison-offres.component.html',
  styleUrls: ['./comparaison-offres.component.css']
})
export class ComparaisonOffresComponent implements OnInit {

  // Data properties
  produits: string[] = [];
  selectedProduit: string = '';
  
  // Comparison data
  comparaisonPrix: any[] = [];
  comparaisonDelais: any[] = [];
  comparaisonComplete: any = null;
  comparaisonFournisseurs: any = null;
  scoresGlobaux: any[] = [];
  
  // UI state
  activeTab: string = 'complet'; // 'complet', 'prix', 'delais', 'fournisseurs', 'scores'
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  
  constructor(private comparaisonOffresService: ComparaisonOffresService) { }

  ngOnInit(): void {
    this.loadAvailableProducts();
  }

  // Load list of available products
  loadAvailableProducts(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.comparaisonOffresService.getAvailableProducts().subscribe({
      next: (data: string[]) => {
        this.produits = data;
        this.isLoading = false;
        if (this.produits.length > 0) {
          this.selectedProduit = this.produits[0];
          this.performComparison();
        }
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des produits';
        this.isLoading = false;
        console.error('Error loading products:', error);
      }
    });
  }

  // Main comparison method - calls based on active tab
  performComparison(): void {
    if (!this.selectedProduit) {
      this.errorMessage = 'Veuillez sélectionner un produit';
      return;
    }
    
    this.errorMessage = '';
    this.successMessage = '';
    this.isLoading = true;
    
    switch(this.activeTab) {
      case 'complet':
        this.getComparaisonComplete();
        break;
      case 'prix':
        this.getComparaisonPrix();
        break;
      case 'delais':
        this.getComparaisonDelais();
        break;
      case 'fournisseurs':
        this.getComparaisonFournisseurs();
        break;
      case 'scores':
        this.getScoresGlobaux();
        break;
    }
  }

  // Get complete comparison
  private getComparaisonComplete(): void {
    this.comparaisonOffresService.comparaisonCompleteProduit(this.selectedProduit).subscribe({
      next: (data) => {
        this.comparaisonComplete = data;
        this.isLoading = false;
        this.successMessage = 'Comparaison complète chargée avec succès';
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la comparaison complète';
        this.isLoading = false;
        console.error('Error in complete comparison:', error);
      }
    });
  }

  // Get price comparison
  private getComparaisonPrix(): void {
    this.comparaisonOffresService.comparerPrixProduit(this.selectedProduit).subscribe({
      next: (data) => {
        this.comparaisonPrix = data;
        this.isLoading = false;
        this.successMessage = `${this.comparaisonPrix.length} offre(s) trouvée(s)`;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la comparaison des prix';
        this.isLoading = false;
        console.error('Error in price comparison:', error);
      }
    });
  }

  // Get delivery comparison
  private getComparaisonDelais(): void {
    this.comparaisonOffresService.comparerDelaisLivraison(this.selectedProduit).subscribe({
      next: (data) => {
        this.comparaisonDelais = data;
        this.isLoading = false;
        this.successMessage = `${this.comparaisonDelais.length} historique(s) trouvé(s)`;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la comparaison des délais';
        this.isLoading = false;
        console.error('Error in delivery comparison:', error);
      }
    });
  }

  // Get supplier comparison
  private getComparaisonFournisseurs(): void {
    this.comparaisonOffresService.comparerFournisseursPourProduit(this.selectedProduit).subscribe({
      next: (data) => {
        this.comparaisonFournisseurs = data;
        this.isLoading = false;
        if (data && data.nombreFournisseurs) {
          this.successMessage = `${data.nombreFournisseurs} fournisseur(s) trouvé(s)`;
        }
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la comparaison des fournisseurs';
        this.isLoading = false;
        console.error('Error in supplier comparison:', error);
      }
    });
  }

  // Get global scores
  private getScoresGlobaux(): void {
    this.comparaisonOffresService.calculerScoreGlobalParProduit(this.selectedProduit).subscribe({
      next: (data) => {
        this.scoresGlobaux = data;
        this.isLoading = false;
        this.successMessage = `${this.scoresGlobaux.length} fournisseur(s) évalué(s)`;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du calcul des scores';
        this.isLoading = false;
        console.error('Error in global scores:', error);
      }
    });
  }

  // Switch tab and reload data
  switchTab(tab: string): void {
    this.activeTab = tab;
    this.performComparison();
  }

  // Change product and reload
  onProduitChanged(): void {
    this.performComparison();
  }

  // Helper method to get supplier name from offer
  getFournisseurName(offer: any): string {
    if (offer?.commande?.fournisseur?.nom) {
      return offer.commande.fournisseur.nom;
    }
    return 'Unknown';
  }

  // Helper method to get score color
  getScoreColor(score: number): string {
    if (score >= 85) return 'success';
    if (score >= 70) return 'info';
    if (score >= 50) return 'warning';
    return 'danger';
  }

  // Helper method to get badge class
  getBadgeClass(score: number): string {
    return `badge bg-${this.getScoreColor(score)}`;
  }

  // Format currency
  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(value);
  }

  // Get list of suppliers from comparison
  getFournisseursFromComparison(): string[] {
    if (this.comparaisonFournisseurs && this.comparaisonFournisseurs.offresParFournisseur) {
      return Object.keys(this.comparaisonFournisseurs.offresParFournisseur);
    }
    return [];
  }

  // Get offers by supplier
  getOffresByFournisseur(fournisseur: string): any[] {
    if (this.comparaisonFournisseurs && this.comparaisonFournisseurs.offresParFournisseur) {
      return this.comparaisonFournisseurs.offresParFournisseur[fournisseur] || [];
    }
    return [];
  }
}