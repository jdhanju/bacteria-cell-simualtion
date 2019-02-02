
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The application skeleton. This is the controller to manage DisplaySystem and
 * all the objects that it displays.
 *
 * @author jdhanju
 */
public class Application implements ActionListener {// i can also do
													// ,MouseMotionListener

	// Static final variables are constants that can be accessed
	// using Application.WIDTH and Application.HEIGHT
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private int WhiteCellInit = 10;
	private int BacterialCellInit = 100;
	private int Obstacles = 50;
	private EditSlide es;
	private Object lock = new Object();
	private Drag drag;
	private int size = 10;
	// private boolean readyToGo = false;

	/**
	 * Application manages one displaySystem
	 */
	public DisplaySystem displaySystem;
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	public ArrayList<EditSlide> slider = new ArrayList<EditSlide>();

	public Application() {
		// The DisplaySystem constructor requires a resolution
		displaySystem = new DisplaySystem(this);
		drag = new Drag(this);
		reset();
	}

	private void reset() {

		for (Cell cell : cells) {
			displaySystem.removeObject(cell);
		}
		cells.clear();

		for (int i = 0; i < BacterialCellInit; i++) {
			cells.add(new BacterialCell((int) (Math.random() * (WIDTH - size * 2)) + size,
					(int) (Math.random() * (600 - size * 2)) + size, size));
		}

		for (int i = 0; i < WhiteCellInit; i++) {
			cells.add(new WhiteCell((int) (Math.random() * (WIDTH - size * 2)) + size,
					(int) (Math.random() * (600 - size * 2)) + size, size));
		}

		for (int i = 0; i < Obstacles; i++) {
			cells.add(new Obstacle((int) (Math.random() * (WIDTH - size * 2)) + size,
					(int) (Math.random() * (600 - size * 2)) + size, size));
		}

		for (Cell cell : cells) {
			displaySystem.addObject(cell);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// restart = false;
		// displaySystem.pause();
		// displaySystem.repaint();
		// if(readyToGo == true){
		// synchronized(lock){
		for (EditSlide slide : slider) {
			// System.out.println(slide.maxValue());
			if (slide.maxValue() == 100) {
				WhiteCellInit = slide.getValue();
			}
			if (slide.maxValue() == 1000) {
				BacterialCellInit = slide.getValue() * 10;// jslider has a max
															// of 100
			}

			if (slide.maxValue() == 200) {
				Obstacles = slide.getValue() * 2;
			}
		}
		// }
		synchronized (lock) {
			reset();
		}
		// restart = true;
		// start();
		// displaySystem.repaint();
		// displaySystem.refresh();

		// TODO Auto-generated method stub

	}

	/**
	 * Initialize the GUI system, currently we only put the DisplaySystem JPanel
	 * into a JFrame and display it.
	 */
	private void initGui() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(displaySystem);
		// frame.pack();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		JButton button = new JButton("Reset");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(800, 20));
		JSlider scroll = new JSlider();
		scroll.setPreferredSize(new Dimension(800, 30));
		es = new EditSlide(100, 0, scroll);
		slider.add(es);
		// panel.setLayout(new FlowLayout());
		panel.add(button);
		panel.add(scroll);
		JSlider scroll2 = new JSlider();
		scroll2.setPreferredSize(new Dimension(800, 30));
		EditSlide es2 = new EditSlide(1000, 0, scroll2);
		slider.add(es2);
		panel.add(scroll2);
		JSlider scroll3 = new JSlider();
		scroll3.setPreferredSize(new Dimension(800, 30));
		EditSlide es3 = new EditSlide(200, 0, scroll3);
		slider.add(es3);
		panel.add(scroll3);
		frame.add(panel, BorderLayout.SOUTH);
		// panel = new JPanel();
		frame.pack();
		displaySystem.addMouseListener(displaySystem);
		displaySystem.addMouseMotionListener(drag); // idk
		// displaySystem.mousePressed(new BacterialCell((int)
		// (displaySystem.get,
		// (int) (Math.random() * (600 - size * 2)) + size, size));
		frame.setVisible(true);
	}

	public Application getApp() {
		return this;
	}

	public void addCell(int x, int y) {
		// boolean finish = false;
		// while(!finish){
		// if(loopCheck = false){
		synchronized (lock) {
			Cell c = new BacterialCell(x, y, size);
			cells.add(c);
			displaySystem.addObject(c);
		}
		// finish = true;
		// }
		// }
	}

	public void dragObstacle(int mouseX, int mouseY) {
		synchronized (lock) {
			for (Cell c : cells) {
				if (c instanceof Obstacle) {
					if ((c.getX()- mouseX)<=size && (c.getX() -mouseX)>=-size  && (c.getY() -mouseY)<=size && (c.getY() -mouseY)>=-size ) {
						c.setX(drag.getMouseX());
						c.setY(drag.getMouseY());
						break;
					}
				}
			}
		}

	}

	public void start() {

		initGui();

		// Create a list of deleteCells that will be used over and over again
		ArrayList<Cell> deletedCells = new ArrayList<Cell>();
		ArrayList<Cell> addedCells = new ArrayList<Cell>();

		while (true) {
			// if(restart){
			synchronized (lock) {

				for (Cell cell : cells) {
					cell.move(cells, addedCells, deletedCells);
				}

				for (Cell cell : deletedCells) {
					cells.remove(cell);
					displaySystem.removeObject(cell);
				}

				for (Cell cell : addedCells) {
					cells.add(cell);
					displaySystem.addObject(cell);
				}
				// readyToGo = true;

				// Clear the list so it can be reuse in the next iteration
				deletedCells.clear();
				addedCells.clear();
			}

			 //synchronized(lock){
			displaySystem.refresh(); // theres a delay cause of lock
			 //}
			
		}

	}

	/**
	 * Main entry point, simply start the application
	 */
	public static void main(String[] args) {
		Application app = new Application();
		app.start();
	}

}
