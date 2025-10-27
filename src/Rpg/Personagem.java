package Rpg;

public abstract class Personagem implements Cloneable {
    private String nome;
    private int pontosVida;
    private int pontosVidaMax;
    private int ataque;
    private int defesa;
    private int nivel;
    private Inventario inventario;

    // --- NOVOS ATRIBUTOS PARA NÍVEL ---
    private int experiencia;
    private int xpParaProximoNivel;

    public Personagem(String nome, int vida, int ataque, int defesa, int nivel) {
        this.nome = nome;
        this.pontosVida = vida;
        this.pontosVidaMax = vida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = new Inventario();

        // --- INICIALIZAÇÃO DO NÍVEL ---
        this.experiencia = 0;
        this.xpParaProximoNivel = 100 * nivel; // (Ex: Nível 1 precisa de 100 XP)
    }

    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVida = original.pontosVida;
        this.pontosVidaMax = original.pontosVidaMax;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        this.inventario = original.inventario.clone();

        // --- COPIA DOS ATRIBUTOS DE NÍVEL ---
        this.experiencia = original.experiencia;
        this.xpParaProximoNivel = original.xpParaProximoNivel;
    }

    // --- Getters ---
    public String getNome() { return this.nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getAtaque() { return this.ataque; }
    public int getDefesa() { return this.defesa; }
    public int getNivel() { return this.nivel; }
    public Inventario getInventario() { return this.inventario; }
    public int getPontosVidaMax() { return this.pontosVidaMax; }
    // Getter para o XP (útil para o toString)
    public int getExperiencia() { return this.experiencia; }
    public int getXPParaProximoNivel() { return this.xpParaProximoNivel; }


    // --- Setters (para buffs) ---
    public void setDefesa(int defesa) { this.defesa += defesa; }
    public void setAtaque(int ataque) { this.ataque += ataque; }
    // Este setNivel não deve ser público, o nível sobe via ganharXP()
    // public void setNivel(int nivel){ this.nivel += nivel; }

    // --- Lógica de Nível (MÉTODO NOVO) ---
    public void ganharXP(int xpGanha) {
        this.experiencia += xpGanha;
        System.out.println("  " + this.nome + " ganhou " + xpGanha + " de XP!");

        // Verifica se subiu de nível
        while (this.experiencia >= this.xpParaProximoNivel) {
            // 1. Sobe de Nível
            this.nivel++;
            // 2. Tira o XP usado para subir
            this.experiencia -= this.xpParaProximoNivel; 
            
            // 3. Aumenta os atributos (exemplo simples)
            int bonusVida = 10;
            int bonusAtk = 3;
            int bonusDef = 2;

            this.pontosVidaMax += bonusVida;
            this.ataque += bonusAtk;
            this.defesa += bonusDef;
            
            // 4. Recupera toda a vida
            this.pontosVida = this.pontosVidaMax; 
            
            // 5. Define a próxima meta de XP
            this.xpParaProximoNivel = 100 * this.nivel;

            // 6. Avisa o jogador!
            System.out.println("  ================================");
            System.out.println("   " + this.nome + " SUBIU PARA O NÍVEL " + this.nivel + "!");
            System.out.println("   HP Máx: +" + bonusVida + " | Ataque: +" + bonusAtk + " | Defesa: +" + bonusDef);
            System.out.println("   HP Restaurado! Próximo Nível: " + this.experiencia + "/" + this.xpParaProximoNivel + " XP");
            System.out.println("  ================================");
        }
    }


    // --- Métodos de Combate (isVivo, receberAtaque, curar) ---
    public boolean isVivo() {
        return this.pontosVida > 0;
    }

    public void receberAtaque(int ataqueBruto) {
        int dano = ataqueBruto - this.getDefesa();
        if (dano < 0) {
            dano = 0; // A sua lógica de permitir 0 de dano
        }
        this.pontosVida -= dano;

        if (dano > 0) {
            System.out.println("  " + this.nome + " recebeu " + dano + " de dano!");
        } else {
            System.out.println("  " + this.nome + " bloqueou o ataque!");
        }

        if (!this.isVivo()) {
            this.pontosVida = 0;
            System.out.println("  " + this.nome + " foi derrotado!");
        }
    }

    public void curar(int vida) {
        int vidaCurada = vida;
        int novaVida = this.getPontosVida() + vida;
        
        if (novaVida > this.getPontosVidaMax()) {
            // Se a cura for 20, mas o HP só faltar 5, cura 5.
            vidaCurada = this.pontosVidaMax - this.getPontosVida();
            this.pontosVida = this.pontosVidaMax;
        } else {
            this.pontosVida = novaVida;
        }

        if (vidaCurada <= 0) {
             System.out.println("  " + this.nome + " já está com a vida cheia.");
        } else {
             System.out.println("  " + this.nome + " curou " + vidaCurada + " de HP. (HP: " + this.pontosVida + "/" + this.pontosVidaMax + ")");
        }
    }

    // --- toString (Atualizado com XP) ---
    @Override
    public String toString() {
        return String.format(
            "Nome: %s | Nível: %d | XP: %d/%d | HP: %d/%d | Atk: %d | Def: %d",
            this.nome, this.nivel, this.experiencia, this.xpParaProximoNivel,
            this.pontosVida, this.pontosVidaMax, this.ataque, this.defesa
        );
    }

    // --- clone (Perfeito) ---
    @Override
    public Personagem clone() {
        try {
            Personagem copiaRasa = (Personagem) super.clone();
            copiaRasa.inventario = this.inventario.clone();
            return copiaRasa;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("A classe Personagem não suporta clone");
        }
    }
}

