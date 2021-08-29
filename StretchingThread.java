package Proiect_AM;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// thread-ul de procesare tip 'stretching image' a imaginii
public class StretchingThread extends ThreadClass {

    public static long findMaxMinTimeElapsed; // timp de procesare prima etapa
    public static long modifyPixelsValuesTimeElapsed; // timp de procesare a 2-a etapa

    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - stretching thread
        threadName = "stretching thread";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // bloc try-catch pt exception handling
        try {
            /* thread-ul incearca sa treaca de semafor
            daca producatorul si consumatorul si-au terminat executia atunci nr de permise
            devine 1 (pt ca -1 + 2 = 1), deci va putea trece de el in acest caz
            in caz contrar, asteapta pana cand nr de permise devine 1
             */
            Main.semaphore.acquire();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        // afiseaza un mesaj de confirmare a rularii thread-ului
        System.out.println("Running " + getThreadName() + ", incepe procesarea imaginii");
        // inregistreaza timpul de executie al thread-ului
        long startTime, finishTime;

        // foloseste histograma preluata de consumator de la producator
        List<ArrayList<Integer>> imageHistogram = Consumer.getConsumedImageHistogram();

        // stocheaza cele 3 canale R, G, B ale histogramei
        ArrayList<Integer> redHistogram = imageHistogram.get(0);
        ArrayList<Integer> greenHistogram = imageHistogram.get(1);
        ArrayList<Integer> blueHistogram = imageHistogram.get(2);

        // creeaza o noua histograma cu ajutorul careia se va creea noua imagine procesata
        ArrayList<ArrayList<Integer>> rgbHistogram = new ArrayList<>();
        // adauga canalele R, G, B ale histogramei imaginii originale in noua histograma
        rgbHistogram.add(0, redHistogram);
        rgbHistogram.add(1, greenHistogram);
        rgbHistogram.add(2, blueHistogram);

        // numarul total de pixeli al imaginii
        int noTotalPixels = Main.image.getWidth() * Main.image.getHeight();

        // se creeaza 2 liste in care se vor retine valorile minime si maxime
        // de pe fiecare canal din cele 3 ale histogramei
        // fiecare din cele 2 liste vor avea deci 3 valori minime, respectiv 3 maxime pt ca sunt 3 canale
        ArrayList<Integer> minOnChannels = new ArrayList<>();
        ArrayList<Integer> maxOnChannels = new ArrayList<>();
        // initializare liste
        for (int i = 0; i < 3; i++) {
            minOnChannels.add(i, 0);
            maxOnChannels.add(i, 0);
        }
        // inregistrare timp de procesare prima etapa
        startTime = System.nanoTime();
        // algoritm stretching cu 2 etape principale
        // prima etapa: se afla intensitatile maxime si minime ale pixelilor pe fiecare canal R, G si B
        // se parcurge histograma pe cele 3 canale
        for (int i = 0; i < 3; ++i) {
            // se parcurg toate cele 256 valori de pe fiecare canal
            for (int j = 0; j < 255; ++j) {
                // adauga la fiecare valoare din cele 256 valoarea anterioara de pe canal, exceptand prima valoare
                rgbHistogram.get(i).set(j + 1, rgbHistogram.get(i).get(j + 1) + rgbHistogram.get(i).get(j));
            }
            // initializez minimul si maximul
            minOnChannels.set(i, 0);
            maxOnChannels.set(i, 255);
            // afla minimul pe fiecare canal luand in considerare nivelul de stretching ales de utilizator
            while (rgbHistogram.get(i).get(minOnChannels.get(i)) < Main.discardRatio * noTotalPixels) {
                minOnChannels.set(i, minOnChannels.get(i) + 1);
            }
            // afla maximul pe fiecare canal luand in considerare nivelul de stretching ales de utilizator
            while (rgbHistogram.get(i).get(maxOnChannels.get(i)) > (1 - Main.discardRatio) * noTotalPixels) {
                maxOnChannels.set(i, maxOnChannels.get(i) - 1);
            }
        }
        finishTime = System.nanoTime();
        // timp total de procesare prima etapa
        findMaxMinTimeElapsed = finishTime - startTime;

        // inregistrare timp de procesare a doua etapa
        startTime = System.currentTimeMillis();
        // a doua etapa: se modifica pixelii imaginii folosind formula de stretching pentru fiecare pixel in parte
        // se folosesc valorile minime si maxime de pe fiecare canal, aflate in etapa anterioara
        // se considera imaginea o matrice si se parcurge pe linii si coloane
        for (int x = 0; x < Main.image.getWidth(); ++x) {
            for (int y = 0; y < Main.image.getHeight(); ++y) {

                // se creeaza o lista cu valorile de pe cele 3 canale pt fiecare pixel
                ArrayList<Integer> rgbPixelsValues = new ArrayList<>();
                // se initializeaza aceasta lista
                for (int i = 0; i < 3; i++) {
                    rgbPixelsValues.add(i, 0);
                }
                // se parcurge lista si se modifica fiecare valoare a pixelului corespondenta fiecarui canal R,G,B
                for (int i = 0; i < 3; ++i) {

                    int pixelValue = 0; // initializam valoarea pixelului
                    // salvez valorile sale de pe cele 3 canale
                    int redValue = new Color(Main.image.getRGB (x, y)).getRed();
                    int greenValue = new Color(Main.image.getRGB (x, y)).getGreen();
                    int blueValue = new Color(Main.image.getRGB (x, y)).getBlue();
                    // pixelul ia valoarea in functie de canalul pe care se pozitioneaza la acel moment
                    switch (i) {
                        // cazul in care canalul e R
                        case 0: {
                            pixelValue = redValue;
                            break;
                        }
                        // cazul in care canalul e G
                        case 1: {
                            pixelValue = greenValue;
                            break;
                        }
                        // cazul in care canalul e B
                        case 2: {
                            pixelValue = blueValue;
                            break;
                        }
                    }
                    // valoarea pixelului pe canalul respectiv se poate schimba
                    // daca e mai mica decat valoarea minima de pe canal atunci ia valoarea minima, altfel nu
                    if (pixelValue < minOnChannels.get(i)) {
                        pixelValue = minOnChannels.get(i);
                    }
                    // daca e mai mare decat valoarea maxima de pe canal atunci ia valoarea maxima, altfel nu
                    if (pixelValue > maxOnChannels.get(i)) {
                        pixelValue = maxOnChannels.get(i);
                    }
                    // ma asigur ca maximul de pe canal nu e egal cu minimul
                    if (!maxOnChannels.get(i).equals(minOnChannels.get(i))) {
                        // pixelul ia o noua valoare conform formulei de stretching
                        rgbPixelsValues.set(i, (pixelValue - minOnChannels.get(i)) * 255 / (maxOnChannels.get(i) - minOnChannels.get(i)));
                    }
                    else {
                        // cazul in care nu s-a putut realiza stretching-ul pt ca max = min pe canal
                        // se afiseaza un mesaj corespunzator de esuare a operatiei
                        System.out.println("Nu s-a putut realiza operatia de stretching!");
                        // programul se opreste
                        System.exit(1);
                    }
                }
                // valoarea alpha a pixelului
                int alphaValue = new Color(Main.image.getRGB(x, y)).getAlpha();
                // valoarea finala a pixelului e stabilita cu ajutorul metodei colorToRGB
                int newPixelValue = ImageHistogram.colorToRGB(alphaValue, rgbPixelsValues.get(0), rgbPixelsValues.get(1), rgbPixelsValues.get(2));
                // pune noua valoare a pixelului in noua imagine in aceeasi pozitie ca in imaginea originala
                Main.newStretchedImage.setRGB(x, y, newPixelValue);
            }
        }
        finishTime = System.currentTimeMillis();
        // timpul de procesare a etapei a doua
        modifyPixelsValuesTimeElapsed = finishTime - startTime;

        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName() + ", procesarea imaginii a fost efectuata");
    }
}
