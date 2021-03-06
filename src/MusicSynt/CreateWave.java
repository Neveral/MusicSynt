/**
 * Created by Neveral on 05.04.14.
 */
package MusicSynt;

import java.io.File;
 
public class CreateWave {
    public static void main(String[] args) throws Exception {

        int sampleRate = 44100;
        Single test = new Single(sampleRate);
        test.fillMap();
        test.music("A5 A4 A5 A4 A5");
        System.out.println("Create file...");
        int[] samples;
        samples = test.synt();
        WaveFile wf = new WaveFile(4, sampleRate, samples);
        wf.saveFile(new File("test1.wav"));
        System.out.println("Продолжительность " + wf.getDurationTime()+ " сек.");
    }
}