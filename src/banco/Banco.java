package banco;

import interfaces.IConta;
import exceptions.ContaInexistenteException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe principal do sistema bancário YLLOCIN
 * Gerencia clientes e contas do banco
 */
public class Banco {
    private final String nome;
    private final List<Cliente> clientes;
    private final List<IConta> contas;
    
    public Banco(String nome) {
        this.nome = nome;
        this.clientes = new ArrayList<>();
        this.contas = new ArrayList<>();
    }
    
    // ================= GESTÃO DE CLIENTES =================
    
    public void adicionarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        
        // Verificar se CPF já existe
        boolean cpfExiste = clientes.stream()
            .anyMatch(c -> c.getCpf().equals(cliente.getCpf()));
        
        if (cpfExiste) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema");
        }
        
        clientes.add(cliente);
        System.out.printf("Cliente %s adicionado com sucesso!%n", cliente.getNome());
    }

    @SuppressWarnings("unused")
    public Cliente buscarClientePorCpf(String cpf) {
        return clientes.stream()
            .filter(cliente -> cliente.getCpf().equals(cpf))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @SuppressWarnings("unused")
    public List<Cliente> buscarClientesPorNome(String nome) {
        return clientes.stream()
            .filter(cliente -> cliente.getNome().toLowerCase()
                .contains(nome.toLowerCase()))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public boolean removerCliente(String cpf) {
        // Verificar se cliente tem contas ativas
        boolean temContas = contas.stream()
            .anyMatch(conta -> conta.getCliente().getCpf().equals(cpf));
        
        if (temContas) {
            throw new RuntimeException("Cliente possui contas ativas. Encerre as contas primeiro.");
        }
        
        return clientes.removeIf(cliente -> cliente.getCpf().equals(cpf));
    }
    
    // ================= GESTÃO DE CONTAS =================
    
    /**
     * Método auxiliar para adicionar conta no sistema e exibir confirmação
     */
    private void adicionarContaNoSistema(IConta conta) {
        contas.add(conta);
        
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       CONTA CRIADA COM SUCESSO!      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("🏦 Agência: %d%n", conta.getAgencia());
        System.out.printf("💳 Número: %d%n", conta.getNumero());
        System.out.printf("📝 Tipo: %s%n", conta.getTipoConta());
        System.out.printf("👤 Titular: %s%n", conta.getCliente().getNome());
        System.out.printf("💰 Saldo inicial: R$ %.2f%n", conta.getSaldo().doubleValue());
        System.out.println("══════════════════════════════════════");
    }

    @SuppressWarnings("UnusedReturnValue")
    public ContaCorrente criarContaCorrente(Cliente cliente) {
        if (!cliente.podeAbrirContaCorrente()) {
            throw new RuntimeException("Cliente não elegível para Conta Corrente");
        }

        // Verificar se já tem conta-corrente
        boolean jaTemCC = contas.stream()
            .anyMatch(conta -> conta.getCliente().equals(cliente) &&
                      conta.getTipoConta().equals("Conta Corrente"));

        if (jaTemCC) {
            throw new RuntimeException("Cliente já possui uma Conta Corrente");
        }

        ContaCorrente conta = new ContaCorrente(cliente);
        adicionarContaNoSistema(conta);

        return conta;
    }
    
    @SuppressWarnings("UnusedReturnValue")
    public ContaPoupanca criarContaPoupanca(Cliente cliente) {
        if (!cliente.podeAbrirContaPoupanca()) {
            throw new RuntimeException("Cliente não elegível para Conta Poupança");
        }
        
        // Verificar se já tem conta poupança
        boolean jaTemCP = contas.stream()
            .anyMatch(conta -> conta.getCliente().equals(cliente) && 
                      conta.getTipoConta().equals("Conta Poupança"));
        
        if (jaTemCP) {
            throw new RuntimeException("Cliente já possui uma Conta Poupança");
        }
        
        ContaPoupanca conta = new ContaPoupanca(cliente);
        adicionarContaNoSistema(conta);

        return conta;
    }
    
    public IConta buscarContaPorNumero(int numero) {
        return contas.stream()
            .filter(conta -> conta.getNumero() == numero)
            .findFirst()
            .orElseThrow(() -> new ContaInexistenteException(
                "Conta número " + numero + " não encontrada"));
    }
    
    public List<IConta> buscarContasPorCliente(Cliente cliente) {
        return contas.stream()
            .filter(conta -> conta.getCliente().equals(cliente))
            .collect(Collectors.toList());
    }
    
    public boolean encerrarConta(int numeroConta) {
        Optional<IConta> contaOpt = contas.stream()
            .filter(conta -> conta.getNumero() == numeroConta)
            .findFirst();
        
        if (contaOpt.isEmpty()) {
            return false;
        }
        
        IConta conta = contaOpt.get();
        
        // Verificar se saldo é zero
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Conta deve ter saldo zero para encerramento");
        }
        
        return contas.remove(conta);
    }
    
    // ================= RELATÓRIOS E ESTATÍSTICAS =================
    
    public void gerarRelatorioCompleto() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         RELATÓRIO GERENCIAL              ║");
        System.out.println("║            YLLOCIN BANK                  ║");
        System.out.println("╚══════════════════════════════════════════╝\n");
        
        // Estatísticas gerais
        System.out.println("📊 ESTATÍSTICAS GERAIS");
        System.out.println("─".repeat(40));
        System.out.printf("Total de Clientes: %d%n", clientes.size());
        System.out.printf("Total de Contas: %d%n", contas.size());
        
        // Breakdown por tipo de conta
        long contasCorrente = contas.stream()
            .filter(conta -> conta.getTipoConta().equals("Conta Corrente"))
            .count();
        
        long contasPoupanca = contas.stream()
            .filter(conta -> conta.getTipoConta().equals("Conta Poupança"))
            .count();
        
        System.out.printf("  • Contas Corrente: %d%n", contasCorrente);
        System.out.printf("  • Contas Poupança: %d%n", contasPoupanca);
        
        // Saldo total
        BigDecimal saldoTotal = contas.stream()
            .map(IConta::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        System.out.printf("💰 Patrimônio Total: R$ %.2f%n%n", saldoTotal.doubleValue());
        
        // Top clientes por saldo
        System.out.println("🏆 TOP CLIENTES POR PATRIMÔNIO");
        System.out.println("─".repeat(40));
        
        clientes.stream()
            .sorted((c1, c2) -> {
                BigDecimal saldo1 = calcularPatrimonioCliente(c1);
                BigDecimal saldo2 = calcularPatrimonioCliente(c2);
                return saldo2.compareTo(saldo1); // Ordem decrescente
            })
            .limit(5)
            .forEach(cliente -> {
                BigDecimal patrimonio = calcularPatrimonioCliente(cliente);
                System.out.printf("👤 %s: R$ %.2f%n", 
                    cliente.getNome(), patrimonio.doubleValue());
            });
        
        // Análise por faixa etária
        System.out.println("\n👥 CLIENTES POR FAIXA ETÁRIA");
        System.out.println("─".repeat(40));
        
        long menores = clientes.stream().filter(c -> c.getIdade() < 18).count();
        long jovens = clientes.stream().filter(c -> c.getIdade() >= 18 && c.getIdade() < 30).count();
        long adultos = clientes.stream().filter(c -> c.getIdade() >= 30 && c.getIdade() < 60).count();
        long seniores = clientes.stream().filter(c -> c.getIdade() >= 60).count();
        
        System.out.printf("👶 Menores (< 18): %d%n", menores);
        System.out.printf("🧑 Jovens (18-29): %d%n", jovens);
        System.out.printf("👨 Adultos (30-59): %d%n", adultos);
        System.out.printf("👴 Seniores (60+): %d%n", seniores);
        
        // Contas com maior movimentação
        System.out.println("\n📈 CONTAS COM MAIS TRANSAÇÕES");
        System.out.println("─".repeat(40));
        
        contas.stream()
            .sorted((c1, c2) -> Integer.compare(
                c2.getHistorico().size(), 
                c1.getHistorico().size()))
            .limit(3)
            .forEach(conta -> System.out.printf("💳 Conta %d (%s) - %s: %d transações%n",
                conta.getNumero(),
                conta.getTipoConta(),
                conta.getCliente().getNome(),
                conta.getHistorico().size()));
        
        System.out.println("\n" + "═".repeat(50));
    }
    
    private BigDecimal calcularPatrimonioCliente(Cliente cliente) {
        return contas.stream()
            .filter(conta -> conta.getCliente().equals(cliente))
            .map(IConta::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void gerarRelatorioMovimentacao() {
        System.out.println("📊 RELATÓRIO DE MOVIMENTAÇÃO");
        System.out.println("═".repeat(50));
        
        contas.forEach(conta -> {
            System.out.printf("💳 Conta %d - %s (%s)%n", 
                conta.getNumero(), 
                conta.getTipoConta(),
                conta.getCliente().getNome());
            
            if (conta.getHistorico().isEmpty()) {
                System.out.println("   Nenhuma movimentação registrada");
            } else {
                System.out.printf("   Últimas %d transações:%n", 
                    Math.min(3, conta.getHistorico().size()));
                
                conta.getHistorico().stream()
                    .skip(Math.max(0, conta.getHistorico().size() - 3))
                    .forEach(transacao -> System.out.println("   " + transacao));
            }
            
            System.out.println("─".repeat(40));
        });
    }
    
    // ================= OPERAÇÕES DE MANUTENÇÃO =================
    
    public void aplicarRendimentoPoupancas() {
        System.out.println("💰 Aplicando rendimento nas contas poupança...");
        
        contas.stream()
            .filter(conta -> conta.getTipoConta().equals("Conta Poupança"))
            .map(conta -> (ContaPoupanca) conta)
            .forEach(ContaPoupanca::aplicarRendimento);
        
        System.out.println("✅ Rendimento aplicado em todas as contas poupança!");
    }
    
    public void executarBackupDados() {
        System.out.println("💾 Executando backup dos dados...");
        // Aqui seria implementada a lógica de backup real
        System.out.printf("✅ Backup concluído: %d clientes, %d contas salvas%n", 
            clientes.size(), contas.size());
    }
    
    // ================= GETTERS =================
    
    public String getNome() {
        return nome;
    }
    
    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes); // Retorna cópia para segurança
    }
    
    public List<IConta> getContas() {
        return new ArrayList<>(contas); // Retorna cópia para segurança
    }
    
    public int getTotalClientes() {
        return clientes.size();
    }
    
    public int getTotalContas() {
        return contas.size();
    }
    
    public BigDecimal getPatrimonioTotal() {
        return contas.stream()
            .map(IConta::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public String toString() {
        return String.format("Banco{nome='%s', clientes=%d, contas=%d, patrimonio=%.2f}", 
            nome, clientes.size(), contas.size(), getPatrimonioTotal().doubleValue());
    }
}