package bank;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    // Atributos
    private int balance;
    private static final int valores[] = {10, 20, 50, 100};

    // Variável de lock para acessar a fila compartilhada
    private final Lock lock = new ReentrantLock();
    // Variável de condição da conta vazia que libera retirada
    private final Condition saldoPositivo = lock.newCondition();


    // Construtor
    Account(int saldoInicial) {
        this.balance = saldoInicial;
        System.out.printf("Conta criada com saldo inicial de: %d\n", saldoInicial);
    }

    // Metodos
    public void deposit() throws InterruptedException {                                      // Deposito na conta
        lock.lock();
        try {
            // Escolha de valor aleatorio para deposito
            Random random = new Random();
            int valor = random.nextInt(4);

            // Atualizacao do saldo
            this.balance += valores[valor];

            // Exibicao no console
            System.out.printf("Cliente: " + Thread.currentThread().getName() + " depositou %d\n", valores[valor]);
            System.out.printf("Conta: saldo atualizado de %d\n", this.balance);

            // Sinal para liberar saques pendentes
            if (balance >= 100) {
                saldoPositivo.signalAll();
            }

        } finally {
            lock.unlock();
        }
    }

    public void withdraw() throws InterruptedException {                                     // Retirada na conta
        lock.lock();
        try {
            // Escolha do valor aleatorio para retirada
            Random random = new Random();
            int valor = random.nextInt(4);

            // Qual seria o valor final do saldo apos retirada

            while ((balance - valores[valor]) < 0) {
                System.out.printf("Cliente: " + Thread.currentThread().getName() + " retirada de %d negada, saldo atual de: %d\n", valores[valor], this.balance);
                saldoPositivo.await();
            }

            // Permite retirada se o saldo final apos saque for maior que 0
            this.balance -= valores[valor];

            // Exibicao no console
            System.out.printf("Cliente: " + Thread.currentThread().getName() + " retirou %d\n", valores[valor]);
            System.out.printf("Conta: saldo atualizado de %d\n", this.balance);

        } finally {
            lock.unlock();
        }
    }
}


