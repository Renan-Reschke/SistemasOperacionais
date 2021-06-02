package bank;

import java.util.Random;

public class Client extends Thread{

    // Atributos
    private String nome;
    private Account account;

    // Construtor
    Client(String nome, Account account){
        super(nome);
        this.account = account;
    }

    // Metodos
    public void execute() {
        Random operacao = new Random();
        try {
            while(true){
                Random random = new Random();
                int op = random.nextInt(2);
                switch (op){
                    case 0:
                        this.account.deposit();
                        break;
                    case 1:
                        this.account.withdraw();
                        break;
                }
                Thread.yield();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
