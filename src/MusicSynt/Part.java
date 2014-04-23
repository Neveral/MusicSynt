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

    public void get(int[] data, int sampleRate) {
        double result;
        int position;
        for (int i = start; i < start + length * 2; i++) {
            position = i - start;
            result  = 0.5 * Math.sin(frequency * i);
            result += 0.4 * Math.sin(frequency / 4 * i);
            result += 0.2 * Math.sin(frequency / 2 * i);
            result *= length(compressor, frequency, position, length, sampleRate) * Integer.MAX_VALUE * 0.25;
            result += data[i];
            if (result > Integer.MAX_VALUE) result = Integer.MAX_VALUE;
            if (result < -Integer.MAX_VALUE) result = -Integer.MAX_VALUE;
            data[i] = (int)(result);
        }
    }

    // делаем затухание
    public static double length(double compressor, double frequency, double position, double length, int sampleRate){
        return Math.exp(((compressor / sampleRate) * frequency * sampleRate * (position / sampleRate)) / (length / sampleRate));
    }
}
