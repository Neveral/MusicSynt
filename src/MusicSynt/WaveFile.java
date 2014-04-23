package MusicSynt;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WaveFile {
 
    private int INT_SIZE = 4;
    private int sampleSize = -1;
    private long framesCount = -1;
    private byte[] data = null;  // массив байт представляющий аудио-данные
    private AudioFormat af = null;

    /**
     * Создает объект из массива целых чисел
     *
     * @param sampleSize - количество байт занимаемых сэмплом
     * @param sampleRate - частота
     * @param samples - массив значений (данные)
     * @throws Exception если размер сэмпла меньше, чем необходимо 
     * для хранения переменной типа int 
     */
    WaveFile(int sampleSize, float sampleRate, int[] samples) throws Exception {

        if(sampleSize < INT_SIZE){
            throw new Exception("sample size < int size");
        }
         
        this.sampleSize = sampleSize;
        this.af = new AudioFormat(sampleRate, sampleSize*8, 1, true, false);
        this.data = new byte[samples.length*sampleSize];
         
        // заполнение данных
        for(int i=0; i < samples.length; i++){
            setSampleInt(i, samples[i]);
        }
         
        framesCount = data.length / (sampleSize);
    }

    // return: формат аудио данных
    public AudioFormat getAudioFormat(){
        return af;
    }
 
    // return: длительность сигнала
    public double getDurationTime() {
        return getFramesCount() / getAudioFormat().getFrameRate();
    }
     
    // return: количество кадров в файле (фреймов)
    public long getFramesCount(){
        return framesCount;
    }
     
    // сохраняем объект Wave в файл
    public void saveFile(File file) throws IOException{
        AudioSystem.write( new AudioInputStream(new ByteArrayInputStream(data), 
                af, framesCount), AudioFileFormat.Type.WAVE, file); 
    }
     
    /**
     * Устанавливает значение сэмпла 
     * 
     * @param sampleNumber - номер сэмпла
     * @param sampleValue - значение сэмпла
     */
    public void setSampleInt(int sampleNumber, int sampleValue){
     
        // представляем целое число в виде массива байт
        byte[] sampleBytes = ByteBuffer.allocate(sampleSize).
            order(ByteOrder.LITTLE_ENDIAN).putInt(sampleValue).array();
 
        // последовательно записываем полученные байты
        // в место, которое соответствует указанному
        // номеру сэмпла
            for(int i=0; i < sampleSize; i++){
                data[sampleNumber * sampleSize + i] = sampleBytes[i];
            }
        }
    }