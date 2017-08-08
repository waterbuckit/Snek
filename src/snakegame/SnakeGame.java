/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author Waterbucket
 */
public class SnakeGame {

    private JFrame frame;

    public SnakeGame() {
        this.frame = new JFrame();
        this.frame.setSize(new Dimension(510, 510));
        this.frame.setTitle("Snek");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.add(new Game());
        this.frame.validate();
    }

    public static void main(String[] args) {
        SnakeGame sg = new SnakeGame();
    }

    private class SnakeSegment {

        private int x;
        private int y;
        
        public SnakeSegment(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
        
    }

    private class Game extends JPanel {

        private Location[][] gameSpace;
        private ArrayList<SnakeSegment> trail;
        private Random rand;
        // add these variables to the head to work out the position of the next movement
        private int snakeMoveX;
        private int snakeMoveY;
        // keeps track of the Snake's head and tail
        private int snakeHeadPointer;
        private int snakeTailPointer;

        public Game() {
            this.setBackground(Color.white);
            this.trail = new ArrayList<>();
            this.snakeHeadPointer = 0;
            this.snakeTailPointer = 0;
            this.snakeMoveX = 0;
            this.snakeMoveY = 0;
            this.rand = new Random();
            /*
             50x50 size 2D array because we scale everything by 10 i
             window is 500x500. 
             */
            this.gameSpace = new Location[50][50];
            this.setKeyBindings();
            this.setUpGameSpace();
            this.placeFirstSegment();
            this.placeFruit();
            this.setVisible(true);
            this.startGame();
        }

        private void startGame() {

            int timeDelay = 100; // msecs delay
            new Timer(timeDelay, (ActionEvent arg0) -> {
                this.moveSnake();
                this.repaint();
                this.revalidate();
            }).start();
        }

        public void addKeyBinding(JComponent component, int keyCode, String id, ActionListener action) {
            InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = component.getActionMap();

            im.put(KeyStroke.getKeyStroke(keyCode, 0, false), id);
            am.put(id, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerformed(e);
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
//            g2d.scale(10, 10);
            for (int y = 0; y < gameSpace.length; y++) {
                for (int x = 0; x < gameSpace.length; x++) {
                    gameSpace[x][y].draw(g2d, x, y);
                }
            }
        }

        /**
         * Places the fruit in a random Location but checks whether there is
         * currently a segment there.
         */
        private void placeFruit() {
            int x = rand.nextInt(50);
            int y = rand.nextInt(50);
            while (this.gameSpace[x][y].getSeg() != null) {
                placeFruit();
            }
            this.gameSpace[x][y].setFruit(new Fruit());
        }

        /**
         * Places the first segment at the centre of the gameSpace array.
         */
        private void placeFirstSegment() {
            this.trail.add(new SnakeSegment(25,25));
            this.gameSpace[25][25].setSeg(new SnakeSegment(25, 25));
        }

        /**
         * Moves the snake by removing the tail segment and adding a new segment
         * to the head based on the result of vector addition on snakeHead and
         * snakeMove
         *
         * Realised the this logic is totally wrong because you can't work out
         * the next position of the tail. In essence, it is impossible to move
         * like this.
         *  
         * Instead we can have a pointer which will point to the next segment to 
         * move, so when the tail moves to the new position, it becomes head 
         * and the original tail - 1 = new tail!
         */
        private void moveSnake() {
            int newHeadPosX = trail.get(snakeHeadPointer).getX() + snakeMoveX;
            int newHeadPosY = trail.get(snakeHeadPointer).getY() + snakeMoveY;
            System.out.println("X: " + newHeadPosX + " Y: " + newHeadPosY);
            if(newHeadPosX < 0){
                newHeadPosX = this.gameSpace.length - 1;
            }else if(newHeadPosX == this.gameSpace.length){
                newHeadPosX = 0;
            }else if(newHeadPosY < 0){
                newHeadPosY = this.gameSpace.length - 1;
            }else if(newHeadPosY == this.gameSpace.length){
                newHeadPosY = 0;
            }
            // check whether the next location is a fruit 
//            
            if(this.gameSpace[newHeadPosX][newHeadPosY].getFruit() != null){
                this.gameSpace[newHeadPosX][newHeadPosY].setFruit(null);
                this.placeFruit();
//                // shift the elements to insert the new segment in front of the head pointer
                this.trail.add(snakeHeadPointer, new SnakeSegment(newHeadPosX, newHeadPosY));
                this.gameSpace[trail.get(snakeHeadPointer).getX()][trail.get(snakeHeadPointer).getY()].setSeg(new SnakeSegment(newHeadPosX, newHeadPosY));
                for(int i = 0; i < trail.size(); i++){
                    System.out.println(i + " x: " + trail.get(i).getX() + " y: " + trail.get(i).getY()) ;
                }
                return;
            }
//            trail.get(snakeHeadPointer).setX(newHeadPosX);
//            trail.get(snakeHeadPointer).setY(newHeadPosY);
            this.gameSpace[trail.get(snakeTailPointer).getX()][trail.get(snakeTailPointer).getY()].setSeg(null);
            int tempTailPointer = snakeTailPointer;
            if(snakeTailPointer - 1 < 0){
                snakeTailPointer = trail.size() - 1;
            }else{
                snakeTailPointer--;
            }
            snakeHeadPointer = tempTailPointer;
            trail.get(snakeHeadPointer).setX(newHeadPosX);
            trail.get(snakeHeadPointer).setY(newHeadPosY);
            this.gameSpace[trail.get(snakeHeadPointer).getX()][trail.get(snakeHeadPointer).getY()].setSeg(new SnakeSegment(newHeadPosX, newHeadPosY));
        }

        private void setUpGameSpace() {
            for (int y = 0; y < this.gameSpace.length; y++) {
                for (int x = 0; x < this.gameSpace.length; x++) {
                    this.gameSpace[x][y] = new Location();
                }
            }
        }

        private void setKeyBindings() {
            this.addKeyBinding(this, KeyEvent.VK_UP, "Up", (evt) -> {
                snakeMoveX = 0;
                if (snakeMoveY == -1 || snakeMoveY == 1) {
                    return;
                }
                snakeMoveY = -1;
            });

            this.addKeyBinding(this, KeyEvent.VK_DOWN, "Down", (evt) -> {
                snakeMoveX = 0;
                if (snakeMoveY == 1 || snakeMoveY == -1) {
                    return;
                }
                snakeMoveY = 1;
            });
            this.addKeyBinding(this, KeyEvent.VK_LEFT, "Left", (evt) -> {
                snakeMoveY = 0;
                if (snakeMoveX == -1 || snakeMoveX == 1) {
                    return;
                }
                snakeMoveX = -1;
            });
            this.addKeyBinding(this, KeyEvent.VK_RIGHT, "Right", (evt) -> {
                snakeMoveY = 0;
                if (snakeMoveX == 1 || snakeMoveX == -1) {
                    return;
                }
                snakeMoveX = 1;
            });
        }

        /**
         * Location class will store either a segment or a fruit.
         */
        class Location {

            private SnakeSegment seg;
            private Fruit fruit;

            public Location() {
                this.fruit = null;
                this.seg = null;
            }

            public void setSeg(SnakeSegment seg) {
                this.seg = seg;
            }

            public void setFruit(Fruit fruit) {
                this.fruit = fruit;
            }

            public Fruit getFruit() {
                return fruit;
            }

            public SnakeSegment getSeg() {
                return seg;
            }

            /**
             * Will draw the necessary rectangles to represent segments or
             * fruits
             *
             * @param g2d
             * @param x
             * @param y
             */
            public void draw(Graphics2D g2d, int x, int y) {
                if (fruit != null) {
                    g2d.setColor(Color.red);
                    g2d.drawRect(x*10, y*10, 1*10, 1*10);
                } else if (seg != null) {
                    g2d.setColor(Color.green);
                    g2d.drawRect(x*10, y*10, 1*10, 1*10);
                }
            }
        }

        private class Fruit {

            public Fruit() {
            }
        }
    }

