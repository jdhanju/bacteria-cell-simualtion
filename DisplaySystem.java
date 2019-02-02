import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 * A Swing based simple OO graphics and animation system
 *
 * The goal is to create something extremely simple to use, and introduce users
 * to the java.awt.Graphics2D class
 *
 * <pre>
 * Example usage:
 *
 *  // Create a GameFrame window with size 800 x 600 pixels
 *  DisplaySystem displaySystem = new displaySystem(800,600);
 *
 *  // Add an object to display
 *  displaySystem.addObject(new SomeObject());
 * </pre>
 *
 * SomeObject can be any object, the only requirement is that it contains a
 * method named draw(Graphics2D graphics).
 *
 * This version of the DisplaySystem is implemented as a JPanel, so that it can
 * be integrated with other Swing components.
 *
 * @see DisplaySystem#addObject addObject
 * @author jdhanju
 */
public class DisplaySystem extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<DrawableObject> drawableObjects = new ArrayList<DrawableObject>();
	private HashMap<Object, DrawableObject> drawableMap = new HashMap<Object, DrawableObject>();
	private ArrayList<Object> removeQueue = new ArrayList<Object>();
	private boolean running = false;
	private int width, height;

	private BufferedImage bm;
	private Graphics2D buffer;
	private Application app;

	/**
	 * Create a graphics window of a specific width and height
	 *
	 * @param width
	 * @param height
	 */
	public DisplaySystem(Application app) {
		this.app = app;
		this.width = app.WIDTH;
		this.height = app.HEIGHT;
		bm = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		buffer = bm.createGraphics();
		setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paintComponent(Graphics g) {
		buffer.clearRect(0, 0, width, height);
		synchronized (DisplaySystem.this) {
			for (DrawableObject drawable : drawableObjects) {
				try {
					Method m = drawable.m;
					m.invoke(drawable.obj, buffer);
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				} catch (InvocationTargetException ex) {
					ex.printStackTrace();
				}
			}
			g.drawImage(bm, 0, 0, width, height, null);
		}
	}

	/**
	 * Add an object to the game canvas. The object must have a draw(Graphics2D
	 * graphics) method.
	 *
	 * During runtime, the GameFrame's graphics object will be passed to the
	 * draw method, so the objects can draw freely to the graphics windows
	 *
	 * @param obj
	 *            the object to add to the display
	 */
	public void addObject(Object obj) throws DisplaySystemException {
		synchronized (this) {
			try {
				Method m = obj.getClass().getMethod("draw", Graphics2D.class);
				DrawableObject drawable = new DrawableObject(obj, m);
				drawableObjects.add(drawable);
				drawableMap.put(obj, drawable);
				refresh(0);
			} catch (NoSuchMethodException ex) {
				throw new DisplaySystemException("Object must have a draw method", ex);
			} catch (SecurityException ex) {
				throw new DisplaySystemException("draw method must be public", ex);
			} catch (IllegalArgumentException ex) {
				throw new DisplaySystemException("draw method must have a Graphics2D parameter", ex);
			}
		}
	}

	/**
	 * Remove an object from the displaySystem, so it won't render
	 *
	 * @param obj
	 *            the object to remove
	 * @return true if the object is successfully removed
	 */
	public boolean removeObject(Object obj) {
		if (drawableMap.containsKey(obj)) {
			removeQueue.add(obj);
			return true;
		} else {
			return false;
		}
	}

	private void processRemoveQueue() {
		synchronized (this) {
			for (Object obj : removeQueue) {
				DrawableObject drawable = drawableMap.remove(obj);
				if (drawable != null) {
					drawableObjects.remove(drawable);
				}
			}
		}
	}

	public Collection getObjects() {
		return drawableMap.keySet();
	}

	public <T> Collection<T> getObjects(Class<T> clazz) {
		ArrayList<T> list = new ArrayList<T>();
		for (Object o : drawableMap.keySet()) {
			if (o.getClass() == clazz) {
				list.add((T) o);
			}
		}
		return list;
	}

	/**
	 * Starts the drawing loop -- each object's draw method will be called at
	 * around 60 times per second. This can be used for animation.
	 */
	public void start() {
		running = true;
		// calls repaint on the canvas
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					while (running) {
						repaint();
						processRemoveQueue();
						// Approx 60 times per second
						sleep(16);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	public void refresh() {
		refresh(16);
	}

	public void refresh(int delay) {
		try {
			repaint(delay);
			processRemoveQueue();
			Thread.sleep(delay);
		} catch (Exception ex) { //InterruptedException
			ex.printStackTrace();
		}
	}

	/**
	 * Pause the loop
	 */
	public void pause() {
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * A class to group together the method and the target object
	 */
	private class DrawableObject {

		public Object obj;
		public Method m;

		public DrawableObject(Object obj, Method m) {
			this.obj = obj;
			this.m = m;
		}
	}

	/**
	 *
	 * @author jmadar
	 */
	public static class DisplaySystemException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DisplaySystemException(String message, Throwable e) {
			super(message, e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		app.addCell(x,y);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	// ~ @Override
	// ~ public void mousePressed(BacterialCell bacterialCell) {
	// ~ // TODO Auto-generated method stub

	// ~ }

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
