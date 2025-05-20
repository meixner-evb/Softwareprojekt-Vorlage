/**
 * LEVEL ist eine Vorlage fuer eigene Klassen, die jeweils ein Level/Spielwelt darstellt.
 * Sie koennen einfach von LEVEL erben und verhalten sich genauso wie die Klasse SPIEL.
 * 
 * Vorteil: Jedes Level hat seine eigenen Konstruktor und Methoden wie tasteReagieren() und klickReagieren() usw..
 * Teamarbeit in Projekten ist dadurch deutlich erleichtert.
 * 
 * WICHTIG: Im KONSTRUKTOR einer Klasse die von SPIEL erbt darf man NICHT sofort ein neues Level aufrufen. 
 * Erst muss die Szene von SPIEL selbst fertig initialisiert sein. Damit ist SPIEL immer das erste Level.
 * sonst -> java.lang.NullPointerException: Cannot invoke "ea.GameLogic.enqueue(java.lang.Runnable)" because "ea.Game.gameLogic" is null
 * 
 * @author Joachim Hofmann
 * @version 1.0
 */

import ea.edu.EduActor;
import ea.edu.Figur;

public class LEVEL
implements TastenReagierbar, Ticker, MausKlickReagierbar, MausRadReagierbar, BildAktualisierungReagierbar {

    private static SPIEL s;

    private Figur hintergrundbild;

    public static void setzeSpiel(SPIEL s) {
        LEVEL.s = s;
    }

    public LEVEL(){
        this(true,true,true);
    }

    public LEVEL(boolean maus, boolean tastatureingabe, boolean bildAktualisierung) {
        s.neueSzene();
        s.benenneSzene("" + getClass().getName());
        if (maus) {
            s.registriereMausKlickReagierbar(this);
            s.registriereMausRadReagierbar(this);
        }
        if (tastatureingabe) {
            s.registriereTastenReagierbar(this);
        }
        if (bildAktualisierung) {
            s.registriereBildAktualisierungReagierbar(this);
        }
    }

    public void setzeHintergrundgrafik(String pfad) {
        if (this.hintergrundbild != null) {
            this.hintergrundbild.setzeSichtbar(false);
        }
        this.hintergrundbild = new Figur("hintergrund", pfad, 1, 1);
        this.hintergrundbild.setzeEbenenposition(-1);
        this.hintergrundbild.setzeMittelpunkt(0, 0);
        this.hintergrundbild.setzeSichtbar(true);
    }

    // =====    T o o l s    ==========================================================================
    /**
     * Gibt eine ganzzahlige Zufallszahl aus.
     *
     * @param von Die Untergrenze der Zufallszahl (INKLUSIVE)
     * @param bis Die Obergrenze der Zufallszahl (INKLUSIVE)
     * @return Eine Zufallszahl z mit:   von <= z <= bis
     */
    public int zufallsGanzzahlVonBis(int von, int bis) {
        return ea.Random.nextInteger(bis - von) + von;
    }

    /**
     * Gibt eine Komma-Zufallszahl aus.
     *
     * @param von Die Untergrenze der Zufallszahl (INKLUSIVE)
     * @param bis Die Obergrenze der Zufallszahl (INKLUSIVE)
     * @return Eine Komma-Zufallszahl z mit:   von <= z <= bis
     */
    public double zufallsKommazahlVonBis(double von, double bis) {
        return (ea.Random.nextFloat() * (bis - von)) + von;
    }

    /**
     * Wartet um die Angegebene Anzahl an Millisekunden BLOCKIEREND bis zur Ausfuehrung des naechsten Befehls.
     *
     * <b> !!! V O R S I C H T !!!  Nur verwenden bei reiner Grafik ohne Engine-Alpha Automatismen !!! <br />
     * <p>
     * Geht NICHT mit tick(), tasteReagieren(...), mausReagieren(...), ... !!! </b>
     *
     * @param sekunden Die zu wartende Zeit in Sekunden
     */
    public void warte(double sekunden) {
        try {
            Thread.sleep((int) (1.0f * sekunden * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // =====   T i c k e r   a n p a s s e n   ===========================================================
    /**
     * Setzt das qrtest.Ticker-Intervall.
     *
     * @param sekunden Die Zeit in Sekunden zwischen zwei Aufrufen der tick()-Methode.
     * @see #starteTickerNeu(double)
     * @see #stoppeTicker(double)
     * @see #tick()
     */
    public void setzeTickerIntervall(double sekunden) {
        s.registriereTicker(sekunden, this);
    }

    /**
     * Stoppt die qrtest.Ticker-Funktion. Die tick()-Methode
     * wird nicht weiter aufgerufen. Der automatische Aufruf der
     * tick()-Methode kann durch die Methode
     * tickerNeuStarten(double sekunden) wiedergestartet werden.
     *
     * @see #starteTickerNeu(double)
     * @see #setzeTickerIntervall(double)
     * @see #stoppeTicker(double)
     */
    public void stoppeTicker() {
        s.entferneTicker(this);
    }

    /**
     * Startet den qrtest.Ticker neu.
     *
     * @param sekunden Die Zeit in Sekunden zwischen zwei Aufrufen der tick()-Methode.
     * @see #setzeTickerIntervall(double)
     * @see #tick()
     * @see #stoppeTicker()
     */
    public void starteTickerNeu(double sekunden) {
        s.registriereTicker(sekunden, this);
    }

    // =====   M e t h o d e n   z u m   U e b e s c h r e i b e n   ======================================
    /**
     * Wird nach Aufruf von tickerNeuStarten(double) regelmaessig automatisch aufgerufen.
     * So kommt Bewegung ins Spiel!
     * Tick-Intervall kann angepasst werden. qrtest.Ticker muss erst gestartet werden!
     * Tickerintervall kleiner 0.05 vermeiden !!! Dann lieber bildAktualisierungReagieren benutzen.
     *
     * @see #starteTickerNeu(double)
     * @see #stoppeTicker()
     * @see #setzeTickerIntervall(double)
     */
    @Override
    public void tick() {

        System.out.println("Tack!");

    }

    /**
     * Wird bei jedem <b>Druecken einer Taste/b> automatisch aufgerufen
     * und automatisch das Kuerzel der entsprechenden Taste mitgegeben.
     *
     * @param taste ganzzahliges Kuerzel der Taste
     *              oder ENUM-Typ aus <b>Klasse qrtest.TASTE</b> (darin die Klassen-Doku lesen)
     */
    @Override
    public void tasteReagieren(int taste) {
        System.out.println("Taste mit Kuerzel " + taste + " wurde gedrueckt");
    }

    /**
     * Wird bei jedem <b>Loslassen einer Taste</b> automatisch aufgerufen
     * und automatisch das Kuerzel der entsprechenden Taste mitgegeben.
     *
     * @param taste ganzzahliges Kuerzel der Taste
     *              oder ENUM-Typ aus <b>Klasse qrtest.TASTE</b> (darin die Klassen-Doku lesen)
     */
    @Override
    public void tasteLosgelassenReagieren(int taste) {
        //System.out.println( "Taste mit Kuerzel " + taste + " wurde losgelassen" );
    }

    /**
     * Ueberprueft, ob eine <b>Taste gerade gedrueckt gehalten</b> wird.
     *
     * @param taste Der ganzzahlige Wert, der fuer die gedrueckte Taste steht.
     *              Details koennen im ENUM-Typen der <b>Klasse qrtest.TASTE</b> nachgelesen werden.
     * @return true, falls die Taste gedrueckt gehalten wird.
     */
    public boolean istTasteGedrueckt(int taste) {
        return ea.Game.isKeyPressed(taste);
    }

    /**
     * Wird bei jedem <b>Mausklick (Linksklick)</b> automatisch aufgerufen.
     * <b> !!! Funktioniert nur, wenn ein Konstruktor von qrtest.SPIEL mit Maus-Unteratuetzung aufgerufen wurde !!! </b>
     *
     * @param x Die X-Koordinate des Klicks
     * @param y Die Y-Koordinate des Klicks
     */
    @Override
    public void klickReagieren(double x, double y) {
        System.out.println("Klick bei (" + x + ", " + y + ").");
    }

    /**
     * Wird bei jedem <b>Loslassen der Mausktaste (Linksklick)</b> automatisch aufgerufen.
     * <b> !!! Funktioniert nur, wenn ein Konstruktor von qrtest.SPIEL mit Maus-Unteratuetzung aufgerufen wurde !!! </b>
     *
     * @param x Die X-Koordinate des Klicks
     * @param y Die Y-Koordinate des Klicks
     */
    @Override
    public void klickLosgelassenReagieren(double x, double y) {
        //System.out.println( "Losgelassen bei (" + x  + ", " + y + ")." );
    }

    /**
     * Wird bei jedem <b>Drehen am Mausrad</b> automatisch aufgerufen.
     * <b> !!! Funktioniert nur, wenn ein Konstruktor von qrtest.SPIEL mit Maus-Unteratuetzung aufgerufen wurde !!! </b>
     *
     * @param drehung Wie stark das Rad gedreht wurde, inkl. Vorzeichen:
     *                + Mausrad von sich weg drehen ;  - zu sich hin drehen
     */
    @Override
    public void mausRadReagieren(double drehung) {
        System.out.println("Mausrad wurde um " + drehung + " gedreht");
    }

    /**
     * Wird fuer jeden Frame (Bild-Aktualisierung) des Spiels exakt einmal aufgerufen.
     * Besser als "schneller" qrtest.Ticker (deren Tickerintervall kleiner als 0.05 ist)
     * <p>
     * Extra-Info fuer Nerds: nur in der aktuellen Szene!
     * --> EDU Games agieren in der Regel nur innerhalb einer Scene ("Hauptszene").
     * Du kannst aber mehrere Szenen erzeugen.
     *
     * @param sekunden Die Anzahl an Sekunden, die seit dem letzten Frame vergangen sind.
     */
    @Override
    public void bildAktualisierungReagieren(double sekunden) {
        // Methode fuer Echtzeitanwendungen, z.B. Methode der kleinen Schritte
        // in Subklasse ueberschreiben und statt tick() nutzen.
        //System.out.println("Frame Update nach " + sekunden + " Sekunden");
    }

    // =====    K a m e r a    =====================================================================
    /**
     * Verschiebt die Kamera um ein Stueck.
     *
     * @param dX Anzahl Pixel in x-Richtung
     * @param dY Anzahl Pixel in y-Richtung
     */
    public void verschiebeKamera(double dX, double dY) {
        s.verschiebeKamera(dX, dY);
    }

    /**
     * Setzt den Zoom-Faktor der Kamera. 1.0 ist normal.
     *
     * @param zoom Zoom-Faktor: >1 vergroessert ; <1 (aber >0) verkleinert
     */
    public void setzeKamerazoom(double zoom) {
        s.setzeKamerazoom(zoom);
    }

    /**
     * Nennt den aktuellen Zoom-Wert der Kamera.
     *
     * @return aktueller Zoom-Wert der Kamera: >1 vergroessert ; <1 (aber >0) verkleinert
     */
    public double nenneKamerazoom() {
        return s.nenneKamerazoom();
    }

    /**
     * Setze den Kamera-Fokus auf ein bestimmtes Objekt.
     *
     * @param focus Das neue Objekt im Zentrum der Kamera
     */
    public void setzeKamerafokus(EduActor focus) {
        s.setzeKamerafokus(focus);
    }

    /**
     * Rotiert die Kamera im oder gegen den Uhrzeigersinn.
     *
     * @param winkelInGrad Winkel, um den gedreht werden soll.
     *                     >0 im Uhrzeigersinn ; <0 gegen den Uhrzeigersinn
     */
    public void rotiereKamera(double winkelInGrad) {
        s.rotiereKamera(winkelInGrad);
    }

    /**
     * Setzt den Rotationswinkel der Kamera auf einen bestimmten Wert.
     *
     * @param winkelInGrad Der neue Kamera-Winkel in Grad
     */
    public void setzeKamerarotation(double winkelInGrad) {
        s.setzeKamerarotation((double) (Math.toRadians(winkelInGrad)));
    }

    // =====   S z e n e n    ========================================================================
    /**
     * Benennt eine Szene
     *
     * @param name neuer Name der Szene
     */
    public void benenneSzene(String name) {
        s.benenneAktiveSzene(name);
    }

    /**
     * Laedt eine (andere) Szene
     *
     * @param name Name der zu ladenden Szene
     */
    public void setzeSzene(String name) {
        s.setzeAktiveSzene(name);
    }

    // public void entferneSzene(String name) {
    // // ToDo
    // }

    /**
     * Erzeugt eine neue Szene
     */
    public void neueSzene() {
        s.erzeugeNeueSzene();
    }

    // =====    D i a l o g e    ======================================================================
    // Diese Methoden koennen den Spiel-Ablauf stoeren! Kann z.B. mit tick() Probleme geben!
    /**
     * Gibt ein <b>blockierendes</b> Nachricht-Fenster aus.
     *
     * @param nachricht angezeigte Nachricht in dem Fenster
     */
    public void zeigeNachricht(String nachricht) {
        s.zeigeNachricht(nachricht);
    }

    /**
     * Gibt ein <b>blockierendes</b> Frage-Fenster aus.
     *
     * @param frage angezeigte Frage in dem Fenster
     * @return 'true' = Ja , 'false' = Nein
     */
    public boolean zeigeNachrichtMitJaNein(String frage) {
        return s.zeigeNachrichtMitJaNein(frage);
    }

    /**
     * Gibt ein <b>blockierendes</b> Frage-Fenster aus.
     *
     * @param frage angezeigte Frage in dem Fenster
     * @return 'true' = Ok , 'false' = Abbrechen
     */
    public boolean zeigeNachrichtMitBestaetigung(String frage) {
        return s.zeigeNachrichtMitBestaetigung(frage);
    }

    /**
     * Gibt ein <b>blockierendes</b> Eingabe-Fenster aus.
     *
     * @param angezeigte Nachricht in dem Fenster
     * @return Benutzer-Eingabe
     */
    public String zeigeNachrichtMitEingabe(String nachricht) {
        return s.zeigeNachrichtMitEingabe(nachricht);
    }

    // =====   P h y s i k   d e r   g e s a m t e n   S z e n e   =====================================
    /**
     * Setzt die Intensitaet der Schwerkraft (normal=9.8).
     *
     * @param meterProQuadratsekunde Wert fuer die gewuenschte Fallbeschleunigung
     */
    public void setzeSchwerkraft(double meterProQuadratsekunde) {
        s.setzeSchwerkraft(meterProQuadratsekunde);
    }

    // =====   N e b e n l a e u f i g k e i t e n   a b s c h i c k e n   ==============================
    /**
     * Die Methode 'parallel' erwartet einen Lambda-Ausdruck der Form:
     * () -> methodenAufruf(parameterListe)   oder
     * () -> {methode1(...);methode2(...);...;}
     * <b>Das ist z.B. noetig, wenn eine Methode mit 'warte(...)' durch Tasten ausgefuehrt werden soll!</b>
     *
     * @param runnable Ein oder mehrere Methodenaufrufe als Lambda-Ausdruck
     */
    public static void parallel(Runnable runnable) {
        new Thread(runnable).start();
    }

    // weitere interssante Technik - aber nicht so multifunktional

    // public static <T> void parallel( java.util.function.Consumer<T> consumer , T argument )
    // {
    // parallel(  () -> consumer.accept(argument)  );
    // }

}