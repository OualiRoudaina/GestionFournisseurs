package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.LigneCommandeAchat;
import com.gestionfournisseurs.entity.HistoriqueAchats;
import com.gestionfournisseurs.repository.LigneCommandeAchatRepository;
import com.gestionfournisseurs.repository.HistoriqueAchatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ComparaisonOffresService {

    @Autowired
    private LigneCommandeAchatRepository ligneCommandeAchatRepository;

    @Autowired
    private HistoriqueAchatsRepository historiqueAchatsRepository;

    /**
     * Compare les prix d'un produit spécifique
     * @param produit Le nom du produit à comparer
     * @return Liste des offres triées par prix croissant
     */
    public List<LigneCommandeAchat> comparerPrixProduit(String produit) {
        return ligneCommandeAchatRepository.findBestPricesForProduct(produit);
    }

    /**
     * Compare les délais de livraison pour un produit spécifique
     * @param produit Le nom du produit à comparer
     * @return Liste des offres triées par délai de livraison croissant
     */
    public List<HistoriqueAchats> comparerDelaisLivraisonProduit(String produit) {
        return historiqueAchatsRepository.findFastestDeliveryForProduct(produit);
    }

    /**
     * Effectue une comparaison complète pour un produit (prix + délais)
     * @param produit Le nom du produit à comparer
     * @return Map contenant les comparaisons de prix et délais
     */
    public Map<String, Object> comparaisonCompleteProduit(String produit) {
        List<LigneCommandeAchat> offresPrix = comparerPrixProduit(produit);
        List<HistoriqueAchats> delaisLivraison = comparerDelaisLivraisonProduit(produit);

        Map<String, Object> comparaison = new HashMap<>();
        comparaison.put("produit", produit);
        comparaison.put("offresPrix", offresPrix);
        comparaison.put("delaisLivraison", delaisLivraison);

        // Meilleure offre prix
        Map<String, Object> meilleureOffrePrix = new HashMap<>();
        if (!offresPrix.isEmpty()) {
            LigneCommandeAchat meilleurPrix = offresPrix.get(0);
            meilleureOffrePrix.put("fournisseur", meilleurPrix.getCommande().getFournisseur().getNom());
            meilleureOffrePrix.put("prixUnitaire", meilleurPrix.getPrixUnitaire());
            meilleureOffrePrix.put("quantite", meilleurPrix.getQuantite());
            meilleureOffrePrix.put("total", meilleurPrix.getTotal());
        }
        comparaison.put("meilleureOffrePrix", meilleureOffrePrix);

        // Meilleure livraison
        Map<String, Object> meilleureLivraison = new HashMap<>();
        if (!delaisLivraison.isEmpty()) {
            HistoriqueAchats meilleurDelai = delaisLivraison.get(0);
            meilleureLivraison.put("fournisseur", meilleurDelai.getFournisseur().getNom());
            meilleureLivraison.put("delaiLivraison", meilleurDelai.getDelaiLivraison());
            meilleureLivraison.put("quantite", meilleurDelai.getQuantite());
        }
        comparaison.put("meilleureLivraison", meilleureLivraison);

        return comparaison;
    }

    /**
     * Compare tous les fournisseurs pour un produit spécifique
     * @param produit Le nom du produit
     * @return Map avec les offres groupées par fournisseur
     */
    public Map<String, Object> comparerFournisseursPourProduit(String produit) {
        List<LigneCommandeAchat> offres = ligneCommandeAchatRepository.findByProduitContainingIgnoreCase(produit);

        Map<String, Object> comparaisonFournisseurs = new HashMap<>();
        comparaisonFournisseurs.put("produit", produit);

        Map<String, List<Map<String, Object>>> offresParFournisseur = new HashMap<>();

        for (LigneCommandeAchat offre : offres) {
            String nomFournisseur = offre.getCommande().getFournisseur().getNom();

            Map<String, Object> offreDetails = new HashMap<>();
            offreDetails.put("prixUnitaire", offre.getPrixUnitaire());
            offreDetails.put("quantite", offre.getQuantite());
            offreDetails.put("total", offre.getTotal());
            offreDetails.put("dateCommande", offre.getCommande().getDate());
            offreDetails.put("statutCommande", offre.getCommande().getStatut());

            offresParFournisseur.computeIfAbsent(nomFournisseur, k -> new ArrayList<>()).add(offreDetails);
        }

        comparaisonFournisseurs.put("offresParFournisseur", offresParFournisseur);
        comparaisonFournisseurs.put("nombreFournisseurs", offresParFournisseur.size());

        return comparaisonFournisseurs;
    }

    /**
     * Calcule un score global pour chaque fournisseur pour un produit donné
     * Score basé sur prix (40%), délai de livraison (30%), et qualité de service (30%)
     * @param produit Le nom du produit
     * @return Liste des fournisseurs triés par score décroissant
     */
    public List<Map<String, Object>> calculerScoreGlobalParProduit(String produit) {
        Map<String, Object> comparaison = comparerFournisseursPourProduit(produit);
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> offresParFournisseur =
            (Map<String, List<Map<String, Object>>>) comparaison.get("offresParFournisseur");

        List<Map<String, Object>> scoresFournisseurs = new ArrayList<>();

        for (Map.Entry<String, List<Map<String, Object>>> entry : offresParFournisseur.entrySet()) {
            String nomFournisseur = entry.getKey();
            List<Map<String, Object>> offres = entry.getValue();

            // Calculer les moyennes
            double prixMoyen = offres.stream()
                .mapToDouble(o -> (Double) o.get("prixUnitaire"))
                .average()
                .orElse(0.0);

            double prixMin = offres.stream()
                .mapToDouble(o -> (Double) o.get("prixUnitaire"))
                .min()
                .orElse(0.0);

            // Obtenir le délai moyen pour ce fournisseur et produit
            List<HistoriqueAchats> historiques = historiqueAchatsRepository
                .findByProduitContainingIgnoreCase(produit)
                .stream()
                .filter(h -> h.getFournisseur().getNom().equals(nomFournisseur))
                .toList();

            double delaiMoyen = historiques.stream()
                .mapToDouble(h -> h.getDelaiLivraison())
                .average()
                .orElse(30.0); // Délai par défaut si pas d'historique

            // Obtenir la qualité de service du fournisseur (simplifié)
            double qualiteService = historiques.stream()
                .findFirst()
                .map(h -> h.getFournisseur().getQualiteService() != null ?
                     h.getFournisseur().getQualiteService().doubleValue() : 3.0)
                .orElse(3.0);

            // Calcul du score (normalisé)
            double scorePrix = Math.max(0, (100 - prixMoyen) / 100 * 40); // Plus le prix est bas, mieux c'est
            double scoreDelai = Math.max(0, (30 - delaiMoyen) / 30 * 30); // Délai idéal = 0 jours
            double scoreQualite = (qualiteService / 5.0) * 30; // Qualité sur 5, pondérée à 30%

            double scoreGlobal = scorePrix + scoreDelai + scoreQualite;

            Map<String, Object> scoreFournisseur = new HashMap<>();
            scoreFournisseur.put("fournisseur", nomFournisseur);
            scoreFournisseur.put("scoreGlobal", Math.round(scoreGlobal * 100.0) / 100.0);
            scoreFournisseur.put("prixMoyen", Math.round(prixMoyen * 100.0) / 100.0);
            scoreFournisseur.put("prixMin", Math.round(prixMin * 100.0) / 100.0);
            scoreFournisseur.put("delaiMoyen", Math.round(delaiMoyen * 100.0) / 100.0);
            scoreFournisseur.put("qualiteService", qualiteService);
            scoreFournisseur.put("nombreOffres", offres.size());

            scoresFournisseurs.add(scoreFournisseur);
        }

        // Trier par score décroissant
        scoresFournisseurs.sort((a, b) ->
            Double.compare((Double) b.get("scoreGlobal"), (Double) a.get("scoreGlobal")));

        return scoresFournisseurs;
    }

    /**
     * Retourne la liste de tous les produits disponibles pour la comparaison
     * @return Liste des noms de produits distincts
     */
    public List<String> getProduitsDisponibles() {
        return ligneCommandeAchatRepository.findAllProducts();
    }
}