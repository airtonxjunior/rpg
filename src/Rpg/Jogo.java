package Rpg;

import java.util.Scanner;
import java.util.Random;
import Rpg.Personagem;
import Rpg.Guerreiro;
import Rpg.Mago;
import Rpg.Arqueiro;
import Rpg.Inimigo;
import Rpg.Item;
import Rpg.SistemaDeCombate;
import Rpg.Inventario;

public class Jogo {

    private static Personagem jogador;
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    // --- NOVOS ATRIBUTOS PARA O FINAL ---
    private static int andarAtual = 1;
    private static final int ANDAR_FINAL_CHEFE = 5; // O Chefe está no 5º andar
    private static boolean jogoGanho = false;

    /**
     * Ponto de Entrada Principal
     */
    public static void iniciar() {
        try {
            escolherClasse();
            darItensIniciais();

            System.out.println("\n--- A AVENTURA COMEÇA ---");
            System.out.println(jogador.getNome() + ", o " + jogador.getClass().getSimpleName() + ", entra na Masmorra das Sombras...");
            System.out.println("A sua missão: encontrar o Amuleto de Yendor no " + ANDAR_FINAL_CHEFE + "º andar.");
            
            // --- LOOP PRINCIPAL ATUALIZADO ---
            // Continua ENQUANTO o jogador estiver vivo E não tiver ganho.
            while (jogador.isVivo() && !jogoGanho) {
                mostrarMenuPrincipal();
            }

            // --- FIM DE JOGO (ATUALIZADO) ---
            System.out.println("\n--- FIM DE JOGO ---");
            if (jogoGanho) {
                System.out.println("*****************************************************************");
                System.out.println("  PARABÉNS, " + jogador.getNome() + "! Você derrotou o Chefe Final!");
                System.out.println("  Você recuperou o Amuleto de Yendor e escapou da masmorra!");
                System.out.println("*****************************************************************");
            } else {
                System.out.println(jogador.getNome() + " foi derrotado. A masmorra reclama mais uma alma...");
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado no jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("O jogo foi encerrado.");
        }
    }

    // ... (escolherClasse() e darItensIniciais() ficam iguais) ...
    // (Vou re-colar darItensIniciais para garantir que usa o "adicionar" correto)
    
    private static void escolherClasse() {
        System.out.println("Bem-vindo, aventureiro!");
        System.out.print("Qual é o seu nome? ");
        String nome = scanner.nextLine();

        int escolha = 0;
        while (escolha < 1 || escolha > 3) {
            System.out.println("\nEscolha a sua classe:");
            System.out.println("1. Guerreiro (Vida: 150, Def: 15, Atk: 10)");
            System.out.println("2. Mago      (Vida: 80,  Def: 5,  Atk: 20)");
            System.out.println("3. Arqueiro  (Vida: 100, Def: 10, Atk: 15)");
            System.out.print("Digite 1, 2 ou 3: ");

            try {
                escolha = Integer.parseInt(scanner.nextLine());
                switch (escolha) {
                    case 1:
                        jogador = new Guerreiro(nome);
                        break;
                    case 2:
                        jogador = new Mago(nome);
                        break;
                    case 3:
                        jogador = new Arqueiro(nome);
                        break;
                    default:
                        System.out.println("Escolha inválida. Tente novamente.");
                        escolha = 0;
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input inválido. Por favor, digite um número.");
                escolha = 0;
            }
        }
    }

    private static void darItensIniciais() {
        try {
            // (Note que o 'adicionar' do Inventario.java já clona o item)
            jogador.getInventario().adicionar(new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 2));
            System.out.println("\nVocê recebeu 2 Poções de Cura Pequenas!");
        } catch (Exception e) {
            System.err.println("Erro ao criar item inicial: " + e.getMessage());
        }
    }


    /**
     * Menu Principal (Atualizado para mostrar o Andar)
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Andar " + andarAtual + " --- O que fazer? ---");
        System.out.println("1. Explorar o próximo corredor");
        System.out.println("2. Ver Inventário / Usar Item");
        System.out.println("3. Ver Status do Personagem");
        System.out.println("4. Sair do Jogo (Desistir)");
        System.out.print("Escolha (1-4): ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            switch (escolha) {
                case 1:
                    explorar(); 
                    break;
                case 2:
                    mostrarInventario(); 
                    break;
                case 3:
                    verStatus(); 
                    break;
                case 4:
                    System.out.println("Você foge da masmorra... (Covarde!)");
                    jogador.receberAtaque(jogador.getPontosVidaMax() * 10); // Mata o jogador
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input inválido. Por favor, digite um número.");
        }
    }

    /**
     * Explorar (Atualizado com Lógica de Chefe)
     */
    private static void explorar() {
        // (Requisito: "Tomar decisões na estória (escolher um caminho...)")
        System.out.println("\nVocê avança... O corredor se divide em dois.");
        System.out.println("1. Caminho da Esquerda (escuro e húmido)");
        System.out.println("2. Caminho da Direita (com ossos espalhados)");
        System.out.print("Escolha (1-2): ");

        try {
            Integer.parseInt(scanner.nextLine()); // A escolha não importa, é só pela história
        } catch (Exception e) {}
        
        System.out.println("Você segue pelo caminho escolhido...");

        // --- LÓGICA DO CHEFE ---
        if (andarAtual == ANDAR_FINAL_CHEFE) {
            encontrarChefe();
            return; // Interrompe a exploração normal
        }

        // --- Exploração Normal ---
        int chance = random.nextInt(100); 

        if (chance < 40) { // 40%
            encontrarInimigo();
        } else if (chance < 70) { // 30%
            encontrarItem();
        } else if (chance < 85) { // 15%
            encontrarArmadilha();
        } else { // 15%
            System.out.println("O corredor está silencioso e vazio... por enquanto.");
        }
        
        // Se o jogador ainda está vivo, ele avança para o próximo andar
        if (jogador.isVivo()) {
            andarAtual++;
            System.out.println(jogador.getNome() + " desce para o " + andarAtual + "º andar.");
        }
    }

    /**
     * MÉTODO NOVO: A Batalha Final
     */
    private static void encontrarChefe() {
        System.out.println("\n=======================================================");
        System.out.println("  Você entra numa câmara vasta! O ar está pesado.");
        System.out.println("  No centro, guardando o Amuleto de Yendor, está o...");
        System.out.println("  REI GOBLIN MONTADO NUM WARG GIGANTE!");
        System.out.println("=======================================================");

        // (nome, vida, ataque, defesa, nivel, xpRecompensa)
        Inimigo chefe = new Inimigo("Rei Goblin", 300, 20, 15, 10, 1000);
        
        try {
            chefe.getInventario().adicionar(new Item("Poção de Cura Grande", "Cura 100 HP.", "CURA:100", 2));
        } catch (Exception e) {} 

        boolean vitoria = SistemaDeCombate.batalhar(jogador, chefe);

        if (vitoria) {
            // --- CONDIÇÃO DE VITÓRIA ATINGIDA ---
            jogoGanho = true; 
            
            // (O loot do chefe)
            Inventario loot = chefe.getInventario().clone();
            if (!loot.estaVazio()) {
                System.out.println("Você saqueia o trono do Rei Goblin:");
                loot.listarItens();
                jogador.getInventario().adicionarItens(loot);
            }
        }
        // Se perder, o loop 'while' principal vai parar.
    }


    /**
     * Encontrar Inimigo (Atualizado com XP)
     */
    private static void encontrarInimigo() {
        System.out.println("Um barulho ecoa! Um inimigo aparece!");
        
        Inimigo inimigo;
        if (random.nextBoolean()) { 
            // (nome, vida, ataque, defesa, nivel, xpRecompensa)
            inimigo = new Inimigo("Goblin", 50, 8, 5, 1, 50);
        } else {
            inimigo = new Inimigo("Orc", 100, 12, 8, 2, 100);
        }

        try {
            if (random.nextInt(100) < 50) { // 50% chance de ter poção
                inimigo.getInventario().adicionar(new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 1));
            }
        } catch (Exception e) {} 

        boolean vitoria = SistemaDeCombate.batalhar(jogador, inimigo);

        if (vitoria) {
            System.out.println(jogador.getNome() + " venceu a batalha!");
            Inventario loot = inimigo.getInventario().clone();
            if (!loot.estaVazio()) {
                System.out.println("Você saqueia os restos do " + inimigo.getNome() + ":");
                loot.listarItens();
                jogador.getInventario().adicionarItens(loot);
            }
        }
    }

