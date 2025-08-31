# 🏦 YLLOCIN Bank - Sistema Bancário Digital

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![POO](https://img.shields.io/badge/Paradigma-Orientado%20a%20Objetos-blue.svg)]()
[![Status](https://img.shields.io/badge/Status-Concluído-green.svg)]()

## 📋 Descrição do Projeto

O **YLLOCIN Bank** é um sistema bancário digital completo desenvolvido em Java, que implementa todos os conceitos fundamentais da Programação Orientada a Objetos (POO). O sistema oferece funcionalidades bancárias essenciais com uma interface intuitiva via terminal.

## 🎯 Objetivos de Aprendizado

### Conceitos POO Aplicados
- ✅ **Encapsulamento**: Proteção e controle de acesso aos dados
- ✅ **Herança**: Classes `ContaCorrente` e `ContaPoupanca` herdam de `Conta`
- ✅ **Polimorfismo**: Comportamentos específicos para cada tipo de conta
- ✅ **Abstração**: Interface `IConta` e classe abstrata `Conta`

### Tecnologias Java Avançadas
- ✅ **Collections Framework**: `ArrayList` para gerenciar dados
- ✅ **Stream API**: Processamento funcional nos relatórios
- ✅ **Lambda Expressions**: Filtros e ordenações
- ✅ **Enum**: `TipoTransacao` para categorizar operações
- ✅ **Exception Handling**: Tratamento personalizado de erros
- ✅ **LocalDateTime**: Controle de datas nas transações
- ✅ **BigDecimal**: Precisão monetária

### 🏗️ Arquitetura do Sistema

YLLOCIN Bank System

├── 👤 Cliente (dados pessoais)

├── 💳 Conta (classe abstrata)

│   ├── ContaCorrente (taxa de saque)

│   └── ContaPoupanca (rendimento mensal)

├── 🏦 Banco (gerenciamento geral)

├── 📊 Transacao (registro de operações)

└── ⚠️ Exceptions (tratamento de erros)

## 🚀 Funcionalidades

### 👤 Gestão de Clientes
- Cadastro com validação de idade
- Consulta de dados pessoais
- Elegibilidade para tipos de conta

### 💳 Operações Bancárias
- **Depósitos**: Sem taxas
- **Saques**: Taxa R$ 0,50 para Conta Corrente
- **Transferências**: Entre qualquer conta
- **Extratos**: Histórico detalhado

### 📊 Conta Poupança Especial
- Rendimento mensal de 0,5%
- Simulação de investimentos
- Metas de poupança
- Relatórios de rendimento

### 💼 Conta Corrente Premium
- Solicitação de cartão de crédito
- Análise de limite automática
- Relatórios anuais detalhados
- Serviços exclusivos

### 📈 Relatórios Gerenciais
- Estatísticas completas do banco
- Top clientes por patrimônio
- Análise por faixa etária
- Contas mais movimentadas

## 📁 Estrutura do Projeto

src/

├── banco/

│   ├── Banco.java                 # Gerenciamento principal

│   ├── Cliente.java               # Dados dos clientes

│   ├── Conta.java                 # Classe abstrata base

│   ├── ContaCorrente.java         # Conta com taxas

│   ├── ContaPoupanca.java         # Conta com rendimento

│   ├── Transacao.java             # Registro de operações

│   └── TipoTransacao.java         # Enum de tipos

├── interfaces/

│   └── IConta.java                # Contrato das contas

├── exceptions/

│   ├── SaldoInsuficienteException.java

│   └── ContaInexistenteException.java

└── Main.java                      # Interface do usuário


## 🛠️ Como Executar

### Pré-requisitos
- Java 21 ou superior
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Compilação e Execução

bash
# Clone o repositório
git clone https://github.com/NickMorethes/bancodigitalyllocin.git

# Entre no diretório
cd yllocin-bank

# Compile o projeto
javac -d out src/**/*.java

# Execute a aplicação
java -cp out Main

### Usando IDE
Importe o projeto na sua IDE
Execute o arquivo Main.java
Interaja com o menu no terminal


👨‍💻 Autor
Nicolly Roberta Morethes
📧 Email: nrmorethes@outlook.com
💼 LinkedIn: Nicolly Morethes
🐙 GitHub: NickMorethes


⭐ Se este projeto foi útil para você, considere dar uma estrela!
