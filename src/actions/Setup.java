/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.awt.Graphics;

/**
 *
 * @author student
 */
public class Setup {
    
    public static void drawGrid(Graphics g){
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
