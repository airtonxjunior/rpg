package Rpg;

import java.util.Scanner; 

public class SistemaDeCombate {

    private static final Scanner scanner = new Scanner(System.in); 

    //metodo estático pois vai ser o mesmo para todos
    public static boolean batalhar(Personagem jogador, Inimigo inimigo) {
        System.out.println("\n--- COMBATE INICIADO ---");
        System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + ") vs " + inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + ")");
        System.out.println("--------------------------");

        //continua enquanto ambos estiverem vivos
        while (jogador.isVivo() && inimigo.isVivo()) {
            boolean turnoDoJogadorAcabou = false;
            
            //enquanto o turno do jogador não acabou e ele estiver vivo, tem acesso as opções
            while (!turnoDoJogadorAcabou && jogador.isVivo()) {
                
                System.out.println("\nTurno de " + jogador.getNome() + " (HP: " + jogador.getPontosVida() + ")");
                System.out.println("1. Atacar");
                System.out.println("2. Usar Item");
                System.out.println("3. Tentar Fugir");
                System.out.print("Escolha (1-3): ");
                
                int escolha = 1;
                try {
                    escolha = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    escolha = 1; //caso dê erro na escolha, ataca como padrão
                }

                switch (escolha) {
                    case 1: //caso escolha atacar
                        System.out.println(jogador.getNome() + " ataca!");
                        int rolagemJogador = Dado.rolar(20); //rola o dado
                        
                        //soma o valor do dado com o ataque
                        int ataqueTotalJogador = jogador.getAtaque() + rolagemJogador;
                        System.out.println("  (Rolagem do dado: " + rolagemJogador + " + Ataque: " + jogador.getAtaque() + " = " + ataqueTotalJogador + ")");
                        
                        inimigo.receberAtaque(ataqueTotalJogador);
                        turnoDoJogadorAcabou = true; //gasta o turno
                        break;

                    case 2: //usar item
                        //mostra o iventario
                        boolean usouItem = Jogo.mostrarInventario(jogador); 
                        
                        if (usouItem) {
                            System.out.println(jogador.getNome() + " usou um item e gasta o seu turno.");
                            turnoDoJogadorAcabou = true; //gasta o turno
                        } else {
                            System.out.println("(Ação cancelada. O seu turno continua.)");
                        }
                        break;
                        
                    case 3: //fugir
                        System.out.println(jogador.getNome() + " tenta fugir...");
                        int rolagemFuga = Dado.rolar(20);
                        if (rolagemFuga > 10) { //50% de chance, precisa de + 11
                            System.out.println("Você conseguiu escapar!");
                            return true;
                        } else {
                            System.out.println("A fuga falhou! O inimigo ataca!");
                            turnoDoJogadorAcabou = true; //gasta o turno
                        }
                        break;
                        
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } 


            //turno do inimigo, só ataca se ele sobreviveu
            if (inimigo.isVivo() && jogador.isVivo()) { //verifica se o jogador não morreu
                System.out.println("\nTurno de " + inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + ")");
                System.out.println(inimigo.getNome() + " ataca!");
                
                int rolagemInimigo = Dado.rolar(20); //rola o dado
                int ataqueTotalInimigo = inimigo.getAtaque() + rolagemInimigo;
                System.out.println("  (Rolagem do dado: " + rolagemInimigo + " + Ataque: " + inimigo.getAtaque() + " = " + ataqueTotalInimigo + ")");
                
                jogador.receberAtaque(ataqueTotalInimigo);
            }
        } //fim do while pq alguém morreu

        System.out.println("--- COMBATE ENCERRADO ---");

        //verifica o resultado
        if (jogador.isVivo()) {
            jogador.ganharXP(inimigo.getXPRecompensa());
            return true; //jogador venceu
        } else {
            return false; //jogador morreu
        }
    }
}