    /**
     * Encontrar Item (Atualizado com Item de Defesa)
     */
    private static void encontrarItem() {
        try {
            System.out.println("Você vê algo brilhando no chão! (Uma bolsa...)");
            
            Item itemEncontrado;
            // 70% chance de ser poção, 30% de ser item de defesa
            if (random.nextInt(100) < 70) {
                itemEncontrado = new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 1);
            } else {
                // --- ITEM DE DEFESA NOVO ---
                itemEncontrado = new Item("Pedra de Defesa", "Aumenta permanentemente a Defesa em 1.", "BUFF_DEFESA:1", 1);
            }
            
            System.out.println("Você encontrou: " + itemEncontrado.getNome() + " (x1)");
            jogador.getInventario().adicionar(itemEncontrado);
        } catch (Exception e) {
            System.err.println("Erro ao criar item encontrado: " + e.getMessage());
        }
    }

    /**
     * Encontrar Armadilha (Lógica de dano corrigida)
     */
    private static void encontrarArmadilha() {
        System.out.println("Você pisa numa placa de pressão! *CLICK!*");
        System.out.println("Dardos venenosos saem da parede!");
        int danoArmadilha = Dado.rolar(10) + 5; // Dano de 6 a 15
        
        System.out.println("Você é atingido e recebe " + danoArmadilha + " de dano (ignora defesa)!");
        
        // Armadilhas ignoram a defesa.
        // O método 'receberAtaque' NÃO ignora a defesa.
        // Para aplicar dano puro, temos de fazer isto:
        int danoPuro = danoArmadilha + jogador.getDefesa();
        jogador.receberAtaque(danoPuro); 
        // A lógica será: (danoPuro - defesa) = (danoArmadilha + defesa) - defesa = danoArmadilha
    }

    /**
     * Mostrar Inventário (Atualizado com Item de Defesa)
     */
    private static void mostrarInventario() {
        Inventario inventario = jogador.getInventario();
        
        if (inventario.estaVazio()) {
            System.out.println("\nSeu inventário está vazio.");
            return;
        }

        System.out.println("\n--- INVENTÁRIO ---");
        inventario.listarItens();
        System.out.println("--------------------");
        System.out.print("Digite o nome do item que quer USAR (ou 'cancelar'): ");
        String nomeItem = scanner.nextLine(); // O bug de acento foi corrigido em Item.java

        if (nomeItem.equalsIgnoreCase("cancelar")) {
            return;
        }

        Item itemParaUsar = inventario.getItem(nomeItem);
        
        if (itemParaUsar == null) {
            System.out.println("Você não tem esse item (Lembre-se: não precisa de acentos).");
            return;
        }

        // --- Lógica de Efeito (Atualizada) ---
        String efeito = itemParaUsar.getEfeito();
        
        try {
            if (efeito.startsWith("CURA:")) {
                int valorCura = Integer.parseInt(efeito.split(":")[1]);
                jogador.curar(valorCura); 
                inventario.remover(nomeItem, 1); // Remove 1 unidade
            
            // --- LÓGICA DO ITEM DE DEFESA ---
            } else if (efeito.startsWith("BUFF_DEFESA:")) {
                int valorDefesa = Integer.parseInt(efeito.split(":")[1]);
                jogador.setDefesa(valorDefesa); // Usa o setter de Personagem (que SOMA)
                System.out.println(jogador.getNome() + " sente sua pele endurecer! (+ " + valorDefesa + " Defesa)");
                inventario.remover(nomeItem, 1);

            } else {
                System.out.println("Você não pode usar este item agora.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao usar o item: " + e.getMessage());
        }
    }

    /**
     * Ver Status (Usa o toString atualizado)
     */
    private static void verStatus() {
        System.out.println("\n--- STATUS DO JOGADOR ---");
        System.out.println(jogador.toString()); // (Agora mostra o XP)
        System.out.println("--------------------------");
    }
}

