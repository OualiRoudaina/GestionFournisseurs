package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.HistoriqueAchats;
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

    public List<HistoriqueAchats> getAllHistoriqueAchats() {
        return historiqueAchatsRepository.findAll();
    }

    public Optional<HistoriqueAchats> getHistoriqueAchatById(Long id) {
        return historiqueAchatsRepository.findById(id);
    }

    public HistoriqueAchats saveHistoriqueAchat(HistoriqueAchats historiqueAchat) {
        if (historiqueAchat.getDateAchat() == null) {
            historiqueAchat.setDateAchat(LocalDate.now());
        }
        return historiqueAchatsRepository.save(historiqueAchat);
    }

    public HistoriqueAchats updateHistoriqueAchat(Long id, HistoriqueAchats historiqueAchatDetails) {
        return historiqueAchatsRepository.findById(id)
            .map(historiqueAchat -> {
                historiqueAchat.setFournisseur(historiqueAchatDetails.getFournisseur());
                historiqueAchat.setProduit(historiqueAchatDetails.getProduit());
                historiqueAchat.setQuantite(historiqueAchatDetails.getQuantite());
                historiqueAchat.setDelaiLivraison(historiqueAchatDetails.getDelaiLivraison());
                historiqueAchat.setDateAchat(historiqueAchatDetails.getDateAchat());
                return historiqueAchatsRepository.save(historiqueAchat);
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
