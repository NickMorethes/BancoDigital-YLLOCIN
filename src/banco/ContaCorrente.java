package banco;

import java.math.BigDecimal;


/**
 * Conta Corrente - especialização da classe Conta para o YLLOCIN Bank
 * -----------------------------------------------
 * Características:
 * - Taxa de R$ 0,50 por saque
 * - Ideal para movimentação diária
 * - Disponível para maiores de 18 anos ou emancipados
 */
public class ContaCorrente extends Conta {
    
    private static final double TAXA_SAQUE = 0.50;
    
    public ContaCorrente(Cliente cliente) {
        super(cliente);
    }
    
    @Override
    protected void aplicarTaxaSaque() {
        // Conta corrente tem taxa de saque
        this.saldo = this.saldo.subtract(BigDecimal.valueOf(TAXA_SAQUE));
        
        // Registra a taxa como transação
        registrarTransacao(new Transacao(
            TipoTransacao.TAXA, 
            BigDecimal.valueOf(TAXA_SAQUE), 
            "Taxa de saque", 
            String.valueOf(this.numero)));
    }
    
    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
    
    // Métodos específicos da Conta Corrente
    public void solicitarCartaoCredito() {
        System.out.println("💳 Solicitação de cartão de crédito processada!");
        System.out.println("📧 Você receberá mais informações por email em até 2 dias úteis.");
        
        // Poderia registrar uma transação de serviço
        registrarTransacao(new Transacao(
            TipoTransacao.TAXA,
            BigDecimal.ZERO,
            "Solicitação de cartão de crédito",
            String.valueOf(this.numero)));
    }
    
    public void consultarLimiteCredito() {
        // Simula consulta de limite baseado no histórico
        BigDecimal limiteBasico = BigDecimal.valueOf(1000.00);
        
        // Limite aumenta baseado no saldo médio
        if (this.saldo.compareTo(BigDecimal.valueOf(5000)) > 0) {
            limiteBasico = limiteBasico.multiply(BigDecimal.valueOf(3));
        } else if (this.saldo.compareTo(BigDecimal.valueOf(1000)) > 0) {
            limiteBasico = limiteBasico.multiply(BigDecimal.valueOf(2));
        }
        
        System.out.printf("💳 Limite de crédito pré-aprovado: R$ %.2f%n", limiteBasico.doubleValue());
        System.out.println("📞 Entre em contato para solicitar aumento de limite.");
    }
    
    public void gerarRelatorioAnual() {
        System.out.println("📊 RELATÓRIO ANUAL - CONTA CORRENTE");
        System.out.println("══════════════════════════════════════");
        System.out.printf("💳 Conta: %d%n", this.numero);
        System.out.printf("👤 Titular: %s%n", this.cliente.getNome());
        System.out.printf("💰 Saldo atual: R$ %.2f%n", this.saldo.doubleValue());
        
        // Análise de transações
        long totalTransacoes = this.historico.size();
        long saques = this.historico.stream()
            .filter(t -> t.getTipo() == TipoTransacao.SAQUE)
            .count();
        long depositos = this.historico.stream()
            .filter(t -> t.getTipo() == TipoTransacao.DEPOSITO)
            .count();
        
        BigDecimal totalTaxas = this.historico.stream()
            .filter(t -> t.getTipo() == TipoTransacao.TAXA)
            .map(Transacao::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        System.out.printf("📈 Total de transações: %d%n", totalTransacoes);
        System.out.printf("💸 Saques realizados: %d%n", saques);
        System.out.printf("💰 Depósitos realizados: %d%n", depositos);
        System.out.printf("💳 Total em taxas pagas: R$ %.2f%n", totalTaxas.doubleValue());
        System.out.println("══════════════════════════════════════");
    }
    
    public double getTAXA_SAQUE() {
        return TAXA_SAQUE;
    }
    
    @Override
    public String toString() {
        return String.format("ContaCorrente{numero=%d, titular=%s, saldo=%.2f, taxa=%.2f}", 
                           numero, cliente.getNome(), saldo.doubleValue(), TAXA_SAQUE);
    }

    /**
     * Exibe serviços exclusivos da Conta Corrente
     * ✅ USA: solicitarCartaoCredito() e consultarLimiteCredito()
     */
    public void exibirServicosEspeciais() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       SERVIÇOS CONTA CORRENTE            ║");
        System.out.println("║            YLLOCIN BANK                  ║");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.printf("👤 Cliente: %s%n", this.cliente.getNome());
        System.out.printf("💳 Conta: %d%n", this.numero);
        System.out.printf("💰 Saldo: R$ %.2f%n", this.saldo.doubleValue());
        System.out.println("─".repeat(50));

        // ✅ USANDO: consultarLimiteCredito()
        System.out.println("🔍 ANÁLISE DE LIMITE DE CRÉDITO:");
        consultarLimiteCredito();

        System.out.println("─".repeat(50));

        // ✅ USANDO: solicitarCartaoCredito()
        System.out.println("💳 CARTÃO DE CRÉDITO:");
        solicitarCartaoCredito();

        System.out.println("─".repeat(50));
        System.out.println("ℹ️  Para mais informações, visite uma agência!");
        System.out.println("═".repeat(50));
    }

}