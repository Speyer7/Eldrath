package main;

import entity.Player;
import tile.Tile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 4;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWith = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;


    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player PLAYER = new Player(this, keyH);
    TileManager tileM = new TileManager(this);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GREEN);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }


    public int drawCount = 0;
    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;


        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1) {
                repaint();
                update();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000) {
                // DISPLAY FPS
                addFPSToHistory(drawCount);
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update() {
        PLAYER.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);

        PLAYER.draw(g2);

        // DISPLAY FPS
        drawFPS(g2);

        g2.dispose();
    }

    // DISPLAY FPS ---------------

    private int[] fpsHistory = new int[10];
    private int fpsHistoryIndex = 0;
    private double smoothedFPS = 0;
    private void addFPSToHistory(int fps) {
        fpsHistory[fpsHistoryIndex] = fps;
        fpsHistoryIndex = (fpsHistoryIndex + 1) % fpsHistory.length;


        double sum = 0;
        for (int i = 0; i < fpsHistory.length; i++) {
            sum += fpsHistory[i];
        }
        smoothedFPS = sum / fpsHistory.length;
    }

    private void drawFPS(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String fpsText = "FPS: " + (int) smoothedFPS;
        g2.drawString(fpsText, screenWidth - 100, 30);
    }



}