    /**
     * creates new locations in the gameSpace
     */
    /**
     * absolutely disgusting method that needs work
     */
//        private void placeNewSegment() {
//            int newHeadPositionX = (int) this.snakeHead.getX() + snakeMoveX;
//            int newHeadPositionY = (int) this.snakeHead.getY() + snakeMoveY;
//            if (newHeadPositionX < 0) {
//                this.snakeHead.setLocation(this.gameSpace.length - 1, newHeadPositionY);
//                this.setSegment(this.snakeHead);
//            } else if (newHeadPositionX == this.gameSpace.length) {
//                this.snakeHead.setLocation(0, newHeadPositionY);
//                this.setSegment(this.snakeHead);
//            } else if (newHeadPositionY < 0) {
//                this.snakeHead.setLocation(newHeadPositionX, this.gameSpace.length - 1);
//                this.setSegment(this.snakeHead);
//            } else if (newHeadPositionY == this.gameSpace.length) {
//                this.snakeHead.setLocation(0, newHeadPositionY);
//                this.setSegment(this.snakeHead);
//            } else {
//                this.snakeHead.setLocation(newHeadPositionX, newHeadPositionY);
//                this.setSegment(this.snakeHead);
//            }
//        }
    /**
     * will set a segment for a particular point.
     *
     * @param point
     */
//        public class KeyEventHandler implements KeyListener {
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int keyCode = e.getKeyCode();
//                switch (keyCode) {
//                    case KeyEvent.VK_UP:
//                        snakeMoveX = 0;
//                        System.out.println("PRESSED");
//                        if (snakeMoveY == -1) {
//                            break;
//                        }
//                        snakeMoveY = -1;
//                        break;
//                    case KeyEvent.VK_DOWN:
//                        snakeMoveX = 0;
//                        if (snakeMoveY == 1) {
//                            break;
//                        }
//                        snakeMoveY = 1;
//                        break;
//                    case KeyEvent.VK_LEFT:
//                        snakeMoveY = 0;
//                        if (snakeMoveX == -1) {
//                            break;
//                        }
//                        snakeMoveX = -1;
//                        break;
//                    case KeyEvent.VK_RIGHT:
//                        snakeMoveY = 0;
//                        if (snakeMoveX == 1) {
//                            break;
//                        }
//                        snakeMoveY = 1;
//                        break;
//                }
//            }
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        }
}
