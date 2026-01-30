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
class AjouterLigneTest {

    @Autowired
    private CommandeService service;

    @Autowired
    private MedicamentRepository medicamentDao;

    @Autowired
    private LigneRepository ligneDao;

    @Autowired
    private CommandeRepository commandeDao;

    @Test
    void ajouterLigne_existingMedicament_increasesQuantities() {
        int commandeNum = 99998;
        int medicRef = 98;
        int ajout = 3;

        Medicament medicamentAvant = medicamentDao.findById(medicRef).orElseThrow();
        int unitesCommandeesAvant = medicamentAvant.getUnitesCommandees();

        Commande commande = commandeDao.findById(commandeNum).orElseThrow();
        Ligne ligneAvant = ligneDao.findByCommandeAndMedicament(commande, medicamentAvant).orElseThrow();
        int quantiteAvant = ligneAvant.getQuantite();

        Ligne ligne = service.ajouterLigne(commandeNum, medicRef, ajout);

        assertEquals(quantiteAvant + ajout, ligne.getQuantite());
        Medicament medicamentApres = medicamentDao.findById(medicRef).orElseThrow();
        assertEquals(unitesCommandeesAvant + ajout, medicamentApres.getUnitesCommandees());
    }

    @Test
    void ajouterLigne_createsNewLineWhenNotPresent() {
        // préparer une commande vide : créer une nouvelle commande pour petit client
        var commande = service.creerCommande("0COM");
        int medicRef = 94; // médicament existant et disponible
        int quantite = 2;

        Ligne ligne = service.ajouterLigne(commande.getNumero(), medicRef, quantite);
        assertNotNull(ligne.getId());
        assertEquals(quantite, ligne.getQuantite());
        Medicament medicament = medicamentDao.findById(medicRef).orElseThrow();
        assertEquals(quantite, medicament.getUnitesCommandees());
    }

    @Test
    void ajouterLigne_invalidQuantity_throws() {
        assertThrows(Exception.class, () -> service.ajouterLigne(99998, 98, 0));
    }

}
