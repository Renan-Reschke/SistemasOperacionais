package prodcom;

// Adaptado de https://javarevisited.blogspot.com/2015/06/java-lock-and-condition-example-producer-consumer.html
public class Main {

    public static void main(String[] args) {
        // Cria o buffer compartilhado
        Buffer buffer = new Buffer();
        // Cria as threads de produtor e consumidor
        Producer p1 = new Producer(buffer, "PRODUTOR 1");
        Producer p2 = new Producer(buffer, "PRODUTOR 2");
        Consumer c1 = new Consumer(buffer, "CONSUMIDOR 1");
        Consumer c2 = new Consumer(buffer, "CONSUMIDOR 2");
        // Inicializa as threads
        p1.start();
        p2.start();
        c1.start();
        c2.start();
    }

}
