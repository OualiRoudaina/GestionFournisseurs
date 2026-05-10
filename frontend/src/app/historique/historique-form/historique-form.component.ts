import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HistoriqueAchatsService } from '../../services/historique-achats.service';
import { FournisseurService } from '../../services/fournisseur.service';
import { Fournisseur } from '../../models/fournisseur.model';

@Component({
  selector: 'app-historique-form',
  templateUrl: './historique-form.component.html',
  styleUrls: ['./historique-form.component.css']
})
export class HistoriqueFormComponent implements OnInit {

  historiqueForm: FormGroup;
  isEditMode: boolean = false;
  historiqueId: number | null = null;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  
  fournisseurs: Fournisseur[] = [];

  constructor(
    private fb: FormBuilder,
    private historiqueService: HistoriqueAchatsService,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.historiqueForm = this.fb.group({
      fournisseurId: ['', [Validators.required]],
      produit: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(200)]],
      quantite: [1, [Validators.required, Validators.min(1)]],
      delaiLivraison: [7, [Validators.required, Validators.min(1)]],
      dateAchat: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadFournisseurs();
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.historiqueId = +params['id'];
        this.loadHistorique(this.historiqueId);
      } else {
        // Mode création : date par défaut = aujourd'hui
        const today = new Date().toISOString().split('T')[0];
        this.historiqueForm.patchValue({ dateAchat: today });
      }
    });
  }

  loadFournisseurs(): void {
    this.fournisseurService.getAllFournisseurs().subscribe({
      next: (data) => {
        this.fournisseurs = data;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des fournisseurs:', error);
        this.errorMessage = 'Impossible de charger la liste des fournisseurs';
      }
    });
  }

  loadHistorique(id: number): void {
    this.loading = true;
    this.historiqueService.getHistoriqueAchatById(id).subscribe({
      next: (historique) => {
        // Formater la date pour l'input date
        const dateFormatted = historique.dateAchat ? new Date(historique.dateAchat).toISOString().split('T')[0] : '';
        
        this.historiqueForm.patchValue({
          fournisseurId: historique.fournisseur?.id || historique.fournisseurId || '',
          produit: historique.produit || '',
          quantite: historique.quantite || 1,
          delaiLivraison: historique.delaiLivraison || 7,
          dateAchat: dateFormatted
        });
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement de l\'historique';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.historiqueForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const formValue = this.historiqueForm.value;
      
      // Préparer les données pour l'API
      const historiqueData: any = {
        fournisseur: {
          id: formValue.fournisseurId
        },
        produit: formValue.produit,
        quantite: parseInt(formValue.quantite),
        delaiLivraison: parseInt(formValue.delaiLivraison),
        dateAchat: formValue.dateAchat
      };

      const request = this.isEditMode && this.historiqueId
        ? this.historiqueService.updateHistoriqueAchat(this.historiqueId, historiqueData)
        : this.historiqueService.createHistoriqueAchat(historiqueData);

      request.subscribe({
        next: (historique) => {
          this.loading = false;
          this.successMessage = this.isEditMode 
            ? 'Historique modifié avec succès !' 
            : 'Achat ajouté à l\'historique avec succès !';
          
          // Rediriger après 1.5 secondes
          setTimeout(() => {
            this.router.navigate(['/historique']);
          }, 1500);
        },
        error: (error: any) => {
          this.errorMessage = this.isEditMode 
            ? 'Erreur lors de la modification de l\'historique' 
            : 'Erreur lors de l\'ajout de l\'achat';
          this.loading = false;
          console.error('Erreur:', error);
          
          if (error.error && error.error.message) {
            this.errorMessage += ': ' + error.error.message;
          }
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.router.navigate(['/historique']);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.historiqueForm.controls).forEach(key => {
      const control = this.historiqueForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldErrorMessage(fieldName: string): string {
    const control = this.historiqueForm.get(fieldName);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return `${this.getFieldLabel(fieldName)} est requis`;
      }
      if (control.errors['minlength']) {
        return `Minimum ${control.errors['minlength'].requiredLength} caractères`;
      }
      if (control.errors['maxlength']) {
        return `Maximum ${control.errors['maxlength'].requiredLength} caractères`;
      }
      if (control.errors['min']) {
        return `La valeur minimum est ${control.errors['min'].min}`;
      }
    }
    return '';
  }

  getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      'fournisseurId': 'Fournisseur',
      'produit': 'Produit',
      'quantite': 'Quantité',
      'delaiLivraison': 'Délai de livraison',
      'dateAchat': 'Date d\'achat'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.historiqueForm.get(fieldName);
    return !!(control?.invalid && control?.touched);
  }

  getSelectedFournisseur(): Fournisseur | undefined {
    const fournisseurId = this.historiqueForm.get('fournisseurId')?.value;
    return this.fournisseurs.find(f => f.id === fournisseurId);
  }
}