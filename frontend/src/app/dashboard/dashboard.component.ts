import { Component, OnInit } from '@angular/core';
import {
  DashboardSummary,
  DashboardSummaryService,
  EvolutionPrixProduit,
  PrixPoint
} from '../services/dashboard-summary.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  summary: DashboardSummary | null = null;
  loading = false;
  errorMessage = '';

  dateFrom = '';
  dateTo = '';

  selectedProduit: string | null = null;

  constructor(private dashboardSummaryService: DashboardSummaryService) {}

  ngOnInit(): void {
    const to = new Date();
    const from = new Date();
    from.setMonth(from.getMonth() - 12);
    this.dateTo = to.toISOString().split('T')[0];
    this.dateFrom = from.toISOString().split('T')[0];
    this.loadSummary();
  }

  loadSummary(): void {
    this.loading = true;
    this.errorMessage = '';
    this.summary = null;
    this.dashboardSummaryService.getSummary(this.dateFrom, this.dateTo).subscribe({
      next: (data) => {
        this.summary = data;
        this.syncSelectedProduit();
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Impossible de charger les statistiques du tableau de bord.';
        this.loading = false;
      }
    });
  }

  onPeriodeChange(): void {
    this.loadSummary();
  }

  private syncSelectedProduit(): void {
    const list = this.summary?.evolutionPrixParProduit ?? [];
    if (!list.length) {
      this.selectedProduit = null;
      return;
    }
    if (!this.selectedProduit || !list.some((e) => e.produit === this.selectedProduit)) {
      this.selectedProduit = list[0].produit;
    }
  }

  get evolutionSelection(): EvolutionPrixProduit | null {
    if (!this.summary || !this.selectedProduit) {
      return null;
    }
    return this.summary.evolutionPrixParProduit.find((e) => e.produit === this.selectedProduit) ?? null;
  }

  maxMontantMois(): number {
    const vals = (this.summary?.montantsParMois ?? []).map((m) => m.montantTotal);
    return Math.max(1, ...vals);
  }

  barPercent(montant: number): number {
    return Math.round((montant / this.maxMontantMois()) * 100);
  }

  formatMoisCourts(mois: string): string {
    const parts = mois.split('-');
    if (parts.length < 2) return mois;
    const y = Number(parts[0]);
    const m = Number(parts[1]);
    if (!y || !m) return mois;
    const d = new Date(y, m - 1, 1);
    return d.toLocaleDateString('fr-FR', { month: 'short', year: '2-digit' });
  }

  sparklinePoints(points: PrixPoint[]): string {
    if (!points || points.length < 2) {
      return '';
    }
    const prices = points.map((p) => p.prixUnitaire);
    const min = Math.min(...prices);
    const max = Math.max(...prices);
    const range = max - min || 1;
    return points
      .map((p, i) => {
        const x = points.length === 1 ? 50 : (i / (points.length - 1)) * 100;
        const y = 48 - ((p.prixUnitaire - min) / range) * 40;
        return `${x.toFixed(2)},${y.toFixed(2)}`;
      })
      .join(' ');
  }

  minMaxPrix(points: PrixPoint[]): { min: number; max: number } {
    if (!points.length) {
      return { min: 0, max: 0 };
    }
    const prices = points.map((p) => p.prixUnitaire);
    return { min: Math.min(...prices), max: Math.max(...prices) };
  }

  getStatusBadgeClass(statut: string): string {
    switch (statut) {
      case 'Livrée':
        return 'badge bg-success';
      case 'Confirmée':
        return 'badge bg-primary';
      case 'En attente':
        return 'badge bg-warning text-dark';
      case 'Annulée':
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }

  formatMontant(montant: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(montant ?? 0);
  }

  formatDate(date: string): string {
    if (!date) return '—';
    return new Date(date).toLocaleDateString('fr-FR');
  }
}
