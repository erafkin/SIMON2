import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 * 
 * @author Emma Rafkin, April 2018
 *
 */

public class SIMONGUI extends DrawingGUI{
	private static final int width=800, height=600;	
	private int delay = 100;		// delay for the timer in milliseconds
	private SIMON game;
	private Section lastSelect;
	private int currIdx=0;
	JLabel scoreL;
	JLabel highscore;
	private int hscore=0;
	private int score=0;
	JButton start;
	private boolean gameStarted = false;
	private boolean replay = false;
	private String redSound;
	private String blueSound;
	private String greenSound;
	private String yellowSound;
	
	
	public SIMONGUI() {
		//set up sections
		super("SIMON Game", width, height);
		
		Section green = new Section("green", 200, 100, 400, 300);
		Section red = new Section("red", 400, 100, 600, 300);
		Section yellow = new Section("yellow", 200, 300, 400, 500);
		Section blue = new Section("blue", 400, 300, 600, 500);
		this.game = new SIMON(red, blue, green, yellow);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setVisible(true);
		//button to start game
		start = new JButton("New Game");
	    start.setEnabled(true);
	    start.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent ae) {
				gameStarted = true;
				resetGame(); 			 
	    		}
	    });
	    JButton changeSounds = new JButton("Switch Sounds");
	    changeSounds.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent ae) {
    				generateSounds();			 
    			}	
	    	});
	    //keeps track of current score
		scoreL = new JLabel("Score: 0");
		//keeps track of user's high score
		highscore = new JLabel("High Score: 0");
		panel.add(scoreL);
		panel.add(highscore);
		panel.add(start);
		panel.add(changeSounds);
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panel, BorderLayout.NORTH);
		cp.add(canvas, BorderLayout.CENTER);
		
		//start timer and game
		generateSounds();
		setTimerDelay(delay);
		startTimer();
		repaint();
		JOptionPane.showMessageDialog(null, "Welcome to SIMON 2.0, a memory game that is a little harder than the classic SIMON game.\nInstead of lighting up and playing sounds, only the sounds will play.\nBefore you start a game, click on the squares to memorize the sounds,\nonce you start the game it is up to you to remember the pattern and match the sounds to the squares! ");
	}
	//turns selected section white and plays sound
	public void select(Section s){
		if(s ==null) {
			lastSelect = null;
			return;
		}
		if(s.getColor().equals("red")) {
			if(!replay) {
				game.red.setColor("white");	
				repaint();
			}
			playSound(redSound);
			lastSelect = game.red;
		}else if(s.getColor().equals("blue")) {
			if(!replay) {
				game.blue.setColor("white");	
				repaint();
			}
			playSound(blueSound);
			lastSelect = game.blue;	
		}else if(s.getColor().equals("green")) {
			if(!replay) {
				game.green.setColor("white");
				repaint();
			}
			playSound(greenSound);
			lastSelect = game.green;
		}else if(s.getColor().equals("yellow")) {
			playSound(yellowSound);
			lastSelect = game.yellow;
			if(!replay) {
				game.yellow.setColor("white");	
				repaint();
			}
		}
	}
	//when user chooses a section, it is "selected"
	@Override
	public void handleMousePress(int x, int y) {
			select(game.inSection(x, y));
	}
	//release mouse, reset colors to normal, and move on
	@Override
	public void handleMouseRelease(int x, int y) {
		game.resetColors();
		repaint();
		if(gameStarted) {	
			nextMove();
		}
	}
	
	//checks whether or not the user selected the correct section, and from there either adds on a color or starts a new game.
	public void nextMove() {
		if(lastSelect == null) return;
		if(game.confirmMove(game.compList.get(currIdx), lastSelect)) {
			currIdx++;
			if(currIdx == game.compList.size()) {
				pause(500);
				score++;
				scoreL.setText("Score: " + score);
				if(score>hscore) {
					hscore++;
					highscore.setText("High Score: " + hscore);
				}
				addColor();
			}
		}else {
			score = 0;
			scoreL.setText("Score: " +score);
			JOptionPane.showMessageDialog(null, "Game Over!");
			gameStarted = false;
			currIdx = 0;
			
		}
	}
	//adds on a color, and replays what the pattern is
	//THIS NEEDS WORK!! the playback is not working!!!
	public void addColor() {
		game.nextColor();
		//pause(500);
		replay = true;
		for(int i = 0; i<game.compList.size(); i++) {
			pause(500);
			select(game.compList.get(i));
		}
		replay = false;
		game.resetColors();
		repaint();
		currIdx = 0;
	}
	
	
	//sets up new game with the old sections
	public void resetGame() {
		replay = true;
		repaint();
		game.nextColor();
		select(game.compList.get(0));
		pause(1000);
		game.resetColors();
		repaint();
		replay = false;
	}
	public void generateSounds() {
		ArrayList<String> sounds = new ArrayList<String>();
		sounds.add("./sounds/beep-2.wav");
		sounds.add("./sounds/beep-3.wav");
		sounds.add("./sounds/beep-4.wav");
		sounds.add("./sounds/beep-6.wav");
		redSound = sounds.remove((int)(Math.random()*4));
		blueSound = sounds.remove((int)(Math.random()*3));
		greenSound = sounds.remove((int)(Math.random()*2));
		yellowSound = sounds.remove(0);

	}
	
	@Override
	public void draw(Graphics g) {
		game.red.draw(g);
		game.blue.draw(g);
		game.green.draw(g);
		game.yellow.draw(g);
	}
	public void playSound(String soundFile) {
	    File f = new File("./" + soundFile);
	    Clip clip;
	    try {
		    AudioInputStream audioIn;
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());   
	    		clip = AudioSystem.getClip();
			clip.open(audioIn);
		    clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//have the program pause for a brief period of time so that
	//the recordings (which are 1 second each) have time to play
	//before the method continues
	public void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SIMONGUI();
			}
		});
	}

}
