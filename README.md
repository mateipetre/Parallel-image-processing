# Parallel-image-processing

Acest proiect are ca scop prezentarea unei teme de procesare a unor imagini și conține o parte teoretică care va descrie procedeele de „histogram stretching”, conversie a unei imagini la imagine grayscale, conversie la imagine negativă, conversie la imagine sepia,  oglindire a imaginii și rotire a acesteia cu un anumit unghi, dar și o parte practică, reprezentată de o aplicație implementată în Java care realizează efectiv operațiile date. În cadrul acestuia, vor fi prezentate concepte bine-cunoscute din teoria procesării imaginilor, precum histograma unei imagini, intensitatea unui pixel, contrastul unei imagini și modelul de culoare RGB. Descrierea acestora este imperios necesară pentru înțelegerea modului de implementare a aplicației.

Aplicația este, așadar, implementată în Java și are ca scop realizarea operațiilor  ,menționate mai sus, pentru o imagine dată. Ea este multimodulară, având o funcționalitate precisă – primește ca input o imagine de tip BMP, aplică operația de corespunzătoare imaginii și va avea ca output (rezultatul rulării aplicației) o nouă imagine de tip BMP procesată. La finalul execuției aplicației, în directorul în care se află imaginea originală neprocesată, vor mai exista încă 6 imagini procesate care atestă funcționalitatea corectă a acesteia.

Aplicația are la bază paradigma Producer-Consumer (cazul un producător și un    consumator), deci este multithreading. Mai exact, în plus față de firul de execuție principal, în cadrul programului mai rulează alte 8 thread-uri – 1 producer, 1 consumer și 6 thread-uri de procesare efectivă a imaginii. Mai multe detalii legate de implementarea acestui mecanism de multithreading vor fi expuse în capitolul de implementare a aplicației.
Etapele de execuție ale aplicației sunt:
•	se citesc informațiile de identificare a fișierului sursă, un nivel de procesare al imaginii dorit (va fi pomenit ca nivel de stretching în cadrul acestei documentații)
și un grad al unghiului de rotire a imaginii
•	se citește fișierul sursă
•	se procesează imaginea
•	se scrie fișierul destinație

La fiecare etapă, se va înregistra timpul de execuție și se vor afișa mesaje corespunzătoare la consolă în vederea aprecierii performanțelor acesteia. De asemenea, aplicația precizează la fiecare pas că imaginea a fost citită, procesată și scrisă pe disc prin mesaje afișate tot la consolă.
Aplicația respectă toate principiile de programare orientată pe obiecte – încapsulare, moștenire, polimorfism și SRP (Single Responsability Principle). Fiecare clasă sau interfață implementată în cadrul aplicației are un singur scop precis, așadar aplicația are o funcționalitate și o structură simplă.

Aplicația este implementată pe baza a 13 clase și o intefață. Cele 13 clase sunt următoarele:

1.	Main – clasa principală utilizată pentru citirea și scriere din și în fișiere, dar și pentru procesare (în ea sunt create thread-urile, unul fiind de procesare efectivă); ea conține metoda main în care sunt realizate operațiile I/O și creare de obiecte necesare implementării (semafoare, imaginile etc.)
2.	ThreadClass – clasa principală de thread, clasă abstractă, părinte ale celor 3 thread-uri specifice
3.	HistogramBuffer – clasa buffer care stochează histogramele imaginilor (se implementează un buffer de capacitate 1)
4.	Producer – thread-ul producător care produce/pune sferturi din histograma imaginii în buffer
5.	Consumer – thread-ul consumator care consumă/preia sferturi din histograma imaginii din buffer și reconstruiește histograma imaginii originale
6.	StretchingThread – thread-ul care realizează operația de „histogram stretching” asupra imaginii prin intermediul histogramei preluate de Consumer
7.	ImageHistogram – clasa care construiește histograma imaginii prin intermediul metodei getImageHistogram
8.	StretchingTimeTable – clasă copil a lui StretchingThread și e folosită mai mult în scop didactic pentru a afișa timpii de procesare pentru fiecare etapă în parte prin intermediul metodei varargs getStretchingStepsTime
9.	ConvertingToGrayscaleThread - thread-ul care realizează operația de conversie la imagine grayscale
10.	ConvertingToNegativeThread - thread-ul care realizează operația de conversie la imagine negativă
11.	ConvertingToSepiaThread - thread-ul care realizează operația de conversie la imagine Sepia
12.	ImageMirroringThread - thread-ul care realizează operația de image mirroring asupra imaginii originale
13.	RotatingImageThread - thread-ul care realizează operația de rotire cu un anumit unghi asupra imaginii originale

Interfața Image conține o metodă care notifică crearea histogramei pentru o imagine și este implementată de clasa ImageHistogram.

Cerințe de implementare ale aplicației:

