package pharmacie.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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
class EnregistreExpeditionTest {

    @Autowired
    private CommandeService service;

    @Autowired
    private MedicamentRepository medicamentDao;

    @Autowired
    private LigneRepository ligneDao;

    @Autowired
    private CommandeRepository commandeDao;

    @Test
    void enregistreExpedition_updatesStockUnitesCommandeesAndSetsDate() {
        int commandeNum = 99998;

        Commande commandeAvant = commandeDao.findById(commandeNum).orElseThrow();
        assertNull(commandeAvant.getEnvoyeele());

        Ligne ligne = ligneDao.findByCommandeNumero(commandeNum).stream().findFirst().orElseThrow();
        Medicament medicamentAvant = medicamentDao.findById(ligne.getMedicament().getReference()).orElseThrow();
        int stockAvant = medicamentAvant.getUnitesEnStock();
        int unitesCommandeesAvant = medicamentAvant.getUnitesCommandees();
        int quantite = ligne.getQuantite();

        Commande commande = service.enregistreExpedition(commandeNum);

        assertNotNull(commande.getEnvoyeele());
        assertEquals(LocalDate.now(), commande.getEnvoyeele());

        Medicament medicamentApres = medicamentDao.findById(medicamentAvant.getReference()).orElseThrow();
        assertEquals(stockAvant - quantite, medicamentApres.getUnitesEnStock());
        assertEquals(unitesCommandeesAvant - quantite, medicamentApres.getUnitesCommandees());
    }

    @Test
    void enregistreExpedition_onAlreadySent_throws() {
        assertThrows(IllegalStateException.class, () -> service.enregistreExpedition(99999));
    }

}
