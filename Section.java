import java.awt.Color;
import java.awt.Graphics;
/**
 * 
 * @author Emma Rafkin, April 2018
 *
 */


public class Section {
	private int x1, x2, y1, y2;
	private String color;
	public Section(String c, int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.color = c;
	}
	public int getX1() {
		return x1;
	}
	public int getX2() {
		return x2;
	}
	public int getY1() {
		return y1;
	}
	public int getY2() {
		return y2;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String s) {
		color = s;
	}
	public void draw(Graphics g) {
		if(color == "red") {
			g.setColor(Color.RED);
		}else if(color == "blue") {
			g.setColor(Color.BLUE);
		}else if(color == "green") {
			g.setColor(Color.GREEN);
		}else if(color == "yellow") {
			g.setColor(Color.YELLOW);
		}else if(color == "white") {
			g.setColor(Color.WHITE);
		}
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}
}
