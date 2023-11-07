import javax.swing.*;

public class Main {
    public static void main(String[] args) {



    int larguraMatriz = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de linhas da matriz: "));
    int alturaMatriz = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de colunas da matriz: "));

    Labirinto l = new Labirinto(larguraMatriz, alturaMatriz);
    Rato r = new Rato();



    }
}
