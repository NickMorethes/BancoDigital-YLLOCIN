import banco.*;
import exceptions.*;
import interfaces.IConta;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.math.BigDecimal;
import java.util.List;

/**
 * Sistema Bancário Digital YLLOCIN
 * Interface principal do usuário
 */
public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final Banco banco = new Banco("YLLOCIN Bank");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static void main(String[] args) {
        exibirBoasVindas();
        menuPrincipal();
    }
    
    private static void exibirBoasVindas() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║          BEM-VINDO AO                 ║");
        System.out.println("║          YLLOCIN BANK! 🏦             ║");
        System.out.println("║                                       ║");
        System.out.println("║     Seu banco digital de confiança    ║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        pausar();
        limparTela();
    }
    
    private static void menuPrincipal() {
        while (true) {
            try {
                exibirCabecalho();
                exibirMenuOpcoes();
                
                int opcao = lerOpcaoMenu();
                limparTela();
                
                if (opcao == 0) {
                    encerrarSistema();
                    break;
                }
                
                executarOpcao(opcao);
                
            } catch (Exception e) {
                exibirErro("Erro inesperado: " + e.getMessage());
                pausar();
                limparTela();
            }
        }
    }
    
    private static void exibirCabecalho() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║              YLLOCIN BANK             ║");
        System.out.println("║           Sistema Bancário            ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    
    private static void exibirMenuOpcoes() {
        System.out.println("📋 MENU PRINCIPAL:");
        System.out.println("─".repeat(40));
        System.out.println("1️⃣  Abrir Nova Conta");
        System.out.println("2️⃣  Realizar Depósito");
        System.out.println("3️⃣  Realizar Saque");
        System.out.println("4️⃣  Fazer Transferência");
        System.out.println("5️⃣  Consultar Extrato");
        System.out.println("6️⃣  Dados do Cliente");
        System.out.println("7️⃣  Buscar Conta");
        System.out.println("8️⃣  Listar Minhas Contas");
        System.out.println("9️⃣  Relatório Gerencial");
        System.out.println("🔟  Listar Contas de Cliente");
        System.out.println("1️⃣1️⃣ Encerrar Conta");
        System.out.println("1️⃣2️⃣ Relatório de Movimentação");
        System.out.println("1️⃣3️⃣ Aplicar Rendimento (Admin)");
        System.out.println("1️⃣4️⃣ Painel Administrativo");
        System.out.println("1️⃣5️⃣ Opções Extras");
        System.out.println("0️⃣  Sair");
        System.out.println("─".repeat(40));
    }


    private static int lerOpcaoMenu() {
        while (true) {
            try {
                System.out.print("🔢 Digite sua opção: ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                if (opcao < 0 || opcao > 15) {
                    exibirErro("Opção inválida! Digite um número entre 0 e 15.");
                    continue;
                }
                
                return opcao;
            } catch (NumberFormatException e) {
                exibirErro("Digite apenas números!");
            }
        }
    }
    
    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> abrirConta();
            case 2 -> realizarDeposito();
            case 3 -> realizarSaque();
            case 4 -> realizarTransferencia();
            case 5 -> consultarExtrato();
            case 6 -> exibirDadosCliente();
            case 7 -> buscarContaEspecifica();
            case 8 -> listarMinhasContas();
            case 9 -> exibirRelatorios();
            case 10 -> listarContasCliente();
            case 11 -> encerrarContaEspecifica();
            case 12 -> exibirRelatorioMovimentacao();
            case 13 -> aplicarRendimentoAdmin();
            case 14 -> painelAdministrativo();
            case 15 -> exibirOpcoesExtras();
            default -> exibirErro("Opção não implementada ainda!");
        }
    }
    
    // ================= SERVIÇOS BANCÁRIOS =================
    
    private static void abrirConta() {
        exibirTitulo("ABRIR NOVA CONTA");
        
        try {
            String nome = lerDadoObrigatorio("Nome completo: ");
            String cpf = lerDadoObrigatorio("CPF: ");
            LocalDate dataNascimento = lerDataNascimento();
            String telefone = lerDadoOpcional("Telefone (opcional): ");
            
            Cliente cliente = new Cliente(nome, cpf, dataNascimento, telefone);
            
            // Verificar elegibilidade
            verificarElegibilidadeCliente(cliente);
            
            boolean clienteExiste = banco.getClientes().stream()
                .anyMatch(c -> c.getCpf().equals(cpf));
            
            if (clienteExiste) {
                criarContaParaClienteExistente();
            } else {
            // Adicionar cliente
                banco.adicionarCliente(cliente);
            
            // Criar primeira conta
                criarPrimeiraConta(cliente);
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao abrir conta: " + e.getMessage());
        }
        
        pausar();
    }
    
    private static void verificarElegibilidadeCliente(Cliente cliente) {
        System.out.println("\n🔍 Verificando elegibilidade...");
        
        if (!cliente.podeAbrirContaCorrente() && !cliente.podeAbrirContaPoupanca()) {
            throw new RuntimeException("Cliente não elegível para nenhum tipo de conta");
        }
        
        System.out.println("✅ Cliente elegível para:");
        if (cliente.podeAbrirContaCorrente()) {
            System.out.println("   • Conta Corrente");
        }
            
        if (cliente.podeAbrirContaPoupanca()) {
            System.out.println("   • Conta Poupança");
        }
        System.out.println();
    }
    
    private static void criarPrimeiraConta(Cliente cliente) {
        System.out.println("🎉 Bem-vindo ao YLLOCIN Bank!");
        System.out.println("Vamos criar sua primeira conta:");
        
        criarNovaContaParaCliente(cliente);
    }
    
    private static void criarContaParaClienteExistente() {
        exibirTitulo("NOVA CONTA PARA CLIENTE EXISTENTE");
        
        Cliente cliente = selecionarCliente();
        if (cliente == null) return;
        
        criarNovaContaParaCliente(cliente);
    }
    
    private static void criarNovaContaParaCliente(Cliente cliente) {
        System.out.println("Tipos de conta disponíveis:");
        System.out.println("1. Conta Corrente" + (cliente.podeAbrirContaCorrente() ? "" : " (não elegível)"));
        System.out.println("2. Conta Poupança" + (cliente.podeAbrirContaPoupanca() ? "" : " (não elegível)"));
        
        int tipoEscolhido = lerInteiro("Escolha o tipo de conta (1 ou 2): ");
        
        try {
            switch (tipoEscolhido) {
                case 1 -> {
                    banco.criarContaCorrente(cliente);
                    exibirSucesso("Conta Corrente criada com sucesso!");
                }
                case 2 -> {
                    banco.criarContaPoupanca(cliente);
                    exibirSucesso("Conta Poupança criada com sucesso!");
                }
                default -> exibirErro("Tipo de conta inválido!");
            }
        } catch (RuntimeException e) {
            exibirErro(e.getMessage());
        }
    }
    
    private static void realizarDeposito() {
        exibirTitulo("REALIZAR DEPÓSITO");
        
        try {
            IConta conta = selecionarConta("Em qual conta deseja depositar?");
            if (conta == null) return;
            
            double valor = lerValorPositivo("Valor do depósito: R$ ");
            
            conta.depositar(BigDecimal.valueOf(valor));
            
            exibirSucesso("Depósito realizado com sucesso!");
            System.out.printf("💰 Novo saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
            
        } catch (Exception e) {
            exibirErro("Erro no depósito: " + e.getMessage());
        }
        
        pausar();
    }
    
    private static void realizarSaque() {
        exibirTitulo("REALIZAR SAQUE");
        
        try {
            IConta conta = selecionarConta("De qual conta deseja sacar?");
            if (conta == null) return;
            
            System.out.printf("💰 Saldo atual: R$ %.2f%n", conta.getSaldo().doubleValue());
            
            double valor = lerValorPositivo("Valor do saque: R$ ");
            
            conta.sacar(BigDecimal.valueOf(valor));
            
            exibirSucesso("Saque realizado com sucesso!");
            System.out.printf("💰 Novo saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
            
        } catch (Exception e) {
            exibirErro("Erro no saque: " + e.getMessage());
        }
        
        pausar();
    }
    
    private static void realizarTransferencia() {
        exibirTitulo("REALIZAR TRANSFERÊNCIA");
        
        try {
            System.out.println("=== CONTA DE ORIGEM ===");
            IConta contaOrigemObj = selecionarConta("Transferir DE qual conta?");
            if (contaOrigemObj == null) return;
            
            System.out.printf("💰 Saldo disponível: R$ %.2f%n", contaOrigemObj.getSaldo().doubleValue());
            
            int numeroDestino = lerInteiro("Número da conta DESTINO: ");
            double valor = lerValorPositivo("Valor da transferência: R$ ");
            
            contaOrigemObj.transferir(BigDecimal.valueOf(valor), numeroDestino);
            
            exibirSucesso("Transferência realizada com sucesso!");
            System.out.printf("💰 Novo saldo: R$ %.2f%n", contaOrigemObj.getSaldo().doubleValue());
            
        } catch (Exception e) {
            exibirErro("Erro na transferência: " + e.getMessage());
        }
        
        pausar();
    }
    
    private static void consultarExtrato() {
        exibirTitulo("CONSULTAR EXTRATO");
        
        try {
            IConta conta = selecionarConta("Extrato de qual conta?");
            if (conta == null) return;
            
            conta.imprimirExtrato();
            
        } catch (Exception e) {
            exibirErro("Erro ao consultar extrato: " + e.getMessage());
        }
        
        pausar();
    }
    
    private static void exibirDadosCliente() {
        exibirTitulo("DADOS DO CLIENTE");
        
        try {
            Cliente cliente = selecionarCliente();
            if (cliente == null) return;
            
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║            DADOS PESSOAIS            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.printf("👤 Nome: %s%n", cliente.getNome());
            System.out.printf("🆔 CPF: %s%n", cliente.getCpf());
            System.out.printf("🎂 Idade: %d anos%n", cliente.getIdade());
            System.out.printf("📅 Nascimento: %s%n", cliente.getDataNascimento());
            System.out.printf("📞 Telefone: %s%n", 
                cliente.getTelefone().isEmpty() ? "Não informado" : cliente.getTelefone());
            
            // Contas do cliente
            List<IConta> contasCliente = banco.buscarContasPorCliente(cliente);
            System.out.printf("💳 Total de contas: %d%n", contasCliente.size());
            
            if (!contasCliente.isEmpty()) {
                System.out.println("\n📋 RESUMO DAS CONTAS:");
                for (IConta conta : contasCliente) {
                    System.out.printf("   • %s (%d) - R$ %.2f%n", 
                        conta.getTipoConta(), conta.getNumero(), conta.getSaldo().doubleValue());
                }
            }
            
            System.out.println("══════════════════════════════════════");
            
        } catch (Exception e) {
            exibirErro("Erro ao exibir dados: " + e.getMessage());
        }
        
        pausar();
    }

    private static void listarMinhasContas() {
        exibirTitulo("MINHAS CONTAS");
        
        if (banco.getContas().isEmpty()) {
            exibirAviso("Nenhuma conta cadastrada.");
            pausar();
            return;
        }
        
        Cliente cliente = selecionarCliente();
        if (cliente != null) {
            System.out.println();
            System.out.printf("👤 Contas de: %s%n%n", cliente.getNome());
            
            // ✅ VERSÃO SIMPLIFICADA - sem Stream API
            boolean encontrouConta = false;
            ContaCorrente contaCorrenteEncontrada = null;
            
            // Loop tradicional em vez de Stream
            for (IConta conta : banco.getContas()) {
                if (conta.getCliente().equals(cliente)) {
                    encontrouConta = true;
                    
                    System.out.printf("💳 %s - Nº %d%n", conta.getTipoConta(), conta.getNumero());
                    System.out.printf("💰 Saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
                    
                    // ✅ DESTACA CONTAS CORRENTE COM SERVIÇOS ESPECIAIS
                    if (conta instanceof ContaCorrente) {
                        System.out.println("🎯 Serviços especiais disponíveis!");
                        contaCorrenteEncontrada = (ContaCorrente) conta;
                    }
                    
                    System.out.println("─".repeat(30));
                }
            }
            
            if (!encontrouConta) {
                exibirAviso("Este cliente não possui contas.");
            } else if (contaCorrenteEncontrada != null) {
                // ✅ PERGUNTA SE QUER VER SERVIÇOS ESPECIAIS
                System.out.print("\n💡 Deseja consultar serviços especiais da conta corrente? (S/N): ");
                String opcao = scanner.nextLine().trim().toUpperCase();
                
                if (opcao.equals("S")) {
                    System.out.println();
                    contaCorrenteEncontrada.exibirServicosEspeciais(); // ✅ USANDO OS MÉTODOS!
                }
            }
        }
        
        pausar();
    }
    
    private static void exibirRelatorios() {
        exibirTitulo("RELATÓRIO GERENCIAL");
        banco.gerarRelatorioCompleto();
        pausar();
    }

    // ================= NOVOS SERVIÇOS =================

    private static void buscarContaEspecifica() {
        exibirTitulo("BUSCAR CONTA POR NÚMERO");

        try {
            if (banco.getContas().isEmpty()) {
                exibirAviso("Nenhuma conta cadastrada.");
                pausar();
                return;
            }

            int numero = lerInteiro("Digite o número da conta: ");

            IConta conta = banco.buscarContaPorNumero(numero);

            System.out.println("\n✅ Conta encontrada:");
            System.out.println("════════════════════════");
            System.out.printf("💳 Tipo: %s%n", conta.getTipoConta());
            System.out.printf("🔢 Número: %d%n", conta.getNumero());
            System.out.printf("🏦 Agência: %d%n", conta.getAgencia());
            System.out.printf("👤 Titular: %s%n", conta.getCliente().getNome());
            System.out.printf("📄 CPF: %s%n", conta.getCliente().getCpf());
            System.out.printf("💰 Saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
            System.out.printf("📊 Transações: %d%n", conta.getHistorico().size());
            System.out.println("════════════════════════");

        } catch (ContaInexistenteException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            exibirErro("Erro na busca: " + e.getMessage());
        }

        pausar();
    }

    private static void listarContasCliente() {
        exibirTitulo("CONTAS DE UM CLIENTE");

        try {
            if (banco.getClientes().isEmpty()) {
                exibirAviso("Nenhum cliente cadastrado.");
                pausar();
                return;
            }

            Cliente cliente = selecionarCliente();
            if (cliente == null) return;

            // Usar o método do Banco
            List<IConta> contasCliente = banco.buscarContasPorCliente(cliente);

            System.out.println("\n👤 Cliente: " + cliente.getNome());
            System.out.println("════════════════════════════════════════");

            if (contasCliente.isEmpty()) {
                exibirAviso("Este cliente não possui contas cadastradas.");
            } else {
                System.out.printf("📊 Total de contas: %d%n%n", contasCliente.size());

                BigDecimal saldoTotal = BigDecimal.ZERO;

                for (int i = 0; i < contasCliente.size(); i++) {
                    IConta conta = contasCliente.get(i);
                    saldoTotal = saldoTotal.add(conta.getSaldo());

                    System.out.printf("%d. 💳 %s%n", i + 1, conta.getTipoConta());
                    System.out.printf("   🔢 Número: %d%n", conta.getNumero());
                    System.out.printf("   💰 Saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
                    System.out.printf("   📊 Transações: %d%n", conta.getHistorico().size());
                    System.out.println("   " + "─".repeat(30));
                }

                System.out.printf("💎 SALDO TOTAL: R$ %.2f%n", saldoTotal.doubleValue());
            }

            System.out.println("════════════════════════════════════════");

        } catch (Exception e) {
            exibirErro("Erro ao listar contas: " + e.getMessage());
        }

        pausar();
    }

    private static void encerrarContaEspecifica() {
        exibirTitulo("ENCERRAR CONTA");

        try {
            if (banco.getContas().isEmpty()) {
                exibirAviso("Nenhuma conta cadastrada.");
                pausar();
                return;
            }

            IConta conta = selecionarConta("Qual conta deseja encerrar?");
            if (conta == null) return;

            // Mostrar informações da conta
            System.out.println("\n📋 DADOS DA CONTA A SER ENCERRADA:");
            System.out.println("══════════════════════════════════");
            System.out.printf("💳 Tipo: %s%n", conta.getTipoConta());
            System.out.printf("🔢 Número: %d%n", conta.getNumero());
            System.out.printf("👤 Titular: %s%n", conta.getCliente().getNome());
            System.out.printf("💰 Saldo: R$ %.2f%n", conta.getSaldo().doubleValue());
            System.out.println("══════════════════════════════════");

            // Verificar saldo
            if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                exibirAviso("ATENÇÃO: A conta possui saldo!");
                System.out.printf("Saldo atual: R$ %.2f%n", conta.getSaldo().doubleValue());
                System.out.println("Recomendamos transferir ou sacar o valor antes do encerramento.");
                System.out.println();
            }

            // Confirmar encerramento
            System.out.print("⚠️ CONFIRMA O ENCERRAMENTO DA CONTA? Esta ação não pode ser desfeita! (s/n): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();

            if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                boolean sucesso = banco.encerrarConta(conta.getNumero());

                if (sucesso) {
                    exibirSucesso("Conta encerrada com sucesso!");
                    System.out.println("🏦 Obrigado por ter sido nosso cliente!");
                } else {
                    exibirErro("Não foi possível encerrar a conta. Tente novamente.");
                }
            } else {
                System.out.println("❌ Operação cancelada.");
            }


        } catch (Exception e) {
            exibirErro("Erro ao encerrar conta: " + e.getMessage());
        }

        pausar();
    }

    // ================= RELATÓRIOS E ADMINISTRAÇÃO =================

    private static void exibirRelatorioMovimentacao() {
        exibirTitulo("RELATÓRIO DE MOVIMENTAÇÃO");
        banco.gerarRelatorioMovimentacao();
        pausar();
    }

    private static void aplicarRendimentoAdmin() {
        exibirTitulo("APLICAR RENDIMENTO - MODO ADMINISTRADOR");

        if (perguntarSimNao("Confirma aplicação de rendimento em todas as poupanças?")) {
            banco.aplicarRendimentoPoupancas();
            exibirSucesso("Rendimentos aplicados com sucesso!");
        } else {
            System.out.println("❌ Operação cancelada.");
        }
        pausar();
    }

    private static void painelAdministrativo() {
        exibirTitulo("PAINEL ADMINISTRATIVO");

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           📊 ESTATÍSTICAS            ║");
        System.out.println("╚══════════════════════════════════════╝");

        // ✅ USA: getTotalClientes()
        System.out.printf("👥 Total de Clientes: %d%n", banco.getTotalClientes());

        // ✅ USA: getTotalContas()
        System.out.printf("💳 Total de Contas: %d%n", banco.getTotalContas());

        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           🛠️ OPERAÇÕES               ║");
        System.out.println("╚══════════════════════════════════════╝");

        // ✅ USA: executarBackupDados()
        if (perguntarSimNao("Deseja executar backup dos dados?")) {
            banco.executarBackupDados();
        } else {
            System.out.println("❌ Backup cancelado.");
        }

        pausar();
    }
    
    private static void exibirOpcoesExtras() {
        exibirTitulo("OPÇÕES EXTRAS");
        
        System.out.println("🎯 FUNCIONALIDADES ESPECIAIS:");
        System.out.println("─".repeat(40));
        System.out.println("1. 📊 Simulação de Rendimento (Poupança)");
        System.out.println("2. 💳 Relatório Anual (Conta Corrente)");
        System.out.println("3. 🎯 Meta de Poupança");
        System.out.println("4. 🔍 Busca Avançada");
        System.out.println("0. Voltar ao menu principal");
        System.out.println("─".repeat(40));
        
        int opcao = lerInteiro("Escolha uma opção: ");
        
        switch (opcao) {
            case 1 -> simulacaoRendimento();
            case 2 -> relatorioAnualContaCorrente();
            case 3 -> definirMetaPoupanca();
            case 4 -> buscaAvancada();
            case 0 -> System.out.println("Voltando ao menu principal...");
            default -> exibirErro("Opção inválida!");
        }
        
        if (opcao != 0) {
            pausar();
        }
    }
    
    private static void simulacaoRendimento() {
        exibirTitulo("SIMULAÇÃO DE RENDIMENTO");
        
        try {
            // Buscar contas poupança
            boolean temPoupanca = false;
            for (IConta conta : banco.getContas()) {
                if (conta instanceof ContaPoupanca) {
                    temPoupanca = true;
                    break;
                }
            }
            
            if (!temPoupanca) {
                exibirAviso("Nenhuma conta poupança encontrada.");
                return;
            }
            
            IConta conta = selecionarConta("Qual conta poupança deseja simular?");
            if (conta == null) return;
            
            if (conta instanceof ContaPoupanca) {
                ContaPoupanca poupanca = (ContaPoupanca) conta;
                poupanca.consultarRendimento();
            } else {
                exibirErro("Conta selecionada não é uma poupança!");
            }
            
        } catch (Exception e) {
            exibirErro("Erro na simulação: " + e.getMessage());
        }
    }
    
    private static void relatorioAnualContaCorrente() {
        exibirTitulo("RELATÓRIO ANUAL - CONTA CORRENTE");
        
        try {
            // Buscar contas corrente
            boolean temCorrente = false;
            for (IConta conta : banco.getContas()) {
                if (conta instanceof ContaCorrente) {
                    temCorrente = true;
                    break;
                }
            }
            
            if (!temCorrente) {
                exibirAviso("Nenhuma conta corrente encontrada.");
                return;
            }
            
            IConta conta = selecionarConta("Qual conta corrente deseja o relatório?");
            if (conta == null) return;
            
            if (conta instanceof ContaCorrente) {
                ContaCorrente corrente = (ContaCorrente) conta;
                corrente.gerarRelatorioAnual();
            } else {
                exibirErro("Conta selecionada não é uma conta corrente!");
            }
            
        } catch (Exception e) {
            exibirErro("Erro no relatório: " + e.getMessage());
        }
    }
    
    private static void definirMetaPoupanca() {
        exibirTitulo("DEFINIR META DE POUPANÇA");
        
        try {
            // Buscar contas poupança
            boolean temPoupanca = false;
            for (IConta conta : banco.getContas()) {
                if (conta instanceof ContaPoupanca) {
                    temPoupanca = true;
                    break;
                }
            }
            
            if (!temPoupanca) {
                exibirAviso("Nenhuma conta poupança encontrada.");
                return;
            }
            
            IConta conta = selecionarConta("Para qual poupança deseja definir meta?");
            if (conta == null) return;
            
            if (conta instanceof ContaPoupanca) {
                ContaPoupanca poupanca = (ContaPoupanca) conta;
                
                double valorMeta = lerValorPositivo("Valor da meta: R$ ");
                int meses = lerInteiro("Prazo em meses: ");
                
                poupanca.definirMetaPoupanca(valorMeta, meses);
            } else {
                exibirErro("Conta selecionada não é uma poupança!");
            }
            
        } catch (Exception e) {
            exibirErro("Erro ao definir meta: " + e.getMessage());
        }
    }
    
    private static void buscaAvancada() {
        exibirTitulo("BUSCA AVANÇADA");
        
        System.out.println("🔍 OPÇÕES DE BUSCA:");
        System.out.println("1. Buscar cliente por nome");
        System.out.println("2. Contas com saldo acima de...");
        System.out.println("3. Contas com mais transações");
        
        int opcao = lerInteiro("Escolha o tipo de busca: ");
        
        try {
            switch (opcao) {
                case 1 -> {
                    String nome = lerDadoObrigatorio("Digite parte do nome: ");
                    System.out.println("\n🔍 Resultados encontrados:");
                    boolean encontrou = false;
                    for (Cliente cliente : banco.getClientes()) {
                        if (cliente.getNome().toLowerCase().contains(nome.toLowerCase())) {
                            System.out.printf("👤 %s (%s)%n", cliente.getNome(), cliente.getCpf());
                            encontrou = true;
                        }
                    }
                    if (!encontrou) {
                        System.out.println("Nenhum cliente encontrado.");
                    }
                }
                case 2 -> {
                    double valorMinimo = lerValorPositivo("Saldo mínimo: R$ ");
                    System.out.println("\n💰 Contas encontradas:");
                    boolean encontrou = false;
                    for (IConta conta : banco.getContas()) {
                        if (conta.getSaldo().doubleValue() >= valorMinimo) {
                            System.out.printf("💳 Conta %d (%s) - %s - R$ %.2f%n", 
                                conta.getNumero(), conta.getTipoConta(),
                                conta.getCliente().getNome(), conta.getSaldo().doubleValue());
                            encontrou = true;
                        }
                    }
                    if (!encontrou) {
                        System.out.println("Nenhuma conta encontrada.");
                    }
                }
                case 3 -> {
                    System.out.println("\n📊 Contas mais movimentadas:");
                    for (IConta conta : banco.getContas()) {
                        if (conta.getHistorico().size() > 0) {
                            System.out.printf("💳 Conta %d - %s transações%n", 
                                conta.getNumero(), conta.getHistorico().size());
                        }
                    }
                }
                default -> exibirErro("Opção inválida!");
            }
        } catch (Exception e) {
            exibirErro("Erro na busca: " + e.getMessage());
        }
    }

    // ================= MÉTODOS AUXILIARES =================
    
    private static Cliente selecionarCliente() {
        if (banco.getClientes().isEmpty()) {
            exibirAviso("Nenhum cliente cadastrado.");
            return null;
        }
        
        System.out.println("👥 Clientes:");
        for (int i = 0; i < banco.getClientes().size(); i++) {
            Cliente cliente = banco.getClientes().get(i);
            System.out.printf("%d. %s (%s)%n", 
                i + 1, cliente.getNome(), cliente.getCpf());
        }
        
        try {
            int indice = lerInteiro("Selecione o cliente: ") - 1;
            
            if (indice < 0 || indice >= banco.getClientes().size()) {
                exibirErro("Cliente inválido!");
                return null;
            }
            
            return banco.getClientes().get(indice);
            
        } catch (Exception e) {
            exibirErro("Seleção inválida!");
            return null;
        }
    }
    
    private static IConta selecionarConta(String titulo) {
        if (banco.getContas().isEmpty()) {
            exibirAviso("Nenhuma conta cadastrada. Abra uma conta primeiro.");
            return null;
        }
        
        if (titulo != null) {
            System.out.println(titulo);
        }
        
        System.out.println("💳 Contas disponíveis:");
        for (int i = 0; i < banco.getContas().size(); i++) {
            IConta conta = banco.getContas().get(i);
            System.out.printf("%d. Conta %d - %s - %s (R$ %.2f)%n", 
                i + 1, conta.getNumero(), conta.getTipoConta(),
                conta.getCliente().getNome(), conta.getSaldo().doubleValue());
        }
        
        try {
            int indice = lerInteiro("Selecione a conta: ") - 1;
            
            if (indice < 0 || indice >= banco.getContas().size()) {
                exibirErro("Conta inválida!");
                return null;
            }
            
            return banco.getContas().get(indice);
            
        } catch (Exception e) {
            exibirErro("Seleção inválida!");
            return null;
        }
    }
    
    // Métodos de entrada de dados
    private static String lerDadoObrigatorio(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                exibirErro("Campo obrigatório!");
            }
        } while (input.isEmpty());
        return input;
    }
    
    private static String lerDadoOpcional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int lerInteiro(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                exibirErro("Digite um número válido!");
            }
        }
    }
    
    private static double lerValorPositivo(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valor <= 0) {
                    exibirErro("Valor deve ser positivo!");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                exibirErro("Digite um valor válido!");
            }
        }
    }
    
    private static LocalDate lerDataNascimento() {
        while (true) {
            try {
                System.out.print("Data de nascimento (dd/MM/yyyy): ");
                String dataStr = scanner.nextLine().trim();
                LocalDate data = LocalDate.parse(dataStr, DATE_FORMAT);
                
                if (data.isAfter(LocalDate.now())) {
                    exibirErro("Data não pode ser futura!");
                    continue;
                }
                
                return data;
            } catch (DateTimeParseException e) {
                exibirErro("Data inválida! Use o formato dd/MM/yyyy");
            }
        }
    }

    private static boolean perguntarSimNao(String pergunta) {
        while (true) {
            System.out.print(pergunta + " (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            } else if (resposta.equals("n") || resposta.equals("não") || resposta.equals("nao")) {
                return false;
            } else {
                exibirErro("Digite 's' para sim ou 'n' para não");
            }
        }
    }


    // Métodos de interface
    private static void exibirTitulo(String titulo) {
        System.out.println("╔" + "═".repeat(titulo.length() + 4) + "╗");
        System.out.println("║  " + titulo + "  ║");
        System.out.println("╚" + "═".repeat(titulo.length() + 4) + "╝");
        System.out.println();
    }
    
    private static void exibirSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
    }
    
    private static void exibirErro(String mensagem) {
        System.out.println("❌ " + mensagem);
    }
    
    private static void exibirAviso(String mensagem) {
        System.out.println("⚠️ " + mensagem);
    }
    
    private static void pausar() {
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private static void limparTela() {
        System.out.print("\033[2J\033[H");
    }
    
    private static void encerrarSistema() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║    Obrigado por escolher o nosso      ║");
        System.out.println("║            YLLOCIN BANK!              ║");
        System.out.println("║                                       ║");
        System.out.println("║         Volte sempre! 🏦              ║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝");
        
        scanner.close();
    }
}
}