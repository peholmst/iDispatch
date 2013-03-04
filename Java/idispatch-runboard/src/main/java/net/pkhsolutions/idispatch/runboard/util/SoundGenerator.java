package net.pkhsolutions.idispatch.runboard.util;

import javax.sound.sampled.Clip;

/**
 *
 * @author peholmst
 */
public class SoundGenerator {

    private Clip clip;

    public SoundGenerator() {
    }
/*
    public void beep() throws LineUnavailableException {
        if (clip != null) {
            clip.stop();
            clip.close();
        } else {
            clip = AudioSystem.getClip();
        }

        int intSR = ((Integer) sampleRate.getSelectedItem()).intValue();
        int intFPW = framesPerWavelength.getValue();

        float sampleRate = (float) intSR;

        // oddly, the sound does not loop well for less than
        // around 5 or so, wavelengths
        int wavelengths = 20;
        byte[] buf = new byte[2 * intFPW * wavelengths];
        AudioFormat af = new AudioFormat(
                sampleRate,
                8, // sample size in bits
                2, // channels
                true, // signed
                false // bigendian
                );

        int maxVol = 127;
        for (int i = 0; i < intFPW * wavelengths; i++) {
            double angle = ((float) (i * 2) / ((float) intFPW)) * (Math.PI);
            buf[i * 2] = getByteValue(angle);
            if (addHarmonic) {
                buf[(i * 2) + 1] = getByteValue(2 * angle);
            } else {
                buf[(i * 2) + 1] = buf[i * 2];
            }
        }

        try {
            byte[] b = buf;
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(b),
                    af,
                    buf.length / 2);

            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

     }*/

    public static void soundAlarm(int numberOfUrgencyBeeps) {
    }

}
