package Rpg;

import java.util.Scanner; 

public class SistemaDeCombate {

    //metodo estático pois vai ser o mesmo para todos
    public static boolean batalhar(Personagem jogador, Inimigo inimigo) {
        System.out.println("\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! COMBATE INICIADO !!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + "/" + jogador.getPontosVidaMax() + ")");
        System.out.println("      VS");
        System.out.println(inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + "/" + inimigo.getPontosVidaMax() + ")");
        System.out.println("--------------------------\n"); 

        int turno = 1;

        //continua enquanto ambos estiverem vivos
        while (jogador.isVivo() && inimigo.isVivo()) {

            System.out.println("\n========= Turno " + turno + " =========");
            boolean turnoDoJogadorAcabou = false;

            //enquanto o turno do jogador não acabou e ele estiver vivo, tem acesso as opções
            while (!turnoDoJogadorAcabou && jogador.isVivo()) {

                System.out.println("\n--- Vez de " + jogador.getNome() + " ---");
                System.out.println("HP Atual: " + jogador.getPontosVida() + "/" + jogador.getPontosVidaMax());
                System.out.println("\nOpções:"); // Adicionado espaço
                System.out.println("1. Atacar");
                System.out.println("2. Usar Item");
                System.out.println("3. Tentar Fugir");
                System.out.print("Escolha (1-3): ");

                int escolha = 1;
                try {
                    Scanner scannerBatalha = new Scanner(System.in);
                    String input = scannerBatalha.nextLine();
                    if (!input.trim().isEmpty()) {
                         escolha = Integer.parseInt(input);
                    } else {
                        System.out.println("\nInput vazio, Atacando por padrão."); 
                        escolha = 1; //caso dê erro na escolha, ataca como padrão
                    }

                } catch (Exception e) {
                     System.out.println("\nInput inválido, Atacando por padrão.");
                    escolha = 1; //caso dê erro na escolha, ataca como padrão
                }
                System.out.println(); 

                //switch para as opções
                switch (escolha) {
                    case 1: //caso escolha atacar
                        System.out.println(">>> " + jogador.getNome() + " ataca!");
                        int rolagemJogador = Dado.rolar(20); //rola o dado

                        //soma o valor do dado com o ataque
                        int ataqueTotalJogador = jogador.getAtaque() + rolagemJogador;
                        System.out.println("   (Rolagem do dado: " + rolagemJogador + " + Ataque Base: " + jogador.getAtaque() + " = Força Total: " + ataqueTotalJogador + ")");
                        System.out.println(); 

                        inimigo.receberAtaque(ataqueTotalJogador);
                        turnoDoJogadorAcabou = true; //gasta o turno
                        break;

                    case 2: //usar item
                        System.out.println("\n--- Abrindo Inventário ---"); 

                        //mostra o iventario
                        boolean usouItem = Jogo.mostrarInventario(jogador);
                        System.out.println(); 

                        if (usouItem) {
                            System.out.println("(Turno gasto usando o item.)"); 
                            turnoDoJogadorAcabou = true; //gasta o turno
                        } else {
                            System.out.println("(Nenhum item usado. O seu turno continua.)");
                        }
                        System.out.println("--- Fechando Inventário ---");
                        break;

                    case 3: //fugir
                        System.out.println(">>> " + jogador.getNome() + " tenta fugir..."); 
                        int rolagemFuga = Dado.rolar(20);
                        System.out.println("   (Rolagem do dado para Fuga: " + rolagemFuga + ")");
                        System.out.println();
                        if (rolagemFuga > 10) { //50% de chance, precisa de mais de 10
                            System.out.println("+++ FUGA BEM SUCEDIDA! +++");
                            System.out.println("Você conseguiu escapar do combate!");
                            System.out.println("--------------------------\n");
                            return false; //fugiu com sucesso, combate termina
                        } else {
                            System.out.println("A fuga falhou! Você perdeu a chance e o inimigo se prepara para atacar!"); 
                            turnoDoJogadorAcabou = true; //gasta o turno
                        }
                        break;

                    default:
                        System.out.println("\nOpção inválida. Você hesita e perde o turno.");
                        turnoDoJogadorAcabou = true; //gasta o turno por opcao invalida
                        break;
                }
            }


            //turno do inimigo, só ataca se ele sobreviveu
            if (inimigo.isVivo() && jogador.isVivo()) { 
                System.out.println("\n--- Vez de " + inimigo.getNome() + " ---");
                System.out.println("HP Atual: " + inimigo.getPontosVida() + "/" + inimigo.getPontosVidaMax());
                System.out.println("\n>>> " + inimigo.getNome() + " ataca!"); 

                int rolagemInimigo = Dado.rolar(20); //rola o dado
                int ataqueTotalInimigo = inimigo.getAtaque() + rolagemInimigo;
                System.out.println("   (Rolagem do dado: " + rolagemInimigo + " + Ataque Base: " + inimigo.getAtaque() + " = Força Total: " + ataqueTotalInimigo + ")");
                 System.out.println(); 

                jogador.receberAtaque(ataqueTotalInimigo);
            }

            System.out.println("\n============================");
            turno++;
        } //fim do while pq alguém morreu


        System.out.println("\n--- COMBATE ENCERRADO ---");

        //verifica o resultado
        if (jogador.isVivo()) {
            System.out.println("\n+++ VITÓRIA +++");
            System.out.println(jogador.getNome() + " sobreviveu!");
            jogador.ganharXP(inimigo.getXPRecompensa());
            System.out.println("--------------------------\n"); 
            return true; //inimigo morreu
        } else {
            System.out.println("\n--- DERROTA ---");
            System.out.println("--------------------------\n"); 
            return false; //jogador morreu
        }
    }
}

