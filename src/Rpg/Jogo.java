package Rpg;

import java.util.Scanner;
import java.util.Random;

public class Jogo {

    private static Personagem jogador;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static int andarAtual = 1;
    private static final int ANDAR_FINAL_CHEFE = 5; 
    private static boolean jogoGanho = false;
    
    //saves
    private static Personagem jogadorSalvo = null;
    private static int andarSalvo = 1; 

    
    public static void iniciar() {
        try {
            //chama as funcoes dessa classe
            escolherClasse();
            darItensIniciais();

            System.out.println("\n--- A AVENTURA COMEÇA ---");
            System.out.println(jogador.getNome() + ", o " + jogador.getClass().getSimpleName() + ", entra na Masmorra das Sombras...");
            System.out.println("A sua missão: encontrar o Amuleto de Yendor no " + ANDAR_FINAL_CHEFE + "º andar.");
            
            loopPrincipalDoJogo();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado no jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("O jogo foi encerrado.");
        }
    }
    
    private static void loopPrincipalDoJogo() {
        
        while (jogador.isVivo() && !jogoGanho) {
            mostrarMenuPrincipal();
        }
        
        if (jogoGanho) {
            System.out.println("\n--- FIM DE JOGO ---");
            System.out.println("*****************************************************************");
            System.out.println("  PARABÉNS, " + jogador.getNome() + "! Você derrotou o Chefe Final!");
            System.out.println("  Você recuperou o Amuleto de Yendor e escapou da masmorra!");
            System.out.println("*****************************************************************");
        
        } else if (!jogador.isVivo()) {
            //jogador morreu
            System.out.println("\n" + jogador.getNome() + " foi derrotado!");
            
            if (jogadorSalvo != null) {
                System.out.print("Carregar o último Save? (S/N): ");
                String escolha = scanner.nextLine();
                
                if (escolha.equalsIgnoreCase("S")) {
                    System.out.println("\n...uma luz o envolve e o retorna ao seu último save point!");

                    jogador = jogadorSalvo.clone(); //clona o save de volta
                    andarAtual = andarSalvo; 
                    
                    System.out.println("Você acorda... pronto para tentar novamente...");
                    loopPrincipalDoJogo(); 
                
                } else {
                    System.out.println(jogador.getNome() + " foi derrotado. A masmorra reclama mais uma alma...");
                }
            } else {
                System.out.println(jogador.getNome() + " foi derrotado. (Não há save para carregar)");
            }
        }
    }
    
