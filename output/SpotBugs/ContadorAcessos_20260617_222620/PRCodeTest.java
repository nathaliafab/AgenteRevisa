import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import br.com.dataset.spotbugs.ContadorAcessos;

public class PRCodeTest {

  @Test
  public void testRegistrarAcessoIncrementaTotalGlobal() {
    int valorInicial = ContadorAcessos.getTotalAcessosGlobais();
    
    ContadorAcessos usuario1 = new ContadorAcessos("Usuario1");
    usuario1.registrarAcesso();
    
    Assertions.assertEquals(valorInicial + 1, ContadorAcessos.getTotalAcessosGlobais(), 
        "O total de acessos globais deve incrementar após o registro.");
  }

  @Test
  public void testMultiplosAcessosPorInstanciasDiferentes() {
    int valorInicial = ContadorAcessos.getTotalAcessosGlobais();
    
    ContadorAcessos u1 = new ContadorAcessos("U1");
    ContadorAcessos u2 = new ContadorAcessos("U2");
    
    u1.registrarAcesso();
    u2.registrarAcesso();
    u2.registrarAcesso();
    
    Assertions.assertEquals(valorInicial + 3, ContadorAcessos.getTotalAcessosGlobais(), 
        "O contador estático deve contabilizar acessos de todas as instâncias.");
  }
}