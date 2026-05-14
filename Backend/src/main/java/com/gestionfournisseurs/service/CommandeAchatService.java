package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.repository.CommandeAchatRepository;
import com.gestionfournisseurs.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommandeAchatService {

    @Autowired
    private CommandeAchatRepository commandeAchatRepository;

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

    public List<CommandeAchat> getAllCommandes() {
        return commandeAchatRepository.findAllWithFournisseur();
    }

    public Optional<CommandeAchat> getCommandeById(Long id) {
        return commandeAchatRepository.findByIdWithFournisseur(id);
    }

    public CommandeAchat saveCommande(CommandeAchat commande) {
        if (commande.getDate() == null) {
            commande.setDate(LocalDate.now());
        }
        commande.setFournisseur(resolveFournisseur(commande.getFournisseur()));
        CommandeAchat saved = commandeAchatRepository.save(commande);
        return commandeAchatRepository.findByIdWithFournisseur(saved.getId()).orElse(saved);
    }

    public CommandeAchat updateCommande(Long id, CommandeAchat commandeDetails) {
        return commandeAchatRepository.findByIdWithFournisseur(id)
            .map(commande -> {
                commande.setFournisseur(resolveFournisseur(commandeDetails.getFournisseur()));
                commande.setDate(commandeDetails.getDate());
                commande.setStatut(commandeDetails.getStatut());
                commande.setMontant(commandeDetails.getMontant());
                return commandeAchatRepository.save(commande);
            })
            .orElse(null);
    }

    public void deleteCommande(Long id) {
        commandeAchatRepository.deleteById(id);
    }

    public List<CommandeAchat> getCommandesByFournisseurId(Long fournisseurId) {
        return commandeAchatRepository.findByFournisseurId(fournisseurId);
    }

    public List<CommandeAchat> getCommandesByFournisseur(Fournisseur fournisseur) {
        return commandeAchatRepository.findByFournisseur(fournisseur);
    }

    public List<CommandeAchat> getCommandesByStatut(String statut) {
        return commandeAchatRepository.findByStatut(statut);
    }

    public List<CommandeAchat> getCommandesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return commandeAchatRepository.findByDateBetween(dateDebut, dateFin);
    }

    public List<CommandeAchat> getRecentCommandesByFournisseur(Long fournisseurId) {
        return commandeAchatRepository.findRecentCommandesByFournisseur(fournisseurId, LocalDate.now().minusMonths(6));
    }

    public CommandeAchat updateStatut(Long id, String nouveauStatut) {
        return commandeAchatRepository.findByIdWithFournisseur(id)
            .map(commande -> {
                commande.setStatut(nouveauStatut);
                return commandeAchatRepository.save(commande);
            })
            .orElse(null);
    }
}
