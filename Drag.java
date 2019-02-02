import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Drag implements MouseMotionListener {
	private Application app;
	private MouseEvent e;

	
	public Drag(Application app){
		this.app = app;
	
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.e = e;
		// TODO Auto-generated method stub
		app.dragObstacle(e.getX(), e.getY());

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	public int getMouseX(){
		return e.getX();
	}
	
	public int getMouseY(){
		return e.getY();
	}

}
