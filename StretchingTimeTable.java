package Proiect_AM;

// clasa copil a thread-ului de procesare
public class StretchingTimeTable extends StretchingThread {

    // metoda varargs care ia ca parametri numele etapei de procesare si unul sau mai multi timpi
    // ai etapelor de procesare
    public static void getStretchingStepsTime(String stepName, long ...stepsTime) {
        int i = 0;
        // numele primei etape de procesare
        String firstStepName = "aflare a intensitatii maxime si minime a pixelilor pe fiecare canal R, G si B";
        // pt fiecare etapa de procesare se afiseaza timpul de executie corespondent
        for (long stepTime: stepsTime) {
            if (i == 0) {
                System.out.println("Timpul pentru etapa de " + firstStepName + " este de " + stepTime + " nanosecunde");
            }
            else {
                System.out.println("Timpul pentru etapa de " + stepName + " este de " + stepTime + " milisecunde");
            }
            i++;
        }
    }
}
