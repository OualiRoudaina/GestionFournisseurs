import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FournisseurService } from '../../services/fournisseur.service';

@Component({
  selector: 'app-fournisseur-form',
  templateUrl: './fournisseur-form.component.html',
  styleUrls: ['./fournisseur-form.component.css']
})
export class FournisseurFormComponent implements OnInit {

  fournisseurForm: FormGroup;
  isEditMode: boolean = false;
  fournisseurId: number | null = null;
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private fournisseurService: FournisseurService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.fournisseurForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      contact: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      qualiteService: [3, [Validators.required, Validators.min(1), Validators.max(10)]],
      note: [5.0, [Validators.required, Validators.min(0), Validators.max(10)]]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.fournisseurId = +params['id'];
        this.loadFournisseur(this.fournisseurId);
      }
    });
  }

  loadFournisseur(id: number): void {
    this.loading = true;
    this.fournisseurService.getFournisseurById(id).subscribe({
      next: (fournisseur) => {
        this.fournisseurForm.patchValue({
          nom: fournisseur.nom,
          contact: fournisseur.contact,
          qualiteService: fournisseur.qualiteService || 3,
          note: fournisseur.note || 5.0
        });
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement du fournisseur';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.fournisseurForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const fournisseurData = this.fournisseurForm.value;

      const request = this.isEditMode && this.fournisseurId
        ? this.fournisseurService.updateFournisseur(this.fournisseurId, fournisseurData)
        : this.fournisseurService.createFournisseur(fournisseurData);

      request.subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/fournisseurs']);
        },
        error: (error: any) => {
          this.errorMessage = this.isEditMode ? 'Erreur lors de la modification' : 'Erreur lors de la création';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.router.navigate(['/fournisseurs']);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.fournisseurForm.controls).forEach(key => {
      const control = this.fournisseurForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldErrorMessage(fieldName: string): string {
    const control = this.fournisseurForm.get(fieldName);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} est requis`;
      }
      if (control.errors['email']) {
        return 'Format d\'email invalide';
      }
      if (control.errors['minlength']) {
        return `Minimum ${control.errors['minlength'].requiredLength} caractères`;
      }
      if (control.errors['maxlength']) {
        return `Maximum ${control.errors['maxlength'].requiredLength} caractères`;
      }
      if (control.errors['min']) {
        return `Valeur minimum: ${control.errors['min'].min}`;
      }
      if (control.errors['max']) {
        return `Valeur maximum: ${control.errors['max'].max}`;
      }
    }
    return '';
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.fournisseurForm.get(fieldName);
    return !!(control?.invalid && control?.touched);
  }
}