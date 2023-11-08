import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
        ratos = new ArrayList<Rato>();
        lock = new ReentrantLock();

        gerarLabirinto();

        //Adicionar o ratos conforme o numero informado
        for (int i = 0; i < numRatos; i++) {
            Rato rato = new Rato();
            ratos.add(rato);
            Thread threadRato = new Thread(rato);
            threadRato.start();
        }

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

        //Desenhar ratos
        for (Rato rato : ratos) {
            int x = (rato.getColuna() + 1) * tamanho;
            int y = (rato.getLinha() + 1) * tamanho;

            g.setColor(Color.darkGray);
            g.fillOval(x, y, tamanho, tamanho);
        }
    }



    private class Rato implements Runnable {
        private int linha;
        private int coluna;
        private int ultimaLinha;
        private int ultimaColuna;

        private Boolean cima = false;
        private Boolean baixo = false;
        private Boolean direita = false;
        private Boolean esquerda = false;

        public Rato() {
            Random random = new Random();
            linha = random.nextInt(altura); //gera uma posição aleatoria para o rato, coforme a dimensão da matriz
            coluna = random.nextInt(largura);//gera uma posição aleatoria para o rato, coforme a dimensão da matriz
            ultimaLinha = linha; //armazena ultima linha percorrida
            ultimaColuna = coluna;//armazena ultima coluna percorrida
        }

        public int getLinha() {
            return linha;
        }

        public int getColuna() {
            return coluna;
        }

        //Iniciar a thread
        @Override
        public void run() {
            while (dimensaoLabirinto[linha][coluna] != 'Q') {
                try {
                    Thread.sleep(900); // Delay para visualização
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock(); // Bloquear o acesso ao labirinto - para não acessar outra trhead

                int direcao = new Random().nextInt(4); // 0: cima, 1: baixo, 2: esquerda, 3: direita

                // Movimentar o rato
                switch (direcao) {
                    //O metodo verificaMovimentar vai realizar a validação dos espaços que não tem '#' parede
                    //e que não tem 'X' caminhos já percorridos
                    //Decremento ou incremento das l ou c, para que o rato não eatraves paredes
                    case 0: //Cima
                        if (verificaMovimentar(linha - 1, coluna)) {
                            dimensaoLabirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            cima = true;
                            linha--;
                        }else {
                            cima = false;}
                        break;
                    case 1: // Baixo
                        if (verificaMovimentar(linha + 1, coluna)) {
                            dimensaoLabirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            baixo = true;
                            linha++;
                        }else {
                            baixo = false;}
                        break;
                    case 2: // Esquerda
                        if (verificaMovimentar(linha, coluna - 1)) {
                            dimensaoLabirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            esquerda = true;
                            coluna--;
                        }else {
                            esquerda = false;}
                        break;
                    case 3: // Direita
                        if (verificaMovimentar(linha, coluna + 1)) {
                            dimensaoLabirinto[linha][coluna] = 'X'; // Marcar o caminho percorrido
                            direita = true;
                            coluna++;
                        }else {
                            direita = false;}
                        break;
                }

                lock.unlock(); // Desbloquear o acesso ao labirinto - para que seja executa outra thread

                repaint(); // Atualizar os movimentos realizados
            }

            JOptionPane.showMessageDialog(null, "O RATINHOOO encontrou o queijo! RAPPPAIIIZ");
            System.exit(0);
        }

        private boolean verificaMovimentar(int linha, int coluna) { //verifica se os ratos não estão nas extremidades e caminhos que não tem parede e caminhos já percorridos
            if (linha >= 0 && linha < altura && coluna >= 0 && coluna < largura &&
                    dimensaoLabirinto[linha][coluna] != '#' && dimensaoLabirinto[linha][coluna] != 'X') {

                return true;
            }
            if (linha >= 0 && linha < altura && coluna >= 0 && coluna < largura && //Para não bugar se inicializar as var de movimentações em false
                    dimensaoLabirinto[linha][coluna] != '#' &&
                    !cima && !baixo && !direita && !esquerda
                    && dimensaoLabirinto[linha][coluna] == 'X'){
                return true;
            }
            return false;
        }

    }

}


































