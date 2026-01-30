package pharmacie.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pharmacie.entity.Commande;

@SpringBootTest
@Transactional
class GetCommandeEnCoursPourTest {

    @Autowired
    private CommandeService service;

    @Test
    void getCommandeEnCoursPour_returnsOpenCommands() {
        List<Commande> list = service.getCommandeEnCoursPour("2COM");
        assertNotNull(list);
        // dans test_data.sql la commande 99998 est en cours pour 2COM
        assertTrue(list.stream().anyMatch(c -> c.getNumero() == 99998));
    }

}
