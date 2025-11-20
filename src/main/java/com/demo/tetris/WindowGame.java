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
public class WindowGame {
    
    public static final int WIDTH = 445, HEIGHT=629;
    
    
    private Board board;
    private JFrame window;
    
    public WindowGame(){
        window = new JFrame("Tetris");
        window.setSize(WIDTH,HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }
    
    public static void main(String[] args){
        new WindowGame();
    }
}
