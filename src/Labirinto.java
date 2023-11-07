import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Labirinto extends JFrame {
    public int largura;
    public int altura;
    private int tamanho = 100;

    private int numRatos;
    private int[][] dimensaoLabirinto;
    private List<Rato> ratos;
    private Lock lock;

    public Labirinto(int largura, int altura, int numRatos) {
        this.largura = largura;
        this.altura = altura;
        this.numRatos = numRatos;
        this.tamanho = tamanho;

        setTitle("Labirinto Rato"); //Titulo da interface
        setVisible(true); //Mostra interface
        setSize(largura * tamanho, altura * tamanho); //Definir tamanho
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Encerrar o progama ao fechar a janela
        setResizable(false); //Desabilita redisionamento da tela
        setLocationRelativeTo(null); //Tela aparece no meio

        dimensaoLabirinto = new int[altura][largura];
        ratos = new ArrayList<>();
        lock = new ReentrantLock();

        gerarLabirinto();

    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getNumRatos() {
        return numRatos;
    }

    public void setNumRatos(int numRatos) {
        this.numRatos = numRatos;
    }


    private void gerarLabirinto() {
        Random random = new Random();

        // Inicializar labirinto com espaços vazios
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                dimensaoLabirinto[i][j] = ' ';
            }
        }

        // Adicionar paredes aleatoriamente
        for (int i = 1; i < altura; i++) {
            for (int j = 1; j < largura; j++) {
                if (random.nextDouble() < 0.3) {
                    dimensaoLabirinto[i][j] = '#';
                }
            }
        }

        // Posicionar queijo na posição inicial
        dimensaoLabirinto[0][0] = 'Q';
    }

    //Sobrescreve o metodo dos elementos da janela, permitindo escolher as cores
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < largura; j++) {
                int x = (j + 1) * tamanho;
                int y = (i + 1) * tamanho;

                //COR PAREDE
                if (dimensaoLabirinto[i][j] == '#') {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, tamanho, tamanho);

                    //COR QUEIJO
                } else if (dimensaoLabirinto[i][j] == 'Q') {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x, y, tamanho, tamanho);

                    //COR  CAMINHO PERCORRIDO
                } else if (dimensaoLabirinto[i][j] == 'X') {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, tamanho, tamanho);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, tamanho, tamanho);
                }
            }
        }
    }

     //Desenhar ratos
        for (Rato rato : ratos) {
        int x = (rato.getColuna() + 1) * TAMANHO_CELULA;
        int y = (rato.getLinha() + 1) * TAMANHO_CELULA;

        g.setColor(Color.gray);
        g.fillOval(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
    }







}

































