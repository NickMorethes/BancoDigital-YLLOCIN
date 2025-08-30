package exceptions;

/**
 * Exception para operações com saldo insuficiente no YLLOCIN Bank
 */
public class SaldoInsuficienteException extends RuntimeException {
    
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
    
    public SaldoInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
    
    public SaldoInsuficienteException(double saldoAtual, double valorSolicitado) {
        super(String.format("Saldo insuficiente. Saldo atual: R$ %.2f, Valor solicitado: R$ %.2f", 
            saldoAtual, valorSolicitado));
    }
}