package Rpg;

import java.util.Scanner; // (Importa se for usar a fuga)

public class SistemaDeCombate {

    private static Scanner scanner = new Scanner(System.in); // Para a Fuga

    /**
     * Método estático que gere uma batalha completa.
     * @return true se o jogador venceu, false se o jogador foi derrotado.
     */
    public static boolean batalhar(Personagem jogador, Inimigo inimigo) {
        System.out.println("\n--- COMBATE INICIADO ---");
        System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + ") vs " + inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + ")");
        System.out.println("--------------------------");

        // Loop da batalha: continua enquanto ambos estiverem vivos
        while (jogador.isVivo() && inimigo.isVivo()) {
            
            // --- Turno do Jogador (com opção de Fuga) ---
            System.out.println("\nTurno de " + jogador.getNome() + " (HP: " + jogador.getPontosVida() + ")");
            System.out.println("1. Atacar");
            System.out.println("2. Tentar Fugir");
            System.out.print("Escolha (1-2): ");
            
            int escolha = 1;
            try {
                escolha = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                escolha = 1; // Padrão é atacar
            }

            if (escolha == 2) {
                // (Requisito: "fugir (com chance de falha, também rolando dados)")
                System.out.println(jogador.getNome() + " tenta fugir...");
                int rolagemFuga = Dado.rolar(20);
                if (rolagemFuga > 10) { // 50% de chance (precisa de 11+)
                    System.out.println("Você conseguiu escapar!");
                    return true; // Retorna "vitória" (sobreviveu)
                } else {
                    System.out.println("A fuga falhou! O inimigo ataca!");
                    // Pula para o turno do inimigo
                }
            } else {
                // --- Ataque do Jogador ---
                System.out.println(jogador.getNome() + " ataca!");
                int rolagemJogador = Dado.rolar(20); // Rola um d20
                int ataqueTotalJogador = jogador.getAtaque() + rolagemJogador;
                System.out.println("  (Rolagem D20: " + rolagemJogador + " + Ataque: " + jogador.getAtaque() + " = " + ataqueTotalJogador + ")");
                
                inimigo.receberAtaque(ataqueTotalJogador);
            }

            // --- Turno do Inimigo ---
            // O inimigo só ataca se ele sobreviveu ao ataque do jogador
            if (inimigo.isVivo()) {
                System.out.println("\nTurno de " + inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + ")");
                System.out.println(inimigo.getNome() + " ataca!");
                
                int rolagemInimigo = Dado.rolar(20); // Inimigo também rola d20
                int ataqueTotalInimigo = inimigo.getAtaque() + rolagemInimigo;
                System.out.println("  (Rolagem D20: " + rolagemInimigo + " + Ataque: " + inimigo.getAtaque() + " = " + ataqueTotalInimigo + ")");
                
                jogador.receberAtaque(ataqueTotalInimigo);
            }
        } // Fim do while (alguém morreu)

        System.out.println("--- COMBATE ENCERRADO ---");

        // Verifica o resultado
        if (jogador.isVivo()) {
            // --- CHAMADA PARA O MÉTODO DE XP (NOVO) ---
            jogador.ganharXP(inimigo.getXPRecompensa());
            return true; // Jogador venceu
        } else {
            return false; // Jogador foi derrotado
        }
    }
}

