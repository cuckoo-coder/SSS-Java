/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.tetris;

import static com.demo.tetris.Board.BLOCK_SIZE;
import static com.demo.tetris.Board.BOARD_HEIGHT;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Admin
 */
public class Shape {
    private int x=4 , y=0 ;
    private int normal = 600;
    private int fast = 50;
    private int delay;
    private int delayTimeForMove = normal;
    private long beginTime;
    
    private int deltaX = 0;
    private boolean collision=false;
    private int [][] coords;
    private Board board;
    private Color color;
    
    private int timePassedFromCollision = -1; // xử lý khi chạm đáy thì tính tiểm
    
    public Shape(int [][] coords, Board board, Color color){
        this.coords=coords;
        this.board = board;
        this.color = color;
    }
    public void setX(int x){
        this.x = x;
        
    }
    public void setY(int y){
        this.y=y;
    }
    public void reset(){
        this.x = 4;
        this.y=0;
        collision = false;
    }
    
    public void update(){
        if (collision) {
            // do mau cho board
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) {
                    if (coords[row][col] != 0) {
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            board.addScore();
            board.setCurrentShape();;
            //return;
        }
        //chuyen dong ngang
        boolean moveX = true;
        if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if (coords[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX) {
                x += deltaX;
            }
        }
        deltaX = 0;
        
        if (System.currentTimeMillis() - beginTime > delayTimeForMove) {
            //cd doc
            if (!(y + coords.length >= BOARD_HEIGHT)) {
                for(int row = 0; row< coords.length; row++){
                    for(int col = 0; col<coords[row].length; col++){
                        if(coords[row][col]!=0){
                            if(board.getBoard()[y+1+row][x+deltaX+col]!=null){
                                collision = true;
                            }
                        }
                    }
                }
                if(!collision)
                y++;
            } else {
                collision = true;
            }
            beginTime = System.currentTimeMillis();
        }
    }
    private int[][] transposeMatrix(int[][] matrix) { // hàm đổi ma trận từ nga thành dọc
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }
    public void rotateShape() {

        int[][] rotatedShape = null;

        rotatedShape = transposeMatrix(coords);

        rotatedShape = reverseRows(rotatedShape);

        if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getBoard()[y + row][x + col] != null) {
                        return;
                    }
                }
            }
        }
        coords = rotatedShape;
    }
    private int[][] reverseRows(int[][] matrix) { // đổi vị trí 

        int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];

            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }

        return matrix;

    }
    private void checkLine(){
        int bottomLine = board.getBoard().length-1;
        for(int topLine =board.getBoard().length-1; topLine>0; topLine-- ){
            int count =0;
            
            for(int col=0; col< board.getBoard()[0].length; col++){
                if(board.getBoard()[topLine][col] != null){
                    count++;
                }
                board.getBoard()[bottomLine][col]= board.getBoard()[topLine][col];
            }
            if(count < board.getBoard()[0].length){
                bottomLine--;
            }
        }
    }
    
    public void render(Graphics g) {
        //ve hinh
        g.setColor(color);
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                if (coords[row][col] != 0) {
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public void speedUp() {
        delayTimeForMove = fast;

    }

    public void speedDown() {
        delayTimeForMove = normal;
    }

    public void moveRight() {
        deltaX = 1;
    }

    public void moveLeft() {
        deltaX = -1;
    }
    public Color getColor() {
        return color;
    }
    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }
    public int[][] getCoords() {
        return coords;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
