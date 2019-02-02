
import java.awt.Color;
import java.util.ArrayList;



/**
 *
 * @author jdhanju
 */
public class Obstacle extends Cell {

    public Obstacle(int x, int y, int size) {
        super(x, y, size, Color.WHITE);
    }

    @Override
    public void move(ArrayList<Cell> cells, ArrayList<Cell> addedCells, ArrayList<Cell> deletedCells) {
        for (Cell cell: cells) {
            if (cell != this && distance(cell) <= getSize()+cell.getSize()) { 
                cell.turnToFace(this);
                cell.setDirection(cell.getDirection() + Math.PI);                
            }
        }
    }
    
}
