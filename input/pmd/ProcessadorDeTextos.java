public class ProcessadorDeTextos {

    public String formatarNome(String nome) {
        
        // PROBLEMA 1 (SpotBugs pegaria): Ignorar retorno de objeto imutável
        // SpotBugs acusa: RV_RETURN_VALUE_IGNORED
        nome.trim();
        nome.toUpperCase();

        // PROBLEMA 2 (Checkstyle pegaria): Má formatação e estilo de código
        // Checkstyle acusa: Falta de espaços ao redor do "==", falta de espaço após o "if"
        // e indentação completamente incorreta do "return".
        if(nome==""){
        return "NOME VAZIO";
        }

        return nome;
    }
}