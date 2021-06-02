// 19.02009-0 Renan Scheidt Reschke
// 19.01370-0 Felipe Freitas Villani 

/* Cabeçalhos necessários ‐> não precisa adicionar mais nada */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define TRUE 1

int main(int argc, char *argv[])
{
    int seconds;      /* para armazenar os segundos do alarme */
    char line[128];   /* para ler uma linha de entrada */
    char message[64]; /* para armazenar a mensagem do usuário */
    pid_t pid;        /* para obter o resultado de fork() */

    while (TRUE)
    {
        printf("Alarme> ");
        /* Se teclou CTRL + D ‐> fim de arquivo e termina */
        if (fgets(line, sizeof(line), stdin) == NULL)
            exit(EXIT_SUCCESS);
        /* Só ENTER ‐> repete a entrada */
        if (strlen(line) <= 1)
            continue;
        /* Senão, usar sccanf() para decompor a linha em número e mensagem
        o especificador 64[^\n] representa "aceite até 64 caracteres que
        não sejam '\n'". Retorna o número de argumentos analisados.*/
        if (sscanf(line, "%d %64[^\n]", &seconds, message) < 2)
            fprintf(stderr, "Comando inválido!\n");
        else
        {
            /* É APENAS ESTE ELSE QUE PRECISA SER TERMINADO */
            /* Execute fork() */
            pid = fork();
            /* SE o resultado de fork() for negativo, exibir uma mensagem
            de erro e terminar com falha*/
            if (pid < 0)
            {
                perror("Erro na execução de fork()");
            }
            /* SE o resultado de fork() for ZERO, escrever o código do
            PROCESSO‐FILHO assim:
            ‐ Dormir a quantidade de segundos especificada
            ‐ Exibir os segundos passados e a mensagem associada
            ‐ Terminar normalmente*/
            if (pid == 0)
            {
                sleep(seconds);
                printf("\33[2K\r");
                printf("(%d) ", seconds);
                printf("%s\n", message);
                printf("Alarme> ");
                exit(0);
            }
            /* SENÃO escrever o código do PROCESSO‐PAI asssim:
            ‐ Faça
            ‐ Aguarde o PID de um processo filho
            ‐ Se este PID tiver valor ‐1,
            ‐ Apresente uma mensagem indicando erro na espera
            de processo‐filho
            ‐ Termine o processo com falha
            Enquanto o PID obtido seja diferente de ZERO
            */
            else
            {
                do
                {
                    pid = waitpid(-1, NULL, WNOHANG);
                    if (pid == -1)
                    {
                        perror("Erro na espera do processo-filho");
                        exit(1);
                    }
                } while (pid != 0);
            }
        }
    }
}
