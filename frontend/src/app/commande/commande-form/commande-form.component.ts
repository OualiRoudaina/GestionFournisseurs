import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommandeAchatService } from '../../services/commande-achat.service';
import { FournisseurService } from '../../services/fournisseur.service';
import { Fournisseur } from '../../models/fournisseur.model';

@Component({
  selector: 'app-commande-form',
  templateUrl: './commande-form.component.html',
  styleUrls: ['./commande-form.component.css']
})
export class CommandeFormComponent implements OnInit {

  commandeForm: FormGroup;
  isEditMode: boolean = false;
  commandeId: number | null = null;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  
  fournisseurs: Fournisseur[] = [];
  statuts: string[] = ['En attente', 'Confirmée', 'Livrée', 'Annulée'];

  constructor(
    private fb: FormBuilder,
    private commandeService: CommandeAchatService,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.commandeForm = this.fb.group({
      fournisseurId: ['', [Validators.required]],
      date: ['', [Validators.required]],
      statut: ['En attente', [Validators.required]],
      montant: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.loadFournisseurs();
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.commandeId = +params['id'];
        this.loadCommande(this.commandeId);
      } else {
        // Mode création : date par défaut = aujourd'hui
        const today = new Date().toISOString().split('T')[0];
        this.commandeForm.patchValue({ date: today });
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

  loadCommande(id: number): void {
    this.loading = true;
    this.commandeService.getCommandeById(id).subscribe({
      next: (commande) => {
        // Formater la date pour l'input date
        const dateFormatted = commande.date ? new Date(commande.date).toISOString().split('T')[0] : '';
        
        this.commandeForm.patchValue({
          fournisseurId: commande.fournisseur?.id || commande.fournisseurId || '',
          date: dateFormatted,
          statut: commande.statut || 'En attente',
          montant: commande.montant || 0
        });
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement de la commande';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.commandeForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const formValue = this.commandeForm.value;
      
      // Préparer les données pour l'API
      const commandeData: any = {
        fournisseur: {
          id: formValue.fournisseurId
        },
        date: formValue.date,
        statut: formValue.statut,
        montant: parseFloat(formValue.montant)
      };

      const request = this.isEditMode && this.commandeId
        ? this.commandeService.updateCommande(this.commandeId, commandeData)
        : this.commandeService.createCommande(commandeData);

      request.subscribe({
        next: (commande) => {
          this.loading = false;
          this.successMessage = this.isEditMode 
            ? 'Commande modifiée avec succès !' 
            : 'Commande créée avec succès !';
          
          // Rediriger après 1.5 secondes
          setTimeout(() => {
            this.router.navigate(['/commandes']);
          }, 1500);
        },
        error: (error: any) => {
          this.errorMessage = this.isEditMode 
            ? 'Erreur lors de la modification de la commande' 
            : 'Erreur lors de la création de la commande';
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
    this.router.navigate(['/commandes']);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.commandeForm.controls).forEach(key => {
      const control = this.commandeForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldErrorMessage(fieldName: string): string {
    const control = this.commandeForm.get(fieldName);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return `${this.getFieldLabel(fieldName)} est requis`;
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
      'date': 'Date de commande',
      'statut': 'Statut',
      'montant': 'Montant'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.commandeForm.get(fieldName);
    return !!(control?.invalid && control?.touched);
  }

  getSelectedFournisseur(): Fournisseur | undefined {
    const fournisseurId = this.commandeForm.get('fournisseurId')?.value;
    return this.fournisseurs.find(f => f.id === fournisseurId);
  }
}