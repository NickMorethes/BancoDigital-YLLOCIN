package exceptions;

/**
 * Exception para quando uma conta não é encontrada no sistema
 * Utilizada principalmente em operações de busca por número da conta
 */
public class ContaInexistenteException extends RuntimeException {
    
    public ContaInexistenteException(String mensagem) {
        super(mensagem);
    }
    
    public ContaInexistenteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
    
    public ContaInexistenteException(int numeroConta) {
        super("Conta número " + numeroConta + " não encontrada no sistema");
    }
}