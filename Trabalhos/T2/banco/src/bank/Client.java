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
    // Escolha aleatoria entre saque ou deposito
    private void execute() throws InterruptedException {

        try {
            Random random = new Random();
            switch (random.nextInt(2)) {
                case 0:
                    account.deposit();
                    break;
                case 1:
                    account.withdraw();
                    break;
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    // Metodo redefinido que executa a função da thread
    @Override
    public void run() {
        try {
            // Tenta realizar uma operacao de deposito ou retirada
            while(true){
                execute();
                Thread.yield();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
