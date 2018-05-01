import java.util.ArrayList;
/**
 * 
 * @author Emma Rafkin, April 2018
 *
 */


public class SIMON {
	public ArrayList<Section> compList, userList;
	public Section red, blue, green, yellow;
	public SIMON(Section r, Section b, Section g, Section y) {
		red = r;
		blue = b;
		green = g;
		yellow = y;
		compList = new ArrayList<Section>();
		userList = new ArrayList<Section>();
	}
	public void resetColors() {
		red.setColor("red");
		blue.setColor("blue");
		green.setColor("green");
		yellow.setColor("yellow");
	}
	public void nextColor() {
		int rand = 1 + (int)(Math.random()*4);//generates random number between 1 and 4
		if(rand==1) compList.add(red);
		else if(rand==2) compList.add(blue);
		else if(rand ==3)compList.add(green);
		else if(rand == 4)compList.add(yellow);
	}
	public Section inSection(int x, int y) {
		if(x>red.getX1() && x<red.getX2() && y>red.getY1() && y<red.getY2()) {
			userList.add(red);
			return red;
		}
		if(x>blue.getX1() && x<blue.getX2() && y>blue.getY1() && y<blue.getY2()) {
			userList.add(blue);
			return blue;
		}
		if(x>green.getX1() && x<green.getX2() && y>green.getY1() && y<green.getY2()) {
			userList.add(green);
			return green;
		}
		if(x>yellow.getX1() && x<yellow.getX2() && y>yellow.getY1() && y<yellow.getY2()) {
			userList.add(yellow);
			return yellow;
		}
		return null;
	}
	public boolean confirmMove(Section s1, Section s2) {
		if(s1.getColor().equals(s2.getColor())) {
			System.out.print("color match\n");
			return true;
		}
		compList.clear();
		userList.clear();
		System.out.print("wrong color\n");
		return false;
	}
	
}
