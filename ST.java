import java.util.LinkedList;
import java.util.Queue;

public class ST<Key extends Comparable<Key>, Value> {

    private Node root;

    public class Node {
        public Key key;
        public Value value;
        public Node left;
        public Node right;
        public int size;

        public Node(Key key, Value value, int size){
            this.key = key;
            this.value = value;
            this.size=size;
        }
    }

    public ST() {
        root=null;
    }

    public void put(Key key, Value val){
        root = put(root,key,val);
    }

    private Node put(Node aux, Key key, Value value){
        if(aux == null){return new Node(key,value,1);}

        if(key.compareTo(aux.key) < 0){
            aux.left=put(aux.left,key,value);
        }
        else if(key.compareTo(aux.key) > 0){
            aux.right=put(aux.right,key,value);
        }
        else{
            aux.value=value;
        }

        aux.size=1 + sz(aux.right) + sz(aux.left);

        return aux;
    }

    private int sz(Node T){
        if(T== null){
            return 0;
        }
        else{
            return T.size;
        }
    }

    public Value get(Key key) {
        return get(key, root);
    }

    private Value get(Key key, Node aux){
        if(aux == null){return null;}

        if(key.compareTo(aux.key) < 0){
            aux = aux.left;
            return get(key,aux);
        }
        else if(key.compareTo(aux.key) > 0){
            aux = aux.right;
            return get(key,aux);
        }
        return aux.value;
    }

    public void delete(Key key){
        root=delete(key, root);
    }
    private Node delete(Key key, Node aux){
        if(aux == null){return null;}

        int comp = key.compareTo(aux.key);
        if(comp < 0){ //se a chave da key for menor que a do aux
            aux.left = delete(key, aux.left);
        }
        else if(comp>0){
            aux.right = delete(key, aux.right);
        }
        else{ //quando encontramos o node que queremos apagar
            //no caso mais bonito onde apenas temos uma folha com um filho apenas
            if (aux.left == null) return aux.right;
            if (aux.right == null) return aux.left;

            Node min = min(aux.right);
            aux.key=min.key; aux.value = min.value;
            aux.right=delete(min.key, aux.right);
        }
        aux.size= sz(aux.left) + sz(aux.right) + 1;

        return aux;
    }
    private Node min(Node aux){
        if(aux==null) return null;
        if(aux.left == null) return aux;
        return min(aux.left);
    }

    public boolean contains(Key key){
        return get(key)!=null;
    }
    public boolean isEmpty() {
        return root==null;
    }
    public int size(){
        return root == null ? 0 : root.size;
    }

    public int height(){
        return height(root);
    }
    private int height(Node aux){
        if (aux == null) {
            return 0;
        }

        int leftHeight = height(aux.left);
        int rightHeight = height(aux.right);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    public Key min(){
        if(root==null) return null;
        Key min = root.key;
        Node aux = root.left;
        while(aux != null){
            min=aux.key;
            aux=aux.left;
        }
        return min;
    }

    public Key max(){
        if(root == null) return null;
        return max(root).key;
    }
    private Node max(Node aux){
        if(aux.right == null) return aux;
        return max(aux.right);
    }

    public Key floor(Key key){
        if (root == null) return null;
        return floor(key, root, null);
    }
    private Key floor(Key key, Node aux, Key best){
        if (aux == null) return best;

        int comp = key.compareTo(aux.key);

        if (comp < 0) { //se tivermos perante uma chave maior, como queremos as menores vamos a procuradas menores que estao na esquerda
            return floor(key, aux.left, best);
        }
        else if (comp > 0) { //se tivermos perante uma chave menor, entao guardamos essa chave pois é uma candidata e vamos para a direta procurar a maior das menores
            return floor(key, aux.right, aux.key);
        }
        else {
            return aux.key;
        }
    }

    public Key ceiling(Key key){
        if (root == null) return null;
        return ceiling(key, root, null);
    }
    private Key ceiling(Key key, Node aux, Key best){
        if(aux == null) return best;
        int comp = key.compareTo(aux.key);
        if(comp > 0){ //se tiveermos perante a uma chave menor vamos para a diretira pois queremos haves maiores
            return ceiling(key, aux.right, best);
        }
        else if (comp < 0){//se tivermos perante uma chave maior vamos para a esquerda pois qeuresmo a emnor das maiores, guardamos tambem essa chave pois é uma candidata
            return ceiling(key, aux.left, aux.key);
        }
        else{
            return aux.key;
        }
    }

    public int rank(Key key){
        return rank(key, root);
    }
    private int rank(Key key, Node aux){
        if(aux==null) return 0;

        if(key.compareTo(aux.key)<0){return rank(key, aux.left);}
        else if(key.compareTo(aux.key)>0){return 1 + sz(aux.left) + rank(key, aux.right);}
        else return sz(aux.left);
    }

    public Key select(int k){
        if(k<0 || k >= size()) throw new IllegalArgumentException();
        return select(k, root);
    }
    private Key select(int k, Node aux){
        if (aux == null) return null;

        int t = sz(aux.left); // número de nós à esquerda (menores)

        if (t > k) return select(k, aux.left);           // chave está à esquerda
        else if (t < k) return select(k - t - 1 ,aux.right); // está à direita
        else return aux.key; // t == k → encontramos a chave com rank k
    }

    public void deleteMin(){
        delete(min());
    }

    public void deleteMax(){
        delete(max());
    }

    public int size(Key lo, Key hi){
        if (lo.compareTo(hi) > 0) return 0;
        int size= rank(hi) - rank(lo);
        if (contains(hi)) size ++;
        return size;
    }

    public Iterable<Key> keys(Key lo, Key hi){
        Queue<Key> q = new LinkedList<>();
        inOrder(root, lo, hi, q);
        return q;
    }
    private void inOrder(Node x, Key lo, Key hi, Queue<Key> q){
        if(x==null) return;

        // so vamos a esquerda se houver la tiver chance de ter chaves maiores que lo
        if (x.key.compareTo(lo) > 0) {inOrder(x.left, lo, hi, q);}
        //agora estamos com aux=chave mais pequena mais maior ou igual a lo
        // Se a chave do nó estiver no intervalo [lo, hi], adiciona na fila
        if (x.key.compareTo(lo) >= 0 && x.key.compareTo(hi) <= 0) {q.add(x.key);}

        // Só vamos à direita se houver hipótese de encontrar chaves menores que hi
        if (x.key.compareTo(hi) < 0) {inOrder(x.right, lo, hi, q);}

    }

    public Iterable<Key> keys(){
        Queue<Key> q = new LinkedList<>();
        inorder(root,q);
        return q;
    }
    private void inorder(Node x, Queue<Key> q){
        if (x == null) return;
        inorder(x.left, q);
        q.add(x.key);
        inorder(x.right, q);
    }
}

//o k vai de 0 a size -1 ou de 1 a size