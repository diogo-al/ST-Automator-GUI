import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.io.*;
import java.util.Random;

import static javax.swing.SwingConstants.CENTER;

public class STValidator {

    /******************************************************
     *                Variaveis Globais
     *******************************************************/
    static int EXIT_ON_ERROR=0;
    static int NR_TESTS=70;
    static int SUC_TESTS=0;
    static int ERR_TESTS=0;
    static JTextPane consola;
    static JTextPane info;
    static JProgressBar barra;
    static ST<String , Integer> st = new ST<String , Integer>();
    static double dindin=0;
    static JTextPane money;

    public static void main(String[] args) throws BadLocationException {
        /******************************************************
         *                Criar Nova Janela
         *******************************************************/
        JFrame janela = new JFrame("STValidator");
        janela.setSize(900,700);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /******************************************************
         *                Criar Novo Painel
         *******************************************************/
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(45, 45, 45));

        /******************************************************
         *                     TITULO
         *******************************************************/
        JLabel Titulo = new JLabel("Bonito Validador");
        Titulo.setFont(new Font("Roboto", Font.BOLD, 22));
        Titulo.setBounds(120, 30, 240, 50);
        Titulo.setText("<html><span style='font-size:22px; color:#ffffff;'>Bonito Validador</span></html>");

        /******************************************************
         *                     CONSOLA
         *******************************************************/

        consola = new JTextPane();
        consola.setEditable(false);
        consola.setBackground(new Color(30, 30, 30)); // fundo escuro
        consola.setForeground(Color.GREEN);
        consola.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        consola.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(consola);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        scroll.setBounds(10,100, 425,550);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /******************************************************
         *                    INFO PLANE
         *******************************************************/
        info = new JTextPane();
        info.setEditable(false);
        info.setBackground(new Color(30,30,30));
        info.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        JScrollPane InfoBox = new JScrollPane(info);
        InfoBox.setBorder(BorderFactory.createLineBorder(new Color(88,101,242),2));
        InfoBox.setBounds(500,100,350,250);
        InfoBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        InfoBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        /******************************************************
         *                   INFO MONEY
         *******************************************************/
        money = new JTextPane();
        money.setEditable(false);
        money.setBackground((new Color(45,45,45)));
        money.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        JScrollPane moneyBox = new JScrollPane(money);
        moneyBox.setBounds(10,10,400,25);
        moneyBox.setBorder(null);
        moneyBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        moneyBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        /******************************************************
         *                   PROGRESS BAR
         *******************************************************/
        UIManager.put("ProgressBar.selectionForeground", new Color(255,255,255));
        UIManager.put("ProgressBar.selectionBackground", new Color(255,255,255));  // Cor de fundo da área atrás do texto
        barra = new JProgressBar(0,100);
        barra.setOrientation(0);
        barra.setBounds(525,425,300,20);
        barra.setBackground(new Color(30,30,30));
        barra.setForeground(new Color(40, 180, 85));
        barra.setBorder(BorderFactory.createLineBorder(new Color(88,101,242),2));
        barra.setStringPainted(true);
        barra.setValue(0);
        barra.setVisible(true);

        /******************************************************
         *                      BOTOES
         *******************************************************/

