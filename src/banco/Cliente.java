package banco;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe Cliente - representa um cliente do banco
 * -----------------------------------------------------
 * CONCEITO: Encapsulamento e Regras de Negócio Flexíveis
 * - Permite clientes de qualquer idade
 * - Regras específicas para cada tipo de conta
 * - Suporte a emancipação legal
 */
public class Cliente {
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private boolean emancipado;
    private Cliente responsavelLegal;
    
    // Construtor principal
    public Cliente(String nome, String cpf, LocalDate dataNascimento) {
        setNome(nome);
        setCpf(cpf);
        setDataNascimento(dataNascimento);
        this.emancipado = false;
        this.responsavelLegal = null;
    }
    
    // Construtor para menores com responsável
    public Cliente(String nome, String cpf, LocalDate dataNascimento, Cliente responsavelLegal) {
        this(nome, cpf, dataNascimento);
        setResponsavelLegal(responsavelLegal);
    }
    
    // Construtor simplificado (compatibilidade)
    public Cliente(String nome, String cpf) {
        this(nome, cpf, LocalDate.of(1990, 1, 1));
    }
    
    // GETTERS E SETTERS BÁSICOS
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (nome.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        this.nome = nome.trim();
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }
        
        this.cpf = formatarCpf(cpfLimpo);
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula");
        }
        
        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser futura");
        }
        
        // ✅ REMOVIDO: Não há idade mínima para ser cliente
        // Qualquer idade pode ser cliente (bebês, crianças, etc.)
        
        this.dataNascimento = dataNascimento;
    }
    
    // ✅ NOVOS GETTERS/SETTERS
    public boolean isEmancipado() {
        return emancipado;
    }
    
    public void setEmancipado(boolean emancipado) {
        if (emancipado && getIdade() >= 18) {
            throw new IllegalArgumentException("Maior de idade não precisa ser emancipado");
        }
        this.emancipado = emancipado;
    }
    
    public Cliente getResponsavelLegal() {
        return responsavelLegal;
    }
    
    public void setResponsavelLegal(Cliente responsavelLegal) {
        if (getIdade() >= 18) {
            throw new IllegalArgumentException("Maior de idade não precisa de responsável legal");
        }
        
        if (responsavelLegal != null && responsavelLegal.getIdade() < 18) {
            throw new IllegalArgumentException("Responsável legal deve ser maior de idade");
        }
        
        this.responsavelLegal = responsavelLegal;
    }
    
    // Getters para email e telefone (mesma implementação anterior)
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!email.contains("@") || !email.contains(".")) {
                throw new IllegalArgumentException("Email inválido");
            }
            this.email = email.trim().toLowerCase();
        } else {
            this.email = null;
        }
    }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
            if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
                throw new IllegalArgumentException("Telefone deve ter 10 ou 11 dígitos");
            }
            this.telefone = formatarTelefone(telefoneLimpo);
        } else {
            this.telefone = null;
        }
    }
    
    // MÉTODO CALCULADO
    public int getIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    
    /**
     * ✅ CONTA POUPANÇA: Qualquer idade é permitida
     * - Bebês, crianças, adultos - todos podem ter
     * - Pais podem abrir para filhos como reserva/educação financeira
     */
    public boolean podeAbrirContaPoupanca() {
        return true;
    }
    
    /**
     * ✅ CONTA CORRENTE: 18+ ou emancipado
     * - Maior de 18 anos: automático
     * - Menor emancipado: com documentação legal
     * - Menor com responsável: NÃO (seria conta conjunta, caso mais complexo)
     */
    public boolean podeAbrirContaCorrente() {
        return getIdade() >= 18 || isEmancipado();
    }
    
    // MÉTODOS UTILITÁRIOS
    private String formatarCpf(String cpf) {
        return String.format("%s.%s.%s-%s", 
            cpf.substring(0, 3),
            cpf.substring(3, 6), 
            cpf.substring(6, 9),
            cpf.substring(9, 11));
    }
    
    private String formatarTelefone(String telefone) {
        if (telefone.length() == 10) {
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 6),
                telefone.substring(6, 10));
        } else { // 11 dígitos
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 7),
                telefone.substring(7, 11));
        }
    }
    
    @Override
    public String toString() {
        return String.format("Cliente{nome='%s', cpf='%s', idade=%d}", 
            nome, cpf, getIdade());
    }

    // Método para impressão detalhada
    public void imprimirDados() {
        System.out.println("=== DADOS DO CLIENTE ===");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Idade: " + getIdade() + " anos");
        System.out.println("Data Nascimento: " + getDataNascimento()); // ✅ USANDO GETTER

        if (getEmail() != null) {
            System.out.println("Email: " + getEmail());
        }

        if (getTelefone() != null) {
            System.out.println("Telefone: " + getTelefone());
        }


        if (getIdade() < 18) {
            System.out.println("Status: Menor de idade");
            if (isEmancipado()) {
                System.out.println("Situação: Emancipado legalmente");
            } else if (getResponsavelLegal() != null) {
                System.out.println("Responsável Legal: " + getResponsavelLegal().getNome());
            } else {
                System.out.println("⚠️ Responsável legal não cadastrado");
            }
        } else {
            System.out.println("Status: Maior de idade");
        }

        System.out.println("Pode abrir CC: " + (podeAbrirContaCorrente() ? "Sim" : "Não"));
        System.out.println("Pode abrir CP: " + (podeAbrirContaPoupanca() ? "Sim" : "Não"));
        System.out.println("========================");
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return cpf.equals(cliente.cpf);
    }
    
    @Override
    public int hashCode() {
        return cpf.hashCode();
    }
}