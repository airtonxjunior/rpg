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
            System.out.println("\nO jogo foi encerrado.");
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
                    loopPrincipalDoJogo(); //reinicia o loop do jogo
                
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
            System.out.println("2. Mago     (Vida: 80,  Def: 5,  Atk: 20)");
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
        System.out.println("\n\n--- Andar " + andarAtual + " --- O que fazer? ---");
        System.out.println("1. Explorar este andar");
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
                    System.out.println("\nVocê foge da masmorra... (Covarde!)");
                    jogador.receberAtaque(jogador.getPontosVidaMax() * 10);
                    break;
                case 5:
                    salvarJogo();
                    break;
                default:
                    System.out.println("\nOpção inválida.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInput inválido. Por favor, digite um número.");
        }
    }
    

    private static void salvarJogo() {
        System.out.println("\nSalvando o seu progresso...");
        jogadorSalvo = jogador.clone();
        andarSalvo = andarAtual;
        System.out.println("Jogo Salvo! (Andar " + andarSalvo + ")");
    }



    private static void explorar() {
        if (andarAtual == ANDAR_FINAL_CHEFE) {
            explorarAndar5_Chefe();
            return;
        }

        //cada andar tem seus próprios eventos
        switch (andarAtual) {
            case 1:
                explorarAndar1_Goblins();
                break;
            case 2:
                explorarAndar2_Cripta();
                break;
            case 3:
                explorarAndar3_Cavernas();
                break;
            case 4:
                explorarAndar4_Forja();
                break;
            default:
                //andares aleatórios depois do 5
                explorarAndarGenerico(); 
                break;
        }
        
        //jogador sobreviveu a exploração
        if (jogador.isVivo()) {
            andarAtual++;
            System.out.println("\n" + jogador.getNome() + " desce para o " + andarAtual + "º andar.");
        }
    }

    private static void explorarAndar1_Goblins() {
        System.out.println("\nVocê está no Salões dos Goblins. O lugar cheira mal.");
        System.out.println("Você vê duas passagens:");
        System.out.println("1. Investigar um acampamento goblin barulhento.");
        System.out.println("2. Seguir por um túnel lateral silencioso.");
        System.out.print("Escolha (1-2): ");
        String escolha = scanner.nextLine();
        System.out.println();

        if (escolha.equals("1")) {
            System.out.println("Você chuta a porta do acampamento! Eles estão surpresos!");
            encontrarInimigo("Goblin Batedor", 50, 8, 5, 1, 50, "Poção de Cura Pequena", 50);
        } else {
            System.out.println("Você segue pelo túnel silencioso...");
            int chance = random.nextInt(100);
            if (chance < 40) {
                System.out.println("...e encontra um baú de goblin mal trancado!");
                encontrarItem("Pedra de Defesa", "Aumenta permanentemente a Defesa em 1.", "BUFF_DEFESA:1", 1);
            } else if (chance < 70) {
                System.out.println("...e pisa numa armadilha de corda!");
                encontrarArmadilha(5, 10, "Uma rede cai e você se corta para sair!");
            } else {
                System.out.println("...o túnel dá numa passagem segura para o próximo andar.");
            }
        }
    }

    private static void explorarAndar2_Cripta() {
        System.out.println("\nVocê desce para uma Cripta Empoeirada. O ar está parado.");
        System.out.println("Você vê um sarcófago ornamentado no centro da sala.");
        System.out.println("1. Abrir o sarcófago (Pegar uma bolsa no chão).");
        System.out.println("2. Ignorar o sarcófago e procurar a saída.");
        System.out.print("Escolha (1-2): ");
        String escolha = scanner.nextLine();
        System.out.println();

        if (escolha.equals("1")) {
            System.out.println("Você força a tampa de pedra... O som ecoa.");
            int chance = random.nextInt(100);
            if (chance < 50) {
                System.out.println("...e um Esqueleto se levanta para atacar!");
                encontrarInimigo("Esqueleto Guardião", 80, 10, 8, 2, 75, "Pergaminho de Ataque", 30);
            } else {
                System.out.println("...e dentro você encontra um pergaminho antigo!");
                encontrarItem("Pergaminho de Ataque", "Aumenta permanentemente o Ataque em 2.", "BUFF_ATAQUE:2", 1);
            }
        } else {
            System.out.println("Você sabiamente ignora os mortos e procura a escada...");
            System.out.println("A passagem para o próximo andar está logo à frente.");
        }
    }

    private static void explorarAndar3_Cavernas() {
        System.out.println("\nAs escadas terminam numa Caverna de Cogumelos húmida.");
        System.out.println("Você vê um brilho estranho vindo de uma poça d'água.");
        System.out.println("1. Beber da poça brilhante.");
        System.out.println("2. Ignorar a poça e seguir rastros de Orcs.");
        System.out.print("Escolha (1-2): ");
        String escolha = scanner.nextLine();
        System.out.println();

        if (escolha.equals("1")) {
            System.out.println("Você bebe a água... Ela tem um gosto metálico.");
            int chance = random.nextInt(100);
            if (chance < 50) {
                System.out.println("Você se sente revigorado! (HP Máx +20!)");
                jogador.setPontosVidaMax(20); 
            } else {
                System.out.println("Você se sente enjoado...");
                jogador.receberAtaque(15 + jogador.getDefesa()); //dano ignora defesa
            }
        } else {
            System.out.println("Você segue os rastros e encontra um bando de Orcs!");
            encontrarInimigo("Orc Brutamontes", 120, 15, 10, 3, 120, "Poção de Cura Pequena", 70);
        }
    }

    private static void explorarAndar4_Forja() {
        System.out.println("\nVocê sente o calor da Forja Abandonada dos Anões.");
        System.out.println("Há uma bigorna antiga numa sala e uma porta de aço na outra.");
        System.out.println("1. Investigar a bigorna.");
        System.out.println("2. Tentar arrombar a porta de aço.");
        System.out.print("Escolha (1-2): ");
        String escolha = scanner.nextLine();
        System.out.println();

        if (escolha.equals("1")) {
            System.out.println("Você se aproxima da bigorna e encontra um item esquecido!");
            encontrarItem("Elixir de Vigor", "Cura 50 HP.", "CURA:50", 1);
        } else {
            System.out.println("Você força a porta de aço... e ela range abrindo!");
            System.out.println("Era uma armadilha! Gás venenoso enche a sala!");
            encontrarArmadilha(20, 30, "O gás queima seus pulmões!");
        }
    }
    
    private static void explorarAndar5_Chefe() {
        System.out.println("\n=======================================================");
        System.out.println("  Você entra numa câmara vasta! O ar está pesado.");
        System.out.println("  No centro, guardando o Amuleto de Yendor, está o...");
        System.out.println("  O TEMIDO REI GOBLIN!!");
        System.out.println("=======================================================");
        
        //cria o inimigo
        Inimigo chefe = new Inimigo("Rei Goblin", 150, 20, 13, 10, 1000); 
        
        try {
            chefe.getInventario().adicionar(new Item("Poção de Cura Grande", "Cura 100 HP.", "CURA:100", 2));
            chefe.getInventario().adicionar(new Item("Amuleto de Yendor (Falso?)", "Um amuleto brilhante.", "QUEST", 1));
        } catch (Exception e) {} 
        
        //faz a batalha
        boolean vitoria = SistemaDeCombate.batalhar(jogador, chefe); 

        if (vitoria) {
            jogoGanho = true; 
            
            //clona o inventario do inimigo
            Inventario loot = chefe.getInventario().clone();
            if (!loot.estaVazio()) {
                System.out.println("\nVocê saqueia o trono do Rei Goblin:");
                loot.listarItens();
                jogador.getInventario().adicionarItens(loot);
            }
        }
    }
    
    //método genérico caso o jogo continue após o andar 5
    private static void explorarAndarGenerico() {
        System.out.println("\nVocê explora um corredor...");
        int chance = random.nextInt(100); 
        if (chance < 60) {
            encontrarInimigo("Orc", 100, 12, 8, 2, 100, "Poção de Cura Pequena", 50);
        } else if (chance < 85) {
            encontrarItem("Poção de Cura Pequena", "Cura 25 HP.", "CURA:25", 1);
        } else {
            encontrarArmadilha(5, 15, "Uma armadilha de urso prende seu pé!");
        }
    }



    private static void encontrarInimigo(String nome, int vida, int atk, int def, int nivel, int xp, String itemDrop, int chanceDrop) {
        System.out.println("\nUm " + nome + " aparece!");
        
        Inimigo inimigo = new Inimigo(nome, vida, atk, def, nivel, xp);

        try {
            if (random.nextInt(100) < chanceDrop) { 
                inimigo.getInventario().adicionar(new Item(itemDrop, "Drop de " + nome, "CURA:25", 1));
            }
        } catch (Exception e) {} 

        //faz a batalha
        boolean vitoria = SistemaDeCombate.batalhar(jogador, inimigo); 

        if (vitoria) {
            System.out.println("\n" + jogador.getNome() + " venceu a batalha!");
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

    private static void encontrarItem(String nome, String desc, String efeito, int qtd) {
        try {
            System.out.println("\nVocê vê algo brilhando no chão");
            
            Item itemEncontrado = new Item(nome, desc, efeito, qtd);
            
            System.out.println("Você encontrou: " + itemEncontrado.getNome() + " (x" + qtd + ")");
            //adiciona no inventario do jogador
            jogador.getInventario().adicionar(itemEncontrado);
        } catch (Exception e) {
            System.err.println("Erro ao criar item encontrado: " + e.getMessage());
        }
    }


    private static void encontrarArmadilha(int danoMin, int danoMax, String texto) {
        System.out.println("\n" + texto);
        int danoArmadilha = random.nextInt(danoMax - danoMin + 1) + danoMin; 
        
        System.out.println("Você é atingido e recebe " + danoArmadilha + " de dano");
        
        //dano q ignora defesa:
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
            System.out.println();
            return false;//não gasta turno
        }

        Item itemParaUsar = inventario.getItem(nomeItem);
        
        if (itemParaUsar == null) {
            System.out.println("\nVocê não tem esse item");
            return false; //não gastou turno
        }

        //lógica de efeito
        String efeito = itemParaUsar.getEfeito();
        System.out.println(); 
        
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
        System.out.println("\n\n--- STATUS DO JOGADOR ---");
        System.out.println(jogador.toString()); 
        System.out.println("--------------------------\n");
    }
}