1.	Aplicația folosește pentru input și output imagini de tip 24bit BMP – RGB.
2.	Pentru operația de „histogram stretching” e folosită formula din secțiunea 2. Prezentarea suportului tehnic, trecere în revistă a unor realizări similare, dar și algoritmi low-level bazați pe calculul noilor valori ale pixelilor (în clasa StretchingThread) și pe construirea histogramei RGB a unei imagini (în clasa HistogramImage). De asemenea, celelalte 5 operații sunt realizate pe baza algoritmilor menționați în secțiunea trecută.
3.	Toate conceptele OOP sunt consistent integrate în implementarea aplicației. Există încapsulare la nivelul claselor care conțin diverse metode și obiecte necesare procesării (de exemplu, clasa Producer conține un obiect buffer de tip HistogramBuffer și alte metode specifice), moștenire – există o ierarhie de clase (clasele Producer, Consumer, StretchingThread, ConvertingToGrayscaleThread, ConvertingToNegativeThread, ConvertingToSepiaThread, ImageMirroringThread și RotatingImageThread moștenesc clasa ThreadClass, care la rândul ei moștenește clasa Thread) și 3 niveluri de moștenire – clasa StretchingTimeTable moștenește clasa StretchingThread, care moștenește clasa ThreadClass, care la rândul ei moștenește clasa Thread), polimorfism – există metode getters implementate în cadrul claselor (de exemplu, clasele de thread moștenesc metoda getThreadName din clasa principală ThreadClass) și abstractizare – există clasa abstractă ThreadClass care conține metoda abstractă getThreadName.
4.	Codul sursă este în întregime comentat și coding style-ul este adaptat limbajului Java.
5.	Aplicația folosește operațiile de I/O pe fișiere, la citirea și la scrierea imaginii (se poate observa în cadrul clasei principale Main).
6.	Există operații de intrare de la tastatură – este citit nivelul de stretching și gradul de rotire a imaginii și prin parametrii liniei de comandă – este furnizat astfel numele fișierului de intrare
7.	Aplicația este multimodulară, în sensul că există o ierarhie de clase și cel puțin 3 niveluri de moștenire.
8.	Există metoda varargs getStretchingStepsTime din clasa StretchingTimeTable.
9.	Există inferfața Image implementată de clasa ImageHistogram.
10.	Există clasa abstractă ThreadClass, clasă părinte a claselor concrete Producer, Consumer și StretchingThread și celelalte 5 clase de thread-uri de procesare. Ea conține metoda abstractă getThreadName.
11.	Aplicația folosește în clase blocuri try-catch pentru toate cazurile de exception/error handling.
12.	Aplicația este multithreading cu următoarele funcționalități:
-	thread-ul Producer creează histograma RGB a imaginii citite, pune câte ¼ din histogramă în buffer (obiectul de tip HistogramBuffer) și intră în sleep după fiecare adăugare; acesta nu poate să mai pună în buffer dacă e plin, adică dacă conține un sfert de histogramă deja care nu a fost preluat de Producer și așteaptă până se golește

-	thread-ul Consumer preia din buffer câte ¼  din histogramă cât timp acesta nu e gol și intră în sleep după fiecare preluare; dacă buffer-ul este gol, așteaptă până când Producer a mai pus un sfert de histogramă; el reconstruiește histograma pentru procesare la sfârșitul preluării tuturor celor 4 sferturi

-	Producer și Consumer comunică printr-un mecanism de wait-notify și există o sincronizare realizată cu synchronized, delimitând astfel zona critică în care se poate afla doar unul dintre aceștia la un anumit moment de timp

-	StretchingThread realizează operația efectivă de stretching; pentru a asigura faptul că acesta are ce procesa, adică Consumer a preluat toate cele 4 sferturi ale histogramei de la Producer, am folosit un semafor care impune ca StretchingThread să aștepte execuția Producer-Consumer

-	Am folosit sleep pentru a evidenția etapele comunicării

-	În toate thread-urile sunt înregistrați timpii de execuție; în cel de procesare sunt înregistrați timpii de execuție pentru ambele etape de procesare: prima etapă – aflarea intensităților minime și maxime ale pixelilor imaginii pe fiecare canal R, G, B și a doua etapa – modificarea intensităților pixelilor utilizând formula de stretching pentru fiecare în parte

-	Celelalte 5 thread-uri realizează operațiile specifice implementate în metoda run

La o simplă rulare, aplicația cere utilizatorului să introducă de la tastatură o valoare a nivelului de stretching dorit, dar și a gradului de rotire a imaginii și poate să producă un mesaj de reintroducere a valorii dacă aceasta este „out of range” (în afara domeniului acceptat). Numele fișierului e citit în linie de comandă, aplicația deschide și citește fișierul cu acest nume, aplică operațiile de procesare și scrie alte 6 fișiere adaptate ca nume după original în directorul cu aceeași cale pe care o are și fișierul de intrare. Pe lângă fișierele de ieșire, utilizatorul va obține la output niște mesaje care sugerează timpii de execuție a procesărilor și modul în care are loc execuția (cum încep thread-urile, cum se termină, când e scrisă imaginea procesată pe disc etc.). Utilizatorul trebuie să aibă instalate în prealabil un IDE pentru Java și un JDK adecvat (recomandări: Eclipse sau IntelliJ IDEA + JDK 8 sau 12).