        JButton validar = new JButton("Rodar Validador");
        validar.addActionListener(e -> {
            try {
                executarValidacao();
            } catch (BadLocationException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Ocorreu um erro ao tentar modificar o conteúdo. Por favor, tente novamente.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        validar.setBounds(600,525,150,50);
        validar.setBackground(new Color(15, 17, 40));
        validar.setForeground(new Color(220,220,220));
        validar.setFocusPainted(false);
        validar.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        validar.setFont(new Font("Arial", Font.BOLD, 14));
        validar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        validar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {validar.setBackground(new Color(20, 26, 56));}
            public void mouseExited(MouseEvent e) {validar.setBackground(new Color(5, 17, 40));}
        });

        JCheckBox exitOnError = new JCheckBox("❌ Parar no Primeiro Erro");
        exitOnError.addActionListener(e -> {
            if (exitOnError.isSelected()) {
                EXIT_ON_ERROR = 1;
                exitOnError.setText("✔ Rodar tudo de uma vez");
            } else {
                EXIT_ON_ERROR = 0;
                exitOnError.setText("❌ Parar no Primeiro Erro");
            }});
        exitOnError.setBounds(550,600,250,50);
        exitOnError.setBackground(new Color(15, 17, 40));
        exitOnError.setForeground(new Color(220,220,220));
        exitOnError.setFocusPainted(false);
        exitOnError.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        exitOnError.setBorderPainted(true);
        exitOnError.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        exitOnError.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitOnError.setHorizontalAlignment(CENTER);
        exitOnError.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {exitOnError.setBackground(new Color(20, 26, 56));}
            public void mouseExited(MouseEvent e) {exitOnError.setBackground(new Color(5, 17, 40));}
        });

        JButton sobre = new JButton("?");
        sobre.setBounds(843,10,35,35);
        sobre.setBackground(new Color(30, 30, 30));
        sobre.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        sobre.setFocusPainted(false);
        sobre.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sobre.setForeground(Color.LIGHT_GRAY);
        sobre.setFont(new Font("Arial",Font.BOLD, 20));
        sobre.addActionListener(e -> {
            JDialog sobreBox = new JDialog();
            sobreBox.setSize(500, 300);
            sobreBox.setLocationRelativeTo(janela);
            sobreBox.getContentPane().setBackground(new Color(30,30,30));
            sobreBox.setTitle("Sobre");
            JLabel text = new JLabel("<html><div style='width: 200px; text-align: center; color: #5865f2'>Boa Tarde, está aqui a pedra do validador, não é preciso tirar um curso para trabalhar com ele tf, mas tens um botão para comecar a validação e outro para selecionar quando parar😘</div></html>");
            text.setFont(new Font("consolas",Font.BOLD, 20));
            text.setHorizontalAlignment(CENTER);
            sobreBox.add(text);
            sobreBox.setVisible(true);
        });

        JButton roleta = new JButton("Gamble");
        roleta.setBounds(692,10,100,35);
        roleta.setBackground(new Color(30, 30, 30));
        roleta.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        roleta.setFocusPainted(false);
        roleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        roleta.setForeground(new Color(220,220,220));
        roleta.setFont(new Font("consolas",Font.BOLD,16));
        roleta.addActionListener(e -> new roletaWindow());

        JButton clean = new JButton("🗑");
        clean.setBounds(440,615,35,35);
        clean.setBackground(new Color(30, 30, 30));
        clean.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        clean.setFocusPainted(false);
        clean.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clean.setForeground(new Color(220,220,220));
        clean.setFont(new Font("Segoe UI Emoji",Font.BOLD,16));
        clean.addActionListener(e -> consola.setText(""));
        clean.setVerticalTextPosition(CENTER);

        JButton trapaca = new JButton("👾");
        trapaca.setBounds(800,10,35,35);
        trapaca.setBackground(new Color(30, 30, 30));
        trapaca.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
        trapaca.setForeground(new Color(220, 55, 95));
        trapaca.setFocusPainted(false);
        trapaca.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trapaca.addActionListener(e->{
            JDialog dialog = new JDialog((Frame) null, "TRAPACICES", true);
            dialog.setSize(350,200);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().setBackground(new Color(45,45,45));
            dialog.setLayout(null);

            JLabel label = new JLabel("Portanto és trapaceiro");
            label.setForeground(Color.WHITE);
            label.setBounds(110, 10, 130, 25);

            JTextField text = new JTextField();
            text.setBackground(new Color(60,60,60));
            text.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
            text.setForeground(Color.WHITE);
            text.setBounds(75, 50, 200, 30);

            JButton apply = new JButton("OK");
            apply.setBounds(150,115,50,40);
            apply.setFont(new Font("consolas",Font.BOLD, 15));
            apply.setBackground(new Color(30, 30, 30));
            apply.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
            apply.setFocusPainted(false);
            apply.setForeground(Color.WHITE);
            apply.addActionListener(r->{
                String input = text.getText();
                try {
                    testarCodigo(input);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            });

            dialog.add(label);
            dialog.add(text);
            dialog.add(apply);
            dialog.setVisible(true);
        });
        /******************************************************
         *                Adicionar Componentes
         *******************************************************/
        panel.add(Titulo);
        panel.add(validar);
        panel.add(exitOnError);
        panel.add(InfoBox);
        panel.add(moneyBox);
        panel.add(scroll);
        panel.add(sobre);
        panel.add(barra);
        panel.add(roleta);
        panel.add(trapaca);
        panel.add(clean);

        /******************************************************
         *                Configs Adicionais
         *******************************************************/
        janela.setLocationRelativeTo(null);
        janela.add(panel);
        janela.setVisible(true);
        upInfo();
        saldoControlador.run();
        saldoControlador.setSaldo(saldoControlador.getSaldo());
        dindin=saldoControlador.getSaldo();
        printMomey();
    }
    /******************************************************
     *                   RODAR TESTES
     *******************************************************/
    private static void reset(){
        consola.setText("");
        info.setText("");
        barra.setValue(0);
        SUC_TESTS=0;
        ERR_TESTS=0;
        st = new ST<String, Integer>();
    }
    public static void executarValidacao() throws BadLocationException {
        try {
            reset();
            print("Class ST e respetivos metodos existem?");
            existMethods();
            if(ERR_TESTS > 0){printE("Metodos em Falta");}
            print("Arvore vazia, testar IsEmpty");
            vazia();
            print("Arvore vazia testar height");
            height(0);
            print("Colocado elemento <Olga> testar IsEmpty");
            st.put("Olga", 123);
            Nvazia();
            print("Testar height aarvore com 1 elemento");
            height(1);
            print("testar contains(\"Olga\")");
            contains("Olga");
            print("testar contains de um elemento nao existente");
            Ncontains("Paulo");
            print("Testar max para uma arvore com apenas 1 elemento");
            maxKey("Olga");
            print("Testar min para as condições anteriores");
            minKey("Olga");
            print("Testar size para uma arvore com 1 elemento");
            size(1);
            print("Testar get para o elemento Olga");
            get("Olga",123);
            print("Testar rank para arvore com 1 elemento");
            rank("Olga",0);
            print("select(0) para arvore com 1 elemento");
            select("Olga",0);
            print("Eliminar um elemento...");
            st.delete("Olga");
            print("Testar size para arvore vazia");
            size(0);
            print("Testar contains para arvore vazia");
            Ncontains("Olga");
            print("colocados: [S,E,X,A,R,C,H,M]");
            colocElem();
            print("Testar size para arvore com 8 elementos");
            size(8);
            print("put para elemento existente");
            AddExst("C",69);
            print("Testar contains para elemento X");
            contains("X");
            print("Testar contains para elemento A");
            contains("A");
            print("Testar contains para elemento H");
            contains("H");
            print("Testar rank para o elemento menor");
            rank("A",0);
            print("Testar rank para o elemento maior");
            rank("X",7);
            print("Testar rank para um elemento nao existente");
            rank("Z",8);
            print("Testar rank para um elemento existente");
            rank("R",5);
            print("testar deleteMin()");
            st.deleteMin();
            size(7);
            Ncontains("A");
            print("testar deleteMax()");
            st.put("A",5);
            st.deleteMax();
            size(7);
            Ncontains("X");
            st.put("X",2);
            print("Testar Height 1");
            height(5);
            print("Testar Height 2");
            st.delete("C");
            height(5);
            print("Testar size");
            size(7);
            st.put("C",6);
            print("Testar Height 3");
            st.delete("M");
            height(4);
            print("Testar size");
            size(7);
            st.put("M",8);
            print("Testar get 1");
            get("C",6);
            print("Testar get 2");
            get("X",2);
            print("Testar get 3");
            get("A",5);
            print("Testar get 3");
            get("H",7);
            print("Testar ceiling(R)");
            ceiling("R","R");
            print("Testar floor(R)");
            floor("R","R");
            print("Testar ceiling(J)");
            ceiling("J","M");
            print("Testar ceiling(Q)");
            ceiling("Q", "R");
            print("Testar floor(G)");
            floor("G","E");
            print("Testar floor(D)");
            floor("D","C");
            print("Testar min()");
            minKey("A");
            print("Testar max()");
            maxKey("X");
            print("Testar select(3)");
            select("H",3);
            print("Testar select(7)");
            select("X",7);
            print("Testar select(0)");
            select("A",0);
            print("Testar keys()");
            Iterable<String> teste = Arrays.asList("A", "C", "E", "H", "M", "R", "S", "X");
            sort(teste);
            print("Testar keys(Args)");
            Iterable<String> teste2 = Arrays.asList("R", "S", "X");
            sortArgs(teste2,"R","X");
            print("Testar keys(Args) 2");
            Iterable<String> teste3 = Arrays.asList("C", "E", "H", "M", "R");
            sortArgs(teste3,"C","R");
            if(ERR_TESTS==0){ dindin+=10;saldoControlador.setSaldo(saldoControlador.getSaldo() + 10); printAddMoney(10.0, "Parabens Testes passados com sucesso!");}
            upInfo();
        }
        catch(TestAbort e){
            printI("Validador parou pois houve um erro");
            upInfo();
        }
    }


    /******************************************************
     *              FUNCOES AUXILIARES
     *******************************************************/

    private static void testarCodigo(String text) throws BadLocationException {
        if(text.startsWith("PaiPaga")){
            try{
                String valor = text.substring(7);
                double quantia=Double.parseDouble(valor);
                dindin+=quantia;
                saldoControlador.setSaldo(saldoControlador.getSaldo() + (float)quantia);

                printAddMoney(quantia, "Codigo Valido");
                printMomey();
            }
            catch(NumberFormatException e){printE("CodigoInvalido");}
        }
        else{printE("CodigoInvalido");}
    }

    private static void printMomey() throws BadLocationException {
        money.setText("");
        StyledDocument doc = money.getStyledDocument();
        Style style = money.addStyle("money", null);
        StyleConstants.setForeground(style, new Color(40, 167, 85));
        Style styleText = money.addStyle("green", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "💲 ", style);
        if(dindin > 99999999) dindin = 100000000;
        DecimalFormat df = new DecimalFormat("0.00");
        doc.insertString(doc.getLength(), String.valueOf(df.format(dindin)), styleText);
    }

    private static void printAddMoney(double quantity, String text) throws BadLocationException {
        StyledDocument doc = consola.getStyledDocument();
        Style style = consola.addStyle("money", null);
        StyleConstants.setForeground(style, new Color(40, 167, 85));
        Style styleText = consola.addStyle("green", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "[+" + quantity , styleText);
        doc.insertString(doc.getLength(), " 💲", style);
        doc.insertString(doc.getLength(), "] " , styleText);
        if(dindin > 99999999) dindin = 100000000.0;
        doc.insertString(doc.getLength(), text + "\n", styleText);
        printMomey();
    }

    public static void printS(String text) throws BadLocationException {
        StyledDocument doc = consola.getStyledDocument();
        Style style = consola.addStyle("Successo", null);
        StyleConstants.setForeground(style, new Color(40, 167, 85));
        Style styleText = consola.addStyle("Texto", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "[", styleText);
        doc.insertString(doc.getLength(), "✔", style);
        doc.insertString(doc.getLength(), "] ", styleText);

        // Adiciona o texto em branco
        doc.insertString(doc.getLength(), text + "\n", styleText);
    }
    public static void printE(String text) throws BadLocationException {
        StyledDocument doc = consola.getStyledDocument();
        Style style = consola.addStyle("Erro", null);
        StyleConstants.setForeground(style, new Color(220, 55, 95));
        Style styleText = consola.addStyle("Texto", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "[", styleText);
        doc.insertString(doc.getLength(), "❌", style);
        doc.insertString(doc.getLength(), "] ", styleText);

        doc.insertString(doc.getLength(), text + "\n", styleText);
    }
    public static void print(String text) throws BadLocationException {
        StyledDocument doc = consola.getStyledDocument(); //cria se um novo documento d estilo
        Style style = consola.addStyle("Info", null); //criamos um estilo chamado info
        StyleConstants.setForeground(style, new Color(88, 101, 242)); //o estilo info será apenas uma cor diferente do normal
        Style styleText = consola.addStyle("Texto", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "[", styleText);
        doc.insertString(doc.getLength(), "💭", style);
        doc.insertString(doc.getLength(), "] ", styleText);

        StyleConstants.setFontFamily(style,"consolas");
        doc.insertString(doc.getLength(), text + "\n", style);
    }
    public static void printI(String text) throws BadLocationException {
        StyledDocument doc = consola.getStyledDocument();
        Style style = consola.addStyle("Quoute",null);
        StyleConstants.setForeground(style, new Color(156, 118, 16));
        Style styleText = consola.addStyle("Texto", null);
        StyleConstants.setForeground(styleText, Color.WHITE);
        StyleConstants.setFontFamily(styleText,"consolas");
        doc.insertString(doc.getLength(), "[", styleText);
        doc.insertString(doc.getLength(), "⚠", style);
        doc.insertString(doc.getLength(), "] ", styleText);
        StyleConstants.setFontFamily(style,"consolas");
        doc.insertString(doc.getLength(), text + '\n', style);
    }
    private static void upbarra(){
        barra.setValue((int)(((double)SUC_TESTS/NR_TESTS)*100.0));
    }

    private static void upInfo() throws BadLocationException{
        StyledDocument doc = info.getStyledDocument();
        Style sucStyle = info.addStyle("Success Info", null);
        StyleConstants.setForeground(sucStyle, new Color(40, 167, 85));

        Style errStyle = info.addStyle("Error Info", null);
        StyleConstants.setForeground(errStyle, new Color(220, 55, 95));

        Style style = info.addStyle("Info", null);
        StyleConstants.setForeground(style, new Color(88, 101, 242));

        Style text = info.addStyle("text Info", null);
        StyleConstants.setForeground(text, Color.WHITE);

        Style grade = info.addStyle("grade", null);
        StyleConstants.setForeground(grade, new Color(200, 120, 242));

        StyleConstants.setFontFamily(text,"consolas");

        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), " [", text);
        doc.insertString(doc.getLength(), "🧾", style);
        doc.insertString(doc.getLength(), "] ", text);
        doc.insertString(doc.getLength(), "Numero Total de Testes: " + NR_TESTS + '\n', text);

        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), " [", text);
        doc.insertString(doc.getLength(), "✅", sucStyle);
        doc.insertString(doc.getLength(), "] ", text);
        doc.insertString(doc.getLength(), "Numero de Testes Passados: " + SUC_TESTS + '\n', text);

        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), " [", text);
        doc.insertString(doc.getLength(), "❌", errStyle);
        doc.insertString(doc.getLength(), "] ", text);
        doc.insertString(doc.getLength(), "Numero de Testes Chumbados: " + ERR_TESTS + '\n', text);

        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "\n", text);
        doc.insertString(doc.getLength(), "             [", text);
        doc.insertString(doc.getLength(), "🎲", grade);
        doc.insertString(doc.getLength(), "] ", text);
        if(SUC_TESTS == 0 && ERR_TESTS == 0){doc.insertString(doc.getLength(), "Nota: -.-" + '\n', text);}
        else{doc.insertString(doc.getLength(), "Nota: " + String.format( "%.1f",((double)SUC_TESTS/NR_TESTS)*20.0) + '\n', text);}
    }

    /******************************************************
     *               EXCESSAO DE EXIT
     *******************************************************/

    public static class TestAbort extends RuntimeException {
        public TestAbort(String mensagem) {
            super(mensagem);
        }
    }

    /******************************************************
     *                 TESTES LOGICOS
     *******************************************************/

    private static void vazia() throws BadLocationException {
        if(st.isEmpty()){printS("IsEmpty correto para uma arvore vazia"); SUC_TESTS++;}
        else{printE("IsEmpty devolveu " + st.isEmpty() + " devia ter devolvido <true>"); ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void Nvazia() throws BadLocationException {
        if(!st.isEmpty()){printS("IsEmpty correto para uma arvore nao vazia"); SUC_TESTS++;}
        else{printE("IsEmpty devolveu " + st.isEmpty() + " devia ter devolvido <false>"); ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void AddExst(String key, int value) throws BadLocationException {
        st.put(key,value);
        if(st.get(key) == value){printS("pu correto para elemento existente"); SUC_TESTS++;}
        else{printE("put devia ter valor" + value + " mas tem" + st.get(key)); ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void contains(String key) throws BadLocationException {
        if(st.contains(key)){printS("contains correto para um elemento existente"); SUC_TESTS++;}
        else{printE("contains devolveu " + st.contains(key) + " devia ter devolvido <true>"); ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void Ncontains(String key) throws BadLocationException {
        if(!st.contains(key)){printS("contains correto para um elemento nao existente"); SUC_TESTS++;}
        else{printE("contains devolveu " + st.contains(key) + " devia ter devolvido <false>"); ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void maxKey(String key) throws BadLocationException {
        if(st.max().equals(key)){printS("max() correto para uma arvore com 1 elemento"); SUC_TESTS++;}
        else{printE("max() incorreto, devolveu: " + st.max() + " expected: " + key);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
    }
    private static void minKey(String key) throws BadLocationException {
        if(st.min().equals(key)){printS("min() correto para uma arvore com 1 elemento"); SUC_TESTS++;}
        else{printE("min() incorreto, devolveu: " + st.min() + " expected: " + key);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void get(String key, int value) throws BadLocationException {
        if(st.get(key) == value){printS("get() correto para a key: " + key); SUC_TESTS++;}
        else{printE("get() incorreto, devolveu: " + st.get(key) + " expected: " + value);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void size(int size) throws BadLocationException {
        if(st.size() == size){printS("size() correto"); SUC_TESTS++;}
        else{printE("size() incorreto, devolveu: " + st.size() + " expected: " + size);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void colocElem(){
        st.put("S",1);st.put("X",2);st.put("E",3);st.put("R",4);st.put("A",5);st.put("C",6); st.put("H",7);
        st.put("M",8);
    }
    private static void rank(String key,int rank) throws BadLocationException {
        if(st.rank(key) == rank){printS("rank() correto"); SUC_TESTS++;}
        else{printE("rank("+key+") incorreto, devolveu: " + st.rank(key) + " expected: " + rank);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void height(int hg) throws BadLocationException {
        if(st.height() == hg){printS("height() correto"); SUC_TESTS++;}
        else{printE("height() incorreto, devolveu: " + st.height() + " expected: " + hg);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void floor(String key, String res) throws BadLocationException {
        if(st.floor(key).equals(res)){printS("floor() correto"); SUC_TESTS++;}
        else{printE("floor() incorreto, devolveu: " + st.floor(key) + " expected: " + res);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void ceiling(String key, String res) throws BadLocationException {
        if(st.ceiling(key).equals(res)){printS("ceiling() correto"); SUC_TESTS++;}
        else{printE("ceiling() incorreto, devolveu: " + st.ceiling(key) + " expected: " + res);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }
    private static void sort(Iterable<String> teste) throws BadLocationException {
        Iterable<String> opcIterable = st.keys();
        Iterator<String> res = teste.iterator();
        Iterator<String> opc = opcIterable.iterator();
        int i=1;
        int test=0;
        while(res.hasNext() && opc.hasNext()){
            String resString = res.next();
            String opcString = opc.next();
            if(!(resString.equals(opcString))){printE("NRº " + i + " Keys() diferente devolveu: " + opcString + " expected: " + resString);ERR_TESTS++;test++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
            i++;
        }
        if(test == 0){printS("keys() correto"); SUC_TESTS++;}
        upbarra();
    }
    private static void sortArgs(Iterable<String> teste, String lo, String hi) throws BadLocationException {
        Iterable<String> opcIterable = st.keys(lo,hi);
        Iterator<String> res = teste.iterator();
        Iterator<String> opc = opcIterable.iterator();
        int i=1;
        int test=0;
        while(res.hasNext() && opc.hasNext()){
            String resString = res.next();
            String opcString = opc.next();
            if(!(resString.equals(opcString))){printE("NRº " + i + " Keys() diferente devolveu: " + opcString + " expected: " + resString);ERR_TESTS++;test++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
            i++;
        }
        if(test == 0){printS("keys(Args) correto"); SUC_TESTS++;}
        upbarra();
    }
    private static void select(String key, int rank) throws BadLocationException {
        if(st.select(rank).equals(key)){printS("select() correto"); SUC_TESTS++;}
        else{printE("select() incorreto, devolveu: " + st.select(rank) + " expected: " + key);ERR_TESTS++; if(EXIT_ON_ERROR == 1){throw new TestAbort("");}}
        upbarra();
    }

    /******************************************************
     *              TESTES METODOS/CLASS
     *******************************************************/
    public static void existMethods() throws BadLocationException{
        try {
            Class<?> clazz = Class.forName("ST");
            printS("Classe ST encontrada");
            SUC_TESTS++;
            upbarra();
            try{
                Constructor<?> bob = clazz.getConstructor();
                printS("Construtor encontrado");
                SUC_TESTS++;
            }
            catch(NoSuchMethodException e){
                printE("Construtor não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("put", Comparable.class, Object.class);
                printS("Método put encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método put não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("get", Comparable.class);
                printS("Método get encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método get não encontrado");
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
                ERR_TESTS++;
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("delete", Comparable.class);
                printS("Método delete encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método delete não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("contains", Comparable.class);
                printS("Método contains encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método contains não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("isEmpty");
                printS("Método isEmpty encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método isEmpty não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("size");
                printS("Método size encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método size não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("height");
                printS("Método height encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método height não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("min");
                printS("Método min encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método min não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("max");
                printS("Método max encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método max não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("floor", Comparable.class);
                printS("Método floor encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método floor não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("ceiling", Comparable.class);
                printS("Método ceiling encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método ceiling não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("rank", Comparable.class);
                printS("Método rank encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método rank não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("select", int.class);
                printS("Método select encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método select não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("deleteMin");
                printS("Método deleteMin encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método deleteMin não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("deleteMax");
                printS("Método deleteMax encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método deleteMax não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("size", Comparable.class, Comparable.class);
                printS("Método size(Key, Key) encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método size(Key, Key) não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("keys", Comparable.class, Comparable.class);
                printS("Método keys(Key, Key) encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método keys(Key, Key) não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();
            try {
                Method metodo = clazz.getMethod("keys");
                printS("Método keys encontrado");
                SUC_TESTS++;
            }
            catch (NoSuchMethodException e) {
                printE("Método keys não encontrado");
                ERR_TESTS++;
                if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
            }
            upbarra();

        }
        catch (ClassNotFoundException e) {
            printE("Class ST não econtrada");
            ERR_TESTS++;
            if(EXIT_ON_ERROR == 1){throw new TestAbort("");}
        }
    }

    /******************************************************
     *                     ROLETA
     *******************************************************/

    static class roletaWindow extends JFrame{ //criar uma nova opcai do Java Swing
        private final String[] opcoes = {"1.1","0.5","2.0","1.25", "0.75", "1.5", "0.25", "5.0" , "0.25" ,"1.4", "0.15","1.75"};
        private double angulo;
        private double velocidade;
        private Timer timer;
        private final JPanel panelRoleta; // dentro dessa nova opcao temos JPanel

        public roletaWindow(){ //quandoi abrirmos a nova opcao isto vai rodar
            setSize(400,400);
            setTitle("Gamblezito");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            getContentPane().setBackground(new Color(45,45,45));

            panelRoleta=new JPanel() { //configurando o JPanel temos:
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g); //limpamos tudo oq pode vir de oprigem
                    int raio=250;//raio da roda
                    int x = (getWidth() - raio)/2; //centor da roda
                    int y = (getHeight() - raio) / 2;
                    double anguloSetor= 360.0/opcoes.length; //angulo que cada setor ocupa
                    Graphics2D g2 = (Graphics2D) g; //criamos um novo grafico 2D
                    //configuramos o antialising e a fonte do texto da roleta
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setFont(new Font("consolas", Font.BOLD, 14));

                    FontMetrics fm = g2.getFontMetrics(); //serve para ter a altura da fonte
                    double raioD = raio * 0.3;

                    for(int i =0; i < opcoes.length; i ++){ //aqui vamos percorrer cada setor e pintar lo colocando tambem o text
                        double startAngle = angulo + i * anguloSetor;
                        g2.setColor(i%2 == 0?  new Color(255, 140, 0) : new Color(220, 20, 60));
                        g2.fillArc(x,y,raio,raio,(int)(startAngle), (int)anguloSetor); //pintamos o setor i

                        double anguloTexto = startAngle + anguloSetor / 2;
                        String texto = opcoes[i];
                        int larguraTexto = fm.stringWidth(texto);
                        int alturaTexto = fm.getAscent();

                        double centroX = getWidth() / 2 + raioD * Math.cos(-Math.toRadians(anguloTexto));
                        double centroY = getHeight() / 2 + raioD * Math.sin(-Math.toRadians(anguloTexto));

                        // Criar uma transformação de rotação para o texto ficar sempre com o placement correto
                        AffineTransform originalTransform = g2.getTransform();
                        AffineTransform rotateTransform = new AffineTransform();
                        rotateTransform.rotate(-Math.toRadians(anguloTexto), centroX, centroY); // Rotacionar em torno do centro do texto
                        g2.transform(rotateTransform);

                        // Desenhar o texto, agora rotacionado
                        int textoX = (int) (centroX - larguraTexto / 2);
                        int textoY = (int) (centroY + alturaTexto / 2);
                        g2.setColor(Color.BLACK);
                        g2.drawString(texto, textoX, textoY);

                        // Restaurar a transformação original para os próximos desenhos
                        g2.setTransform(originalTransform);

                        //criamos uma borda para a roda
                        g2.setColor(new Color(88, 101, 242));
                        g2.setStroke(new BasicStroke(4));
                        g2.drawArc(x,y,raio,raio,0,360);

                        //criamos uma seta
                        int centerX = getWidth() / 2;
                        int topoY = y - 10; // ligeiramente acima da roleta

                        Polygon seta = new Polygon();
                        seta.addPoint(centerX - 10, topoY); // canto esquerdo
                        seta.addPoint(centerX + 10, topoY); // canto direito
                        seta.addPoint(centerX, topoY + 20);

                        g2.setColor(Color.BLACK);
                        g2.fillPolygon(seta);

                    }
                }
            };
            panelRoleta.setBounds(50,10,300,300);
            panelRoleta.setBackground(new Color(45,45,45));
            panelRoleta.setOpaque(false);
            panelRoleta.setDoubleBuffered(true);

            JTextField inp = new JTextField();
            inp.setBackground(new Color(30,30,30));
            inp.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
            inp.setForeground(Color.WHITE);
            inp.setFont(new Font("Fira Code",Font.BOLD,20));
            inp.setHorizontalAlignment(JTextField.CENTER);
            inp.setBounds(217, 300, 150, 50);

            int[] escolha = new int[1];
            float[] aposta = new float[1];
            JButton girar = new JButton("DA'LE GAAZZ");
            girar.setBackground(new Color(30,30,30));
            girar.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242),2));
            girar.setForeground(new Color(220,220,220));
            girar.setBounds(15,300,150,50);
            girar.setFont(new Font("consolas",Font.BOLD,20));
            girar.setFocusPainted(false);
            girar.addActionListener(e-> {
                if(eNumero(inp.getText())) {
                    aposta[0]=Float.parseFloat(inp.getText());
                    if(aposta[0]<=dindin) {
                        dindin -= aposta[0];
                        saldoControlador.setSaldo((float) dindin);
                        try {
                            printMomey();
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                        Random r = new Random();
                        escolha[0] = r.nextInt(2) + 1;
                        velocidade = 15 + Math.random() * 10;
                        timer.start();
                    }
                    else{
                        try {
                            printE("PARECES ATRASADO");
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else{
                    try {
                        printE("APOSTA INVALIDA");
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });


            //a cada 10ms vai ser executada o que esta depois do e->
            timer = new Timer(10, e-> {
                angulo += velocidade; //faz se a rotacao
                velocidade *= 0.975; //diminui se a velocidade
                panelRoleta.repaint();
                if(velocidade < 0.1){ //qunando tiver parado
                    timer.stop();//paramos o timer para nao se repeir
                    int setor = (int)((360 - (angulo-90) % 360) / (360.0 / opcoes.length)) % opcoes.length; //calculamos o setor
                    double[] gamb = {Double.parseDouble(opcoes[setor])};

                    JDialog resultado = new JDialog((Frame)null , "Resultado");
                    resultado.setSize(350,200);
                    resultado.getContentPane().setBackground(new Color(30,30,30));
                    resultado.setLayout(null);
                    resultado.setLocationRelativeTo(null);

                    JLabel label = new JLabel("", CENTER);
                    if(gamb[0] > 1 && gamb[0] < 5){label.setText("[X"+gamb[0]+"] "+"Completo Iludido");}
                    else if(gamb[0] == 5){label.setText("[X"+gamb[0]+"] "+"Completo Vendado, Ridiculo");}
                    else if(gamb[0] < 1){label.setText("[X"+gamb[0]+"] "+"Vai maze estudar");}
                    else if(gamb[0] == 1){label.setText("[X"+gamb[0]+"] "+"És um gajo equilibrado");}
                    label.setForeground(new Color(88 ,101 ,242));
                    label.setFont(new Font("consolas",Font.BOLD,15));
                    label.setBounds(0, 10, 350, 25);

                    JButton ok = new JButton("OK");
                    ok.setBackground(new Color(15, 17, 40));
                    ok.setForeground(new Color(220,220,220));
                    ok.setFocusPainted(false);
                    ok.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
                    ok.setBounds(150,100,50,50);
                    ok.setFont(new Font("consolas",Font.BOLD,20));
                    ok.addActionListener(ex-> {
                        dindin = dindin + aposta[0] * gamb[0];
                        saldoControlador.setSaldo((float)dindin);
                        try {
                            printMomey();
                            if(gamb[0] >= 1)
                                printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e ganhaste + " + (aposta[0] * gamb[0] - aposta[0]));
                            else
                                printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e perdeste " + + (aposta[0] - aposta[0] * gamb[0]));
                        } catch (BadLocationException err) {
                            System.err.println("Ja ta tudo fudido");
                        }
                        resultado.setVisible(false);
                    });

                    if(gamb[0] > 1.0) {
                        JButton arrisca = new JButton("Arrisca Tudo");
                        arrisca.setBackground(new Color(15, 17, 40));
                        arrisca.setForeground(new Color(220, 220, 220));
                        arrisca.setFocusPainted(false);
                        arrisca.setBorder(BorderFactory.createLineBorder(new Color(88, 101, 242), 2));
                        arrisca.setBounds(75, 40, 200, 50);
                        arrisca.setFont(new Font("consolas", Font.BOLD, 20));
                        arrisca.addActionListener(ex -> {
                            JDialog arrPanel = new JDialog(resultado, "Arriscou Tudoo");
                            arrPanel.setSize(350, 200);
                            arrPanel.getContentPane().setBackground(new Color(30,30,30));
                            arrPanel.setLocationRelativeTo(null);
                            arrPanel.setLayout(null);

                            JLabel labelChoice = new JLabel("2X OU NADA");
                            labelChoice.setForeground(new Color(88 ,101 ,242));
                            labelChoice.setFont(new Font("consolas",Font.BOLD,15));
                            labelChoice.setBounds(127, 5, 150, 25);
                            labelChoice.setHorizontalTextPosition(CENTER);
                            label.setFont(new Font("consolas", Font.BOLD, 16));

                            JButton Opc1 = new JButton("👾");
                            Opc1.setBackground(new Color(15, 17, 40));
                            Opc1.setForeground(new Color(220, 220, 220));
                            Opc1.setFont(new Font("Segoe UI Emoji",Font.BOLD,15));
                            Opc1.setFocusPainted(false);
                            Opc1.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            Opc1.setBorder(BorderFactory.createLineBorder(new Color(157, 88, 242), 2));
                            Opc1.setBounds(40, 25, 75, 100);
                            Opc1.addActionListener(o1 -> {
                                if (escolha[0] == 1) {
                                    gamb[0] = gamb[0] * 2;
                                    dindin = dindin + aposta[0] * gamb[0];
                                    saldoControlador.setSaldo((float)dindin);
                                    try {
                                        printMomey();
                                        if(gamb[0] >= 1)
                                            printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e ganhaste + " + (aposta[0] * gamb[0] - aposta[0]));
                                        else
                                            printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e perdeste " + + (aposta[0] - aposta[0] * gamb[0]));
                                    } catch (BadLocationException err) {
                                        System.err.println("Ja ta tudo fudido");
                                    }
                                }
                                else {
                                    gamb[0] = 0;
                                    try {
                                        printMomey();
                                    } catch (BadLocationException err) {
                                        System.err.println("Ja ta tudo fudido");
                                    }
                                }
                                arrPanel.setVisible(false);
                                resultado.setVisible(false);

                                JDialog resultadoArr1 = new JDialog((Frame)null , "Resultadoo");
                                resultadoArr1.setSize(200,100);
                                resultadoArr1.getContentPane().setBackground(new Color(30,30,30));
                                resultadoArr1.setLayout(null);
                                resultadoArr1.setLocationRelativeTo(null);

                                JLabel labelArr1 = new JLabel("[X"+ gamb[0] + "]");
                                labelArr1.setForeground(new Color(88 ,101 ,242));
                                labelArr1.setFont(new Font("consolas",Font.BOLD,15));
                                labelArr1.setBounds(65, 10, 350, 25);
                                resultadoArr1.add(labelArr1);
                                resultadoArr1.setVisible(true);
                            });

                            JButton Opc2 = new JButton("🤡");
                            Opc2.setFont(new Font("Segoe UI Emoji",Font.BOLD,15));
                            Opc2.setBackground(new Color(15, 17, 40));
                            Opc2.setForeground(new Color(220, 220, 220));
                            Opc2.setFocusPainted(false);
                            Opc2.setBounds(220, 25, 75, 100);
                            Opc2.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            Opc2.setBorder(BorderFactory.createLineBorder(new Color(157, 88, 242), 2));
                            Opc2.addActionListener(o1 -> {
                                if (escolha[0] == 2) {
                                    gamb[0] = gamb[0] * 2;
                                    dindin = dindin + aposta[0] * gamb[0];
                                    saldoControlador.setSaldo((float)dindin);
                                    try {
                                        printMomey();
                                        if(gamb[0] >= 1)
                                            printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e ganhaste + " + (aposta[0] * gamb[0] - aposta[0]));
                                        else
                                            printAddMoney(aposta[0] * gamb[0], "Apostaste " + aposta[0] + "$ e perdeste " + + (aposta[0] - aposta[0] * gamb[0]));

                                    } catch (BadLocationException err) {
                                        System.err.println("Ja ta tudo fudido");
                                    }
                                }
                                else {
                                    gamb[0] = 0;
                                    try {
                                        printMomey();
                                    } catch (BadLocationException err) {
                                        System.err.println("Ja ta tudo fudido");
                                    }
                                }
                                arrPanel.setVisible(false);
                                resultado.setVisible(false);

                                JDialog resultadoArr2 = new JDialog((Frame)null , "Resultadoo");
                                resultadoArr2.setSize(200,100);
                                resultadoArr2.getContentPane().setBackground(new Color(30,30,30));
                                resultadoArr2.setLayout(null);
                                resultadoArr2.setLocationRelativeTo(null);

                                JLabel labelArr2 = new JLabel("[X"+ gamb[0] + "]");
                                labelArr2.setForeground(new Color(88 ,101 ,242));
                                labelArr2.setFont(new Font("consolas",Font.BOLD,15));
                                labelArr2.setBounds(65, 10, 350, 25);
                                resultadoArr2.add(labelArr2);
                                resultadoArr2.setVisible(true);
                            });

                            arrPanel.add(labelChoice);
                            arrPanel.add(Opc1);
                            arrPanel.add(Opc2);
                            arrPanel.setVisible(true);
                        });
                        resultado.add(arrisca);
                    }

                    resultado.add(ok);
                    resultado.add(label);
                    resultado.setVisible(true);

                }
            });
            setLayout(null);
            add(panelRoleta);
            add(girar);
            add(inp);
            setVisible(true);
        }

    }

    /******************************************************
     *                   SALVAR MONEY
     *******************************************************/
    public static class saldoControlador{
        private static final String fileName = "valInfo.dat";
        private static float dindin;

        public static void run(){
            try{
                dindin = loadDinDin();
            }
            catch(IOException e){
                dindin=500f;
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                try{
                    saveDinDin(dindin);
                }
                catch(IOException e){
                    System.err.println("ERRO DE MERDA");
                }
            }));
        }
        private static float loadDinDin() throws IOException{
            DataInputStream in = new DataInputStream(new FileInputStream(fileName));
            float valor = in.readFloat();
            in.close();
            return valor;
        }

        private static void saveDinDin(float money) throws IOException {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            out.writeFloat(money);
            out.close();
        }

        public static float getSaldo() {
            return dindin;
        }

        public static void setSaldo(float valor) {
            dindin = valor;
        }
    }

    public static boolean eNumero(String s) {
        try {
            Double.parseDouble(s); // ou Integer.parseInt(s) se quiseres só inteiros
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}