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
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Adam
 */
public class SnakeGame {

    private JFrame frame;

    public SnakeGame() {
        this.frame = new JFrame();
        this.frame.setSize(new Dimension(500, 500));
        this.frame.setBackground(Color.white);
        this.frame.setTitle("Snek");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.add(new Game());
    }

    public static void main(String[] args) {
        SnakeGame sg = new SnakeGame();
    }

    private class SnakeSegment {

        public SnakeSegment() {
        }
    }

    private class Game extends JPanel {

        private Location[][] gameSpace;
        private Random rand;
        // add these variables to the head to work out the position of the next movement
        private int snakeMoveX;
        private int snakeMoveY;
        // keeps track of the Snake's head and tail
        private Point snakeHead;
        private Point snakeTail;

        public Game() {
            this.snakeHead = new Point();
            this.snakeTail = new Point();
            this.snakeMoveX = 0;
            this.snakeMoveY = 0;
            this.rand = new Random();
            this.gameSpace = new Location[50][50];
            this.setUpGameSpace();
            this.placeFirstSegment();
            this.placeFruit();

            while (true) {
                moveSnake();
                this.repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(10, 10);
            for (int y = 0; y < gameSpace.length; y++) {
                for (int x = 0; x < gameSpace.length; x++) {
                    gameSpace[x][y].draw(g2d, x, y);
                }
            }
        }

        // handles key presses
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    snakeMoveX = 0;
                    if (snakeMoveY == -1) {
                        break;
                    }
                    snakeMoveY = -1;
                    break;
                case KeyEvent.VK_DOWN:
                    snakeMoveX = 0;
                    if (snakeMoveY == 1) {
                        break;
                    }
                    snakeMoveY = 1;
                    break;
                case KeyEvent.VK_LEFT:
                    snakeMoveY = 0;
                    if (snakeMoveX == -1) {
                        break;
                    }
                    snakeMoveX = -1;
                    break;
                case KeyEvent.VK_RIGHT:
                    snakeMoveY = 0;
                    if (snakeMoveX == 1) {
                        break;
                    }
                    snakeMoveY = 1;
                    break;
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

        private void placeFirstSegment() {
            this.snakeHead.setLocation(25, 25);
            this.snakeTail.setLocation(25, 25);
            this.gameSpace[25][25].setSeg(new SnakeSegment());
        }

        /**
         * Moves the snake by removing the tail segment and adding a new segment
         * to the head based on the result of vector addition on snakeHead and
         * snakeMove
         *
         * - still need to figure out how to reassign the snake tail :S
         */
        private void moveSnake() {
            this.gameSpace[(int) this.snakeTail.getX()][(int) this.snakeTail.getY()].setSeg(null);
            this.snakeHead.setLocation(this.snakeHead.getX() + snakeMoveX, this.snakeHead.getY() + this.snakeMoveY);
            this.gameSpace[(int) this.snakeHead.getX()][(int) this.snakeHead.getY()].setSeg(new SnakeSegment());
            // if the next move would go off screen
            
            // if the segment has a fruit on it
            if (this.gameSpace[(int) this.snakeHead.getX()][(int) this.snakeHead.getY()].getFruit() != null) {
                this.gameSpace[(int) this.snakeHead.getX()][(int) this.snakeHead.getY()].setFruit(null);
                this.placeNewSegment();
                this.placeFruit();
            }
        }

        private void setUpGameSpace() {
            for (int y = 0; y < gameSpace.length; y++) {
                for (int x = 0; x < gameSpace.length; x++) {
                    gameSpace[x][y] = new Location();
                }
            }
        }

        private void placeNewSegment() {
            int newHeadPositionX = (int) this.snakeHead.getX() + snakeMoveX;
            int newHeadPositionY = (int) this.snakeHead.getY() + snakeMoveY;
            if (newHeadPositionX < 0) {
                this.snakeHead.setLocation(this.gameSpace.length - 1, newHeadPositionY);
                this.setSegment(this.snakeHead);
            } else if (newHeadPositionX == this.gameSpace.length) {
                this.snakeHead.setLocation(0, newHeadPositionY);
                this.setSegment(this.snakeHead);
            } else if (newHeadPositionY < 0) {
                this.setSegment(this.snakeHead);
                this.snakeHead.setLocation(newHeadPositionX, this.gameSpace.length - 1);
                this.setSegment(this.snakeHead);
            } else if (newHeadPositionY == this.gameSpace.length) {
                this.snakeHead.setLocation(0, newHeadPositionY);
                this.setSegment(this.snakeHead);
            }else{
                this.snakeHead.setLocation(newHeadPositionX, newHeadPositionY);
                this.setSegment(this.snakeHead);
            }
            
        }
        private void setSegment(Point point){
            this.gameSpace[(int)point.getX()][(int)point.getY()].setSeg(new SnakeSegment());
        }

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

        public void draw(Graphics2D g2d, int x, int y) {
            if (seg == null && fruit != null) {
                g2d.setColor(Color.red);
            } else if (seg != null) {
                g2d.setColor(Color.green);
            }
            g2d.drawRect(x, y, 1, 1);
        }
    }

    private class Fruit {

        public Fruit() {
        }
    }
}
