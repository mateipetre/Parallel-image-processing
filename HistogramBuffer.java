package Proiect_AM;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// buffer-ul care contine histogramele imaginilor
public class HistogramBuffer {

    private final int capacity = 1; // capacitatea buffer-ului este limitata la 1
    /* se creeaza un vector de histograme
    fiecare histograma este o lista de 3 arraylist-uri
    pt ca aceasta contine 3 canale (R, G si B) cu cate 256 de valori fiecare
     */
    private final Vector<List<ArrayList<Integer>>> histVector = new Vector<>(capacity);
    // metoda put pt producer
    void put (List<ArrayList<Integer>> histogram){
        // creez zona critica in care se poate afla doar un thread la un anumit moment de timp
        // sincronizare necesara pt a evita interferenta thread-urilor
        synchronized (this) {
            // cat timp buffer-ul e plin, thread-ul asteapta sa fie golit
            while (histVector.size() >= capacity) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            // buffer-ul e gol, deci poate sa puna histograma imaginii in el
            histVector.add(histogram);
            // notifica consumatorul ca poate prelua histograma din buffer
            this.notify();
        }
    }
    // metoda get pt consumer
    List<ArrayList<Integer>> get () {
        // creez zona critica in care se poate afla doar un thread la un anumit moment de timp
        // sincronizare necesara pt a evita interferenta thread-urilor
        synchronized (this) {
            // cat timp buffer-ul e gol, thread-ul asteapta sa fie umplut
            while (histVector.size() == 0) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            // buffer-ul e plin, deci poate sa scoata histograma imaginii din el
            List<ArrayList<Integer>> histogram = histVector.remove(0);
            // notifica producatorul ca poate sa puna histograma in buffer
            this.notifyAll();
            return histogram;
        }
    }
}

