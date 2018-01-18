package com.Thread0712;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Date;
import java.text.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Pong extends JPanel implements ActionListener, KeyListener, WindowListener {
	static int ballx = 10, bally = 100, xinc = 1, yinc = 1, ovalsize = 20, ptlsize = 30, ball2x = 580, ball2y = 100;
	static int xrec1 = 10, yrec1 = 10, xrec2 = 10, yrec2 = 500, widthrecb = 20, heightrecb = 100, widthreca2 = 20,
			heightreca2 = 100;
	static int locationx = 450, locationy = 150, width = 600, height = 600;
	static int bouncewid = 23, bounceleg = 103;
	static int scorea = 0, scoreb = 0;
	static int holex1 = 470, holex2 = 144, holex3 = 465, holex4 = 290, holey1 = 170, holey2 = 236, holey3 = 365, holey4 = 350;
	static int dic1 = 1, dic2 = 1;
	static JFrame f;
	static int level = 1;
	static float time = 30;
	static Timer t;
	static File ballbounce = new File("8023.wav"), background = new File("4783.wav"), timesout = new File("4731.wav"),
			getpoint = new File("2103.wav");
	static BufferedImage back;
	static BufferedImage homepage;
	static BufferedImage leaderb;
	static BufferedImage levelup;
	static BufferedImage portal;
	static BufferedImage blackhole;
	static BufferedImage ball;
	static JTextField name1 = new JTextField("Name1");
	static JTextField name2 = new JTextField("Name2");
	static JTextField name3 = new JTextField("Name");
	static JButton submit = new JButton("Submit");
	static boolean game = false, record = false, gameAI = false, levelstart = false, shoot = false, name = false,
			nameAI = false, didgame = true;
	static double music = 20, score = 0;

	public static void main(String args[]) {
		Pong p = new Pong();
		// construct Jpanel
		name1.setBounds(100, 100, 600, 600);
		name2.setBounds(300, 100, 600, 600);
		name3.setBounds(300, 100, 100, 100);
		submit.setBounds(500, 100, 100, 100);
		submit.addActionListener(p);
		name1.setVisible(false);
		name2.setVisible(false);
		name3.setVisible(false);
		submit.setVisible(false);
		p.add(name1);
		p.add(name2);
		p.add(name3);
		p.add(submit);
		f = new JFrame();
		// construct Jframe
		f.add(p);
		// add panel to frame
		f.setSize(width, height + 20);
		f.setVisible(true);
		f.setTitle("Rebecca's Pong");
		f.setLocation(locationx, locationy);
		t = new Timer(6, p);
		t.start();
		// set a timer associated with ActionPerformed
		soundEffect(background);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// set background music
	}

	public Pong() {
		addKeyListener(this);
		setFocusable(true);
		// add keyListener to Jpanel
		try {
			back = ImageIO.read(new File("wonderland.jpg"));
			homepage = ImageIO.read(new File("homepage.jpg"));
			leaderb = ImageIO.read(new File("leaderb.jpg"));
			levelup = ImageIO.read(new File("levelup.jpg"));
			portal = ImageIO.read(new File("Portal.png"));
			ball = ImageIO.read(new File("ball.png"));
			blackhole = ImageIO.read(new File("blackhole.jpg"));
		} catch (IOException ex) {
			System.err.println("image not found");
		}
	}

	public void paintComponent(Graphics g) {
		music = music - 0.005;
		if (music <= 0) {
			soundEffect(background);
			music = 20;
		}
		if (game == false && record == false && gameAI == false) {
			// in the situation that none of the game with player,game with AI or record board is on
			g.drawImage(homepage, 0, 0, width, height, this);
			g.setFont(new Font("serif", Font.BOLD, 50));
			g.setColor(Color.DARK_GRAY);
			g.drawString("press i to play with A.I.", 20, 200);
			g.drawString("press p to play with player", 20, 100);
			g.drawString("press r to see the record", 20, 300);
		}

		if (name == true && game == false) {
			g.drawImage(homepage, 0, 0, width, height, this);
			submit.setVisible(true);
			name1.setVisible(true);
			name2.setVisible(true);
		}

		if (nameAI == true && gameAI == false) {
			g.drawImage(homepage, 0, 0, width, height, this);
			submit.setVisible(true);
			name3.setVisible(true);
		}

		if ((game == true && name == false) || gameAI == true) {
			// in situation that either game with player or game with A.I. is on
			if (levelstart == false) {
				g.drawImage(levelup, 0, 0, width, height, this);
			} else {
				g.drawImage(back, 0, 0, width, height, this);
			}
			g.setColor(Color.yellow);
			g.fillOval(ballx, bally, ovalsize, ovalsize);
			g.setColor(Color.green);
			g.fillRect(xrec2, yrec2, widthrecb, heightrecb);
			g.setColor(Color.red);
			g.fillRect(xrec1, yrec1, widthreca2, heightreca2);
			g.setColor(Color.BLUE);
			g.fillRect(200, 0, 200, 10);
			g.fillRect(200, 590, 200, 10);
			g.setFont(new Font("serif", Font.PLAIN, 20));
			g.setColor(Color.PINK);
			if (gameAI == true) {
				score += 0.005;
				g.drawString("level" + level, 100, 100);
				g.drawString("Your score is " + (int) score, 30, 15);
			}
			if (game == true) {
				g.drawString("Score of A side is " + scorea, 30, 15);
				g.drawString("Score of B side is " + scoreb, 30, 590);
			}
			g.drawImage(portal, holex1, holey1, ptlsize, ptlsize, this);
			g.drawImage(portal, holex2, holey2, ptlsize, ptlsize, this);
			g.drawImage(portal, holex3, holey3, ptlsize, ptlsize, this);
			if (time <= 0) {
				// the situation that the time counter is less or equal to zero
				if (game == true) {
					g.drawString("Time's out", 200, 300);
					game = false;
					soundEffect(timesout);
					name = true;
				} else if (gameAI == true) {
					// if the game is in AI mode
					time = 30;
					level++;
					xinc = xinc * 2;
					yinc = yinc * 2;
					levelstart = true;
					// as the level goes up, the speed of the ball increases
				}
			} else {
				g.setColor(Color.PINK);
				g.drawString("Time left: " + (int) time, 400, 15);
			}
			// display the time left
			time -= 0.005;
			g.drawImage(blackhole, holex4, holey4, ptlsize, ptlsize, this);
		}

		if ((time <= 0 && game == false && name == false && gameAI == false)
				|| (gameAI == false && nameAI == false && didgame == false)) {
			// situation that time is over and both game with player and game with AI are over
			time = 30;
			fileReader("Highscore.txt");
			didgame = true;
		}

		if (record == true) {
			// situation that record board is displayed
			g.drawImage(leaderb, 0, 0, width, height, this);
			String file = "Highscore.txt";
			FileReader fileobj = null;
			int rows = 10;
			int columns = 3;
			String array[][] = new String[rows][columns];
			try {
				fileobj = new FileReader(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			Scanner input = new Scanner(fileobj);
			// scan the date in the file
			g.setColor(Color.BLUE);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Leaderboard", 225, 30);
			for (int a = 0; a < rows; a++) {
				for (int b = 0; b < columns; b++) {
					String tmp = input.next();
					array[a][b] = tmp;
					g.setFont(new Font("serif", Font.PLAIN, 30));
					g.setColor(Color.blue);
					if (b == 2)
						g.drawString(array[a][b] + "\n", (b + 1) * 80, (a + 1) * 55);
					else if (b == 0)
						g.drawString(array[a][b], 10, (a + 1) * 55);
					else if (b == 1)
						g.drawString(array[a][b], (b + 1) * 80, (a + 1) * 55);
				}
			}
			// display the data in the panel
			input.close();
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("press i to restart game with A.I.  press p to restart game with player", 10, 580);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			submit.setVisible(false);
			name1.setVisible(false);
			name2.setVisible(false);
			name3.setVisible(false);
			shoot = true;
			name = false;
			nameAI = false;
		}

		if (game == true || gameAI == true) {
			if ((bally > height - 20) || (bally < 0)
					|| (ballx + bouncewid >= xrec1 && ballx <= xrec1 + bounceleg && bally + bouncewid >= yrec1
							&& bally < yrec1 && yinc > 0 && widthreca2 == 100)
					|| (ballx + bouncewid >= xrec1 && ballx <= xrec1 + bounceleg && bally <= yrec1 + bouncewid
							&& bally > yrec1 && yinc < 0 && widthreca2 == 100)
					|| (ballx + bouncewid >= xrec2 && ballx <= xrec2 + bounceleg && bally + bouncewid >= yrec2
							&& bally < yrec2 && yinc > 0 && widthrecb == 100)
					|| (ballx + bouncewid >= xrec2 && ballx <= xrec2 + bounceleg && bally <= yrec2 + bouncewid
							&& bally > yrec2 && yinc < 0 && widthrecb == 100)
					|| (bally + bouncewid == yrec1 && widthreca2 == 20 && ballx + bouncewid >= xrec1
							&& ballx <= xrec1 + bouncewid && yinc > 0)
					|| (bally == yrec1 + bounceleg && widthreca2 == 20 && ballx + bouncewid >= xrec1
							&& ballx <= xrec1 + bouncewid && yinc < 0)
					|| (bally + bouncewid == yrec2 && widthrecb == 20 && ballx + bouncewid >= xrec2
							&& ballx <= xrec2 + bouncewid && yinc > 0)
					|| (bally == yrec2 + bounceleg && widthrecb == 20 && ballx + bouncewid >= xrec2
							&& ballx <= xrec2 + bouncewid && yinc < 0)) {
				yinc = -yinc;
				// when the ball hit the wall or the paddle, it will bounce vertically
				soundEffect(ballbounce);
			}
			if ((ballx > width - 20) || (ballx < 0)
					|| (bally + bouncewid >= yrec1 && bally <= yrec1 + bounceleg && ballx + bouncewid >= xrec1
							&& ballx < xrec1 && xinc > 0 && widthreca2 == 20)
					|| (bally + bouncewid >= yrec1 && bally <= yrec1 + bounceleg && ballx <= xrec1 + bouncewid
							&& ballx > xrec1 && xinc < 0 && widthreca2 == 20)
					|| (bally + bouncewid >= yrec2 && bally <= yrec2 + bounceleg && ballx + xrec1 >= xrec2
							&& ballx < xrec2 && xinc > 0 && widthrecb == 20)
					|| (bally + bouncewid >= yrec2 && bally <= yrec2 + bounceleg && ballx <= xrec2 + bouncewid
							&& ballx > xrec2 && xinc < 0 && widthrecb == 20)
					|| (ballx + bouncewid == xrec1 && widthreca2 == 100 && bally + bouncewid >= yrec1
							&& bally <= yrec1 + bouncewid && xinc > 0)
					|| (ballx == xrec1 + bounceleg && widthreca2 == 100 && bally + bouncewid >= yrec1
							&& bally <= yrec1 + bouncewid && xinc < 0)
					|| (ballx + bouncewid == xrec2 && widthrecb == 100 && bally + bouncewid >= yrec2
							&& bally <= yrec2 + bouncewid && xinc > 0)
					|| (ballx == xrec2 + bounceleg && widthrecb == 100 && bally + bouncewid >= yrec2
							&& bally <= yrec2 + bouncewid && xinc < 0)) {
				xinc = -xinc;
				// when the ball hit the wall or the paddle, it will bounce horizontally
				soundEffect(ballbounce);
			}
			if (((bally + 20) == 600) && ((ballx + 10) >= 200) && ((ballx + 10) <= 400)) {
				scorea = scorea + 5;
				// when it ball his the goal on B side, player A score
				soundEffect(getpoint);
			}
			if (game == true) {
				// when the game is in player mode
				if (bally == 0 && (ballx + 10) >= 200 && (ballx + 10) <= 400) {
					scoreb = scoreb + 5;
					// when it ball his the goal on A side, player B score
					soundEffect(getpoint);
				}
			}
			if (gameAI == true) {
				// when the game is in AI mode
				if (bally == 0 && (ballx + 10) >= 200 && (ballx + 10) <= 400) {
					gameAI = false;
					nameAI = true;
					// stop the game
				}
			}
			if ((Math.sqrt((ballx - holex4) * (ballx - holex4) + (bally - holey4) * (bally - holey4)) <= 20)
					&& ballx != holex4 && bally != holey4) {
				// when the ball is close to the area of the hole
				ballx = ballx + (holex4 - ballx) / 3;
				bally = bally + (holey4 - bally) / 3;
				ballx = (int) (Math.random() * 300);
				bally = (int) (Math.random() * 300);
				xinc = -xinc;
				// the ball will be delivered to a random location
			} else if (Math.sqrt((ballx - holex1) * (ballx - holex1) + (bally - holey1) * (bally - holey1)) <= 10
					&& ballx != holex1 && bally != holey1) {
				ballx = ballx + (holex1 - ballx) / 3;
				bally = bally + (holey1 - bally) / 3;
				ballx = holex3 + (int) (Math.random() * 50);
				bally = holey3 + (int) (Math.random() * 50);
				// the ball will be delivered to another hole
				xinc = -xinc;
			} else if (Math.sqrt((ballx - holex2) * (ballx - holex2) + (bally - holey2) * (bally - holey2)) <= 10
					&& ballx != holex2 && bally != holey2) {
				ballx = ballx + (holex2 - ballx) / 3;
				bally = bally + (holey2 - bally) / 3;
				ballx = holex1 + (int) (Math.random() * 50);
				bally = holey1 + (int) (Math.random() * 50);
				xinc = -xinc;
			} else if (Math.sqrt((ballx - holex3) * (ballx - holex3) + (bally - holey3) * (bally - holey3)) <= 10
					&& ballx != holex3 && bally != holey3) {
				ballx = ballx + (holex3 - ballx) / 3;
				bally = bally + (holey3 - bally) / 3;
				ballx = holex2 + (int) (Math.random() * 50);
				bally = holey2 + (int) (Math.random() * 50);
				xinc = -xinc;
			} else {
				ballx = ballx + xinc;
				bally = bally + yinc;
				// the increment of the ball
			}
		}
		if (gameAI == true) {

			if (xinc < 0 && ballx < xrec2 + 100) {
				xrec2 = xrec2 + (ballx - xrec2);
			}
			if (xinc > 0 && ballx > xrec2) {
				xrec2 = xrec2 + (ballx + 30 - xrec2);
			}
			// Panel movement is determined by the location of the ball
		}
		f.repaint();
	}

	public static void fileReader(String Highscore) {
		String file = Highscore;
		FileReader fileobj = null;
		try {
			fileobj = new FileReader(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// find the file and catch the exception
		Scanner rowcounter = new Scanner(fileobj);
		int rows = 0;
		int columns = 3;
		String array[][] = new String[rows + 100][columns];
		while (rowcounter.hasNext()) {
			rows++;
			rowcounter.nextLine();
		}
		// count the number of lines in the file
		try {
			fileobj = new FileReader(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Scanner input = new Scanner(fileobj);
		for (int a = 0; a < rows; a++) {
			for (int b = 0; b < columns; b++) {
				String tmp = input.next();
				array[a][b] = tmp;
			}
		}
		input.close();
		// scan the file
		String arrnew[][] = new String[rows + 100][columns];
		for (int a = 0; a < rows; a++) {
			for (int b = 0; b < columns; b++) {
				arrnew[a][b] = array[a][b];
			}
		}
		// set a new string array and put the scanned into this array
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		Date date = new Date();
		arrnew[rows][2] = dateFormat.format(date);
		// get the date and put it in the next column of the rows
		if (didgame == true) {
			arrnew[rows][0] = name1.getText();
			arrnew[rows + 1][0] = name2.getText();
			int player1score = scorea;
			int player2score = scoreb;
			arrnew[rows][1] = Integer.toString(player1score);
			arrnew[rows + 1][1] = Integer.toString(player2score);
			// put the scores and names in the next two rows
			arrnew[rows + 1][2] = dateFormat.format(date);
		}
		if (didgame == false) {
			arrnew[rows][0] = name3.getText();
			int AIplayer = (int) score;
			arrnew[rows][1] = Integer.toString(AIplayer);
		}
		bubbleSort(arrnew);
		// bubble sort the arrnew
		FileWriter fw;
		String in;
		try {
			fw = new FileWriter(file);
			for (int i = 0; i < 10; i++) {
				in = arrnew[i][0] + " " + arrnew[i][1] + " " + arrnew[i][2] + "\r\n";
				// write the string into the file
				fw.write(in);
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void bubbleSort(String[][] number) {
		boolean didSwap = false;
		for (int loop = 1; loop < number.length - 1; loop++) {
			for (int max = 0; !(number[max + 1][0] == null); max++) {
				int temp1 = Integer.parseInt(number[max][1]);
				int temp2 = Integer.parseInt(number[max + 1][1]);
				if (temp1 < temp2) {
					String temp = number[max][1];
					number[max][1] = number[max + 1][1];
					number[max + 1][1] = temp;
					String temp3 = number[max][2];
					number[max][2] = number[max + 1][2];
					number[max + 1][2] = temp3;
					String temp4 = number[max][0];
					number[max][0] = number[max + 1][0];
					number[max + 1][0] = temp4;
					didSwap = true;
					//bubble sort the data 
				}
			}
			if (didSwap == false) {
				return;
			}
			didSwap = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_P) {
			widthrecb = 20;
			heightrecb = 100;
			game = true;
			record = false;
			scorea = 0;
			scoreb = 0;
			ballx = 10;
			bally = 100;
			xrec1 = 10;
			yrec1 = 10;
			xrec2 = 10;
			yrec2 = 500;
			time = 30;
			xinc = 1;
			yinc = 1;
			didgame = true;
			levelstart = false;
			JTextField name1 = new JTextField("Name1");
			JTextField name2 = new JTextField("Name2");
			//set 'p' as initiator for game with player 
		}
		if (k == KeyEvent.VK_R)
			record = true;
		//set 'p' as initiator for the leader board 
		if (k == KeyEvent.VK_I) {
			time = 30;
			score = 0;
			ballx = 10;
			bally = 100;
			xrec1 = 10;
			yrec1 = 10;
			xinc = 1;
			yinc = 1;
			gameAI = true;
			record = false;
			level = 1;
			xrec2 = 300;
			yrec2 = 570;
			widthrecb = 100;
			heightrecb = 20;
			xinc = 1;
			yinc = 1;
			didgame = false;
			levelstart = false;
			JTextField name3 = new JTextField("Name");
			//set 'p' as initiator for game with AI
		}

		if ((k == KeyEvent.VK_DOWN || k == KeyEvent.VK_UP) && dic1 == 0) {
			int temp = widthreca2;
			widthreca2 = heightreca2;
			heightreca2 = temp;
			dic1 = 1;
		} else if ((k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) && dic1 == 1) {
			int temp = heightreca2;
			heightreca2 = widthreca2;
			widthreca2 = temp;
			dic1 = 0;
		} else if (k == KeyEvent.VK_DOWN && yrec1 <= 500) {
			yrec1 += 18;
		} else if (k == KeyEvent.VK_UP && yrec1 >= 0) {
			yrec1 -= 18;
		} else if (k == KeyEvent.VK_LEFT && xrec1 >= 0) {
			xrec1 -= 18;
		} else if (k == KeyEvent.VK_RIGHT && xrec1 <= 500) {
			xrec1 += 18;
		}
		//set the movement of the paddle of player 1

		if (game == true) {
			k = e.getKeyCode();
			if ((k == KeyEvent.VK_W || k == KeyEvent.VK_S) && dic2 == 0) {
				int temp2 = widthrecb;
				widthrecb = heightrecb;
				heightrecb = temp2;
				dic2 = 1;
			} else if ((k == KeyEvent.VK_A || k == KeyEvent.VK_D) && dic2 == 1) {
				int temp2 = heightrecb;
				heightrecb = widthrecb;
				widthrecb = temp2;
				dic2 = 0;
			} else if (k == KeyEvent.VK_S && yrec2 <= 500) {
				yrec2 += 18;
			} else if (k == KeyEvent.VK_W && yrec2 >= 0) {
				yrec2 -= 18;
			} else if (k == KeyEvent.VK_A && xrec2 >= 0) {
				xrec2 -= 18;
			} else if (k == KeyEvent.VK_D && xrec2 <= 500) {
				xrec2 += 18;
			}
		}
	}
	//set the movement of the paddle of player 2
	public void keyReleased(KeyEvent e) {
      
	}

	static public void soundEffect(File sound) {  
		//set sound effect
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.err.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}
	
	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
		
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
