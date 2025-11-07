package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioPlayer {

    private static volatile MediaPlayer player;

    public static void play(String resourcePath, int stopAfterMillis) {
        try {
            URL audioUrl = AudioPlayer.class.getResource(resourcePath);
            if (audioUrl == null) {
                System.err.println("Audio resource not found: " + resourcePath);
                return;
            }

            stop();

            Media media = new Media(audioUrl.toExternalForm());
            MediaPlayer newPlayer = new MediaPlayer(media);

            synchronized (AudioPlayer.class) {
                player = newPlayer;
            }

            player.play();

            final MediaPlayer created = newPlayer;
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    synchronized (AudioPlayer.class) {
                        try {
                            if (player == created) {
                                try {
                                    created.stop();
                                } catch (Exception ignored) {
                                }
                                try {
                                    created.dispose();
                                } catch (Exception ignored) {
                                }
                                player = null;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }, stopAfterMillis);

        } catch (Exception e) {
            System.err.println("Failed to play audio: " + e.getMessage());
        }
    }

    public static void stop() {
        synchronized (AudioPlayer.class) {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ignored) {
                }
                try {
                    player.dispose();
                } catch (Exception ignored) {
                }
                player = null;
            }
        }
    }
}
