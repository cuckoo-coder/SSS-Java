/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.demo.tetris;

import javax.swing.JFrame;
/**
 *
 * @author macairm1
 */
public class Tetris {
    
    public static final int WIDTH = 445, HEIGHT=640;
    
    
    private Board board;
    private Title title;
    private JFrame window;
    
    public Tetris(){
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        board = new Board();
        title = new Title(this);

        window.addKeyListener(board);

        window.add(title);

        window.setVisible(true);
    }
    public void startTetris() {
        window.remove(title);
        board.addMouseMotionListener(board);
        board.addMouseListener(board);
        window.add(board);
        board.startGame();
        window.revalidate();
    }
    public static void main(String[] args){
        new Tetris();
    }
}
