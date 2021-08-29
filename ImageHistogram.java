package Proiect_AM;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// clasa pentru histograma imaginii, implementeaza interfata Image
public class ImageHistogram implements Image {

    public static long histogramCreatingTime; // timpul de constructie a histogramei imaginii

    // suprascrie metoda din interfata implementata pt notificare a crearii histogramei
    @Override
    public void notifyCreateHist () {
        // afiseaza un mesaj de notificare + timpul in care a fost construita histograma
        System.out.println("Histograma RGB pentru imaginea data a fost creata in " + histogramCreatingTime + " milisecunde");
    }
    // metoda care construieste si intoarce histograma imaginii
    public static List<ArrayList<Integer>> getImageHistogram(BufferedImage image) {

        // inregistrez timpul de constructie
        long startTime = System.currentTimeMillis();
        // histograma are 3 canale, fiecare continand 256 de valori intregi
        List<ArrayList<Integer>> histogram = new ArrayList<>();
        // creare canale R, G si B ale histogramei
        ArrayList<Integer> redHistogram = new ArrayList<>();
        ArrayList<Integer> greenHistogram = new ArrayList<>();
        ArrayList<Integer> blueHistogram = new ArrayList<>();

        // initializare canale cu 0 pt fiecare valoare din cele 256
        for (int i = 0; i < 256; i++) {

            redHistogram.add(i, 0);
            greenHistogram.add(i, 0);
            blueHistogram.add(i, 0);
        }

        // algoritmul low-level pentru construirea efectiva a histogramei unei imagini
        // se considera imaginea o matrice si e parcursa pe linii si coloane
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                // sunt extrase valorile pixelilor din imagine pt fiecare canal R, G, B
                int redValue = new Color(image.getRGB(x, y)).getRed();
                int greenValue = new Color(image.getRGB(x, y)).getGreen();
                int blueValue = new Color(image.getRGB(x, y)).getBlue();

                // se incrementeaza nr de pixeli care au valorile gasile mai sus pt fiecare canal in parte
                redHistogram.set(redValue, redHistogram.get(redValue) + 1);
                greenHistogram.set(greenValue, greenHistogram.get(greenValue) + 1);
                blueHistogram.set(blueValue, blueHistogram.get(blueValue) + 1);
            }
        }
        // adauga in histograma fiecare din cele 3 canale calculate in urma algoritmului de mai sus
        histogram.add(redHistogram);
        histogram.add(greenHistogram);
        histogram.add(blueHistogram);
        long finishTime = System.currentTimeMillis();
        histogramCreatingTime = finishTime - startTime; // timpul in care a fost construita histograma
        new ImageHistogram().notifyCreateHist(); // se apeleaza metoda de notificare a construirii histogramei
        // intoarce histograma imaginii
        return histogram;
    }
    // metoda care schimba valoarea unui pixel cu o alta calculata
    public static int colorToRGB(int alphaPixelValue, int redPixelValue, int greenPixelValue, int bluePixelValue) {

        // sunt folosite valorile de pe cele 3 canale R, G, B ale unui pixel si valoarea sa alpha
        int newPixelValue = 0;
        // aduna valoarea de pe fiecare canal, inclusiv pe cea alpha la noua valoare a pixelului
        // si shifteaza la stanga cu 8 biti aceasta valoare nou obtinuta la fiecare adunare
        newPixelValue += alphaPixelValue;
        newPixelValue = newPixelValue << 8;
        newPixelValue += redPixelValue;
        newPixelValue = newPixelValue << 8;
        newPixelValue += greenPixelValue;
        newPixelValue = newPixelValue << 8;
        newPixelValue += bluePixelValue;
        // intoarce noua valoare a pixelului
        return newPixelValue;
    }
}
