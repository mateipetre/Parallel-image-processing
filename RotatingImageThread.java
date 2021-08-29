package Proiect_AM;

import java.awt.*;

// thread-ul de conversie a imaginii la imagine rotita
public class RotatingImageThread extends ThreadClass {

    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - stretching thread
        threadName = "rotating image thread";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // afiseaza un mesaj de confirmare a rularii thread-ului
        System.out.println("Running " + getThreadName() + ", incepe procesarea imaginii");

        int width = Main.image.getWidth();
        int height = Main.image.getHeight();

        Graphics2D g2 = Main.newRotatedImage.createGraphics();

        g2.rotate(Math.toRadians(Main.degreeOfRotation), width / 2, height / 2);
        g2.drawImage(Main.image, null, 0, 0);
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName() + ", procesarea imaginii a fost efectuata");
    }
}
