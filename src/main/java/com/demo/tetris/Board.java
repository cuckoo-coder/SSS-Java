/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.demo.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
/**
 *
 * @author macairm1
 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
private static final long serialVersionUID = 1L;    
    private static int FPS=60;
    private static int delay = FPS/1000;
    
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
    
    private Timer looper;
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    private Random random;
    
    private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};

    private Shape shapes[] = new Shape[7];
    private static Shape currentShape, nextShape;
    
    private BufferedImage pause, refresh, bgsetting, homeButton;
    private Rectangle stopBounds, refreshBounds;
    private Rectangle pauseBoundsCenter; 
    private Rectangle homeBounds;
    
    private int mouseX, mouseY;

    private boolean leftClick = false;

    private BufferedImage bgBoard;

    private boolean gamePaused = false;

    private boolean gameOver = false;
    
    
    //coin animation
    private int score = 0;
    private BufferedImage coinSheet;      
    private BufferedImage[] coinFrames;   
    private int coinFrameIndex = 0;      
    private int coinFrameCounter = 0; 
    private Timer buttonLapse = new Timer(300, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });
    public Board(){
        random = new Random();
        
        pause = ImageLoader.loadImage("/PAUSE2.png");
        refresh = ImageLoader.loadImage("/RESTART.png");
        bgBoard = ImageLoader.loadImage("/BACKGROUND2.png");
        coinSheet = ImageLoader.loadImage("/coin_sheet.png");
        bgsetting = ImageLoader.loadImage("/main_1.png");
        homeButton = ImageLoader.loadImage("/HOME.png");
        
        int frameCount = 6;                             // số frame
        int frameWidth  = coinSheet.getWidth() / frameCount;
        int frameHeight = coinSheet.getHeight();
        
        coinFrames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            coinFrames[i] = coinSheet.getSubimage(
                    i * frameWidth,   // x = frame i
                    0,                // y = 0 vì nằm 1 hàng ngang
                    frameWidth,
                    frameHeight
            );
        }
        mouseX = 0;
        mouseY = 0;
        int btnW = 60;
        int btnH = 40;
        stopBounds = new Rectangle(340, 500, btnW, btnH);
        pauseBoundsCenter = new Rectangle(
        Tetris.WIDTH / 2 - btnW / 2,
        Tetris.HEIGHT / 2 - btnH / 2,
        btnW, btnH);
//        refreshBounds = new Rectangle(340, 450, btnW, btnH);
        refreshBounds = new Rectangle(
        Tetris.WIDTH / 2 - btnW / 2,
        Tetris.HEIGHT / 2 - 100,
        btnW, btnH);
        homeBounds = new Rectangle(
            Tetris.WIDTH / 2 - btnW / 2,
            Tetris.HEIGHT / 2 + 80,  // dưới nút Restart
            btnW, btnH
        );
        looper = new Timer(delay, new GameLooper());
        
        shapes[0] = new Shape(new int[][]{{1,1,1,1}},
                this, colors[0]);
        shapes[1] = new Shape(new int[][]{
            {1,1,1},
            {0,1,0},
        },
                this, colors[1]);
        shapes[2] = new Shape(new int[][]{
            {1,1,1},
            {1,0,0},
        },
                this, colors[2]);
        shapes[3] = new Shape(new int[][]{
            {1,1,1},
            {0,0,1},
        },
                this, colors[3]);
        shapes[4] = new Shape(new int[][]{
            {0,1,1},
            {1,1,0},
        },
                this, colors[4]);
        shapes[5] = new Shape(new int[][]{
            {1,1,0},
            {0,1,1},
        },
                this, colors[5]);
        shapes[6] = new Shape(new int[][]{
            {1,1},
            {1,1},
        },
                this, colors[6]);
        
        currentShape = shapes[0];
        
//        looper = new Timer(delay, new ActionListener(){  Đoạn này chuyển xuống dưới thành class Gameloop
//            int n = 0;
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                update();
//                repaint();
//            }
//            
//        }); 
//        looper.start();
                
    }
    private void update(){
        
    if (!gamePaused) {
        if (stopBounds.contains(mouseX, mouseY) &&
            leftClick && !buttonLapse.isRunning() && !gameOver) {
                buttonLapse.start();
                gamePaused = true;
        }
    } else {
        // Đang ở màn setting → dùng nút pause ở giữa
        if (pauseBoundsCenter.contains(mouseX, mouseY) &&
            leftClick && !buttonLapse.isRunning()) {

            buttonLapse.start();
            gamePaused = false;   // tắt setting, tiếp tục game
        }
        if (refreshBounds.contains(mouseX, mouseY) &&
            leftClick && !buttonLapse.isRunning()) {

            gamePaused = false;  
            startGame();         
        }
    }
    if(gameOver)
    {
       if (refreshBounds.contains(mouseX, mouseY) &&
            leftClick && !buttonLapse.isRunning()) {

            gamePaused = false;  
            startGame();         
        } 
    }
        

        if (gamePaused || gameOver) {
            return;
        }
        currentShape.update();
        coinFrameCounter++;
        if (coinFrameCounter >= 8) {           
            coinFrameCounter = 0;
            coinFrameIndex = (coinFrameIndex + 1) % coinFrames.length;
        }
    }
    public void setNextShape() {
        int index = random.nextInt(shapes.length);
        int colorIndex = random.nextInt(colors.length);
        nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);
    }
    public void setCurrentShape(){
//        currentShape = shapes[random.nextInt(shapes.length)];
        currentShape = nextShape;
        setNextShape();
        for (int row = 0; row < currentShape.getCoords().length; row++) {
            for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
                if (currentShape.getCoords()[row][col] != 0) {
                    if (board[currentShape.getY() + row][currentShape.getX() + col] != null) {
                        gameOver = true;
                    }
                }
            }
        }
//        currentShape.reset();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.setColor(Color.black);
//        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);

        g.setFont(new Font("Georgia", Font.BOLD, 30));
        g.drawString(score + "", Tetris.WIDTH - 80, Tetris.HEIGHT / 2 + 25);
        if (coinFrames != null) {
            BufferedImage frame = coinFrames[coinFrameIndex];

            int coinDrawWidth  = 30;  
            int coinDrawHeight = 30;

            int coinX = 35;
            int coinY = 60;

            g.drawImage(frame, Tetris.WIDTH - 125, Tetris.HEIGHT / 2, coinDrawWidth, coinDrawHeight, null);
        }
        g.drawImage(bgBoard, 0, 0, getWidth(), getHeight(), null);
        currentShape.render(g);
        
        for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] != null) {
                        g.setColor(board[row][col]);
                        g.fillRect(col*BLOCK_SIZE, row*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCoords().length; row++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }
        //ve bang
        g.setColor(Color.white);
        for(int row =0 ; row<= BOARD_HEIGHT;row++){
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * row);
        }
        
        for(int col =0 ; col<= BOARD_WIDTH;col++){
            g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * BOARD_HEIGHT);
        }
        
        int normalW = 60, normalH = 40;
        int hoverW  = 65, hoverH  = 45;
        if (stopBounds.contains(mouseX, mouseY)) {
            g.drawImage(pause, stopBounds.x, stopBounds.y, hoverW, hoverH, null);
        } else {
            g.drawImage(pause, stopBounds.x, stopBounds.y, normalW, normalH, null);
        }
        
        if (!gamePaused) {
            if (stopBounds.contains(mouseX, mouseY)) {
                g.drawImage(pause,
                            stopBounds.x, stopBounds.y,
                            hoverW, hoverH, null);
            } else {
                g.drawImage(pause,
                            stopBounds.x, stopBounds.y,
                            normalW, normalH, null);
            }
        }
        if (gamePaused) {
            
            g.drawImage(bgsetting, 0, 0, getWidth(), getHeight(), null);
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 100, Tetris.HEIGHT - 550);
            if (pauseBoundsCenter.contains(mouseX, mouseY)) {
                g.drawImage(pause,
                            pauseBoundsCenter.x, pauseBoundsCenter.y,
                            hoverW, hoverH, null);
            } else {
                g.drawImage(pause,
                            pauseBoundsCenter.x, pauseBoundsCenter.y,
                            normalW, normalH, null);
            }
            if (refreshBounds.contains(mouseX, mouseY)) {
                g.drawImage(refresh, 
                        refreshBounds.x,
                        refreshBounds.y, 
                        hoverW, hoverH, 
                        null);
            } else {
                g.drawImage(refresh, refreshBounds.x, refreshBounds.y, normalW, normalH, null);
            }
            
        }
        if (gameOver) {
            g.drawImage(bgsetting, 0, 0, getWidth(), getHeight(), null);
            String gameOverString = "GAME OVER";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gameOverString, 100, Tetris.HEIGHT - 550);
            if (refreshBounds.contains(mouseX, mouseY)) {
                g.drawImage(refresh, 
                        refreshBounds.x,
                        refreshBounds.y, 
                        hoverW, hoverH, 
                        null);
            } else {
                g.drawImage(refresh, refreshBounds.x, refreshBounds.y, normalW, normalH, null);
            }
        }
        
        
    }
    
    public Color[][] getBoard(){
        return board;
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {}
    public void startGame() {
        stopGame();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        looper.start();

    }
    public void stopGame() {
        score = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = null;
            }
        }
        looper.stop();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotateShape();
        }else if(e.getKeyCode()== KeyEvent.VK_DOWN){
            currentShape.speedUp();
        }else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
            currentShape.moveRight();
        }else if(e.getKeyCode()== KeyEvent.VK_LEFT){
            currentShape.moveLeft();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            currentShape.speedDown();
        }
    }
    class GameLooper implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            repaint();
        }

    }
    public void addScore() {
        score++;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
}
