package prodcom;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    // Dados manipulados pelo consumidor e produtor

    private static final int CAPACITY = 10;
    private final Queue<Integer> queue = new LinkedList<>();

    // Variável de lock para acessar a fila compartilhada
    private final Lock lock = new ReentrantLock();
    // Variável de condição do produtor que libera o consumidor para consumir
    private final Condition bufferNotFull = lock.newCondition();
    // Variável de condição do consumidor que libera o produtor para produzir 
    private final Condition bufferNotEmpty = lock.newCondition();

    // Armazena um número na fila
    public void put(Integer number) throws InterruptedException {
        lock.lock();
        try {
            // Se buffer cheio, aguarda o consumidor consumir algum número
            while (queue.size() == CAPACITY) {
                System.out.println(Thread.currentThread().getName()
                        + " : Buffer cheio, aguardando...");
                bufferNotEmpty.await();
            }
            // Adiciona um número na fila
            boolean isAdded = queue.offer(number);
            if (isAdded) {
                System.out.printf("%s produziu %d na fila%n",
                        Thread.currentThread().getName(), number);
                // Sinaliza a thread consumidor que ela pode consumir
                System.out.println(Thread.currentThread().getName()
                        + " : Buffer já tem elemento!");
                bufferNotFull.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    // Remove um número da fila
    public void get() throws InterruptedException {
        lock.lock();
        try {
            // Se buffer vazio, aguarda o produtor produzir algum número
            while (queue.size() == 0) {
                System.out.println(Thread.currentThread().getName()
                        + " : Buffer vazio, aguardando...");
                bufferNotFull.await();
            }
            // Remove um número da fila
            Integer value = queue.poll();
            if (value != null) {
                System.out.printf("%s consumiu %d da fila%n",
                        Thread.currentThread().getName(), value);
                // Sinaliza a thread produtor que ela pode produzir
                System.out.println(Thread.currentThread().getName()
                        + " : Buffer tem espaço!");
                bufferNotEmpty.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

}
