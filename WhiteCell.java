
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author jdhanju
 */
public class WhiteCell extends Cell {
    
    public WhiteCell(int x, int y, int size) {
        super(x, y, size, Color.blue);
    }

    @Override
    public void move(ArrayList<Cell> cells, ArrayList<Cell> addedCells, ArrayList<Cell> deletedCells) {
        super.move(cells, addedCells, deletedCells); 
        
        Cell closestBacterialCell = null;
        // find all bacterial cells
        for (Cell cell : cells) {
            
            if (cell instanceof BacterialCell) {
                // touched, kill
                if (distance(cell) < getSize()+cell.getSize()) {
                    deletedCells.add(cell);
                }
                
                // tractor beam
                if (distance(cell) < (getSize()+cell.getSize() * 5)) {
                    cell.turnToFace(this);
                    cell.setColor(Color.yellow);
                    cell.setSpeed(2);
                }
                
                if (closestBacterialCell == null) {
                    closestBacterialCell = cell;
                } else {
                    if (distance(cell) < distance(closestBacterialCell)) {
                        closestBacterialCell = cell;
                    }
                }
            }                        
        }
        if (closestBacterialCell != null) {
            turnToFace(closestBacterialCell);
        }
    }        
    
}
