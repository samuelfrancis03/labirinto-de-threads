import java.util.Random;
public class Rato implements Runnable{
    private int linha;
    private int coluna;
    private int linhaAnterior;
    private int colunaAnterior;

    private Boolean cima = true;
    private Boolean baixo = true;
    private Boolean direira = true;
    private Boolean esquerda = true;

    public Rato() {
        Random random = new Random();
        linha = random.nextInt();
        coluna = random.nextInt();
        linhaAnterior = linha;
        colunaAnterior = coluna;
    }


}
