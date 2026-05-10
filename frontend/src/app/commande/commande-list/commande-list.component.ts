import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommandeAchatService } from '../../services/commande-achat.service';

@Component({
  selector: 'app-commande-list',
  templateUrl: './commande-list.component.html',
  styleUrls: ['./commande-list.component.css']
})
export class CommandeListComponent implements OnInit {

  commandes: any[] = [];

  constructor(
    private commandeService: CommandeAchatService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCommandes();
  }

  loadCommandes(): void {
    this.commandeService.getAllCommandes().subscribe({
      next: (data) => {
        this.commandes = data;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commandes:', error);
      }
    });
  }

  onNewCommande(): void {
    this.router.navigate(['/commandes/new']);
  }

  onEdit(commande: any): void {
    this.router.navigate(['/commandes/edit', commande.id]);
  }

  onDelete(commande: any): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette commande ?')) {
      // TODO: Implement delete
      this.loadCommandes();
    }
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