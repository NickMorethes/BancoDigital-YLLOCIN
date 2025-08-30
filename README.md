# 🏦 Banco Digital - Projeto POO em Java

## 📋 Descrição do Projeto

Este projeto implementa um sistema bancário digital utilizando os conceitos fundamentais da Programação Orientada a Objetos (POO) em Java. O objetivo é demonstrar na prática os pilares da POO: **Encapsulamento**, **Herança**, **Polimorfismo** e **Abstração**.

## 🎯 Objetivos de Aprendizado

- **Programação Orientada a Objetos**: Classes, objetos, métodos e atributos
- **Herança**: Reutilização de código e especialização de classes
- **Polimorfismo**: Comportamentos diferentes para objetos de classes relacionadas
- **Encapsulamento**: Proteção e controle de acesso aos dados
- **Abstração**: Simplificação de conceitos complexos
- **Collections**: ArrayList, HashMap para gerenciar dados
- **Stream API**: Processamento funcional de coleções
- **Lambda Expressions**: Programação funcional
- **Interfaces**: Contratos e múltipla herança de comportamento

## 🏗️ Arquitetura do Sistema

### Entidades Principais
- **Cliente**: Representa os usuários do banco
- **Conta**: Classe abstrata base para todos os tipos de conta
- **ContaCorrente**: Conta com funcionalidades específicas
- **ContaPoupanca**: Conta poupança com rendimento
- **Banco**: Gerencia clientes e contas
- **Transacao**: Registro de operações bancárias

### Funcionalidades Implementadas
- ✅ Criação de clientes e contas
- ✅ Depósitos e saques
- ✅ Transferências entre contas
- ✅ Consulta de saldo e extrato
- ✅ Histórico de transações
- ✅ Relatórios com Stream API

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Linguagem principal
- **Collections Framework**: Gerenciamento de dados
- **Stream API**: Processamento de dados
- **Lambda Expressions**: Programação funcional
- **LocalDateTime**: Manipulação de datas

## 📁 Estrutura do Projeto

src/
├── banco/
│   ├── Banco.java
│   ├── Cliente.java
│   ├── Conta.java
│   ├── ContaCorrente.java
│   ├── ContaPoupanca.java
│   ├── Transacao.java
│   └── TipoTransacao.java
├── interfaces/
│   └── IConta.java
├── exceptions/
│   ├── SaldoInsuficienteException.java
│   └── ContaInexistenteException.java
└── Main.java


``` 
## 🚀 Como Executar

1. Clone o repositório:
```
bash git clone [https://github.com/seu-usuario/banco-digital.git](https://github.com/seu-usuario/banco-digital.git)
``` 

2. Compile o projeto:
```
bash javac -d out src/**/*.java
``` 

3. Execute a aplicação:
```
bash java -cp out Main
``` 

## 💡 Conceitos POO Demonstrados

### 1. **Encapsulamento**
```
java private double saldo; public double getSaldo() { return saldo; }
``` 

### 2. **Herança**
```java
public class ContaCorrente extends Conta { }
public class ContaPoupanca extends Conta { }
```

### 3. **Polimorfismo**
```java
List<Conta> contas = Arrays.asList(
    new ContaCorrente(),
    new ContaPoupanca()
);
```

### 4. **Abstração**
```
