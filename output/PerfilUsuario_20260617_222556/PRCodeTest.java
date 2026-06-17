import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import br.com.dataset.spotbugs.PerfilUsuario;
import java.util.Date;

public class PRCodeTest {

    @Test
    public void testPerfilUsuarioAtribuicao() {
        String nome = "Teste";
        Date data = new Date();
        PerfilUsuario usuario = new PerfilUsuario(nome, data);

        Assertions.assertEquals(nome, usuario.getNome(), "O nome deve ser igual ao fornecido no construtor.");
        // A comparacao de datas funciona via .equals() mesmo apos a copia defensiva
        Assertions.assertEquals(data, usuario.getDataCriacao(), "A data deve ser igual à fornecida no construtor.");
    }

    @Test
    public void testSetDataCriacao() {
        PerfilUsuario usuario = new PerfilUsuario("Teste", new Date());
        Date novaData = new Date(System.currentTimeMillis() + 10000);
        
        usuario.setDataCriacao(novaData);
        
        Assertions.assertEquals(novaData, usuario.getDataCriacao(), "A data deve ser atualizada corretamente.");
    }
}