import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Path;
import com.ficticio.pmd.ExportadorBancoDeDados;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PRCodeTest {

    @TempDir
    Path tempDir;

    @Test
    public void testExportarBackupCsvComParametrosInvalidos() {
        ExportadorBancoDeDados exportador = new ExportadorBancoDeDados();
        File arquivo = tempDir.resolve("backup.csv").toFile();

        assertDoesNotThrow(() -> {
            exportador.exportarBackupCsv(
                "jdbc:invalid:url", 
                "usuario", 
                "senha", 
                arquivo.getAbsolutePath()
            );
        });
    }
}