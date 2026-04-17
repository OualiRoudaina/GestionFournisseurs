package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.repository.CommandeAchatRepository;
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

    public List<CommandeAchat> getAllCommandes() {
        return commandeAchatRepository.findAll();
    }

    public Optional<CommandeAchat> getCommandeById(Long id) {
        return commandeAchatRepository.findById(id);
    }

    public CommandeAchat saveCommande(CommandeAchat commande) {
        if (commande.getDate() == null) {
            commande.setDate(LocalDate.now());
        }
        return commandeAchatRepository.save(commande);
    }

    public CommandeAchat updateCommande(Long id, CommandeAchat commandeDetails) {
        return commandeAchatRepository.findById(id)
            .map(commande -> {
                commande.setFournisseur(commandeDetails.getFournisseur());
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
        return commandeAchatRepository.findById(id)
            .map(commande -> {
                commande.setStatut(nouveauStatut);
                return commandeAchatRepository.save(commande);
            })
            .orElse(null);
    }
}
