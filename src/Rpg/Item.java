package Rpg;

// IMPORT NECESSÁRIO PARA REMOVER ACENTOS
import java.text.Normalizer;
import java.util.Objects; // (Pode ser que você precise importar)

public class Item implements Comparable<Item>, Cloneable{
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;
    
    // Construtor de Cópia (Corrigido para usar o construtor principal)
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
    
    // Construtor Padrão (para o 'new Item(nome, "", "", 0)' funcionar)
    public Item(String nome, String descricao, String efeito, int quantidade) throws Exception{
        this.inicializar(nome, descricao, efeito, quantidade);
    }

    // Método de inicialização privado para evitar código duplicado
    private void inicializar(String nome, String descricao, String efeito, int quantidade) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome não pode estar vazio");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    // --- Getters e Setters ---
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

    // --- O "toString" ---
    @Override
    public String toString(){
        // (Exemplo: "Poção de Cura (x3): Cura 25 HP.")
        return String.format("%s (x%d): %s", this.nome, this.quantidade, this.descricao);
    }
    
    // --- FUNÇÃO AJUDANTE PARA CORRIGIR O BUG ---
    /**
     * Remove acentos e passa para minúsculas.
     * "Poção de Cura" -> "pocao de cura"
     */
    public String normalizarNome() {
        if (this.nome == null) return "";
        // 1. Normaliza (separa 'ç' em 'c' e '¸')
        String nomeNormalizado = Normalizer.normalize(this.nome, Normalizer.Form.NFD);
        // 2. Remove os acentos (o '¸') e passa para minúsculas
        return nomeNormalizado.replaceAll("\\p{M}", "").toLowerCase();
    }

    // --- CORREÇÃO DO BUG no equals() ---
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        
        Item item = (Item) obj;
        
        // Compara os nomes normalizados (sem acento, sem maiúsculas)
        return this.normalizarNome().equals(item.normalizarNome());
    }
    
    // --- CORREÇÃO DE CONSISTÊNCIA no hashCode() ---
    @Override
    public int hashCode(){
        // A Regra de Ouro: hashCode() DEVE usar os mesmos campos do equals().
        // (Estilo do professor)
        int ret = 1;
        
        // Usa o nome normalizado, o mesmo do 'equals()'
        int nomeHash = this.normalizarNome().hashCode(); 
        
        ret = ret * 2 + nomeHash;
        if (ret < 0) {
            ret = -ret;
        }
        return ret;
    }
    
    // --- compareTo (para ordenar) ---
    @Override
    public int compareTo(Item outro){
        // Ordena pelo nome normalizado também, para "Pocao" e "Poção"
        // ficarem juntos na lista.
        return this.normalizarNome().compareTo(outro.normalizarNome());
    }
    
    // --- clone ---
    @Override 
    public Item clone(){
        try{
            // Cópia rasa é segura (só tem int e String)
            return (Item) super.clone();
        } catch (CloneNotSupportedException e){
            throw new AssertionError("A classe Item não suporta clone");
        }
    }
}

