package com.demo.tetris;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    // 5 biến riêng biệt – dễ hiểu nhất
    private static Clip bgm;
    private static Clip rotate;
    private static Clip move;
    private static Clip clear;
    private static Clip gameover;

    private static float volume = 0.6f; // âm lượng từ 0.0 đến 1.0

    // Load tất cả âm thanh khi game khởi động
    static {
        bgm      = loadClip("data/sound/bgm.wav");         // nhạc nền
        rotate   = loadClip("data/sound/rotate.wav");      // xoay khối
        move     = loadClip("data/sound/move.wav");        // di chuyển
        clear    = loadClip("data/sound/clear.wav");       // xóa dòng
        gameover = loadClip("data/sound/gameover.wav");   // thua cuộc

        // Chỉ nhạc nền mới loop liên tục
        if (bgm != null) {
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }


    private static Clip loadClip(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Không tìm thấy: " + path);
                return null;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            System.out.println("Lỗi load âm thanh: " + path);
            return null;
        }
    }


    public static void playBGM() {
        if (bgm != null && !bgm.isRunning()) {
            bgm.setFramePosition(0);
            bgm.start();
        }
    }

    public static void stopBGM() {
        if (bgm != null && bgm.isRunning()) {
            bgm.stop();
        }
    }

    public static void playRotate() {
        playOnce(rotate);
    }

    public static void playMove() {
        playOnce(move);
    }

    public static void playClear() {
        playOnce(clear);
    }

    public static void playGameOver() {
        playOnce(gameover);
    }


    private static void playOnce(Clip clip) {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);


        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = 20f * (float) Math.log10(volume);
            gainControl.setValue(Math.max(gain, gainControl.getMinimum()));
        } catch (Exception ignored) {}

        clip.start();
    }
    
    public static void pauseBGM() {
        if (bgm != null && bgm.isRunning()) {
            bgm.stop(); // Chỉ stop, không reset position
        }
    }

    public static void resumeBGM() {
        if (bgm != null && !bgm.isRunning()) {
            bgm.start(); // Tiếp tục từ vị trí cũ
        }
    }

    public static void restartBGM() {
        if (bgm != null) {
            bgm.stop();
            bgm.setFramePosition(0); // Quay về đầu
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY); // Đảm bảo vẫn loop
        }
    }

    public static void stopAll() {  
        if (bgm != null) bgm.stop();
        if (rotate != null) rotate.stop();
        if (move != null) move.stop();
        if (clear != null) clear.stop();
        if (gameover != null) gameover.stop();
    }
}