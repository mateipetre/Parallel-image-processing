package Proiect_AM;

import java.io.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;

// clasa principala pentru citire, procesare, scriere a imaginilor procesate pe disc
public class Main {

    public static BufferedImage image = null; // imaginea citita din fisier
    public static BufferedImage newStretchedImage; // imaginea cu stretching
    public static BufferedImage newGrayscaleImage; // imaginea grayscale
    public static BufferedImage newNegativeImage; // imaginea negativa
    public static BufferedImage newSepiaImage; // imaginea sepia
    public static BufferedImage newMirroredImage; // imaginea oglindita
    public static BufferedImage newRotatedImage; // imaginea rotita
    public static double discardRatio; // nivelul de stretching scalat la o valoare intre 0 si 0.5
    public static int degreeOfRotation; // gradul de rotire al imaginii
    /* semafor folosit pentru sincronizare, initializat cu -1 permise
       asigura faptul ca producer si consumer ruleaza inainte de stretching thread
     */
    public static Semaphore semaphore = new Semaphore(-1);

    public static void main(String[] args) {
        // numele fisierului de intrare e preluat ca argument din linia de comanda
        String inputFileName = args[0];
        // citim de la tastatura nivelul de stretching
        // impunand o restrictie pentru intervalul in care se poate afla
        System.out.println("Alegeti valoarea nivelului de stretching ce va fi aplicat imaginii (valoare intreaga intre 0 si 50): ");
        // inregistrez timpul de citire a imaginii si a nivelului de stretching
        long startTime = System.currentTimeMillis();
        // nivelul de stretching care va fi citit
        int levelOfStretching = 0;
        // bloc try-catch pentru exception handling
        try {
            // se verifica cu ajutorul ei daca valoarea introdusa de utilizator este corecta
            boolean inputIsGood = false;
            // utilizatorul va trebui sa reintroduca o noua valoare cat timp nu introduce una corecta
            while (!inputIsGood) {
                Scanner input = new Scanner(System.in);
                // citire nivel stretching
                levelOfStretching = input.nextInt();
                System.out.println("Alegeti gradul de rotire a imaginii (valoare intreaga intre 0 si 360): ");
                degreeOfRotation = input.nextInt();
                // intervalul restrictionat pentru nivelul de stretching si gradul de rotire
                if ((levelOfStretching >= 0 && levelOfStretching < 50) && (degreeOfRotation >= 0 && degreeOfRotation <= 360)) {
                    inputIsGood = true;
                }
                else {
                    // mesaj de eroare - una din valorile introduse sau amandoua nu se afla in interval
                    System.out.println("O valoare aleasa (sau amandoua) se afla in afara intervalului mentionat, incercati din nou: ");
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            // imaginea este citita din fisierul de intrare
            image = ImageIO.read(new File(inputFileName));
            long finishTime = System.currentTimeMillis();
            // timpul de citire al imaginii calculat cu ajutorul timpilor de la inceput/sfarsit executie
            long readingTimeElapsed = finishTime - startTime;
            System.out.println("Imaginea a fost citita in " + readingTimeElapsed + " milisecunde");

            // masor si timpul de procesare a imaginii + scrierea noii imagini in fisierul de iesire
            startTime = System.currentTimeMillis();
            // ma asigur ca imaginea a fost citita
            assert image != null;
            // se creeaza noua imagine rezultata in urma procesarii
            newStretchedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            newGrayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            newNegativeImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            newSepiaImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            newMirroredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            newRotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            // se calculeaza nivelul de stretching scalat
            discardRatio = (double)levelOfStretching / 100;

            // consider bufferul pe care vor lucra producatorul si consumatorul
            HistogramBuffer histogramBuffer = new HistogramBuffer();
            // consider un vector de 8 thread-uri folosite pentru executia programului
            Thread threads[] = new ThreadClass[8];
            // creez instantele producatorului, consumatorului si ale thread-urilor folosite pentru procesare (6 tipuri)
            threads[0] = new Consumer(histogramBuffer);
            threads[1] = new Producer(histogramBuffer);
            threads[2] = new StretchingThread();
            threads[3] = new ConvertingToGrayscaleThread();
            threads[4] = new ConvertingToNegativeThread();
            threads[5] = new ConvertingToSepiaThread();
            threads[6] = new ImageMirroringThread();
            threads[7] = new RotatingImageThread();
            // pornesc cele 8 thread-uri
            for (int i = 0; i < 8; i++) {
                threads[i].start();
            }
            // cele 8 thread-uri sunt puse in asteptare pana la terminarea lor
            for (int i = 0; i < 8; i++) {
                // bloc try-catch pt exception handling
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // numele celei de-a 2 etape a procesului de stretching a imaginii
            String secondStepName = "modificare a pixelilor imaginii folosind formula de stretching pentru fiecare in parte";
            // se apeleaza metoda cu varargs pentru a afisa timpul de executie a fiecarei etape de stretching
            StretchingTimeTable.getStretchingStepsTime(secondStepName, StretchingThread.findMaxMinTimeElapsed, StretchingThread.modifyPixelsValuesTimeElapsed);
            // noile imagini procesate sunt scrise in fisierul parinte al imaginii originale
            // numele fisierelor de iesire au urmatorul pattern: numeFisierInput_tipEfect.bmp
            ImageIO.write(newStretchedImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_stretchedImage.bmp")));
            ImageIO.write(newGrayscaleImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_grayscaleImage.bmp")));
            ImageIO.write(newNegativeImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_negativeImage.bmp")));
            ImageIO.write(newSepiaImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_sepiaImage.bmp")));
            ImageIO.write(newMirroredImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_mirroredImage.bmp")));
            ImageIO.write(newRotatedImage, "bmp", new File(inputFileName.replaceAll(".bmp", "_rotatedImage.bmp")));
            finishTime = System.currentTimeMillis();
            // e calculat timpul de procesare + scriere ale imaginilor in fisierele de iesire
            long writingTimeElapsed = finishTime - startTime;
            // acest timp este si afisat la consola
            System.out.println("Imaginile au fost procesate si scrise pe disc in " + writingTimeElapsed + " milisecunde");
        }
        catch(IOException exception) {
            System.out.println("Error: " + exception);
        }
    }
}
