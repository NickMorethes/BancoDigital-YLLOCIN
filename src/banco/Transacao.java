package banco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe que representa uma transação bancária no YLLOCIN Bank
 * -----------------------------------------------------
 * Objeto imutável que registra todas as informações de uma operação
 */
public class Transacao {
    private final TipoTransacao tipo;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;
    private final String descricao;
    private final String contaOrigem;
    private final String contaDestino;
    
    // Construtor principal (com conta destino)
    public Transacao(TipoTransacao tipo, BigDecimal valor, String descricao, 
                     String contaOrigem, String contaDestino) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }
    
    // Construtor simplificado (sem conta destino)
    public Transacao(TipoTransacao tipo, BigDecimal valor, String descricao, String contaOrigem) {
        this(tipo, valor, descricao, contaOrigem, null);
    }
    
    // Getters (sem setters - objeto imutável)
    public TipoTransacao getTipo() {
        return tipo;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public LocalDateTime getdataHora() {
        return dataHora;
    }
    
    public String getdescricao() {
        return descricao;
    }
    
    public String getcontaOrigem() {
        return contaOrigem;
    }
    
    public String getContaDestino() {
        return contaDestino;
    }
    
    public boolean isTransferencia() {
        return tipo == TipoTransacao.TRANSFERENCIA;
    }
    
    public boolean isCredito() {
        return tipo == TipoTransacao.DEPOSITO || tipo == TipoTransacao.RENDIMENTO;
    }
    
    public boolean isDebito() {
        return tipo == TipoTransacao.SAQUE || tipo == TipoTransacao.TAXA || 
               tipo == TipoTransacao.TRANSFERENCIA;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s - R$ %.2f", 
            dataHora.format(formatter),
            tipo.getDescricao(),
            valor.doubleValue()));
        
        if (descricao != null && !descricao.isEmpty()) {
            sb.append(" - ").append(descricao);
        }
        
        if (contaDestino != null && !contaDestino.isEmpty()) {
            sb.append(" → Conta ").append(contaDestino);
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Transacao transacao = (Transacao) obj;
        return tipo == transacao.tipo &&
               valor.equals(transacao.valor) &&
               dataHora.equals(transacao.dataHora) &&
               contaOrigem.equals(transacao.contaOrigem);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(tipo, valor, dataHora, contaOrigem);
    }
}