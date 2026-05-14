package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.HistoriqueAchats;
import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.repository.FournisseurRepository;
import com.gestionfournisseurs.repository.HistoriqueAchatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoriqueAchatsService {

    @Autowired
    private HistoriqueAchatsRepository historiqueAchatsRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private Fournisseur resolveFournisseur(Fournisseur ref) {
        if (ref == null || ref.getId() == null) {
            return null;
        }
        return fournisseurRepository.findById(ref.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fournisseur introuvable avec l'id " + ref.getId()));
    }

    public List<HistoriqueAchats> getAllHistoriqueAchats() {
        return historiqueAchatsRepository.findAllWithFournisseur();
    }

    public Optional<HistoriqueAchats> getHistoriqueAchatById(Long id) {
        return historiqueAchatsRepository.findByIdWithFournisseur(id);
    }

    public HistoriqueAchats saveHistoriqueAchat(HistoriqueAchats historiqueAchat) {
        if (historiqueAchat.getDateAchat() == null) {
            historiqueAchat.setDateAchat(LocalDate.now());
        }
        historiqueAchat.setFournisseur(resolveFournisseur(historiqueAchat.getFournisseur()));
        HistoriqueAchats saved = historiqueAchatsRepository.save(historiqueAchat);
        return historiqueAchatsRepository.findByIdWithFournisseur(saved.getId()).orElse(saved);
    }

    public HistoriqueAchats updateHistoriqueAchat(Long id, HistoriqueAchats historiqueAchatDetails) {
        return historiqueAchatsRepository.findByIdWithFournisseur(id)
            .map(historiqueAchat -> {
                historiqueAchat.setFournisseur(resolveFournisseur(historiqueAchatDetails.getFournisseur()));
                historiqueAchat.setProduit(historiqueAchatDetails.getProduit());
                historiqueAchat.setQuantite(historiqueAchatDetails.getQuantite());
                historiqueAchat.setDelaiLivraison(historiqueAchatDetails.getDelaiLivraison());
                historiqueAchat.setDateAchat(historiqueAchatDetails.getDateAchat());
                HistoriqueAchats saved = historiqueAchatsRepository.save(historiqueAchat);
                return historiqueAchatsRepository.findByIdWithFournisseur(saved.getId()).orElse(saved);
            })
            .orElse(null);
    }

    public void deleteHistoriqueAchat(Long id) {
        historiqueAchatsRepository.deleteById(id);
    }

    public List<HistoriqueAchats> getHistoriqueByFournisseurId(Long fournisseurId) {
        return historiqueAchatsRepository.findByFournisseurId(fournisseurId);
    }

    public List<HistoriqueAchats> getHistoriqueByProduit(String produit) {
        return historiqueAchatsRepository.findByProduitContainingIgnoreCase(produit);
    }

    public List<HistoriqueAchats> getHistoriqueByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return historiqueAchatsRepository.findByDateAchatBetween(dateDebut, dateFin);
    }

    public Double getAverageDelaiLivraisonByFournisseur(Long fournisseurId) {
        return historiqueAchatsRepository.getAverageDelaiLivraisonByFournisseur(fournisseurId);
    }

    public List<HistoriqueAchats> getFastestDeliveryForProduct(String produit) {
        return historiqueAchatsRepository.findFastestDeliveryForProduct(produit);
    }

    public Long getTotalAchatsByFournisseur(Long fournisseurId) {
        return historiqueAchatsRepository.getTotalAchatsByFournisseur(fournisseurId);
    }
}
