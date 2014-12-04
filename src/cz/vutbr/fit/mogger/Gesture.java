package cz.vutbr.fit.mogger;

// trida slouzi pro praci s gesty (jejich ulozeni, nacteni, kontrolu)

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Gesture {

    // cesta k souboru
    public String fileSound;
    public String name;
    private int threshold;

    // gesto akcelerometr
    private ArrayList<Integer> coord_x;
    private ArrayList<Integer> coord_y;
    private ArrayList<Integer> coord_z;

    // signalizace zapoceti nahravani noveho gesta a ukonceni nahravani
    Sounds sounds;

    public Gesture() {
        this("", "", 0);
    }

    public Gesture(String name, String fileSound, int threshold) {
        this.name = name;
        this.fileSound = fileSound;
        this.threshold = threshold;

        // gesto akcelerometr
        coord_x = new ArrayList<Integer>();
        coord_y = new ArrayList<Integer>();
        coord_z = new ArrayList<Integer>();

        sounds = new Sounds();
    }

    public int size() {
        return coord_x.size();
    }

    // vklada nove vektory na konec listu, pokud je list delsi jak 80 vektoru, nejstarsi vektor odstrani
    public void addCoords(int x, int y, int z) {
        coord_x.add(x);
        coord_y.add(y);
        coord_z.add(z);

        if (coord_x.size() > 80) {
            coord_x.remove(0);
            coord_y.remove(0);
            coord_z.remove(0);
        }
    }

    public int[][] getCoordsArray() {
        int size = coord_x.size();
        int[][] coords = new int[3][size];

        for (int i = 0; i < size; i++) {
            coords[0][i] = coord_x.get(i);
            coords[1][i] = coord_y.get(i);
            coords[2][i] = coord_z.get(i);
        }

        return coords;
    }

    public int[][] getTestedCoordsArray(int size) {
        int tested_size = coord_x.size();
        int[][] coords = new int[3][size];
        int k = 0;
        for (int i = tested_size - size; i < tested_size; i++) {
            coords[0][k] = coord_x.get(i);
            coords[1][k] = coord_y.get(i);
            coords[2][k] = coord_z.get(i);
            k++;
        }

        return coords;
    }

    // promaze aktualne posbirana data
    public void cleanCoords() {
        coord_x.clear();
        coord_y.clear();
        coord_z.clear();
    }

    // spocita prah pro rozpoznani prislusneho gesta podle jeho slozitosti
    protected int calculateThreshold() {
        int size = coord_x.size();
        int sum = 0;

        int strange_const = 4;

        for (int i = 0; i < size; i++) {
            sum += abs(coord_x.get(i)) + abs(coord_y.get(i)) + abs(coord_z.get(i));
        }

        if (size > 20) {
            strange_const = 5;
        } else if (size > 30) {
            strange_const = 6;
        }

        return (sum / size) * strange_const;
    }

    public int getThreshold() {
        return threshold;
    }
    public void setThreshold(int v) {
        threshold = v;
    }

}
