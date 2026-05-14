import { CommandeAchat } from './commande-achat.model';

/** Référence envoyée en POST/PUT, ou commande enrichie renvoyée par l’API */
export type LigneCommandeCommandeRef = CommandeAchat | { id: number };

export interface LigneCommandeAchat {
  id?: number;
  commande?: LigneCommandeCommandeRef;
  commandeId?: number;
  produit: string;
  quantite: number;
  prixUnitaire: number;
}
