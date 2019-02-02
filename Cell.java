
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * An abstract cell, moves randomly around the blood stream
 * 
 * @author jdhanju
 */
public class Cell {
    
    /** The current direction of the cell, in radians */
    private double direction;
    
    /** The speed per frame */
    private double speed;
    
    /** location and size */
    private double x, y, size;
    
    /** current rendering color */
    private Color color;
    
    /** a counter to keep track of the current frame */
    private int frame = 0;
    
    /** the duration of frame to keep the current direction */
    private int duration = 120;

    public Cell(int x, int y, int size, Color color) {        
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;        
    }

    /**
     * The move method move the cell to the next location, based on 
     * direction, speed, and duration
     */
    public void move(ArrayList<Cell> cells, ArrayList<Cell> addedCells, ArrayList<Cell> deletedCells) {
        if ((frame++%getDuration())==0) {
            changeDirection();
        }
        
        // move the x and y
        setX(getX() + Math.sin(getDirection()) * getSpeed());
        setY(getY() + Math.cos(getDirection()) * getSpeed());
        
        // Make sure we bounce off walls
        if (getX() < getSize() || 
                getX() > Application.WIDTH-getSize() ||
                getY() < getSize() ||
                getY() > Application.HEIGHT-getSize()) {
            setDirection(getDirection() + Math.PI/2);
        }        
    }
    
    /**
     * Pick a random direction, speed, and duration
     */
    private void changeDirection() {
        setDirection(Math.random() * Math.PI * 2);
        setSpeed(0.5+ (Math.random()*0.5));
        setDuration(100+(int)(Math.random() * 200));
    }    

    /**
     * Draw a circle as a default
     * @param graphics 
     */
    public void draw(Graphics2D graphics) {        
        graphics.setColor(getColor());
        graphics.fillOval((int) (getX() - getSize()), (int) (getY() - getSize()), (int) (getSize() * 2), (int) (getSize() * 2));        
    }

    // Utility methods
    
        
    /**
     * Calculates the distance between cells     
     * @param The cell to compare this to
     * @return 
     */
    public double distance(Cell cell) {
        return Math.sqrt(Math.pow(getX()-cell.getX(), 2) + Math.pow(getY()-cell.getY(), 2));
    }
    
    public double distance(Point point) {
        return Math.sqrt(Math.pow(getX()-point.getX(), 2) + Math.pow(getY()-point.getY(), 2));
    }
    
    /**
     * Calculates the angle between cells
     * @param cell
     * @return 
     */
    public void turnToFace(Cell cell) {
        setDirection(Math.atan2(cell.getX()-getX(), cell.getY()-getY()));
    }
    
    // Standard accessors and mutators so sub-classes and other classes 
    // can modify them
    
    /**
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(double direction) {
        this.direction = direction;        
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

}
