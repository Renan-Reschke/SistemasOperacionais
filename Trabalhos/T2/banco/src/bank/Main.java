package bank;

public class Main {

    public static void main(String[] args) {
        // Cria o buffer compartilhado
        Account conta = new Account(1000);
        // Cria as threads de produtor e consumidor
        Client c1 = new Client("Tiberius", conta);
        Client c2 = new Client("Augustus", conta);
        Client c3 = new Client("Claudius", conta);
        Client c4 = new Client("Lucius", conta);
        // Inicializa as threads
        c1.start();
        c2.start();
        c3.start();
        c4.start();
    }
}
