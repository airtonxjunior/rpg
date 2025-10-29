package Rpg;

//abstract pq não é possivel dar new Personagem
//personagem é apenas um molde
public abstract class Personagem implements Cloneable {
    private String nome;
    private int pontosVida;
    private int pontosVidaMax;
    private int ataque;
    private int defesa;
    private int nivel;
    private Inventario inventario;


    private int experiencia;
    private int xpParaProximoNivel;

    //construtor padrao
    public Personagem(String nome, int vida, int ataque, int defesa, int nivel) {
        this.nome = nome;
        this.pontosVida = vida;
        this.pontosVidaMax = vida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = new Inventario();
        this.experiencia = 0;
        this.xpParaProximoNivel = 100 * nivel; 
    }

    //construtor de copia
    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVida = original.pontosVida;
        this.pontosVidaMax = original.pontosVidaMax;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        
        //clone no inventario pq ele é objeto
        this.inventario = original.inventario.clone();


        this.experiencia = original.experiencia;
        this.xpParaProximoNivel = original.xpParaProximoNivel;
    }

    //getters
    public String getNome() { return this.nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getAtaque() { return this.ataque; }
    public int getDefesa() { return this.defesa; }
    public int getNivel() { return this.nivel; }
    public Inventario getInventario() { return this.inventario; }
    public int getPontosVidaMax() { return this.pontosVidaMax; }
    public int getExperiencia() { return this.experiencia; }
    public int getXPParaProximoNivel() { return this.xpParaProximoNivel; }


    //setters
    public void setDefesa(int defesa) { this.defesa += defesa; }
    public void setAtaque(int ataque) { this.ataque += ataque; }
    public void setPontosVidaMax(int vida) { this.pontosVidaMax += vida; }



    public void ganharXP(int xpGanho) {
        this.experiencia += xpGanho;
        System.out.println("  " + this.nome + " ganhou " + xpGanho + " de XP!");

        //verifica se subiu de nível
        while (this.experiencia >= this.xpParaProximoNivel) {
            this.nivel++;
            //tira o xp usado para subir
            this.experiencia -= this.xpParaProximoNivel; 
            
            //aumenta os atributos
            int bonusVida = 10;
            int bonusAtk = 3;
            int bonusDef = 2;

            this.pontosVidaMax += bonusVida;
            this.ataque += bonusAtk;
            this.defesa += bonusDef;
            
            // recupera toda a vida pq subiu de nivel
            this.pontosVida = this.pontosVidaMax; 
            
            //define a próxima meta de xp
            this.xpParaProximoNivel = 100 * this.nivel;


            System.out.println("  ================================");
            System.out.println("   " + this.nome + " SUBIU PARA O NÍVEL " + this.nivel + "!");
            System.out.println("   HP Máx: +" + bonusVida + " | Ataque: +" + bonusAtk + " | Defesa: +" + bonusDef);
            System.out.println("   HP Restaurado! Próximo Nível: " + this.experiencia + "/" + this.xpParaProximoNivel + " XP");
            System.out.println("  ================================");
        }
    }



    public boolean isVivo() {
        return this.pontosVida > 0;
    }

    public void receberAtaque(int ataqueBruto) {
        //cria a variavel dano, que subtrai o ataque recebido pela defesa
        int dano = ataqueBruto - this.getDefesa();
        
        //se der dano negativo, então dá 0
        if (dano < 0) {
            dano = 0; 
        }
        //subtrai a vida pelo dano recebido
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
            //se a cura for 20, mas o HP só faltar 5, cura 5.
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


    @Override
    public String toString() {
        return String.format(
            "Nome: %s | Nível: %d | XP: %d/%d | HP: %d/%d | Atk: %d | Def: %d",
            this.nome, this.nivel, this.experiencia, this.xpParaProximoNivel,
            this.pontosVida, this.pontosVidaMax, this.ataque, this.defesa
        );
    }


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

