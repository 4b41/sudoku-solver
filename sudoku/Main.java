package sudoku;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Main {
	
	JFrame window = new JFrame();
	JPanel gamePanel = new JPanel();
	JPanel options = new JPanel();
	JPanel menu = new JPanel();
	JPanel[][] grid = new JPanel[3][3];
	JLabel warning = new JLabel();
	JButton solve = new JButton();
	JButton reset = new JButton();
	JTextField[][] inp = new JTextField[9][9];

	String[][] board = new String[9][9];

	public static void main(String[] args) {
		Main game = new Main();
	}
	
	public Main() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = ".";
			}
		}
		createUI();
	}
	
	public void createUI() {
		
		ActionListener click = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String s = e.getActionCommand();
				if (s.equals("solve")) {
					if (checkSolvable()) {
						solveBoard(0);
						updateBoard();
						warning.setVisible(false);
					} else {
						warning.setVisible(true);
					}
				} else if (s.equals("reset")) {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							board[i][j] = ".";
							inp[i][j].setText("");
							inp[i][j].setForeground(Color.BLACK);
							inp[i][j].setEditable(true);
							warning.setVisible(false);
						}
					}
				}
			}
		};
		
		window.setSize(480,580);
		window.setTitle("Sudoku Solver");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setBackground(Color.white);
		window.setLayout(null);
		window.setResizable(false);
		
		options.setBounds(12, 12, 456, 64);
		options.setBackground(new Color(216, 216, 216));
		options.setBorder(BorderFactory.createLineBorder(Color.black));
		window.add(options);
		
		solve.setText("Solve");
		solve.setActionCommand("solve");
		solve.addActionListener(click);
		options.add(solve);
		
		reset.setText("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(click);
		options.add(reset);
		
		warning.setText("Error! This Sudoku is unsolvable! Please try again!");
		warning.setVisible(false);
		options.add(warning);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				grid[i][j] = new JPanel();
				grid[i][j].setBounds(22+(i*145),95+(j*145),145,145);
				grid[i][j].setBackground(new Color(184, 184, 184));
				grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[i][j].setLayout(new GridLayout(3,3));
				window.add(grid[i][j]);
				
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int one = i, two = j;
				inp[i][j] = new JTextField();;
				inp[i][j].setBackground(new Color(246, 246, 242));
				inp[i][j].setHorizontalAlignment(JTextField.CENTER);
				inp[i][j].addKeyListener(new KeyAdapter() {
				    public void keyTyped(KeyEvent e) { 
				    	if (inp[one][two].getText().length() >= 1) {
				            e.consume(); 
				    	} else {
					    	int val = Integer.valueOf(e.getKeyChar()) - 48;
					    	if (val > 0 && val < 10) {
					    		board[one][two] = "" + val;
					    	} else {
								board[one][two] = ".";
								inp[one][two].setText("");
					    	}
				    	}
				    }  
				});
				grid[j/3][i/3].add(inp[i][j]);
			}
		}
		
		gamePanel.setBounds(12, 84, 456, 456);
		gamePanel.setBackground(new Color(216, 216, 216));
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		window.add(gamePanel);
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public void updateBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				inp[i][j].setText(board[i][j]);
				inp[i][j].setEditable(false);
			}
		}
	}
	
	public boolean checkSolvable() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				String cur = board[i][j];
				if (cur.equals(".")) {
					continue;
				}
				for (int k = 0; k < 9; k++) {
					if(board[i][k].equals(cur) && k != j) {
						return false;
					}
					if(board[k][j].equals(cur) && k != i) {
						return false;
					}
					int r = (3*(i/3))+(j/3);
					if (board[(3*(r/3))+(k/3)][(3*(r%3))+(k%3)].equals(cur)) {
						if ((3*(r/3))+(k/3) != i && (3*(r%3))+(k%3) != j) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean solveBoard(int n) {
		if (n == 81) {
			return true;
		}
		int r = n / 9, c = n % 9;
		if (board[r][c] != ".") {
			return solveBoard(n+1);
		} else {
			for (int i = 1; i <= 9; i++) {
				if (isValid(r, c, "" + i)) {
					inp[r][c].setForeground(Color.RED);
					board[r][c] = "" + i;
					if (solveBoard(n+1)) {
						return true;
					}
					board[r][c] = ".";
				}
			}
		}
		return false;
	}
	
	public boolean isValid(int i, int j, String f) {
		for (int k = 0; k < 9; k++) {
			int r = (3*(i/3))+(j/3);
			if(board[i][k].equals(f) || board[k][j].equals(f) || board[(3*(r/3))+(k/3)][(3*(r%3))+(k%3)].equals(f)) {
				return false;
			}
		}
		return true;
	}
}
