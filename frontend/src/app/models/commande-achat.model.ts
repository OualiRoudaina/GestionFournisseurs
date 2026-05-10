import { Fournisseur } from './fournisseur.model';

export interface CommandeAchat {
  id?: number;
  fournisseur?: Fournisseur;
  fournisseurId?: number;
  date?: string;
  statut: string;
  montant: number;
}
