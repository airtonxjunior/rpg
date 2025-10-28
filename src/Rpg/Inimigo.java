package Rpg;

public class Inimigo extends Personagem {
    
    private int xpRecompensa;

    //construtor do inimigo
    public Inimigo(String nome, int vida, int ataque, int defesa, int nivel, int xpRecompensa) {
        //chama o construtor da classe Personagem
        super(nome, vida, ataque, defesa, nivel);
        
        //define o atributo de xp do Inimigo
        this.xpRecompensa = xpRecompensa;
    }
    
    
    public int getXPRecompensa() {
        return this.xpRecompensa;
    }

}

