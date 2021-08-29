package Proiect_AM;

// thread-ul de conversie a imaginii la imagine in oglinda
public class ImageMirroringThread extends ThreadClass{

    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - stretching thread
        threadName = "image mirroring thread";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // afiseaza un mesaj de confirmare a rularii thread-ului
        System.out.println("Running " + getThreadName() + ", incepe procesarea imaginii");
        int width = Main.image.getWidth();
        int height = Main.image.getHeight();
        for (int y = 0; y < height; y++)
        {
            for (int lx = 0, rx = width - 1; lx < width; lx++, rx--)
            {
                int p = Main.image.getRGB(lx, y);
                Main.newMirroredImage.setRGB(rx, y, p);
            }
        }
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName() + ", procesarea imaginii a fost efectuata");
    }
}
