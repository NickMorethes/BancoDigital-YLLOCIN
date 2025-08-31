package interfaces;

import banco.Cliente;
import banco.Transacao;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interface que define o contrato para operações bancárias no YLLOCIN Bank
 * Define métodos obrigatórios que todas as contas devem implementar
 */
public interface IConta {
    
    // Operações bancárias básicas
    void sacar(BigDecimal valor);
    void depositar(BigDecimal valor);
    void transferir(BigDecimal valor, IConta contaDestino);

   // Getters para atributos essenciais
    int getNumero();
    int getAgencia();
    BigDecimal getSaldo();
    Cliente getCliente();
    String getTipoConta();
    List<Transacao> getHistorico();
    
    // Operações de consulta
    void imprimirExtrato();
    
    // Operações padrão (opcionais)
    default void consultarSaldo() {
        System.out.printf("💰 Saldo atual: R$ %.2f%n", getSaldo().doubleValue());
    }
    
    // Utilitários estáticos
    static boolean validarValor(BigDecimal valor) {
        return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }
    
    static String formatarMoeda(BigDecimal valor) {
        return String.format("R$ %.2f", valor.doubleValue());
    }


}