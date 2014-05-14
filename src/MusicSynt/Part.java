/**
 * Created by Neveral on 05.04.14.
 */
package MusicSynt;

public class Part {
    double frequency;
    double compressor; // коэффициент затухания
    int length; // множитель коэффициента (длина звучания ноты в секундах)
    int start;

    public Part(double frequency, double compressor, double start, double length, int sampleRate) {
        this.frequency = Math.PI * 2 * frequency / sampleRate ;
        this.start = (int)(start * sampleRate);
        this.length = (int)(length * sampleRate);
        this.compressor = compressor / sampleRate;
    }

    public void get(int[] data) {
        double result;
        for (int i = start; i < start + length * 2; i++) {
            //result  = 0.5f * Math.sin(frequency * i)*Integer.MAX_VALUE/2;
            result = musicFunc(i, frequency) * Integer.MAX_VALUE;
            result += data[i];
            data[i] = (int)(result);
        }
    }
    private static double musicFunc(int i, double frequency){
        //return  0.5f * Math.sin(frequency * i);
        if (Math.sin(frequency * i ) > 0) return 1;
        else return -1;
    }
}
