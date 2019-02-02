
import javax.swing.JSlider;

public class EditSlide {
	private int max;
	private int min;
	private JSlider scroll;
	
	public EditSlide(int max, int min, JSlider scroll){
		this.max = max;
		this.min = min;
		this.scroll = scroll;
		maxANDmin();
		
	}
	private void maxANDmin(){
		scroll.setMajorTickSpacing(max);
		scroll.setMinorTickSpacing(min);
		
	}
	
	public int getValue(){
		return scroll.getValue();
	
	}
	
	public int maxValue(){
		return max;
	}
}
