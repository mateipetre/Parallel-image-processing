package Proiect_AM;

import java.util.ArrayList;
import java.util.List;

// thread-ul producator
public class Producer extends ThreadClass {

    public HistogramBuffer buffer; // buffer-ul de histograme

    // creeaza un producator care poate pune histograme in buffer-ul indicat ca parametru al constructorului
    Producer(HistogramBuffer buffer) {
        this.buffer = buffer;
    }

    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - producator
        threadName = "Producer";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // afiseaza un mesaj care confirma rularea thread-ului
        System.out.println("Running " + getThreadName());
        // inregistreaza timpul de executie al producatorului
        long startTime = System.currentTimeMillis();

        // creeaza histograma imaginii
        List<ArrayList<Integer>> imageHistogram = ImageHistogram.getImageHistogram(Main.image);

        // pune in buffer cate 1/4 din histograma imaginii
        for (int i = 0; i < 4; i++) {

            // sfertul de histograma are 3 canale, fiecare continand cate 64 de valori intregi
            List<ArrayList<Integer>> quarterImageHistogram = new ArrayList<>();

            // creare canale R, G si B ale sfertului de histograma
            ArrayList<Integer> redQuarterHistogram = new ArrayList<>();
            ArrayList<Integer> greenQuarterHistogram = new ArrayList<>();
            ArrayList<Integer> blueQuarterHistogram = new ArrayList<>();

            // construieste cele 3 canale ale sfertului de histograma
            for (int j = 64 * i; j < 64 * (i + 1); j++) {
                redQuarterHistogram.add(imageHistogram.get(0).get(j));
                greenQuarterHistogram.add(imageHistogram.get(1).get(j));
                blueQuarterHistogram.add(imageHistogram.get(2).get(j));
            }
            // adauga in sfertul de histograma fiecare din cele 3 canale
            quarterImageHistogram.add(redQuarterHistogram);
            quarterImageHistogram.add(greenQuarterHistogram);
            quarterImageHistogram.add(blueQuarterHistogram);

            // pune 1/4 din histograma imaginii in buffer + afisare mesaj de confirmare a operatiei
            buffer.put(quarterImageHistogram);
            System.out.println(getThreadName() + " a pus partea " + (i+1) +"/4 din histograma imaginii in buffer");
            // bloc try-catch exception handling
            try {
                // pun un sleep pentru a evidentia etapele comunicarii producer-consumer
                sleep((long) (Math.random() * 1000));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName());
        // obtine un permis pentru semaforul folosit pt sincronizare
        Main.semaphore.release();
        long finishTime = System.currentTimeMillis();
        // afiseaza timpul masurat de executie a producatorului
        long producingTimeElapsed = finishTime - startTime;
        System.out.println("Etapa de producing a durat " + producingTimeElapsed + " milisecunde");
    }
}