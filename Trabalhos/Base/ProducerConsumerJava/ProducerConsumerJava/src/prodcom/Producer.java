package prodcom;

import java.util.Random;

class Producer extends Thread {
    // Referência para buffer compartilhado

    Buffer buffer;

    // Construtor
    public Producer(Buffer buffer, String p) {
        super(p); //chama o construtor de Thread e passa o nome do parâmetro
        this.buffer = buffer;
    }

    // Método redefinido que executa a função da thread
    @Override
    public void run() {
        Random random = new Random();
        try {
            // Tenta produzir um número inteiro aleatório
            while (true) {
                buffer.put(random.nextInt(1000));
                Thread.yield();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
