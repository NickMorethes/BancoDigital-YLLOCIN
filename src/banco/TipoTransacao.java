package banco;

/**
 * Enum que representa os tipos de transações possíveis no YLLOCIN Bank
 */
public enum TipoTransacao {
    DEPOSITO("Depósito"),
    SAQUE("Saque"), 
    TRANSFERENCIA("Transferência"),
    TAXA("Taxa"),
    RENDIMENTO("Rendimento");
    
    private final String descricao;
    
    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}