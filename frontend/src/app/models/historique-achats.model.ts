import { Fournisseur } from './fournisseur.model';

export interface HistoriqueAchats {
  id?: number;
  fournisseur?: Fournisseur;
  fournisseurId?: number;
  produit: string;
  quantite: number;
  delaiLivraison: number;
  dateAchat?: string;
}
