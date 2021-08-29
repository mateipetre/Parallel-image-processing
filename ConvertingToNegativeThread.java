package Proiect_AM;

// thread-ul de conversie a imaginii la imagine negativa
public class ConvertingToNegativeThread extends ThreadClass{
    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - stretching thread
        threadName = "converting to negative image thread";
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
            for (int x = 0; x < width; x++)
            {
                int p = Main.image.getRGB(x,y);
                int a = (p>>24) & 0xff;
                int r = (p>>16) & 0xff;
                int g = (p>>8) & 0xff;
                int b = p & 0xff;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                p = (a<<24) | (r<<16) | (g<<8) | b;
                Main.newNegativeImage.setRGB(x, y, p);
            }
        }
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName() + ", procesarea imaginii a fost efectuata");
    }
}
