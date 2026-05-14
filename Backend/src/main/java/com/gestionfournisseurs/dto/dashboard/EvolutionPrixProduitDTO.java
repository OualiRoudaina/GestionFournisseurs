package com.gestionfournisseurs.dto.dashboard;

import java.util.List;

public record EvolutionPrixProduitDTO(String produit, List<PrixPointDTO> points) {
}
