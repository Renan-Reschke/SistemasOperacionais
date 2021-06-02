package bank;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    // Atributos
    private int balance;
    private final int valores[] = {10, 20, 50, 100};

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
        Random random = new Random();
        int valor = random.nextInt(4);
        this.balance += valores[valor];
        System.out.printf(" depositou %d\n", valores[valor]);
    }

    public void withdraw() throws InterruptedException {                                     // Retirada na conta
        Random random = new Random();
        int valor = random.nextInt(4);
        int saldoFinal = this.balance -= valores[valor];
        if(saldoFinal >= 0){
            saldoPositivo.signalAll();
        }
        lock.lock();

        try {
            while (saldoFinal < 0) {
                System.out.printf("Retirada de %d negada, saldo atual de: %d", valores[valor], this.balance);
                saldoPositivo.await();
            }
            this.balance -= valores[valor];       // Permite retirada se o saldo final apos saque for maior que 0
            System.out.printf(" retirou %d\n", valores[valor]);
        } finally {
            lock.unlock();
        }
    }
}


