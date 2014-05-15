/**
 * Created by Neveral on 05.04.14.
 */
package MusicSynt;

public class Part {

    ///////////////////
    float FdM[]; // external force divided by mass
    float P[];   // position
    float V[];   // velocity
    int stLen;

    float k1, k2;

    int NN;
    float NK;
    ////////////////////////////////

    double frequency;
    static int factor = 10000;
    int length;
    int start;
    float Frc;
    int LenMain;
    float ArS[];
    int ArI[];

    public Part(double frequency, float Frc, double compressor, double start, double length, int sampleRate) {
        this.frequency = Math.PI * 2 * frequency / sampleRate ;
        this.start = (int)(start * sampleRate);
        this.length = (int)(length * sampleRate);
        this.Frc = Frc;
    }

    public void get(int[] data, int lenM) {
        //....

        this.LenMain=lenM;
        int sLen = 200;
        //float Frc = 1000.0f / 1000.0f;
        float kDemp = 1.0f * (250.0f / 1000.0f) * (250.0f / 1000.0f) * (250.0f / 1000.0f) * (250.0f / 1000.0f);
        float kHard = 50.0f / 100.0f;
        float kRand = 50.0f / 100.0f;

        initString(sLen, Frc, kDemp, kRand, kHard);
        render();
        stoI();

        for (int i = start; i < start+length*2; i++) {
            //result  = 0.5f * Math.sin(frequency * i)*Integer.MAX_VALUE/2;
            //result = musicFunc(i, frequency) * Integer.MAX_VALUE;
            //result += data[i];
            data[i] = (ArI[i])*factor;
        }
    }
    private static double musicFunc(int i, double frequency){
        //return  0.5f * Math.sin(frequency * i);
        if (Math.sin(frequency * i ) > 0) return 1;
        else return -1;
    }

    void initString(int sLen, float Frc, float kDemp, float kRand, float kHard)
    {
        int i;

        stLen = sLen;

        P = new float[4*sLen];
        V = new float[4*sLen];
        FdM = new float[4*sLen];

        k1 = 1 - kDemp * 0.5f;
        k2 = kDemp * 0.25f;

        NK = ((float)stLen * 0.1f) * (1.0f - kHard) + 1.0f;
        NN = (int)(NK);
        NK = NK - NN;

        for(i = 0 ; i < stLen ; i++)
        {
            FdM[i] = 0;
            V[i] = P[i] = 0;
        }

        for(i = 1 ; i < stLen - 1 ; i++)
        {
            float m = 1.0f + kRand * (frand() - 0.5f);
            FdM[i] = Frc / m;
        }
        for(i = 1 ; i < stLen / 4 ; i++)
        {
            V[i] = 1;
        }
    }

    float frand() { return ( ((float)Math.random())/(float)32767); }

    /////

    float MaxS;

    void render()
    {
        ArS = new float[this.LenMain*4];
        int i;

        MaxS = 0;

        for(i = 0 ; i < LenMain ; i++)
        {
            ArS[i] = StTick();
            if(MaxS < Math.abs(ArS[i])) { MaxS = Math.abs(ArS[i]); }
        }
    }

    void stoI()
    {
        ArI = new int[LenMain*2];
        int i;
        float k = 32767 / MaxS;

        for(i = 0 ; i < LenMain ; i++)
        {
            ArI[i] = (int)(ArS[i] * k);
        }
    }

    float StTick()
    {
        int i;

        P[0] = -P[1];
        V[0] = -V[1];

        P[stLen] = -P[stLen - 1];
        V[stLen] = -V[stLen - 1];

        // velocities
        for(i = 1 ; i < stLen - 1 ; i++)
        {
            float d = (P[i - 1] + P[i + 1]) * 0.5f - P[i];
            V[i] += d * FdM[i];
        }

        // damping
        for(i = 1 ; i < stLen - 1 ; i++)
        {
            V[i] = V[i] * k1 + (V[i - 1] + V[i + 1]) * k2;
        }

        // positions
        for(i = 1 ; i < stLen ; i++)
        {
            P[i] += V[i];
        }

        float res = P[NN] * (1 - NK) + P[NN + 1] * NK;
        return res;
    }
}
