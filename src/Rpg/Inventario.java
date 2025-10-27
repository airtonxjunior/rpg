package Rpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// NOVOS IMPORTS NECESSÁRIOS
import java.text.Normalizer;

/**
 * Inventario.java (Versão 3 - CORRIGIDA)
 * * MUDANÇAS:
 * 1. Adicionado 'normalizarTexto' para limpar a busca do utilizador.
 * 2. Adicionado 'encontrarItemPorNome' (método privado) que usa 'startsWith'
 * em vez de 'equals'. Isto é muito mais robusto.
 * 3. 'getItem' e 'remover' agora usam este novo método.
 */
public class Inventario implements Cloneable {
    private List<Item> itens;

    // --- Construtores (Perfeitos) ---
    public Inventario() {
        this.itens = new ArrayList<>();
    }

    public Inventario(Inventario original) {
        this.itens = new ArrayList<>();
        // O clone profundo do construtor de cópia está perfeito.
        for (Item itemOriginal : original.itens) {
            this.itens.add(itemOriginal.clone());
        }
    }

    // --- Métodos de Gestão (Perfeitos) ---

    /**
     * Adiciona um item (ou soma a quantidade).
     * O 'indexOf' aqui funciona, porque o 'itemAdicionar'
     * é um objeto Item completo, e o Item.java (com o fix dos acentos)
     * fará o 'equals' funcionar corretamente.
     */
    public void adicionar(Item itemAdicionar) {
        // Esta lógica de 'adicionar' está 100% correta.
        int indice = this.itens.indexOf(itemAdicionar);

        if (indice != -1) {
            Item itemExistente = this.itens.get(indice);
            int quantidadeAtual = itemExistente.getQuantidade();
            int quantidadeAdicionar = itemAdicionar.getQuantidade();
            itemExistente.setQuantidade(quantidadeAtual + quantidadeAdicionar);
        } else {
            // Adiciona um clone para garantir independência
            this.itens.add(itemAdicionar.clone());
        }
    }

    /**
     * Adiciona todos os itens de outro inventário (para o 'loot').
     * (Este método estava em falta no seu ficheiro)
     */
    public void adicionarItens(Inventario outroInventario) {
        for (Item item : outroInventario.itens) {
            this.adicionar(item); // Reutiliza a lógica de 'adicionar'
        }
    }

    // --- MÉTODOS DE BUSCA (AQUI ESTÁ A CORREÇÃO) ---

    /**
     * Função ajudante (privada) para normalizar nomes
     */
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String nomeNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return nomeNormalizado.replaceAll("\\p{M}", "").toLowerCase();
    }

    /**
     * Método ajudante (privado) que PROCURA o item.
     * Esta é a nova lógica.
     */
    private Item encontrarItemPorNome(String nomeBusca) {
        // 1. Limpa o texto que o utilizador digitou ("Pocao") -> "pocao"
        String buscaNormalizada = normalizarTexto(nomeBusca);

        // 2. Faz um loop pela lista
        for (Item item : this.itens) {
            // 3. Limpa o nome do item da lista ("Poção de Cura") -> "pocao de cura"
            String nomeItemNormalizado = item.normalizarNome(); // Usamos o método do Item.java

            // 4. VERIFICA SE O NOME DO ITEM "COMEÇA COM" O QUE O UTILIZADOR DIGITOU
            if (nomeItemNormalizado.startsWith(buscaNormalizada)) {
                return item; // Encontrado! Retorna o item.
            }
        }
        
        // 5. Se o loop acabar, não encontrou
        return null;
    }


    /**
     * Remove uma quantidade de um item, procurando pelo nome.
     * (Agora usa a busca 'startsWith')
     */
    public boolean remover(String nomeItem, int quantidadeARemover) {
        
        // 1. Usa o novo método de busca robusto
        Item itemNoInventario = this.encontrarItemPorNome(nomeItem);

        // 2. O resto da lógica é a mesma (e estava correta)
        if (itemNoInventario == null) {
            return false; // Item não encontrado
        }

        int quantidadeAtual = itemNoInventario.getQuantidade();
        if (quantidadeAtual < quantidadeARemover) {
            return false; // Não tem o suficiente
        }

        itemNoInventario.setQuantidade(quantidadeAtual - quantidadeARemover);

        if (itemNoInventario.getQuantidade() <= 0) {
            this.itens.remove(itemNoInventario); // Remove o objeto
        }

        return true; // Sucesso
    }

    /**
     * Retorna um item do inventário, procurando pelo nome.
     * (Agora usa a busca 'startsWith')
     */
    public Item getItem(String nomeItem) {
        // 1. Usa o novo método de busca robusto
        return this.encontrarItemPorNome(nomeItem);
        
        // O "try...catch (new Item)" foi TODO removido.
        // O seu 'Item.java' já lança exceção se o nome for vazio
        // no construtor, mas 'encontrarItemPorNome' lida com 'null'
        // e nomes vazios sem problemas.
    }

    // --- Outros Métodos (Perfeitos) ---

    /**
     * Verifica se o inventário está vazio.
     * (Este método estava em falta no seu ficheiro)
     */
    public boolean estaVazio() {
        return this.itens.isEmpty();
    }

    /**
     * Lista todos os itens, ordenados.
     */
    public void listarItens() {
        if (this.itens.isEmpty()) {
            System.out.println("  Inventário vazio.");
            return;
        }

        // 'Collections.sort' vai usar o 'Item.compareTo'
        // que nós corrigimos para ignorar acentos.
        Collections.sort(this.itens); 
        System.out.println("  --- Inventário ---");
        for (Item item : this.itens) {
            // 'item.toString()' também foi melhorado em Item.java
            System.out.println("  - " + item.toString()); 
        }
        System.out.println("  ------------------");
    }

    /**
     * Clone (Perfeito)
     */
    @Override
    public Inventario clone() {
        try {
            Inventario copiaRasa = (Inventario) super.clone();
            
            // A cópia profunda (Deep Copy) está perfeita.
            copiaRasa.itens = new ArrayList<>();
            for (Item itemOriginal : this.itens) {
                copiaRasa.itens.add(itemOriginal.clone());
            }
            return copiaRasa;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError("A classe Inventario não suporta clone.");
        }
    }
}

