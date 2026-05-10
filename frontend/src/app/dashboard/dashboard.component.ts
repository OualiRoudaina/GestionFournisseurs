import { Component, OnInit } from '@angular/core';
import { FournisseurService } from '../services/fournisseur.service';
import { CommandeAchatService } from '../services/commande-achat.service';
import { HistoriqueAchatsService } from '../services/historique-achats.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  stats = {
    totalFournisseurs: 0,
    totalCommandes: 0,
    totalAchats: 0,
    montantTotal: 0,
    commandesEnAttente: 0,
    commandesLivre: 0,
    moyenneDelaiLivraison: 0,
    topFournisseur: ''
  };

  recentCommandes: any[] = [];
  topFournisseurs: any[] = [];

  constructor(
    private fournisseurService: FournisseurService,
    private commandeService: CommandeAchatService,
    private historiqueService: HistoriqueAchatsService
  ) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    // Charger les statistiques
    this.fournisseurService.getAllFournisseurs().subscribe(fournisseurs => {
      this.stats.totalFournisseurs = fournisseurs.length;
    });

    this.commandeService.getAllCommandes().subscribe(commandes => {
      this.stats.totalCommandes = commandes.length;
      this.stats.montantTotal = commandes.reduce((sum, cmd) => sum + (cmd.montant || 0), 0);
      this.stats.commandesEnAttente = commandes.filter(cmd => cmd.statut === 'En attente').length;
      this.stats.commandesLivre = commandes.filter(cmd => cmd.statut === 'Livrée').length;

      // Récupérer les 5 dernières commandes
      this.recentCommandes = commandes
        .filter(cmd => cmd.date)
        .sort((a, b) => new Date(b.date!).getTime() - new Date(a.date!).getTime())
        .slice(0, 5);
    });

    this.historiqueService.getAllHistoriqueAchats().subscribe({
      next: (historique) => {
        this.stats.totalAchats = historique.length;
        if (historique.length > 0) {
          this.stats.moyenneDelaiLivraison = Math.round(
            historique.reduce((sum: number, h: any) => sum + h.delaiLivraison, 0) / historique.length
          );
        }
      },
      error: (error) => {
        console.error('Erreur lors du chargement de l\'historique:', error);
        this.stats.totalAchats = 0;
        this.stats.moyenneDelaiLivraison = 7;
      }
    });

    // Charger les meilleurs fournisseurs
    this.fournisseurService.getTopSuppliers().subscribe(fournisseurs => {
      this.topFournisseurs = fournisseurs.slice(0, 3);
      if (fournisseurs.length > 0) {
        this.stats.topFournisseur = fournisseurs[0].nom;
      }
    });
  }

  getStatusBadgeClass(statut: string): string {
    switch (statut) {
      case 'Livrée': return 'badge bg-success';
      case 'Confirmée': return 'badge bg-primary';
      case 'En attente': return 'badge bg-warning';
      case 'Annulée': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  formatMontant(montant: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(montant);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR');
  }
}