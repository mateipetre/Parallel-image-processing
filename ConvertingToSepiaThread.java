package Proiect_AM;

// thread-ul de conversie a imaginii la imagine sepia
public class ConvertingToSepiaThread extends ThreadClass{

    // suprascrie metoda getter din clasa parinte de thread
    @Override
    public String getThreadName() {

        // intoarce numele thread-ului - stretching thread
        threadName = "converting to sepia image thread";
        return threadName;
    }
    // suprascrie metoda run
    @Override
    public void run() {

        // afiseaza un mesaj de confirmare a rularii thread-ului
        System.out.println("Running " + getThreadName() + ", incepe procesarea imaginii");
        int width = Main.image.getWidth();
        int height = Main.image.getHeight();
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int p = Main.image.getRGB(x,y);
                int a = (p>>24) & 0xff;
                int R = (p>>16) & 0xff;
                int G = (p>>8) & 0xff;
                int B = p & 0xff;
                int newR = (int)(0.393*R + 0.769*G + 0.189*B);
                int newG = (int)(0.349*R + 0.686*G + 0.168*B);
                int newB = (int)(0.272*R + 0.534*G + 0.131*B);
                R = Math.min(newR, 255);
                G = Math.min(newG, 255);
                B = Math.min(newB, 255);
                p = (a<<24) | (R<<16) | (G<<8) | B;
                Main.newSepiaImage.setRGB(x, y, p);
            }
        }
        // afisare mesaj de terminare a thread-ului
        System.out.println("Going dead " + getThreadName() + ", procesarea imaginii a fost efectuata");
    }
}
