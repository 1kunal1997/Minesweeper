// Kunal Lakhanpal
// 4/8/12
// Game.java
// This program will use all that I have learned
// in java create a game called Minesweeper.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game extends JApplet
{
	private Container c;
	private CardLayout cards;
	private HomePanel panel1;
	private InstructionPanel panel2;
	private SettingPanel panel3;
	private GamePanel panel4;
	private QuestionPanel panel5;
	private Timer timer;
	private JLabel title;
	private int mineValue, flagCount, boxes, boxLength, inc, flagPos, numSize, numPos, timeMin, timeSec, score, finish;
	private int[][] gameValue, check;
	private BufferedImage bomb;
	private boolean[][] flagDisp;
	private boolean painted;

	public void init()
	{
		painted = false;
		score = 0;
		gameValue = new int[12][12];
		check = new int[12][12];
		for (int i = 0; i < 12; i++)
		{
			for (int j = 0; j < 12; j++)
			{
				gameValue[i][j] = 0;
				check[i][j] = 0;
			}
			
		}
		finish = mineValue = flagCount = boxes = 10;
		flagPos = 12;
		boxLength = 35;
		inc = 40;
		numSize = 30;
		numPos = 55;
		c = this.getContentPane();
		cards = new CardLayout();
		c.setLayout(cards);
		
		panel1 = new HomePanel();								// instances of the panels
		panel1.setBackground(Color.black);
		c.add(panel1, "Panel 1");
		
		panel2 = new InstructionPanel();
		panel2.setBackground(Color.gray);
		c.add(panel2, "Panel 2");
		
		panel3 = new SettingPanel();
		panel3.setBackground(Color.darkGray);
		c.add(panel3, "Panel 3");
		
		panel4 = new GamePanel();
		panel4.setBackground(Color.black);
		c.add(panel4, "Panel 4");
		
		panel5 = new QuestionPanel();
		panel5.setBackground(Color.pink);
		c.add(panel5, "Panel 5");
		
	}
	public class HomePanel extends JPanel implements ActionListener					// HOME
	{
		private JButton instruction, play, setting;
		private Mine canvas;
		public class Mine extends JPanel
		{
			int box = 50;
			public void paintComponent (Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.gray);
				Font font = new Font("Comic Sans", Font.PLAIN, 60);
				g.setFont(font);
				g.drawString("MINESWEEPER", 40, 300);
				
				for (int i = 0; i < 3; i++)
				{
					g.setColor(Color.white);
					g.fillRect(box, 50, 120, 120);
					box += 140;
				}
				g.setColor(Color.red);
				g.fillRect(190, 50, 120, 120);
				box = 50;
				g.drawImage(bomb, 200, 60, 100, 100, null);					// draw the bomb
				g.setColor(Color.blue);
				g.drawString("1", 95, 120);
				g.setColor(Color.red);
				g.drawString("3", 375, 120);
				
			}
		}
		public HomePanel()
		{
			
			this.setLayout(new BorderLayout());
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 40));
			panel.setBackground(Color.black);
			
			instruction = new JButton("Instructions");
			instruction.addActionListener(this);
			panel.add(instruction);
			
			play = new JButton("Play");
			play.addActionListener(this);
			panel.add(play);
			
			setting = new JButton("Settings");
			setting.addActionListener(this);
			panel.add(setting);
			
			canvas = new Mine();
			canvas.setBackground(Color.black);
			
			this.add(canvas, BorderLayout.CENTER); 
			this.add(panel, BorderLayout.SOUTH);
			
			try 													// sets bomb to the image
			{                 
				bomb = ImageIO.read(new File("Bomb(2).png")); 
			} 
			catch (IOException ex)
			{ 
					System.out.print("AHHHH!!! Picture not found");
			} 

		}

		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			if ( command.equals("Settings") )
			{
				cards.show(c, "Panel 3");
			}
			else if (command.equals("Play"))
			{
				for (int i = 0; i < 12; i++)
				{
					for (int j = 0; j < 12; j++)
					{
						gameValue[i][j] = 0;
					}
					
				}
				panel4.setGridAndMines();
				cards.show(c, "Panel 4");
				timer.start();
			}
			else if (command.equals("Instructions"))
			{
				cards.show(c, "Panel 2");
			}
		}
	}
	public class InstructionPanel extends JPanel implements ActionListener						// INSTRUCTION
	{
		private JButton back;
		
		public InstructionPanel()
		{
			this.setLayout(null);
			
			back = new JButton("Back");
			back.setBounds(10, 460, 70, 30); 
			back.addActionListener(this);
			this.add(back);
			
			title = new JLabel("MINESWEEPER");
			title.setForeground(Color.red);
			title.setFont(new Font("SansSerif", Font.PLAIN, 40));
			title.setBounds(100, 10, 400, 80);
			this.add(title);
		
		}
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			
			if (command.equals("Back"))
			{
				cards.show(c, "Panel 1");
			}
		}
		public void paintComponent(Graphics g)						// paints the instructions on the screen
		{
			super.paintComponent(g);
			g.setFont(new Font("Serif", Font.PLAIN, 16));
			g.setColor(Color.black);
			g.drawString("The objective of this game is to uncover all the squares", 30, 120);
			g.drawString("except for the mines. Each square reveals a number 1 - 8", 30, 140);
			g.drawString("(a zero is just blank), or a mine. The number tells you", 30, 160);
			g.drawString("how many mines are adjacent to the square. For example,", 30, 180);
			g.drawString("if you click a square and it reveals a two, you know that", 30, 200);
			g.drawString("there are two mines around it. If you hit a mine, you will", 30, 220);
			g.drawString("have to answer a high-school curriculum question. If you ", 30, 240);
			g.drawString("get it right, you can continue the game. You can also flag", 30, 260);
			g.drawString("squares that you are sure are a mine by right-clicking. Your", 30, 280);
			g.drawString("score is based on how fast you finished, how many mines", 30, 300);
			g.drawString("you flagged, and the difficulty you were playing on. The", 30, 320);
			g.drawString("difficulty can be changed in the settings, and the higher", 30, 340);
			g.drawString("the number of mines and grid size, the more challenging.", 30, 360);
			
		}
	
	}
	public class SettingPanel extends JPanel implements ActionListener, ChangeListener							// SETTING
	{
		private JButton back;
		private JSlider mines;
		private JLabel title;
		private JRadioButton small, medium, large;
		private ButtonGroup group;
		public SettingPanel()								// constructor
		{

			this.setLayout(null);
			group = new ButtonGroup();
			
			back = new JButton("Back");
			back.setBounds(10, 460, 70, 30); 
			back.addActionListener(this);
			this.add(back);
			
			mines = new JSlider(5, 15, 10);
			mines.setBounds(30, 160, 400, 50);
			mines.setBackground(Color.darkGray);
			mines.addChangeListener(this);
			this.add(mines);
			
			title = new JLabel("SETTINGS");
			title.setForeground(Color.orange);
			title.setFont(new Font("ComicSans", Font.PLAIN, 40));
			title.setBounds(150, 20, 350, 50);
			this.add(title);
			
			small = new JRadioButton("Small");
			small.setBounds(30, 300, 100, 50);
			small.setBackground(Color.darkGray);
			small.setForeground(Color.black);
			group.add(small);
			small.addActionListener(this);
			this.add(small);
			
			medium = new JRadioButton("Medium");
			medium.setBounds(200, 300, 100, 50);
			medium.setBackground(Color.darkGray);
			medium.setForeground(Color.black);
			group.add(medium);
			medium.addActionListener(this);
			this.add(medium);
			
			large = new JRadioButton("Large");
			large.setBounds(400, 300, 100, 50);
			large.setBackground(Color.darkGray);
			large.setForeground(Color.black);
			group.add(large);
			large.addActionListener(this);
			this.add(large);
		}
		public void stateChanged(ChangeEvent e) 				// slider in setting
		{
			repaint();
			mineValue = mines.getValue();
			System.out.println("MineValue in Slider = " + mineValue);
			finish = mineValue;
			panel4.changeValue();
			
		}
		public void actionPerformed(ActionEvent e)					// changes the grid sizes
		{
			String command = e.getActionCommand();
			
			if (command.equals("Back"))
			{
				cards.show(c, "Panel 1");
			}
			if (small.isSelected())									// if the small size is selected
			{
				boxes = 9;
				boxLength = 39;
				inc = 45;
				flagPos = 14;
				numSize = 33;
				numPos = 56;
			}
			else if (medium.isSelected())							// if the medium size is selected
			{
				boxes = 10;
				boxLength = 35;
				inc = 40;
				flagPos = 12;
				numSize = 30;
				numPos = 55;
			}
			else if (large.isSelected())							// if the large size is selected
			{
				boxes = 12;
				boxLength = 28;
				inc = 33;
				flagPos = 10;
				numSize = 25;
				numPos = 52;
			}
			panel4.repaint();
		}
		public void paintComponent(Graphics g)					// paints the titles
		{
			super.paintComponent(g);
			g.setColor(Color.orange);
			g.setFont(new Font("ComicSans", Font.PLAIN, 20));
			g.drawString("Number of Mines:", 30, 120);
			g.drawString("Grid Size:", 30, 250);
			g.setFont(new Font("ComicSans", Font.BOLD, 17));
			g.drawString("" + mineValue, 460, 190);
		}
	}
	public class QuestionPanel extends JPanel implements ActionListener
	{
		private JButton giveUp, submit;
		private String[] questions;
		private String[][] answers;
		private int[] correctAnswers;
		private JRadioButton ans1, ans2, ans3, ans4;
		private ButtonGroup group;
		private int x;
		public QuestionPanel()							// contructor
		{
			this.setLayout(null);
			this.setBackground(Color.pink);
			giveUp = new JButton("Give Up");
			giveUp.setBounds(10, 460, 100, 30);
			giveUp.addActionListener(this);
			this.add(giveUp);
			
			group = new ButtonGroup();
			
			correctAnswers = new int[10];
			
			questions = new String[10];
			answers = new String[4][10];
			
			questions[0] = "What are the four biomolecules?";									// all the questions in an array
			questions[1] = "What is the strongest bond?";	
			questions[2] = "In what situations is cosine of x equal to 0?";	
			questions[3] = "What is the diffence between i++ and ++i?";	
			questions[4] = "What is the informal command form of the word 'Hacer'?";	
			questions[5] = "What does the word 'harrowing' mean?";	
			questions[6] = "What is the MAIN funtion of the lymphatic system?";	
			questions[7] = "When did World War II officially end?";	
			questions[8] = "What is equivalent to tangent 45 degrees?";	
			questions[9] = "In order to produce 'beats', two sound waves should have...?";
			
			answers[0][0] = "Protien, Hydrogen, Oxygen, and Metal";
			answers[1][0] = "Lipids, Protiens, Carbahydrates, and Nucleic Acids";				// all the answers in an array
			answers[2][0] = "Potassium, Iron, Carbohydrates, and Carbon";
			answers[3][0] = "Protien, Nucleic Acids, Lipids, and fire";
			answers[0][1] = "Peptide bond";
			answers[1][1] = "Hydrogen bond";
			answers[2][1] = "Ionic bond";
			answers[3][1] = "Covalent bond";
			answers[0][2] = "When x is 180 degrees";
			answers[1][2] = "When x is 90 degrees ONLY";
			answers[2][2] = "When x is 90 OR 270 degrees";
			answers[3][2] = "When x is 270 degrees ONLY";
			answers[0][3] = "++i increments first and i++ increments after the sentence executes";
			answers[1][3] = "i++ increments first and ++i increments after the sentence executes";
			answers[2][3] = "They have the same effect";
			answers[3][3] = "i++ double increments and ++i single increments";
			answers[0][4] = "Haria";
			answers[1][4] = "Hacemos";
			answers[2][4] = "Ha";
			answers[3][4] = "Haciando";
			answers[0][5] = "To narrow or lessen in matter";
			answers[1][5] = "To cause distress";
			answers[2][5] = "To divide into several groups";
			answers[3][5] = "to intensify or increase";
			answers[0][6] = "To circulate blood around the body";
			answers[1][6] = "To return tissue fluids leaking out back into the veins";
			answers[2][6] = "Digest the fats not digested in the small intestine";
			answers[3][6] = "Filters out pathogens from the bloodstream";
			answers[0][7] = "1951";
			answers[1][7] = "1932";
			answers[2][7] = "1940";
			answers[3][7] = "1946";
			answers[0][8] = "cosine 0 degrees AND sine 45 degrees";
			answers[1][8] = "sine 0 degrees ONLY";
			answers[2][8] = "sine 90 degrees AND tangent 225 degrees";
			answers[3][8] = "tangent 135 degrees ONLY";
			answers[0][9] = "difference in sharpness";
			answers[1][9] = "similarity in sharpness";
			answers[2][9] = "different wavelengths";
			answers[3][9] = "same wavelengths";
			
			ans1 = new JRadioButton();
			ans1.setBackground(Color.pink);
			ans1.addActionListener(this);
			ans1.setBounds(40, 200, 420, 50);
			group.add(ans1);
			this.add(ans1);
			ans2 = new JRadioButton();
			ans2.setBackground(Color.pink);
			ans2.addActionListener(this);
			ans2.setBounds(40, 250, 420, 50);
			group.add(ans2);
			this.add(ans2);
			ans3 = new JRadioButton();
			ans3.setBackground(Color.pink);
			ans2.addActionListener(this);
			ans3.setBounds(40, 300, 420, 50);
			group.add(ans3);
			this.add(ans3);
			ans4 = new JRadioButton();
			ans4.setBackground(Color.pink);
			ans2.addActionListener(this);
			ans4.setBounds(40, 350, 420, 50);
			group.add(ans4);
			this.add(ans4);
            
			submit = new JButton("SUBMIT");
			submit.setBounds(150, 460, 100, 30);
			submit.addActionListener(this);
			this.add(submit);
			
		}
		public void actionPerformed (ActionEvent e)
		{
			String command = e.getActionCommand();
			if (command.equals("Give Up"))							// if the user cant answer the question, quit the game
			{
				timer.stop();
				finish = 0;
				painted = false;
				timeSec = timeMin = 0;
				flagCount = finish = mineValue;
				for (int i = 0; i < 12; i++)
				{
					for (int j = 0; j < 12; j++)
					{
						flagDisp[i][j] = false;
						gameValue[i][j] = 0;
						check[i][j] = 0;
					}
					
				}
				cards.show(c, "Panel 1");
			}
			else if (command.equals("SUBMIT"))					// calls a method that checks if the answer is correct
			{
				submitted();
			}
			
		}
		public void submitted()					// checks if answer is correct and sends you either bakc to the game or home screen
		{
			if (x == 0 || x == 5 || x == 6)
			{
				if (ans2.isSelected())
				{
					cards.show(c, "Panel 4");
				}
				else  											// if statements for the different answers
				{
					JOptionPane.showMessageDialog(panel5, "Sorry, you lost! Click 'OK' to return to the home screen", "Game Over", JOptionPane.INFORMATION_MESSAGE);
					timer.stop();
					painted = false;
					timeSec = timeMin = 0;
					flagCount = finish = mineValue;
					for (int i = 0; i < 12; i++)
					{
						for (int j = 0; j < 12; j++)
						{
							check[i][j] = 0;
							flagDisp[i][j] = false;
							gameValue[i][j] = 0;
						}
						
					}
					cards.show(c, "Panel 1");
				}
			}
			else if (x == 4 || x == 3)
			{
				if (ans1.isSelected())
				{
					cards.show(c, "Panel 4");
				}
				else
				{
					JOptionPane.showMessageDialog(panel5, "Sorry, you lost! Click 'OK' to return to the home screen", "Game Over", JOptionPane.INFORMATION_MESSAGE);
					timer.stop();
					painted = false;
					timeSec = timeMin = 0;
					flagCount = finish = mineValue;
					for (int i = 0; i < 12; i++)
					{
						for (int j = 0; j < 12; j++)
						{
							check[i][j] = 0;
							flagDisp[i][j] = false;
							gameValue[i][j] = 0;
						}
						
					}
					cards.show(c, "Panel 1");
					
				}
			}
			else if (x == 2 || x == 9 || x == 8)
			{
				if (ans3.isSelected())
				{
					cards.show(c, "Panel 4");
				}
				else
				{
					JOptionPane.showMessageDialog(panel5, "Sorry, you lost! Click 'OK' to return to the home screen", "Game Over", JOptionPane.INFORMATION_MESSAGE);
					timer.stop();
					painted = false;
					timeSec = timeMin = 0;
					flagCount = finish = mineValue;
					for (int i = 0; i < 12; i++)
					{
						for (int j = 0; j < 12; j++)
						{
							check[i][j] = 0;
							flagDisp[i][j] = false;
							gameValue[i][j] = 0;
						}
						
					}
					cards.show(c, "Panel 1");
				}
			}
			else if (x == 1 || x == 7)
			{
				if (ans4.isSelected())
				{
					cards.show(c, "Panel 4");
				}
				else
				{
					JOptionPane.showMessageDialog(panel5, "Sorry, you lost! Click 'OK' to return to the home screen", "Game Over", JOptionPane.INFORMATION_MESSAGE);
					timer.stop();
					painted = false;
					timeSec = timeMin = 0;
					flagCount = finish = mineValue;
					for (int i = 0; i < 12; i++)
					{
						for (int j = 0; j < 12; j++)
						{
							check[i][j] = 0;
							flagDisp[i][j] = false;
							gameValue[i][j] = 0;
						}
						
					}
					cards.show(c, "Panel 1");
				}
			}
			
			
		}
		public void paintComponent (Graphics g)						// paints the question on the panel
		{
			super.paintComponent(g);
			x = (int)(Math.random() * 10);
			System.out.println("    " + x);
			g.setFont(new Font("SansSerif", Font.BOLD, 15));
			g.drawString("" + questions[x], 50, 100);
			setAnswers();
			
		}
		public void setAnswers()								// gives the appropriate answers
		{
			System.out.println("    " + x);
			ans1.setText(answers[0][x]);
			ans2.setText(answers[1][x]);
			ans3.setText(answers[2][x]);
			ans4.setText(answers[3][x]);
		}
	}
	public class GamePanel extends JPanel implements ActionListener, MouseListener			// GAME
	{
		private JButton quit;
		private JPanel info;
		private int xpos = 0;
		private int ypos = 0;
		private TimerPainting paint;
		private FlagPainting flag;
		private Scanner read;
		private JButton option;
		
		public GamePanel()					// constructor
		{
			System.out.println("CONSTRUCTORRRRR");
			flagDisp = new boolean[12][12];
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < 12; j++)
				{
					flagDisp[i][j] = false;
				}
				
			}
			
			this.setLayout(null);
			addMouseListener(this);
			quit = new JButton("Quit");
			quit.addActionListener(this);
			quit.setBounds(0, 0, 500, 20);
			this.add(quit);
			
			option = new JButton("Revive");
			option.setBounds(150, 300, 100, 30);
			option.addActionListener(this);
			
			info = new JPanel(new GridLayout(1, 2, 5, 5));
			info.setBackground(Color.darkGray);
			paint = new TimerPainting();
			flag = new FlagPainting();
			info.add(paint);
			info.add(flag);
			info.setBounds(0, 430, 500, 70);
			this.add(info);
			
			Count count = new Count(); 
			timer = new Timer(1000, count);
			
		}
		public void changeValue()
		{
			flagCount = mineValue;
			System.out.println("MineValue = " + mineValue);
		}

		public void paintComponent(Graphics g)											// Paints the grid
		{
			int x, y;
			x = y = 0;
			super.paintComponent(g);
			
			g.setColor(Color.white);
			int xstart = 45;
			int ystart = 28;
			for (int i = 0; i < boxes; i++)
			{
				for (int j = 0; j < boxes; j++)
				{
					g.fillRect(xstart, ystart, boxLength, boxLength);
					xstart += inc;
				}
				ystart += inc;
				xstart = 45;
			}
			System.out.println("GAMEEEE CAME");
			if (painted == true)
			{
				
				for (int i = 0; i < boxes; i++)
				{
					for (int j = 0; j < boxes; j++)
					{
						g.setFont(new Font("SansSerif", Font.PLAIN, numSize));
						if (check[i][j] == 1)
						{
							g.setColor(Color.gray);
							g.fillRect(45 + x, 28 + y, boxLength, boxLength);
							g.setColor(Color.blue);
							g.drawString("" + gameValue[i][j], x + numPos, y + numPos);
						}
						else if (check[i][j] == 2)
						{
							g.setColor(Color.red);
							g.fillRect((x + 45) + flagPos, (y + 28) + flagPos, 10, 10);
							check[i][j] = 2;
							flagDisp[i][j] = true;
						}
						x += inc;
					}
					y += inc;
					x = 0;
					
				}
				
			}
			painted = true;

		}
		private class TimerPainting extends JPanel										// Paints the timer
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.red);
				g.setFont(new Font("SansSerif", Font.PLAIN, 50));
				if(timeMin < 10)
				{
					g.drawString("0" + timeMin, 40, 50);
				}
				else
				{
					g.drawString("" + timeMin, 40, 50);
				}
				if(timeSec < 10)
				{
					g.drawString("0" + timeSec, 125, 50);
				}
				else
				{
					g.drawString("" + timeSec, 125, 50);
				}
				g.drawString(":", 105, 47);
			}
		}
		class FlagPainting extends JPanel										// Paints the flag count
		{
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.blue);
				g.setFont(new Font("SansSerif", Font.PLAIN, 40));
				g.drawString("FLAGS", 5, 50);
				g.setFont(new Font("SansSerif", Font.PLAIN, 50));
				g.drawString("" + flagCount, 160, 50);
				System.out.println("FlagCount = " + flagCount);
			}
		}
		class Count implements ActionListener											// class that increments the time
		{
			public Count()
			{
				
			}
			public void actionPerformed(ActionEvent e)
			{
				timeSec++;
				if (timeSec >= 60)
				{
					timeSec = 0;
					timeMin++;
				}
				paint.repaint();
			}
		}
		public void actionPerformed(ActionEvent e)							// Quit resets everything
		{
			String command = e.getActionCommand();
			
			if (command.equals("Quit"))
			{
				timer.stop();
				painted = false;
				timeSec = timeMin = 0;
				flagCount = finish = mineValue;
				for (int i = 0; i < 12; i++)
				{
					for (int j = 0; j < 12; j++)
					{
						check[i][j] = 0;
						flagDisp[i][j] = false;
						gameValue[i][j] = 0;
					}
					
				}
				cards.show(c, "Panel 1");
			}
			else if (command.equals("Revive"))
			{
				cards.show(c, "Panel 5");
			}
		}
		private class Flag														// class that draws the flags
		{
			int x, y;
			Graphics g = getGraphics();
			public Flag()
			{
				x = y = 0;
			}
			public void disp()												// this  method toggles the flags
			{
				for (int i = 0; i < boxes; i++)
				{
					for (int j = 0; j < boxes; j++)
					{
						if (check[i][j] != 1)
						{
							if(xpos > 45 + x && xpos < boxLength + 45 + x && ypos > 28 + y && ypos < boxLength + 28 + y)
							{
								
								if (flagDisp[i][j])
								{
									g.setColor(Color.white);
									g.fillRect(45 + x, 28 + y, boxLength, boxLength);
									flagCount++;
									flagDisp[i][j] = false;
									check[i][j] = 0;
								}
								else if (flagCount != 0)
								{
									if (!flagDisp[i][j])
									{
										g.setColor(Color.red);
										g.fillRect((x + 45) + flagPos, (y + 28) + flagPos, 10, 10);
										flagCount--;
										check[i][j] = 2;
										flagDisp[i][j] = true;
									}
								}
								
							}
						}
						x += inc;
					}
					y += inc;
					x = 0;
				}
			}
			
		}
		public void mousePressed(MouseEvent e)
		{
			xpos = e.getX();
			ypos = e.getY();
			System.out.print("CLick");
			if (e.isMetaDown())												// if you press the right mouse button
			{

					Flag fcount = new Flag();
					fcount.disp();
					
					flag.repaint();	
						
			}
			else 															// calls drawNum which reveals the number 
			{
				drawNum();
			}
								
		}
		public void drawNum()					// one of the main methods that draws the number on the square that you click
		{
			int a, b, x, y;
			x = y = a = b = 0;
			Graphics g = getGraphics();
			for (int i = 0; i < boxes; i++)
			{
				for (int j = 0; j < boxes; j++)
				{
					if(xpos > 45 + x && xpos < boxLength + 45 + x && ypos > 28 + y && ypos < boxLength + 28 + y)
					{
						g.setFont(new Font("SansSerif", Font.PLAIN, numSize));
						if (check[i][j] == 0)
						{
							if (gameValue[i][j] == 9)
							{
								for (int k = 0; k < boxes; k++)
								{
									for (int l = 0; l < boxes; l++)
									{
										g.setColor(Color.gray);
										g.fillRect(45 + a, 28 + b, boxLength, boxLength);
										g.setColor(Color.blue);
										if (gameValue[k][l] == 9)
										{
											g.drawImage(bomb, a + 47, b + 32, 25, 25, null);
										}
										else
										{
											g.drawString("" + gameValue[k][l], a + numPos, b + numPos);
										}
										a += inc;
									}
									b += inc;
									a = 0;
									
								}
								revive();
							}
							else
							{

								g.setColor(Color.gray);
								g.fillRect(45 + x, 28 + y, boxLength, boxLength);
								g.setColor(Color.blue);
								if (gameValue[i][j] == 9)
								{
									g.fillOval(x + 55, y + 38, 10, 10);
								}
								else
								{
									g.drawString("" + gameValue[i][j], x + numPos, y + numPos);
								}
								finish++;
								System.out.println("GOO " + finish);
								if (finish == boxes * boxes)
								{
									score = (boxes * 30) + (mineValue * 5) - (timeMin * 60) - timeSec;
									JOptionPane.showMessageDialog(panel5, "Your score is : " + score, "You Won!!!", JOptionPane.INFORMATION_MESSAGE);
									timer.stop();
									painted = false;
									timeSec = timeMin = 0;
									flagCount = mineValue;
									for (int k = 0; k < 12; k++)
									{
										for (int l = 0; l < 12; l++)
										{
											check[k][l] = 0;
											flagDisp[k][l] = false;
											gameValue[k][l] = 0;
										}
										
									}
									cards.show(c, "Panel 1");
								}
								check[i][j] = 1;
							}
						}
						
					}
					x += inc;
				}
				y += inc;
				x = 0;
			}
			g.dispose();
		}
		public void revive()													// if you hit a mine, it gives you a chance to continue
		{
			
			panel5.repaint();
			cards.show(c, "Panel 5");
			
		}												
		public void setGridAndMines()															// sets the random mines and numbers across the grid
		{
			for (int i = 0; i < mineValue; i++)
			{
				boolean done = false;
				do
				{
					int x = (int)(Math.random() * boxes);
					int y = (int)(Math.random() * boxes);
					if (gameValue[x][y] == 0)
					{
						gameValue[x][y] = 9;
						done = true;
					}
				}
				while (!done);
			}
			for (int i = 0; i < boxes; i++)
			{
				for (int j = 0; j < boxes; j++)
				{
					if (gameValue[i][j] != 9)
					{
						if (i == 0 && j == 0)							// top left corner
						{
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							
						}
						else if (i == boxes - 1 && j == boxes - 1)					// bottom right corner
						{
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							} 
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							
						}
						else if (i == boxes - 1 && j == 0)						// bottom left corner
						{
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							 
						}
						else if (i == 0 && j == boxes - 1)						// top right corner
						{
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
						}
						else if (i != 0 && i != boxes - 1 && j == 0)			// left row
						{
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
						}
						else if (i != 0 && i != boxes - 1 && j == boxes - 1)			// right row
						{
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
						}
						else if (i == 0 && j != 0 && j != boxes - 1)			// top row
						{
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							
						}
						else if (i == boxes - 1 && j != 0 && j != boxes - 1)			// bottom row
						{
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							
						}
						else if (i != 0 && i != boxes - 1 && j != 0 && j != boxes - 1)			// rest of the squares.
						{
							if (gameValue[i][j + 1] == 9)
							{
								gameValue[i][j]++;
							} 
							if (gameValue[i][j - 1] == 9)
							{
								gameValue[i][j]++;
							} 
							if (gameValue[i + 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i + 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j + 1] == 9)
							{
								gameValue[i][j]++;
							}
							if (gameValue[i - 1][j - 1] == 9)
							{
								gameValue[i][j]++;
							}
						}
					}
						
					if (j == boxes - 1)
					{
						System.out.println(" " + gameValue[i][j]);
					} 
					else 
					{
						System.out.print(" " + gameValue[i][j]);
					}
				}
				
			} 
			
		}
		
		public void mouseReleased(MouseEvent e)							// unused method
		{
			
		}
		public void mouseClicked(MouseEvent e)							// unused method
		{
			
		}
		public void mouseEntered(MouseEvent e)							// unused method
		{
			
		}
		public void mouseExited(MouseEvent e)							// unused method
		{
			
		}
		
	}
}
