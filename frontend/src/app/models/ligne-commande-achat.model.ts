import { CommandeAchat } from './commande-achat.model';

export interface LigneCommandeAchat {
  id?: number;
  commande?: CommandeAchat;
  commandeId?: number;
  produit: string;
  quantite: number;
  prixUnitaire: number;
}