    private static void escolherClasse() {
        System.out.println("Bem-vindo, aventureiro!");
        System.out.print("Qual é o seu nome? ");
        
        //recebe o nome digitado
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
            jogador.getInventario().adicionar(new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 2));
            jogador.getInventario().adicionar(new Item("Pergaminho de Ataque", "Aumenta o Ataque em 5.", "BUFF_ATAQUE:5", 1));
            System.out.println("\nVocê recebeu 2 Poções de Cura Pequenas e 1 Pergaminho de Ataque!");
        } catch (Exception e) {
            System.err.println("Erro ao criar item inicial: " + e.getMessage());
        }
    }


    //menu de escolhas
    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Andar " + andarAtual + " --- O que fazer? ---");
        System.out.println("1. Explorar o próximo corredor");
        System.out.println("2. Ver Inventário / Usar Item");
        System.out.println("3. Ver Status do Personagem");
        System.out.println("4. Sair do Jogo (Desistir)");
        System.out.println("5. Salvar Jogo"); 
        System.out.print("Escolha (1-5): ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            switch (escolha) {
                case 1:
                    explorar(); 
                    break;
                case 2:
                    mostrarInventario(jogador); 
                    break;
                case 3:
                    verStatus(); 
                    break;
                case 4:
                    System.out.println("Você foge da masmorra... (Covarde!)");
                    jogador.receberAtaque(jogador.getPontosVidaMax() * 10);
                    break;
                case 5:
                    salvarJogo();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input inválido. Por favor, digite um número.");
        }
    }
    

    private static void salvarJogo() {
        System.out.println("\nSalvando o seu progresso...");
        jogadorSalvo = jogador.clone();
        andarSalvo = andarAtual;
        System.out.println("Jogo Salvo! (Andar " + andarSalvo + ")");
    }

    private static void explorar() {
        System.out.println("\nVocê avança... O corredor se divide em dois.");
        System.out.println("1. Caminho da Esquerda (escuro e húmido)");
        System.out.println("2. Caminho da Direita (com ossos espalhados)");
        System.out.print("Escolha (1-2): ");

        try {
            Integer.parseInt(scanner.nextLine()); 
        } catch (Exception e) {}
        
        System.out.println("Você segue pelo caminho escolhido...");

        if (andarAtual == ANDAR_FINAL_CHEFE) {
            encontrarChefe();
            return; 
        }

        int chance = random.nextInt(100); 

        if (chance < 40) { //40%
            encontrarInimigo();
        } else if (chance < 70) { //30%
            encontrarItem();
        } else if (chance < 85) { //15%
            encontrarArmadilha();
        } else { //15%
            System.out.println("O corredor está silencioso e vazio... por enquanto.");
        }
        
        //jogador sobreviveu a exploração
        if (jogador.isVivo()) {
            andarAtual++;
            System.out.println(jogador.getNome() + " desce para o " + andarAtual + "º andar.");
        }
    }

    private static void encontrarChefe() {
        System.out.println("\n=======================================================");
        System.out.println("  Você entra numa câmara vasta! O ar está pesado.");
        System.out.println("  No centro, guardando o Amuleto de Yendor, está o...");
        System.out.println("  REI GOBLIN MONTADO NUM WARG GIGANTE!");
        System.out.println("=======================================================");
        
        //cria o inimigo
        Inimigo chefe = new Inimigo("Rei Goblin", 300, 20, 15, 10, 1000);
        
        try {
            chefe.getInventario().adicionar(new Item("Poção de Cura Grande", "Cura 100 HP.", "CURA:100", 2));
        } catch (Exception e) {} 
        
        //faz a batalha
        boolean vitoria = SistemaDeCombate.batalhar(jogador, chefe); // Mantido "SistemaDeCombate"

        if (vitoria) {
            jogoGanho = true; 
            
            //clona o inventario do inimigo
            Inventario loot = chefe.getInventario().clone();
            if (!loot.estaVazio()) {
                System.out.println("Você saqueia o trono do Rei Goblin:");
                loot.listarItens();
                jogador.getInventario().adicionarItens(loot);
            }
        }
    }


    private static void encontrarInimigo() {
        System.out.println("Um barulho ecoa! Um inimigo aparece!");
        
        Inimigo inimigo;
        if (random.nextBoolean()) { 
            inimigo = new Inimigo("Goblin", 50, 8, 5, 1, 50);
        } else {
            inimigo = new Inimigo("Orc", 100, 12, 8, 2, 100);
        }

        try {
            if (random.nextInt(100) < 50) { 
                inimigo.getInventario().adicionar(new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 1));
            }
        } catch (Exception e) {} 

        //faz a batalha
        boolean vitoria = SistemaDeCombate.batalhar(jogador, inimigo); 

        if (vitoria) {
            System.out.println(jogador.getNome() + " venceu a batalha!");
            //clona o inventario do inimigo para a variavel
            Inventario loot = inimigo.getInventario().clone();
            //se nao tiver vazio, adiciona no inventario do jogador
            if (!loot.estaVazio()) {
                System.out.println("Você saqueia os restos do " + inimigo.getNome() + ":");
                loot.listarItens();
                jogador.getInventario().adicionarItens(loot);
            }
        }
    }

    private static void encontrarItem() {
        try {
            System.out.println("Você vê algo brilhando no chão! Uma bolsa)");
            
            Item itemEncontrado;
            int chanceItem = random.nextInt(100);
            if (chanceItem < 60) {
                itemEncontrado = new Item("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 1);
            } else if (chanceItem < 85) {
                itemEncontrado = new Item("Pedra de Defesa", "Aumenta permanentemente a Defesa em 1.", "BUFF_DEFESA:1", 1);
            } else {
                itemEncontrado = new Item("Pergaminho de Ataque", "Aumenta permanentemente o Ataque em 2.", "BUFF_ATAQUE:2", 1);
            }
            
            System.out.println("Você encontrou: " + itemEncontrado.getNome() + " (x1)");
            //adiciona no inventario do jogador
            jogador.getInventario().adicionar(itemEncontrado);
        } catch (Exception e) {
            System.err.println("Erro ao criar item encontrado: " + e.getMessage());
        }
    }

    private static void encontrarArmadilha() {
        System.out.println("Você pisa numa placa de pressão! *CLICK!*");
        System.out.println("Dardos venenosos saem da parede!");
        int danoArmadilha = Dado.rolar(10); 
        
        System.out.println("Você é atingido e recebe " + danoArmadilha + " de dano!");
        
        //dano ignora defesa:
        int danoPuro = danoArmadilha + jogador.getDefesa();
        jogador.receberAtaque(danoPuro); 
    }


    public static boolean mostrarInventario(Personagem jogador) {
        Inventario inventario = jogador.getInventario();
        
        if (inventario.estaVazio()) {
            System.out.println("\nSeu inventário está vazio.");
            return false; //não gasta turno
        }

        System.out.println("\n--- INVENTÁRIO ---");
        inventario.listarItens();
        System.out.println("--------------------");
        System.out.print("Digite o nome do item que quer USAR (ou 'cancelar'): ");
        String nomeItem = scanner.nextLine(); 

        if (nomeItem.equalsIgnoreCase("cancelar") || nomeItem.trim().isEmpty()) {
            return false;//não gasta turno
        }

        Item itemParaUsar = inventario.getItem(nomeItem);
        
        if (itemParaUsar == null) {
            System.out.println("Você não tem esse item");
            return false; //não gastou turno
        }

        //lógica de efeito
        String efeito = itemParaUsar.getEfeito();
        
        try {
            if (efeito.startsWith("CURA:")) {
                int valorCura = Integer.parseInt(efeito.split(":")[1]);
                jogador.curar(valorCura); 
                inventario.remover(itemParaUsar.getNome(), 1); //remove 1
                return true; //gastou um turno
            
            } else if (efeito.startsWith("BUFF_DEFESA:")) {
                int valorDefesa = Integer.parseInt(efeito.split(":")[1]);
                jogador.setDefesa(valorDefesa); //aumenta a defesa
                System.out.println(jogador.getNome() + " sente sua pele endurecer! (+ " + valorDefesa + " Defesa)");
                inventario.remover(itemParaUsar.getNome(), 1); //remove 1
                return true; //gastou um turno

            } else if (efeito.startsWith("BUFF_ATAQUE:")) {
                int valorAtaque = Integer.parseInt(efeito.split(":")[1]);
                jogador.setAtaque(valorAtaque); //aumenta o ataque
                System.out.println(jogador.getNome() + " sente seus músculos arderem! (+ " + valorAtaque + " Ataque)");
                inventario.remover(itemParaUsar.getNome(), 1); //remove 1
                return true; //gastou um turno

            } else {
                System.out.println("Você não pode usar este item agora.");
                return false; //não gastou turno
            }
        } catch (Exception e) {
            System.out.println("Erro ao usar o item: " + e.getMessage());
            return false; //não gastou turno
        }
    }

    //usa o toString de personagem
    private static void verStatus() {
        System.out.println("\n--- STATUS DO JOGADOR ---");
        System.out.println(jogador.toString()); 
        System.out.println("--------------------------");
    }
}

