package Proiect_AM;

// clasa abstracta, parinte al celor 3 thread-uri folosite
public abstract class ThreadClass extends Thread {

    public String threadName; // numele thread-ului
    // metoda abstracta tip getter care intoarce numele thread-ului
    public abstract String getThreadName();

    // metoda start() a clasei Thread suprascrisa pentru a afisa un mesaj de pornire a thread-ului
    @Override
    public void start() {
        // anunta pornirea thread-ului
        System.out.println("Starting " + getName());
        // invoca metoda start() a superclasei
        super.start();
    }
}
