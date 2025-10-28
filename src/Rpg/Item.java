package Rpg;

import java.text.Normalizer;

public class Item implements Comparable<Item>, Cloneable{
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;
    
    //construtor de cópia
    public Item(Item original){
        // Chama o construtor principal para garantir as validações
        // (Isto é uma prática melhor do que copiar campo a campo)
        try {
            this.inicializar(original.nome, original.descricao, original.efeito, original.quantidade);
        } catch (Exception e) {
            // Isto "nunca" deve acontecer se o original for válido
            throw new AssertionError("Erro ao copiar item original.", e);
        }
    }
    
    //construtor padrão
    public Item(String nome, String descricao, String efeito, int quantidade) throws Exception{
        this.inicializar(nome, descricao, efeito, quantidade);
    }

    // método de inicialização privado para evitar código duplicado
    private void inicializar(String nome, String descricao, String efeito, int quantidade) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome não pode estar vazio");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    //getters e setters
    public String getNome(){ return this.nome; }
    public String getDescricao(){ return this.descricao; }
    public String getEfeito(){ return this.efeito; }
    public int getQuantidade(){ return this.quantidade; }
    
    public void setNome(String nome) throws Exception{
        if(nome == null || nome.trim().isEmpty()){
            throw new Exception("O nome não pode estar vazio");
        }
        this.nome = nome;
    }
    public void setDescricao(String descricao){ this.descricao = descricao; }
    public void setEfeito(String efeito){ this.efeito = efeito; }
    public void setQuantidade(int quantidade){ this.quantidade = quantidade; }


    @Override
    public String toString(){
        //"Poção de Cura (x3): Cura 25 HP."
        return String.format("%s (x%d): %s", this.nome, this.quantidade, this.descricao);
    }
    
    //normaliza a palavra para a busca no inventário
    public String normalizarNome() {
        if (this.nome == null) return "";
        // normaliza (separa ç em c e ¸)
        String nomeNormalizado = Normalizer.normalize(this.nome, Normalizer.Form.NFD);
        // remove os acentos (o '¸') e passa para minúsculas
        return nomeNormalizado.replaceAll("\\p{M}", "").toLowerCase();
    }


    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        Item item = (Item) obj;
        return this.normalizarNome().equals(item.normalizarNome());
    }
    
    @Override
    public int hashCode(){
        int ret = 1;
        
        int nomeHash = this.normalizarNome().hashCode(); 
        
        ret = ret * 2 + nomeHash;
        if (ret < 0) {
            ret = -ret;
        }
        return ret;
    }
    
    
    @Override
    public int compareTo(Item outro){
        return this.normalizarNome().compareTo(outro.normalizarNome());
    }
    

    @Override 
    public Item clone(){
        try{
            //copia rasa
            return (Item) super.clone();
        } catch (CloneNotSupportedException e){
            throw new AssertionError("A classe Item não suporta clone");
        }
    }
}

