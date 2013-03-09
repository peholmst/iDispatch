package net.pkhsolutions.idispatch.runboard;

import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.pkhsolutions.idispatch.runboard.rest.Notification;

public class Alarm implements Observer {

    private AudioInputStream alarmWav;
    private AudioInputStream beepWav;
    private Clip alarmClip;
    private Clip beepClip;

    public Alarm() {
        try {
            alarmWav = AudioSystem.getAudioInputStream(Alarm.class
                    .getResource("/sounds/alarm.wav"));
            beepWav = AudioSystem.getAudioInputStream(Alarm.class.getResource("/sounds/beep.wav"));
            alarmClip = AudioSystem.getClip();
            alarmClip.open(alarmWav);
            beepClip = AudioSystem.getClip();
            beepClip.open(beepWav);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, "Error initializing sound system, no sounds will be played", ex);
        }
    }

    public void soundAlarm() {
        try {
            alarmClip.stop();
            alarmClip.loop(8);
        } catch (Exception ex) {
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, "Error playing alarm", ex);
        }
    }

    public void soundBeep() {
        try {
            beepClip.stop();
            beepClip.loop(1);
        } catch (Exception ex) {
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, "Error playing beep", ex);
        }
    }
    private Set<Notification> seenNotifications = new HashSet<>();

    @Override
    public void update(Observable o, Object arg) {
        Model model = (Model) o;
        Set<Notification> notificationsToDelete = new HashSet<>(seenNotifications);
        for (Notification n : model.getVisibleNotifications()) {
            if (!notificationsToDelete.remove(n)) {
                seenNotifications.add(n);
                soundAlarm();
            }
        }
        seenNotifications.removeAll(notificationsToDelete);

        if (model.hasError()) {
            soundBeep();
        }
    }

    public static void main(String[] args) throws Exception {
        Alarm alarm = new Alarm();
        alarm.soundAlarm();
        Thread.sleep(3000);
        alarm.soundAlarm();
        System.in.read();
    }
}
