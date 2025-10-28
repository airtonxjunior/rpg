package Rpg;

//imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.Normalizer;


public class Inventario implements Cloneable {
    private List<Item> itens;

    //construtor inicia uma lista vazia
    public Inventario() {
        this.itens = new ArrayList<>();
    }

    //construtor de copia, utiliza clone de item
    public Inventario(Inventario original) {
        this.itens = new ArrayList<>();
        for (Item itemOriginal : original.itens) {
            this.itens.add(itemOriginal.clone());
        }
    }

    //adiciona item no inventario
    public void adicionar(Item itemAdicionar) {
        //aqui ele procura se o item para adicionar já existe no inventário de quem chama a função, através do índice
        int indice = this.itens.indexOf(itemAdicionar);

        //se encontrou
        if (indice != -1) {
            //cria um obj de item e pega o endereço do item que já existe no inventario
            Item itemExistente = this.itens.get(indice);
            //pega a quantidade de item do item que encontramos no inventario
            int quantidadeAtual = itemExistente.getQuantidade();
            
            // pega a quantidade de itens à adicionar, no caso, o que veio do parâmetro
            int quantidadeAdicionar = itemAdicionar.getQuantidade();
            itemExistente.setQuantidade(quantidadeAtual + quantidadeAdicionar);
            
        } else {
            //se não existe no inventário, adiciona através do clone
            this.itens.add(itemAdicionar.clone());
        }
    }

    //adiciona vários itens
    public void adicionarItens(Inventario outroInventario) {
        for (Item item : outroInventario.itens) {
            this.adicionar(item);
        }
    }

    
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String nomeNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return nomeNormalizado.replaceAll("\\p{M}", "").toLowerCase();
    }


   
    private Item encontrarItemPorNome(String nomeBusca) {
        //limpa o texto digitado ("Pocao") -> "pocao"
        String buscaNormalizada = normalizarTexto(nomeBusca);

        //faz um loop pela lista
        for (Item item : this.itens) {
            // limpa o texto digitado  ("Poção de Cura") -> "pocao de cura"
            String nomeItemNormalizado = item.normalizarNome(); //usa o método do Item.java

            // verifica se o nome do item começa com o que foi digitado
            if (nomeItemNormalizado.startsWith(buscaNormalizada)) {
                return item; 
            }
        }
        
        //se o loop acabar, não encontrou
        return null;
    }

    
    public boolean remover(String nomeItem, int quantidadeARemover) {
        
        //usa o método de busca 
        Item itemNoInventario = this.encontrarItemPorNome(nomeItem);

        //se estiver null
        if (itemNoInventario == null) {
            return false; //item não encontrado
        }

        int quantidadeAtual = itemNoInventario.getQuantidade();
        if (quantidadeAtual < quantidadeARemover) {
            return false; //nao tem o suficiente
        }
        
        //remove o item que foi usado
        itemNoInventario.setQuantidade(quantidadeAtual - quantidadeARemover);

        //se não tiver mais o item usado no inventario, entao remove
        if (itemNoInventario.getQuantidade() <= 0) {
            this.itens.remove(itemNoInventario);
        }

        return true;
    }


    
    public Item getItem(String nomeItem) {
        //usa o método de busca
        return this.encontrarItemPorNome(nomeItem);
    }

    
    //verifica se o inventario esta vazio
    public boolean estaVazio() {
        return this.itens.isEmpty();
    }


    
    public void listarItens() {
        if (this.itens.isEmpty()) {
            System.out.println("  Inventário vazio.");
            return;
        }

        // collections.sort vai usar o item.compareTo
        Collections.sort(this.itens); 
        System.out.println("  --- Inventário ---");
        for (Item item : this.itens) {
            System.out.println("  - " + item.toString()); 
        }
        System.out.println("  ------------------");
    }


    @Override
    public Inventario clone() {
        try {
            //cria uma copia rasa com super.clone
            //no caso, copia apenas os atributos simples
            Inventario copiaRasa = (Inventario) super.clone();
           
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

