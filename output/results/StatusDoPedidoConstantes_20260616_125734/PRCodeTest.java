import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ficticio.pmd.StatusDoPedidoConstantes;

public class PRCodeTest {

    @Test
    public void testStatusPendente() {
        assertEquals("PENDENTE", StatusDoPedidoConstantes.STATUS_PENDENTE);
    }

    @Test
    public void testStatusPago() {
        assertEquals("PAGO", StatusDoPedidoConstantes.STATUS_PAGO);
    }

    @Test
    public void testStatusCancelado() {
        assertEquals("CANCELADO", StatusDoPedidoConstantes.STATUS_CANCELADO);
    }

    @Test
    public void testStatusEnviado() {
        assertEquals("ENVIADO", StatusDoPedidoConstantes.STATUS_ENVIADO);
    }
}