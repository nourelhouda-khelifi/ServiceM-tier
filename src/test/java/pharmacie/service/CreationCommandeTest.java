package pharmacie.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pharmacie.dao.DispensaireRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Ce test est basé sur le jeu de données dans "test_data.sql"
class CreationCommandeTest {
    private static final String ID_PETIT_CLIENT = "0COM";
    private static final String ID_GROS_CLIENT = "2COM";
    private static final BigDecimal REMISE_POUR_GROS_CLIENT = new BigDecimal("0.15");

    @Autowired
    private CommandeService service;
    @Autowired
    private DispensaireRepository daoClient;

    // ✅ TEST 1 : Créer une commande pour gros client → remise 15%
    @Test
    void testCreerCommandePourGrosClient() {
        var commande = service.creerCommande(ID_GROS_CLIENT);
        assertNotNull(commande.getNumero(), "On doit avoir la clé de la commande");
        assertEquals(REMISE_POUR_GROS_CLIENT, commande.getRemise(),
            "Une remise de 15% doit être appliquée pour les gros clients");
    }

    // ✅ TEST 2 : Créer une commande pour petit client → pas de remise
    @Test
    void testCreerCommandePourPetitClient() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertNotNull(commande.getNumero());
        assertEquals(BigDecimal.ZERO, commande.getRemise(),
            "Aucune remise ne doit être appliquée pour les petits clients");
    }

    // ✅ TEST 3 : Adresse de livraison initialisée
    @Test
    void testCreerCommandeInitialiseAdresseLivraison() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        var client = daoClient.findById(ID_PETIT_CLIENT).orElseThrow();
        assertEquals(client.getAdresse(), commande.getAdresseLivraison(),
            "On doit recopier l'adresse du client dans l'adresse de livraison");
    }

    // ❌ TEST 4 : Dispensaire inexistant → NoSuchElementException
    @Test
    void testCreerCommandeDispensaireInexistant() {
        assertThrows(NoSuchElementException.class, 
            () -> service.creerCommande("INEXISTANT"),
            "Doit lever NoSuchElementException pour dispensaire inexistant");
    }

    // ✅ TEST 5 : La commande a une date de saisie
    @Test
    void testCreerCommandeAvecDateSaisie() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertNotNull(commande.getSaisiele(), "La date de saisie ne doit pas être null");
    }

    // ✅ TEST 6 : La commande a une liste de lignes (initialement vide)
    @Test
    void testCreerCommandeAvecListeLignesVide() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertNotNull(commande.getLignes(), "La liste de lignes ne doit pas être null");
        assertTrue(commande.getLignes().isEmpty(), "La liste de lignes doit être vide au départ");
    }
}
