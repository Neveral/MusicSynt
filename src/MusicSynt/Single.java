package MusicSynt;

import java.util.*;


/**
 * Created by Neveral on 05.04.14.
 */
public class Single {
    private int sampleRate;
    private ArrayList<Part> parts = new ArrayList<Part>();
    private int[] data;
    private int length;
    private static Map<String, Integer> notes = new HashMap<String, Integer>();
    private double durationTime = 0.25;
    private double position = 0;

    public void fillMap() {
        notes.put("C", -9);
        notes.put("C#", -8);
        notes.put("D", -7);
        notes.put("D#", -6);
        notes.put("E", -5);
        notes.put("F", -4);
        notes.put("F#", -3);
        notes.put("G", -2);
        notes.put("G#", -1);
        notes.put("A", 0);
        notes.put("A#", 1);
        notes.put("B", 2);
        notes.put("L", 3);
        notes.put("S", 4);
    }

    public void music (String sound) {
        String[] words = sound.split(" ");
        // разбиваем мелодию на ноты
        for (String word : words) {
            int note = notes.get(word.substring(0, word.length() - 1));
            int octave = Integer.parseInt(word.substring(word.length() - 1, word.length()));
            double frequency = getNote(note,octave);
            add(frequency, -0.51, position, durationTime); // double frequency, double compressor, double start, double length
            position += durationTime;
        }
    }

    // высчитываем частоту ноты определенной октавы
    private static double getNote(int key, int octave) {
        return 27.5 * Math.pow(2, (key + octave * 12.0) / 12.0);
    }

    public Single(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void add(double frequency, double compressor, double start, double length) {
        if (this.length < (start + length * 2 + 1) * sampleRate) this.length = (int)(start + length * 2 + 1) * sampleRate;
        // добавляем в массив частей по одной ноте определенной частоты
        // с сжатием с позиции start длинной length частотой дескритизации sampleRate
        parts.add(new Part(frequency, compressor, start, length, sampleRate));
    }

    public int[] synt() {
        data = new int[length];
        for (Part part : parts) {
            part.get(data, sampleRate);
        }
        return data;
    }
}
