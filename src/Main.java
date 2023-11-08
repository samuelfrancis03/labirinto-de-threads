import javax.swing.*;

public class Main {
    public static void main(String[] args) {

    int larguraMatriz = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de linhas do labirinto: "));
    int alturaMatriz = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de colunas do labirinto: "));
    int qtdRatos = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de ratos: "));
    Labirinto l = new Labirinto(larguraMatriz, alturaMatriz, qtdRatos);

    }
}
