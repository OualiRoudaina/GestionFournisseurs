import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LigneCommandeAchatService } from '../../services/ligne-commande-achat.service';
import { CommandeAchatService } from '../../services/commande-achat.service';
import { CommandeAchat } from '../../models/commande-achat.model';

@Component({
  selector: 'app-ligne-commande-form',
  templateUrl: './ligne-commande-form.component.html',
  styleUrls: ['./ligne-commande-form.component.css']
})
export class LigneCommandeFormComponent implements OnInit {
  ligneForm: FormGroup;
  isEditMode = false;
  ligneId: number | null = null;
  loading = false;
  errorMessage = '';
  successMessage = '';
  commandes: CommandeAchat[] = [];

  constructor(
    private fb: FormBuilder,
    private ligneService: LigneCommandeAchatService,
    private commandeService: CommandeAchatService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.ligneForm = this.fb.group({
      commandeId: ['', [Validators.required]],
      produit: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(200)]],
      quantite: [1, [Validators.required, Validators.min(1)]],
      prixUnitaire: [0.01, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.loadCommandes();
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.ligneId = +params['id'];
        this.loadLigne(this.ligneId);
      }
    });
  }

  loadCommandes(): void {
    this.commandeService.getAllCommandes().subscribe({
      next: (data) => {
        this.commandes = data;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger la liste des commandes';
      }
    });
  }

  loadLigne(id: number): void {
    this.loading = true;
    this.ligneService.getLigneCommandeById(id).subscribe({
      next: (ligne) => {
        this.ligneForm.patchValue({
          commandeId: ligne.commande?.id ?? '',
          produit: ligne.produit ?? '',
          quantite: ligne.quantite ?? 1,
          prixUnitaire: ligne.prixUnitaire ?? 0.01
        });
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement de la ligne de commande';
        this.loading = false;
      }
    });
  }

  commandeLabel(c: CommandeAchat): string {
    const nom = c.fournisseur?.nom ?? '—';
    const dateStr = c.date
      ? new Date(c.date).toLocaleDateString('fr-FR')
      : '';
    return `#${c.id} — ${nom}${dateStr ? ' — ' + dateStr : ''}`;
  }

  onSubmit(): void {
    if (!this.ligneForm.valid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const v = this.ligneForm.value;
    const body = {
      commande: { id: Number(v.commandeId) },
      produit: String(v.produit).trim(),
      quantite: parseInt(String(v.quantite), 10),
      prixUnitaire: parseFloat(String(v.prixUnitaire))
    };

    const request =
      this.isEditMode && this.ligneId
        ? this.ligneService.updateLigneCommande(this.ligneId, body)
        : this.ligneService.createLigneCommande(body);

    request.subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = this.isEditMode
          ? 'Ligne de commande modifiée avec succès.'
          : 'Ligne de commande créée avec succès.';
        setTimeout(() => this.router.navigate(['/lignes-commande']), 1200);
      },
      error: (error: unknown) => {
        this.errorMessage = this.isEditMode
          ? 'Erreur lors de la modification de la ligne'
          : 'Erreur lors de la création de la ligne';
        this.loading = false;
        console.error(error);
        const err = error as { error?: unknown };
        const bodyErr = err?.error;
        if (typeof bodyErr === 'string') {
          this.errorMessage += ': ' + bodyErr;
        } else if (bodyErr && typeof bodyErr === 'object') {
          const o = bodyErr as Record<string, unknown>;
          if (typeof o['error'] === 'string') this.errorMessage += ': ' + o['error'];
          else if (typeof o['message'] === 'string') this.errorMessage += ': ' + o['message'];
          else if (Array.isArray(o['violations'])) {
            const msgs = (o['violations'] as { message?: string }[])
              .map((x) => x.message)
              .filter(Boolean)
              .join(' ');
            if (msgs) this.errorMessage += ': ' + msgs;
          }
        }
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/lignes-commande']);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.ligneForm.controls).forEach((key) => {
      this.ligneForm.get(key)?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const c = this.ligneForm.get(fieldName);
    return !!(c?.invalid && c?.touched);
  }

  getFieldLabel(fieldName: string): string {
    const labels: Record<string, string> = {
      commandeId: 'Commande',
      produit: 'Produit',
      quantite: 'Quantité',
      prixUnitaire: 'Prix unitaire'
    };
    return labels[fieldName] || fieldName;
  }

  getFieldErrorMessage(fieldName: string): string {
    const control = this.ligneForm.get(fieldName);
    if (!control?.errors || !control.touched) return '';
    if (control.errors['required']) {
      return `${this.getFieldLabel(fieldName)} est requis`;
    }
    if (control.errors['minlength']) {
      return `Minimum ${control.errors['minlength'].requiredLength} caractère(s)`;
    }
    if (control.errors['maxlength']) {
      return `Maximum ${control.errors['maxlength'].requiredLength} caractères`;
    }
    if (control.errors['min']) {
      return `La valeur minimum est ${control.errors['min'].min}`;
    }
    return '';
  }
}
