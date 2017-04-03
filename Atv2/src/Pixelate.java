

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Lucas on 01/04/2017.
 */


public class Pixelate {

    static float contraste[][] = {
            { 0, -1,  0},
            {-1,  5, -1},
            { 0, -1,  0}
    };

    public static int saturate(int value){
        if (value > 255)
            return 255;
        if(value < 0)
            return 0;
        else
            return value;
    }


    static Color applyKernel(Color[][] colors, float[][] kernel) {

        Color novaCores[][] = new Color[3][3];
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++) {
                int r = saturate((int) (colors[x][y].getRed() * kernel[x][y]));
                int g = saturate((int) (colors[x][y].getGreen() * kernel[x][y]));
                int b = saturate((int) (colors[x][y].getBlue() * kernel[x][y]));
                novaCores[x][y] = new Color(r, g, b);
            }

        int r = 0;
        int g = 0;
        int b = 0;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                r +=  novaCores[x][y].getRed();
                g +=  novaCores[x][y].getGreen();
                b +=  novaCores[x][y].getBlue();
            }
        }
        return new Color(r, g, b);
    }

    static Color getCor (BufferedImage img, int x, int y) {
        if(x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight())
            return new Color(0,0,0);
        else
            return new Color (img.getRGB(x, y));

    }
    static Color[][] getCorPixel(BufferedImage img, int x, int y) {
        return new Color[][]{
                {getCor(img,x-1, y-1), getCor(img, x, y-1), getCor(img, x+1, y-1)},
                {getCor(img,x-1, y+0), getCor(img, x, y+0), getCor(img, x+1, y+0)},
                {getCor(img,x-1, y+1), getCor(img, x, y+1), getCor(img, x+1, y+1)}
        };

    }
    public static void setProxCorContraste(BufferedImage img, BufferedImage out, int x, int y, int pixelSize, Color cor) {
        for (int h = y; h < y + pixelSize ; h++) {
            for (int w = x; w < x + pixelSize ; w++) {
                if(h >= img.getHeight() || w >= img.getWidth()) {
                    continue;
                }
                out.setRGB(w,h, cor.getRGB());
            }
        }
    }

    static BufferedImage convolve(BufferedImage img, float[][] kernel, int tamanho) {
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y+=tamanho)
            for (int x = 0; x < img.getWidth() ; x+=tamanho) {
                Color cor = new Color(applyKernel(getCorPixel(img, x, y), kernel).getRGB());
                setProxCorContraste(img,out,x,y,tamanho,cor);
                out.setRGB(x, y, cor.getRGB());
            }
        return out;
    }


    // pixelizar
    public static void setNextColor(BufferedImage img,BufferedImage out, int x, int y, int tamanho) {

        Color proximaCor = new Color(img.getRGB(x,y));

        for (int h = y; h < y + tamanho; h++)
            for (int w = x; w < x + tamanho; w++) {

                if(h >= img.getHeight() || w >= img.getWidth())
                    continue;
                else
                    out.setRGB(w, h, proximaCor.getRGB());
            }
    }

    public static BufferedImage pixelate(BufferedImage img, int tamanho) {

        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(),img.getType());

        for (int y = 0; y < img.getHeight();  y += tamanho)
            for (int x = 0; x < img.getWidth(); x += tamanho)
                setNextColor(img, out, x, y, tamanho);
        return out;
    }

    public static void run() throws IOException {

    Scanner scanner = new Scanner(System.in);
    int PIX_SIZE = scanner.nextInt();
    String Path = "C:\\Users\\LUCAS\\Pictures\\img";
    BufferedImage img = ImageIO.read(new File(Path,"DBZ.png"));

    BufferedImage pixelateImg = pixelate(img, PIX_SIZE);
    BufferedImage contrasteImg = convolve(pixelateImg, contraste, PIX_SIZE);

    ImageIO.write(pixelateImg, "png", new File("DBZpixelado.png"));
    ImageIO.write(contrasteImg, "png", new File("DBZcontraste.png"));

    }

    public static void main(String[] args) throws IOException {
        new Pixelate().run();
    }
}
