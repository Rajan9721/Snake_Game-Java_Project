import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	static final int screenWidth = 600;  // Screen width size
	static final int screenHeight = 600;  // Screen Height size
	static final int unitSize = 25; 
	static final int gameUnits = (screenWidth*screenHeight)/unitSize; // total area of the screen in the game
	static final int delay = 110;
	final int x[] =  new int[gameUnits];
	final int y[] = new int[gameUnits];
	int bodyParts = 6; // creating a variable and initialize the snake body size 
	int applesEaten; 
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	} // OK
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	} // OK
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	} // OK
	public void draw(Graphics g) {
		if(running) {
//			 for loop are used for show grid lines in the screen 
//			 if we comment it then grid lines are not show on the snake game screen
			for(int i=0; i<screenWidth/unitSize; i++) {
				g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
				g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, unitSize, unitSize);
			for(int i = 0; i<bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], unitSize, unitSize);
				}
				else {
					g.setColor(new Color(45,180,0));
					// random color changed the snake color random snake color
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i],y[i],unitSize, unitSize);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(screenWidth - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	} // OK
	public void newApple() {
		appleX = random.nextInt((int)(screenWidth/unitSize))*unitSize;
		appleY = random.nextInt((int)(screenHeight/unitSize))*unitSize;
	}
	public void move() {
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - unitSize;
			break;
		case 'D':
			y[0] = y[0] + unitSize;
			break;
		case 'L':
			x[0] = x[0] - unitSize;
			break;
		case 'R':
			x[0] = x[0] + unitSize;
			break;
		}
	} // OK
	public void checkApple() {
		if((x[0] == appleX)&&(y[0]==appleY)){
			bodyParts++;
			applesEaten++;
			newApple();
		}
	} //OK
	public void checkCollisions() {
		// check if head collides with body
		for(int i = bodyParts; i>0; i--) {
			if((x[0]==x[i]) && (y[0]==y[i])) {
				running = false;
			}
		}
		// Check if head touches left border
		if(x[0]<0) {
			running = false;
		}
		//check if head touches right border
		if(x[0]>screenWidth) {
			running = false;
		}
		//check if head touches top border
		if(y[0]<0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0]>screenWidth) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	} //OK 
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (screenWidth - metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (screenWidth - metrics2.stringWidth("Game Over"))/2,screenHeight/2);
		
	} // OK
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint(); 
	} // OK
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			/**
		     * Constant for the non-numpad <b>left</b> arrow key.
		     * @see #VK_KP_LEFT
		     */
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L'; 
				}
				break;
				/**
			     * Constant for the non-numpad <b>right</b> arrow key.
			     * @see #VK_KP_RIGHT
			     */
			case KeyEvent.VK_RIGHT:	
				if(direction != 'L') {
					direction = 'R'; 
				}
				break;
				/**
			     * Constant for the non-numpad <b>up</b> arrow key.
			     * @see #VK_KP_UP
			     */
			case KeyEvent.VK_UP:
				if(direction != 'D') { 
					direction = 'U'; 
				}
				break;
				 /**
			     * Constant for the non-numpad <b>down</b> arrow key.
			     * @see #VK_KP_DOWN
			     */
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D'; 
				}
				break;
			} // OK
		}
	}
} 
