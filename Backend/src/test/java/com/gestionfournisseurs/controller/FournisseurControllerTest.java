package com.gestionfournisseurs.controller;

import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.exception.GlobalExceptionHandler;
import com.gestionfournisseurs.service.FournisseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FournisseurController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class FournisseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FournisseurService fournisseurService;

    @Test
    void getAllFournisseursReturnsOk() throws Exception {
        when(fournisseurService.getAllFournisseurs()).thenReturn(List.of());

        mockMvc.perform(get("/api/fournisseurs"))
                .andExpect(status().isOk());
    }

    @Test
    void createFournisseurReturnsCreated() throws Exception {
        Fournisseur saved = new Fournisseur();
        saved.setId(1L);
        saved.setNom("ACME");
        saved.setContact("contact@acme.test");
        when(fournisseurService.saveFournisseur(any(Fournisseur.class))).thenReturn(saved);

        mockMvc.perform(post("/api/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nom":"ACME","contact":"contact@acme.test","qualiteService":4,"note":8.5}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("ACME"));
    }

    @Test
    void createFournisseurReturnsBadRequestWhenInvalid() throws Exception {
        mockMvc.perform(post("/api/fournisseurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"\",\"contact\":\"fournisseur@example.org\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Erreur de validation"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations[0].field").exists())
                .andExpect(jsonPath("$.violations[0].message").exists());
    }
}
