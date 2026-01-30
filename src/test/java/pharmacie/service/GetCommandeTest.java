package pharmacie.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pharmacie.entity.Commande;

@SpringBootTest
@Transactional
class GetCommandeTest {

    @Autowired
    private CommandeService service;

    @Test
    void getCommande_returnsCommande() {
        Commande c = service.getCommande(99998);
        assertNotNull(c);
        assertEquals(99998, c.getNumero());
    }

    @Test
    void getCommande_nonExisting_throws() {
        assertThrows(Exception.class, () -> service.getCommande(-1));
    }

}
