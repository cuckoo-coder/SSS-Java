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

import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel implements  MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
	private BufferedImage instructions;
	private Tetris window;

	private Timer timer;
	private BufferedImage background;
        private BufferedImage play;
        private Rectangle playBounds;
        private BufferedImage setting;
        private Rectangle settingBounds;
	private int mouseX, mouseY;
        private boolean isShowHelp = false;
        
        
	public Title(Tetris window){
                instructions = ImageLoader.loadImage("/arrow.png");
                play = ImageLoader.loadImage("/PLAY.png");
                background = ImageLoader.loadImage("/BACKGROUND.png");
                setting = ImageLoader.loadImage("/setting_1.png");
                //Play button
                int btnW = 120;
                int btnH = 50;
                int btnX = Tetris.WIDTH / 2 - btnW / 2;
                int btnY = 400;
                playBounds = new Rectangle(btnX, btnY, btnW, btnH);
                
                //Setting button
                int btnSettingY = 460;
                settingBounds = new Rectangle(btnX, btnSettingY, btnW, btnH);
                
                
		timer = new Timer(1000/60, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		timer.start();
		this.window = window;
		addMouseListener(this);
                addMouseMotionListener(this);
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
//		g.setColor(Color.BLACK);
//		
//		g.fillRect(0, 0, Tetris.WIDTH, Tetris.HEIGHT);

                
                g.drawImage(background, 0, 0, Tetris.WIDTH, Tetris.HEIGHT, null);
		String game_name = "TETRIS";
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Georgia", Font.BOLD, 60));
                int textWidth  = g.getFontMetrics().stringWidth(game_name);
                int textHeight = g.getFontMetrics().getAscent();
                int x = (Tetris.WIDTH - textWidth) / 2;

                int y = 200;
                g.drawString(game_name, x, y);
		if(isShowHelp)
                {
                    g.drawImage(instructions, Tetris.WIDTH/2 - instructions.getWidth()/2 - 5, 30 - instructions.getHeight()/2 + 150, null);
                }
		
		
		int normalW = 120;
                int normalH = 50;
                int hoverW = 125;
                int hoverH = 55;
		if(playBounds.contains(mouseX, mouseY)){
                    g.drawImage(play, playBounds.x, playBounds.y, hoverW, hoverH, null);
                }
                else{
                    g.drawImage(play, playBounds.x, playBounds.y, normalW, normalH, null);
                }
                
                if(settingBounds.contains(mouseX, mouseY)) {
                    g.drawImage(setting, settingBounds.x, settingBounds.y, hoverW, hoverH, null);
                }
                else{
                    g.drawImage(setting, settingBounds.x, settingBounds.y, normalW, normalH, null);
                }
	}	


    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) return;

        if (playBounds.contains(mouseX, mouseY)) {
            window.startTetris();
            return;
        }

        if (settingBounds.contains(mouseX, mouseY)) {
            isShowHelp = !isShowHelp;
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
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
