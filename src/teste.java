import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Labirinto extends JFrame {
    private static final int TAMANHO_CELULA = 30;
    private static final int NUMERO_RATOS = 5;
    private static final int LARGURA_LABIRINTO = 5;
    private static final int ALTURA_LABIRINTO = 5;

    private char[][] labirinto;
    private List<Rato> ratos;
    private Lock lock;

    public Labirinto() {
        setTitle("Labirinto");
        setSize((LARGURA_LABIRINTO + 2) * TAMANHO_CELULA, (ALTURA_LABIRINTO + 2) * TAMANHO_CELULA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        labirinto = new char[ALTURA_LABIRINTO][LARGURA_LABIRINTO];
        ratos = new ArrayList<>();
        lock = new ReentrantLock();

        // Gerar o labirinto
        gerarLabirinto();

        // Adicionar ratos
        for (int i = 0; i < NUMERO_RATOS; i++) {
            Rato rato = new Rato();
            ratos.add(rato);
            Thread threadRato = new Thread(rato);
            threadRato.start();
        }

        setVisible(true);
    }

    private void gerarLabirinto() {
        Random random = new Random();

        // Inicializar labirinto com espaços vazios
        for (int i = 0; i < ALTURA_LABIRINTO; i++) {
            for (int j = 0; j < LARGURA_LABIRINTO; j++) {
                labirinto[i][j] = ' ';
            }
        }

        // Adicionar paredes aleatoriamente
        for (int i = 1; i < ALTURA_LABIRINTO; i++) {
            for (int j = 1; j < LARGURA_LABIRINTO; j++) {
                if (random.nextDouble() < 0.1) { // Aumentamos para 30% para ter mais paredes
                    labirinto[i][j] = '#';
                }
            }
        }

        // Posicionar queijo na posição inicial
        labirinto[0][0] = 'Q';
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < ALTURA_LABIRINTO; i++) {
            for (int j = 0; j < LARGURA_LABIRINTO; j++) {
                int x = (j + 1) * TAMANHO_CELULA;
                int y = (i + 1) * TAMANHO_CELULA;

                if (labirinto[i][j] == '#') {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
                } else if (labirinto[i][j] == 'Q') {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
                } else if (labirinto[i][j] == 'X') {
                    g.setColor(Color.ORANGE);
                    g.fillRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
                }
            }
        }

        // Desenhar ratos
        for (Rato rato : ratos) {
            int x = (rato.getColuna() + 1) * TAMANHO_CELULA;
            int y = (rato.getLinha() + 1) * TAMANHO_CELULA;

            g.setColor(Color.gray);
            g.fillOval(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
        }
    }

    private class Rato implements Runnable {
        private int linha;
        private int coluna;
        private int ultimaLinha;
        private int ultimaColuna;

        private Boolean verificaCima = true;
        private Boolean verificaBaixo = true;
        private Boolean verificaDireita = true;
        private Boolean verificaEsquerda = true;

        public Rato() {
            Random random = new Random();
            linha = random.nextInt(ALTURA_LABIRINTO);
            coluna = random.nextInt(LARGURA_LABIRINTO);
            ultimaLinha = linha;
            ultimaColuna = coluna;
        }

        public int getLinha() {
            return linha;
        }

        public int getColuna() {
            return coluna;
        }

        @Override
        public void run() {
            while (labirinto[linha][coluna] != 'Q') {
                try {
                    Thread.sleep(500); // Delay para visualização
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock(); // Bloquear o acesso ao labirinto

                int direcao = new Random().nextInt(4); // 0: cima, 1: baixo, 2: esquerda, 3: direita

                // Movimentar o rato
                switch (direcao) {
                    case 0: // Cima
                        if (podeMover(linha - 1, coluna)) {
                            labirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            verificaCima = true;
                            linha--;
                        }else {verificaCima = false;}
                        break;
                    case 1: // Baixo
                        if (podeMover(linha + 1, coluna)) {
                            labirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            verificaBaixo = true;
                            linha++;
                        }else {verificaBaixo = false;}
                        break;
                    case 2: // Esquerda
                        if (podeMover(linha, coluna - 1)) {
                            labirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            verificaEsquerda = true;
                            coluna--;
                        }else {verificaEsquerda = false;}
                        break;
                    case 3: // Direita
                        if (podeMover(linha, coluna + 1)) {
                            labirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            verificaDireita = true;
                            coluna++;
                        }else {verificaDireita = false;}
                        break;
                }

                lock.unlock(); // Desbloquear o acesso ao labirinto

                repaint(); // Atualizar a interface gráfica
            }

            JOptionPane.showMessageDialog(null, "Um rato encontrou o queijo!");
            System.exit(0);
        }

        private boolean podeMover(int linha, int coluna) {
            if (linha >= 0 && linha < ALTURA_LABIRINTO && coluna >= 0 && coluna < LARGURA_LABIRINTO &&
                    labirinto[linha][coluna] != '#' && labirinto[linha][coluna] != 'X') {

                return true;
            }
            if (linha >= 0 && linha < ALTURA_LABIRINTO && coluna >= 0 && coluna < LARGURA_LABIRINTO &&
                    labirinto[linha][coluna] != '#' &&
                    verificaCima == false && verificaBaixo == false && verificaDireita == false && verificaEsquerda == false
                    && labirinto[linha][coluna] == 'X'){
                return true;
            }
            return false;
        }

    }

    public static void main(String[] args) {
        new Labirinto();
    }
}
