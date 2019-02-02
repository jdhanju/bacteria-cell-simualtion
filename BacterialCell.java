
import java.awt.Color;
import java.util.ArrayList;


/**
 *
 * @author jdhanju
 */
public class BacterialCell extends Cell {
    
    private int frame;
    
    public BacterialCell(int x, int y, int size) {
        super(x, y, size, Color.red);
    }

    @Override
    public void move(ArrayList<Cell> cells, ArrayList<Cell> addedCells, ArrayList<Cell> deletedCells) {
        super.move(cells, addedCells, deletedCells); 
        
        if (frame++ % 40 == 0) {
            if (Math.random() < 0.1) {
                addedCells.add(new BacterialCell((int)getX(), (int)getY(), (int)getSize()));
            }
        }                
        
    }
    
}
