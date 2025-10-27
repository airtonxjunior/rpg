package Rpg;

public class Inimigo extends Personagem {

    // --- ATRIBUTO NOVO ---
    private int xpRecompensa;

    /**
     * Construtor do Inimigo (Atualizado com XP)
     */
    public Inimigo(String nome, int vida, int ataque, int defesa, int nivel, int xpRecompensa) {
        // 1. Chama o construtor da classe-mãe (Personagem)
        super(nome, vida, ataque, defesa, nivel);
        
        // 2. Define o atributo específico do Inimigo
        this.xpRecompensa = xpRecompensa;
    }
    
    // --- GETTER NOVO ---
    public int getXPRecompensa() {
        return this.xpRecompensa;
    }
    
    // Não precisa de construtor de cópia ou clone se não formos
    // copiar inimigos (o Personagem.clone() já funciona).
}

