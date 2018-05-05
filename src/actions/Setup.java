/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author student
 */
public class Setup {
    private static boolean isSet = false;
    private static boolean isWon = false;
    private static Players[] order = new Players[4];
    private static Players[][] grid = new Players[8][8];
    private static final int yoffset = 20;
    private static int activeid = 0;
    private static final int xoffset = 10;
    private static int windowWidth;
    private static int windowHeight;
    private static Graphics g;
    
    public enum Players {
        SQUARE, CIRCLE, TRIANGLE, X
    }
    
    public static void start(Graphics brush, int width, int height){
        if(!isSet){
            windowWidth = width;
            windowHeight = height;
            
            setPlayerturnOrder();
            isSet = true;
        }
        g = brush;
        drawGrid();
        drawTurnOrder();
        drawPlayerBlocks();
        if(isWon){
            drawWinner();
        }
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
    
    private static void drawTurnOrder(){
        int initx = windowWidth-150;
        int inity = 30;
        
        g.setFont(new Font(null,0,20));
        g.drawString("Turn Order", initx, inity);
        
        for(int i=0; i<4; i++){
            String shape = "";
            
            switch(order[i]){
                case CIRCLE:
                    shape = i+1+". Circle";
                    break;
                case SQUARE:
                    shape = i+1+". Square";
                    break;
                case TRIANGLE:
                    shape = i+1+". Triangle";
                    break;
                case X:
                    shape = i+1+". X";
                    break;
            }
            if(i == activeid)
                g.setFont(new Font(null,1,15));
            else
                g.setFont(new Font(null,0,15));
            g.drawString(shape, initx, inity+((i+1)*20));
        }
    }
    
    private static boolean isWithinGrid(int x, int y){
        int width = windowWidth - 200;
        int height = windowHeight;
        int ytiledelta = (height-yoffset-10)/8; 
        int xtiledelta = (width-xoffset-10)/8;
        
        return (
                (x>xoffset && x<8*xtiledelta+xoffset) &&
                (y>yoffset && y<8*ytiledelta+yoffset)
        );
    }
    
    private static int columnNumber(int x){
        int width = windowWidth - 200;
        int height = windowHeight;
        int ytiledelta = (height-yoffset-10)/8; 
        int xtiledelta = (width-xoffset-10)/8;
        
        int column = -1;
        
        for(int i=0; i<8; i++){
            if(x>=(i*xtiledelta)+xoffset && x<=((i+1)*xtiledelta)+xoffset){
                
                column = i;
                break;
            }
        }
        return column;
    }
    
    public static void reset(){
        isSet = false;
        grid = new Players[8][8];
        activeid = 0;
        isWon = false;
    }
    
    
    private static void checkNeighbours(int x, int y){
        int successiveCount = 0;
        
        for(int i=y-1; i<=y+1; i++){
            if(i<0 || i>=8){
                System.out.println("Skipped Row "+(8-i)+"th row check");
                continue;
            }
            for(int j=x-1; j<=x+1; j++){
                if((x == j && y == i) || j<0 || j>=8 || grid[i][j]==null){
                    System.out.println("Skipped Neighbour ("+(j+1)+","+(8-i)+") of "+grid[y][x]);
                    continue;
                }
                if(grid[i][j] == grid[y][x]){
                    System.out.println("Successive Count: 1 of "+grid[y][x]);
                    int xtrav = 0;
                    int ytrav = 0;
                    
                    if(i<y){
                        ytrav = -1;
                    }else if(i==y){
                        ytrav = 0;
                    }else if(i>y){
                        ytrav = 1;
                    }
                    
                    if(j<x){
                        xtrav = -1;
                    }else if(j==x){
                        xtrav = 0;
                    }else if(j>x){
                        xtrav = 1;
                    }
                    successiveCount = 1;
                    
                    for(int q=0; q<2; q++){
                        for(
                            int k=1; 
                            k<4 && 
                            j+(k*xtrav) < 8 && 
                            j+(k*xtrav) >= 0 &&
                            i+(k*ytrav) < 8 &&
                            i+(k*ytrav) >= 0 &&
                            grid[i+(k*ytrav)][j+(k*xtrav)] != null &&
                            grid[i+(k*ytrav)][j+(k*xtrav)] == grid[y][x] &&
                            successiveCount < 4
                            ; k++, successiveCount++
                        ){System.out.println("Successive Count: "+successiveCount+" of "+grid[y][x]);}
                        xtrav *= -1;
                        ytrav *= -1;
                    }
                    
                    if(successiveCount == 4){
                        isWon = true;
                        return;
                    }
                }
                System.out.println("Skipped Neighbour ("+(j+1)+","+(8-i)+") of "+grid[y][x]);
            }
        }
    }
    
    public static boolean mouseAction(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        
        if(isWon){
            return false;
        }
        
        if(isWithinGrid(x,y)){
            int col = columnNumber(x);
            
            int i;
            for(i=0; i<8 && grid[i][col] != null; i++){}
            
            grid[i][col] = order[activeid];
            checkNeighbours(col,i);
            
            activeid = (activeid+1)%4;
    
            return true;
        }else{
            return false;
        }
    }
    
    private static void drawWinner(){
        int winnerid = (activeid-1 < 0)? 3 : activeid-1;
        String winnerShape = "";
        
        switch(order[winnerid]){
            case CIRCLE:
                winnerShape = "Circle";
                break;
            case SQUARE:
                winnerShape = "Square";
                break;
            case X:
                winnerShape = "X";
                break;
            case TRIANGLE:
                winnerShape = "Triangle";
                break;
        }
        g.setColor(new Color(0,0,0));
        g.drawString("Winner is: "+winnerShape, windowWidth-150, 210);
    }
    
    private static void drawPlayerBlocks(){
        int width = windowWidth - 200;
        int height = windowHeight;
        
        int ytiledelta = (height-yoffset-10)/8;
        int xtiledelta = (width-xoffset-10)/8;
        
        int bottomyoffset = 12;
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(grid[i][j]!= null){
                    switch(grid[i][j]){
                        case SQUARE: 
                            g.setColor(new Color(0,255,0));
                            g.fillRect(j*xtiledelta+xoffset, height-((i+1)*ytiledelta)-bottomyoffset, xtiledelta, ytiledelta);
                            break;
                        case CIRCLE:
                            g.setColor(new Color(255,0,0));
                            g.fillOval(j*xtiledelta+xoffset, height-((i+1)*ytiledelta)-bottomyoffset, xtiledelta, ytiledelta);
                            break;
                        case TRIANGLE: 
                            g.setColor(new Color(0,0,255));
                            g.fillPolygon(
                                    new int[]{j*xtiledelta+xoffset,j*xtiledelta+xoffset+(xtiledelta/2),(j+1)*xtiledelta+xoffset}, 
                                    new int[]{height-(i*ytiledelta)-bottomyoffset,height-((i+1)*ytiledelta)-bottomyoffset,height-(i*ytiledelta)-bottomyoffset}, 
                                    3
                            );
                            break;
                        case X: 
                            g.setColor(new Color(0,0,0));
                            g.drawLine(j*xtiledelta+xoffset,height-(i*ytiledelta)-bottomyoffset,(j+1)*xtiledelta+xoffset,height-((i+1)*ytiledelta)-bottomyoffset);
                            g.drawLine(j*xtiledelta+xoffset,height-((i+1)*ytiledelta)-bottomyoffset,(j+1)*xtiledelta+xoffset,height-((i)*ytiledelta)-bottomyoffset);
                            break;
                    }
                }
            }
        }
    }
    
    private static void drawGrid(){
        int width = windowWidth - 200;
        int height = windowHeight;
        
        int ytiledelta = (height-yoffset-10)/8;
        int xtiledelta = (width-xoffset-10)/8;
        
        for(int i=0; i<9; i++){ 
            g.drawLine(xoffset, i*ytiledelta+yoffset, 8*xtiledelta+xoffset, i*ytiledelta+yoffset); //hortizonta;
            g.drawLine(i*xtiledelta+xoffset, yoffset, i*xtiledelta+xoffset, 8*ytiledelta+yoffset); //vertical
            if(i<8){
                g.drawString(Integer.toString(i+1), xoffset-10, i*ytiledelta+yoffset+10); //vertical labels
                g.drawString(Integer.toString(i+1), i*xtiledelta+xoffset+10, yoffset-5); //horizontal labels
            }
        }
        
    }
}
