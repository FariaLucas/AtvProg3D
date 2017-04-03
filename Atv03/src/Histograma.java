import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lucas on 02/04/2017.
 */
public class Histograma {


    public static final String Path = "C:\\Users\\LUCAS\\Pictures\\img";

    int [] calculaHistograma(BufferedImage img){
        int[] histograma = new int [256];

        for(int y = 0; y<img.getHeight(); y++)
            for(int x = 0; x<img.getWidth(); x++){
            Color cor = new Color(img.getRGB(x,y));
            int red = cor.getRed();
            histograma[red] +=1;
            }
        return histograma;
    }
    public int[] calculaHistogramaAcumulado(int[] histograma){
        int[] acumulado = new int[256];
        acumulado[0] = histograma[0];
        for(int i = 1;i<histograma.length; i++)
            acumulado[i] = histograma[i] + acumulado[i-1];
        return acumulado;

    }

    private int menorValor(int[] histograma){
        for(int i = 0; i<histograma.length; i++)
            if(histograma[i] !=0)
                return histograma[i];
        return 0;
    }

    int [] calculaMapadeCores(int[] histograma, int pixels){
        int[] mapaDeCores = new int[256];
        int[] acumulado = calculaHistogramaAcumulado(histograma);
        float menor = menorValor(histograma);

        for(int i =0; i<histograma.length; i++)
            mapaDeCores[i] = Math.round(((acumulado[i] - menor)/(pixels-menor))*255);
        return  mapaDeCores;
    }
    BufferedImage equalizar(BufferedImage img) throws IOException{
        int[] histograma = calculaHistograma(img);
        int[] mapaDeCores = calculaMapadeCores(histograma, img.getWidth()*img.getHeight());
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y<img.getHeight(); y++)
            for(int x = 0; x<img.getWidth(); x++){
            Color cor = new Color (img.getRGB(x,y));
            int tom = cor.getRed();

            int novoTom = mapaDeCores[tom];
            Color novaCor = new Color (novoTom, novoTom, novoTom);
            out.setRGB(x, y, novaCor.getRGB());
            }
        return out;
    }

    void run()throws IOException {
        BufferedImage img = ImageIO.read(new File(Path,"DBZ.png"));
    }

    public static void main (String[] args)throws IOException{
        new Histograma().run();
    }
}
