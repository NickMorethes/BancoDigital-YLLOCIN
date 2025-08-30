package banco;

import interfaces.IConta;
import exceptions.SaldoInsuficienteException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Classe abstrata que representa uma conta bancária no YLLOCIN Bank
 *---------------------------------------------------------------------
 * Define a estrutura comum para todas as contas
 */
public abstract class Conta implements IConta {

    // Atributos protegidos - visíveis para subclasses
    protected static final int AGENCIA_PADRAO = 1;
    protected static int SEQUENCIAL = 1001;

    protected int agencia;
    protected int numero;
    protected BigDecimal saldo;
    protected Cliente cliente;
    protected List<Transacao> historico;

    // Construtor protegido - só pode ser chamado pelas subclasses
    protected Conta(Cliente cliente) {
        this.agencia = AGENCIA_PADRAO;
        this.numero = SEQUENCIAL++;
        this.saldo = BigDecimal.ZERO;
        this.cliente = cliente;
        this.historico = new ArrayList<>();
    }

    // Implementação das operações bancárias
    @Override
    public void sacar(double valor) {
        BigDecimal valorSaque = BigDecimal.valueOf(valor);

        if (valorSaque.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        if (this.saldo.compareTo(valorSaque) < 0) {
            throw new SaldoInsuficienteException(
                    String.format("Saldo insuficiente. Saldo atual: R$ %.2f" ,
                            this.saldo.doubleValue()));
        }

        this.saldo = this.saldo.subtract(valorSaque);

        // Registra a transação
        registrarTransacao(new Transacao(TipoTransacao.SAQUE, valorSaque,
                "Saque realizado" , String.valueOf(this.numero)));

        // Chama método específico de cada tipo de conta
        aplicarTaxaSaque();
    }

    @Override
    public void depositar(double valor) {
        BigDecimal valorDeposito = BigDecimal.valueOf(valor);

        if (valorDeposito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        this.saldo = this.saldo.add(valorDeposito);

        // Registra a transação
        registrarTransacao(new Transacao(TipoTransacao.DEPOSITO, valorDeposito,
                "Depósito realizado" , String.valueOf(this.numero)));
    }

    @Override
    public void transferir(double valor, IConta contaDestino) {
        BigDecimal valorTransferencia = BigDecimal.valueOf(valor);

        // Validações
        if (valorTransferencia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        if (contaDestino == null) {
            throw new IllegalArgumentException("Conta destino não pode ser nula");
        }

        if (this.numero == contaDestino.getNumero()) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        // Verifica saldo suficiente (incluindo possíveis taxas)
        BigDecimal saldoNecessario = valorTransferencia;
        if (this instanceof ContaCorrente) {
            saldoNecessario = saldoNecessario.add(BigDecimal.valueOf(0.50)); // Taxa de saque
        }

        if (this.saldo.compareTo(saldoNecessario) < 0) {
            throw new SaldoInsuficienteException(
                    String.format("Saldo insuficiente para transferência. Saldo atual: R$ %.2f" ,
                            this.saldo.doubleValue()));
        }

        // Executa a transferência
        this.sacar(valor); // Já registra transação e aplica taxas
        contaDestino.depositar(valor);

        // Registra transação específica de transferência no histórico do destinatário
        Conta contaDestinoImpl = (Conta) contaDestino;
        contaDestinoImpl.registrarTransacao(new Transacao(
                TipoTransacao.TRANSFERENCIA,
                valorTransferencia,
                "Transferência recebida de Conta " + this.numero,
                String.valueOf(contaDestino.getNumero()),
                String.valueOf(this.numero)));
    }

    // Método abstrato - cada tipo de conta implementa sua própria taxa
    protected abstract void aplicarTaxaSaque();

    // Método para obter o tipo da conta (para relatórios)
    public abstract String getTipoConta();

    // Método protegido para registrar transação
    protected void registrarTransacao(Transacao transacao) {
        this.historico.add(transacao);
    }

    @Override
    public void imprimirExtrato() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           EXTRATO DA CONTA           ║");
        System.out.println("║            YLLOCIN BANK              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("👤 Titular: %s%n" , this.cliente.getNome());
        System.out.printf("🏦 Agência: %d%n" , this.agencia);
        System.out.printf("💳 Número: %d%n" , this.numero);
        System.out.printf("📝 Tipo: %s%n" , getTipoConta());
        System.out.printf("💰 Saldo: R$ %.2f%n" , this.saldo.doubleValue());
        System.out.println("══════════════════════════════════════");

        System.out.println("\n📊 HISTÓRICO DE TRANSAÇÕES");
        System.out.println("──────────────────────────────────────");

        if (historico.isEmpty()) {
            System.out.println("   Nenhuma transação realizada.");
        } else {
            // Mostra as últimas 10 transações
            int limite = Math.min(10, historico.size());
            int inicio = historico.size() - limite;

            for (int i = inicio; i < historico.size(); i++) {
                System.out.println(historico.get(i));
            }

            if (historico.size() > 10) {
                System.out.printf("\n... e mais %d transações anteriores%n" ,
                        historico.size() - 10);
            }
        }

        System.out.println("══════════════════════════════════════");
    }

    // Getters da interface
    @Override
    public int getAgencia() {
        return agencia;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public BigDecimal getSaldo() {
        return saldo;
    }

    @Override
    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public List<Transacao> getHistorico() {
        return new ArrayList<>(historico);
    }

    @Override
    public String toString() {
        return String.format("%s{agencia=%d, numero=%d, saldo=R$ %.2f, cliente=%s}" ,
                getTipoConta(),
                getAgencia(),
                getNumero(),
                getSaldo().doubleValue(),
                getCliente().getNome());
    }
}
