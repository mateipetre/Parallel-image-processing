package Proiect_AM;

import java.util.ArrayList;
import java.util.List;

// thread-ul consumator
public class Consumer extends ThreadClass {

    // histograma preluata de consumator din buffer
    public static List<ArrayList<Integer>> consumedImageHistogram;
    public HistogramBuffer buffer; // buffer-ul de histograme

    // creeaza un consumator care poate sa scoata histograme din buffer-ul indicat ca parametru al constructorului
    Consumer(HistogramBuffer buffer) {
        this.buffer = buffer;
    }
    // metoda getter care intoarce histograma preluata din buffer
    public static List<ArrayList<Integer>> getConsumedImageHistogram() {
        return consumedImageHistogram;
    }
    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - consumator
        threadName = "Consumer";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // afiseaza un mesaj care confirma rularea thread-ului
        System.out.println("Running " + getThreadName());
        // inregistreaza timpul de executie al consumatorului
        long startTime = System.currentTimeMillis();
        // creez histograma ce va fi folosita la procesare
        consumedImageHistogram = new ArrayList<>();
        // creare canale R, G si B ale histogramei ce va folosita la procesare
        ArrayList<Integer> redConsumedHistogram = new ArrayList<>();
        ArrayList<Integer> greenConsumedHistogram = new ArrayList<>();
        ArrayList<Integer> blueConsumedHistogram = new ArrayList<>();
        // initalizarea celor 3 canale ale histogramei (fiecare cu 256 de valori)
        for (int j = 0; j < 256; j++) {

            redConsumedHistogram.add(j, 0);
            greenConsumedHistogram.add(j, 0);
            blueConsumedHistogram.add(j, 0);
        }
        // preia din buffer cate 1/4 din histograma imaginii
        for (int i = 0; i < 4; i++) {

            // preia 1/4 din histograma din buffer + afisare mesaj de confirmare a operatiei
            List<ArrayList<Integer>> quarterImageHistogram = buffer.get();
            System.out.println(getThreadName() + " a preluat partea " + (i+1) + "/4 din histograma imaginii din buffer");

            // adauga 1/4 din histograma preluata la histograma care va fi folosita la procesare pe cele 3 canale
            int k = 0;
            for (int j = 64 * i; j < 64 * (i + 1); j++) {

                redConsumedHistogram.set(j, quarterImageHistogram.get(0).get(k));
                greenConsumedHistogram.set(j, quarterImageHistogram.get(1).get(k));
                blueConsumedHistogram.set(j, quarterImageHistogram.get(2).get(k));
                k++;
            }
            // bloc try-catch exception handling
            try {
                // pun un sleep pentru a evidentia etapele comunicarii producer-consumer
                sleep((long) (Math.random() * 1000));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        // adauga in histograma care va fi folosita la procesare cele 3 canale
        consumedImageHistogram.add(redConsumedHistogram);
        consumedImageHistogram.add(greenConsumedHistogram);
        consumedImageHistogram.add(blueConsumedHistogram);
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName());
        // obtine un permis pentru semaforul folosit pt sincronizare
        Main.semaphore.release();
        long finishTime = System.currentTimeMillis();
        // afiseaza timpul masurat de executie a consumatorului
        long consumingTimeElapsed = finishTime - startTime;
        System.out.println("Etapa de consuming a durat " + consumingTimeElapsed + " milisecunde");
    }
}