package pharmacie.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pharmacie.dao.CommandeRepository;
import pharmacie.dao.LigneRepository;
import pharmacie.dao.MedicamentRepository;
import pharmacie.entity.Commande;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

@SpringBootTest
@Transactional
class SupprimerLigneTest {

    @Autowired
    private CommandeService service;

    @Autowired
    private MedicamentRepository medicamentDao;

    @Autowired
    private LigneRepository ligneDao;

    @Autowired
    private CommandeRepository commandeDao;

    @Test
    void supprimerLigne_decrementsMedicamentUnitesCommandeesAndDeletesLine() {
        Commande commande = commandeDao.findById(99998).orElseThrow();
        Medicament medicament = medicamentDao.findById(98).orElseThrow();
        Ligne ligne = ligneDao.findByCommandeAndMedicament(commande, medicament).orElseThrow();

        int quantite = ligne.getQuantite();
        int unitesCommandeesAvant = medicament.getUnitesCommandees();

        service.supprimerLigne(ligne.getId());

        assertTrue(ligneDao.findById(ligne.getId()).isEmpty());
        Medicament medicamentApres = medicamentDao.findById(98).orElseThrow();
        assertEquals(unitesCommandeesAvant - quantite, medicamentApres.getUnitesCommandees());
    }

    @Test
    void supprimerLigne_onSentCommande_throws() {
        // ligne belonging to commande 99999 which is already sent
        Ligne ligne = ligneDao.findByCommandeNumero(99999).stream().findFirst().orElseThrow();
        assertThrows(IllegalStateException.class, () -> service.supprimerLigne(ligne.getId()));
    }

}
