package banco;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Conta Poupança - especialização da classe Conta para o YLLOCIN Bank
 * -----------------------------------------------
 * Características:
 * - Rendimento mensal de 0.5%
 * - Sem taxa de saque
 * - Disponível para todos os clientes
 */
public class ContaPoupanca extends Conta {
    
    private static final double RENDIMENTO_MENSAL = 0.005; // 0.5% ao mês
    private LocalDate dataUltimoRendimento;
    
    public ContaPoupanca(Cliente cliente) {
        super(cliente);
        this.dataUltimoRendimento = LocalDate.now();
    }
    
    @Override
    protected void aplicarTaxaSaque() {
        // Conta poupança não tem taxa de saque
        // Método vazio, sem implementação necessária
    }
    
    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
    
    // Métodos específicos da Conta Poupança
    public void aplicarRendimento() {
        BigDecimal rendimento = this.saldo.multiply(BigDecimal.valueOf(RENDIMENTO_MENSAL));
        
        if (rendimento.compareTo(BigDecimal.ZERO) > 0) {
            this.saldo = this.saldo.add(rendimento);
            this.dataUltimoRendimento = LocalDate.now();
            
            // Registra o rendimento como transação
            registrarTransacao(new Transacao(
                TipoTransacao.RENDIMENTO,
                rendimento,
                "Rendimento mensal aplicado",
                String.valueOf(this.numero)));
            
            System.out.printf("💰 Rendimento aplicado: R$ %.2f%n", rendimento.doubleValue());
        }
    }
    
    public void consultarRendimento() {
        System.out.println("📊 SIMULAÇÃO DE RENDIMENTO - CONTA POUPANÇA");
        System.out.println("═══════════════════════════════════════════");
        System.out.printf("🐷 Conta: %d%n", this.numero);
        System.out.printf("👤 Titular: %s%n", this.cliente.getNome());
        System.out.printf("💰 Saldo atual: R$ %.2f%n", this.saldo.doubleValue());
        
        // Simulação para próximos meses
        BigDecimal saldoAtual = this.saldo;
        System.out.println("\n🔮 PROJEÇÃO DE RENDIMENTOS:");
        
        for (int mes = 1; mes <= 12; mes++) {
            BigDecimal rendimentoMes = saldoAtual.multiply(BigDecimal.valueOf(RENDIMENTO_MENSAL));
            saldoAtual = saldoAtual.add(rendimentoMes);
            
            if (mes <= 6 || mes == 12) { // Mostra primeiros 6 meses e o 12º
                System.out.printf("%2dº mês: R$ %.2f (rendimento: +R$ %.2f)%n", 
                    mes, saldoAtual.doubleValue(), rendimentoMes.doubleValue());
            }
        }
        
        BigDecimal rendimentoAnual = saldoAtual.subtract(this.saldo);
        System.out.printf("\n💎 Rendimento total em 12 meses: R$ %.2f%n", rendimentoAnual.doubleValue());
        System.out.printf("📊 Taxa anual efetiva: %.2f%%%n", (Math.pow(1 + RENDIMENTO_MENSAL, 12) - 1) * 100);
        System.out.println("═══════════════════════════════════════════");
    }
    
    public void definirMetaPoupanca(double valorMeta, int meses) {
        System.out.println("🎯 PLANEJAMENTO DE POUPANÇA");
        System.out.println("═══════════════════════════════════════════");
        System.out.printf("💰 Meta desejada: R$ %.2f%n", valorMeta);
        System.out.printf("⏰ Prazo: %d meses%n", meses);
        System.out.printf("💳 Saldo atual: R$ %.2f%n", this.saldo.doubleValue());
        
        // Calcular quanto precisa depositar mensalmente
        BigDecimal metaDecimal = BigDecimal.valueOf(valorMeta);
        BigDecimal saldoFuturo = this.saldo;
        
        // Simular crescimento só com rendimento
        for (int i = 0; i < meses; i++) {
            saldoFuturo = saldoFuturo.multiply(BigDecimal.valueOf(1 + RENDIMENTO_MENSAL));
        }
        
        BigDecimal diferenca = metaDecimal.subtract(saldoFuturo);
        
        if (diferenca.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("🎉 PARABÉNS! Você já atingirá a meta apenas com rendimentos!");
            System.out.printf("💎 Saldo final projetado: R$ %.2f%n", saldoFuturo.doubleValue());
        } else {
            // Calcular depósito mensal necessário (aproximação simples)
            double depositoMensal = diferenca.doubleValue() / meses;
            System.out.printf("📈 Depósito mensal necessário: R$ %.2f%n", depositoMensal);
            System.out.printf("💡 Total a ser depositado: R$ %.2f%n", depositoMensal * meses);
        }
        
        System.out.println("═══════════════════════════════════════════");
    }

    // ✅ CORREÇÃO 1: Método usado no Main.java - opção 12
    public void gerarRelatorioRendimentos() {
        System.out.println("📊 RELATÓRIO DE RENDIMENTOS - CONTA POUPANÇA");
        System.out.println("════════════════════════════════════════════");
        System.out.printf("🐷 Conta: %d%n", this.numero);
        System.out.printf("👤 Titular: %s%n", this.cliente.getNome());
        System.out.printf("💰 Saldo atual: R$ %.2f%n", this.saldo.doubleValue());
        System.out.printf("📅 Último rendimento: %s%n", this.dataUltimoRendimento);
        

        BigDecimal totalRendimentos = this.historico.stream()
            .filter(t -> t.getTipo() == TipoTransacao.RENDIMENTO)
            .map(Transacao::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long quantidadeRendimentos = this.historico.stream()
            .filter(t -> t.getTipo() == TipoTransacao.RENDIMENTO)
            .count();
        
        System.out.printf("💵 Total em rendimentos: R$ %.2f%n", totalRendimentos.doubleValue());
        System.out.printf("📈 Rendimentos aplicados: %d vezes%n", quantidadeRendimentos);
        System.out.printf("📊 Taxa mensal: %.2f%%%n", RENDIMENTO_MENSAL * 100);
        System.out.printf("📊 Taxa anual: %.2f%%%n", (Math.pow(1 + RENDIMENTO_MENSAL, 12) - 1) * 100);
        
        System.out.println("════════════════════════════════════════════");
    }
    

    public double getTaxaRendimento() {
        return RENDIMENTO_MENSAL;
    }
    

    public LocalDate getDataUltimoRendimento() {
        return dataUltimoRendimento;
    }
    
    @Override
    public String toString() {
        return String.format("ContaPoupanca{numero=%d, titular=%s, saldo=%.2f, rendimento=%.3f%%}", 
                           numero, cliente.getNome(), saldo.doubleValue(), RENDIMENTO_MENSAL * 100);
    }
}