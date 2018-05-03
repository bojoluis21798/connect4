/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author student
 */
public class Setup {
    private static Players[] order = new Players[4];
    
    public enum Players {
        SQUARE, CIRCLE, TRIANGLE, X
    }
    
    public static void start(Graphics g){
        drawGrid(g);
        setPlayerturnOrder();
    }
    
    public static Players[] getPlayerturnOrder(){
        return order;
    }
    
    private static void setPlayerturnOrder(){
        order[0] = Players.SQUARE;
        order[1] = Players.CIRCLE;
        order[2] = Players.TRIANGLE;
        order[3] = Players.X;
        
        Random rnd = ThreadLocalRandom.current();
        for (int i = order.length - 1; i > 0; i--)
        {
          int index = rnd.nextInt(i + 1);
          // Simple swap
          Players a = order[index];
          order[index] = order[i];
          order[i] = a;
        }
    }
    
    private static void drawGrid(Graphics g){
        int tiledelta = 471/8;
        
        for(int i=0; i<9; i++){ //horizontal
            if(i<8){
                g.drawString(i+1+"", 0, i*tiledelta+30);
            }
            g.drawLine(8, i*tiledelta+15, 471, i*tiledelta+15);
        }
        for(int i=0; i<9; i++){ //vertical
            if(i<8){
                g.drawString(i+1+"", i*tiledelta+15, 13);
            }
            g.drawLine(i*tiledelta+8, 15, i*tiledelta+8, 479);
        }
    }
}
