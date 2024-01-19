package bg_smg;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	public static Field[][] field = new Field[8][8];
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	static int stage, turn, movex, movey;
	private static JPanel panel_Player1;
	private static JPanel panel_Player2;
	static int n1 = 0, n2 = 0;
	private static JLabel lblMessages = new JLabel("Да играем!");
	private static int xKiller = 0;
	private static int yKiller = 0;
	static Icon saverIcon;
	static Figure saverFigure;
	static int saverTeam;

	Icon black_rook = new ImageIcon(MainFrame.class.getResource("/ressources/black_rook.png"));
	Icon black_knight = new ImageIcon(MainFrame.class.getResource("/ressources/black_knight.png"));
	Icon black_bishop = new ImageIcon(MainFrame.class.getResource("/ressources/black_bishop.png"));
	Icon black_queen = new ImageIcon(MainFrame.class.getResource("/ressources/black_queen.png"));
	Icon black_king = new ImageIcon(MainFrame.class.getResource("/ressources/black_king.png"));
	Icon black_pawn = new ImageIcon(MainFrame.class.getResource("/ressources/black_pawn.png"));
	Icon white_rook = new ImageIcon(MainFrame.class.getResource("/ressources/white_rook.png"));
	Icon white_knight = new ImageIcon(MainFrame.class.getResource("/ressources/white_knight.png"));
	Icon white_bishop = new ImageIcon(MainFrame.class.getResource("/ressources/white_bishop.png"));
	Icon white_queen = new ImageIcon(MainFrame.class.getResource("/ressources/white_queen.png"));
	Icon white_king = new ImageIcon(MainFrame.class.getResource("/ressources/white_king.png"));
	Icon white_pawn = new ImageIcon(MainFrame.class.getResource("/ressources/white_pawn.png"));

	public static void moveFigure(int a, int b, int c, int d) {
		Figure o = field[a][b].figure;
		field[a][b].figure = field[c][d].figure;
		field[c][d].figure = o;
		Icon p = field[a][b].button.getIcon();
		Icon q = field[c][d].button.getIcon();
		field[c][d].button.setIcon(p);
		field[a][b].button.setIcon(q);
	}

	public static void eraseFigure(int a, int b) {
		field[a][b].button.setIcon(null);
		field[a][b].figure = new Figure();
	}


	public static boolean jumpOverFigures(int a, int b, int c, int d) {
		if (field[a][b].figure.type == "knight")
			return true;
		int dx, dy, x, y;
		if (a < c)
			dx = 1;
		else if (a == c)
			dx = 0;
		else
			dx = -1;

		if (b < d)
			dy = 1;
		else if (b == d)
			dy = 0;
		else
			dy = -1;

		x = a + dx;
		y = b + dy;
		while (x != c || y != d) {
			if (field[x][y].figure.team != 0)
				return false;
			x = x + dx;
			y = y + dy;
		}
		return true;
	}

	public static boolean check() {
		int a = 0, b = 0, c = 0, d = 0;
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (field[i][k].figure.type == "king" && field[i][k].figure.team == -1) {
					a = i;
					b = k;
				}
				if (field[i][k].figure.type == "king" && field[i][k].figure.team == 1) {
					c = i;
					d = k;
				}
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				if (field[i][k].figure.team == -1) {
					if (field[i][k].figure.validMove(i, k, c, d, -1) && jumpOverFigures(i, k, c, d)) {
						xKiller = i;
						yKiller = k;
						lblMessages.setText("Шах за черните!");
						lblMessages.setForeground(new Color(0, 100, 0));
						return true;
					}
				}
				if (field[i][k].figure.team == 1) {
					if (field[i][k].figure.validMove(i, k, a, b, 1) && jumpOverFigures(i, k, a, b)) {
						xKiller = i;
						yKiller = k;
						lblMessages.setText("Шах за белите!");
						lblMessages.setForeground(new Color(0, 100, 0));
						return true;
					}
				}
			}
		}
		return false;

	}

	public static boolean checkmate() {
		if (check()) {
			int turn = 0;
			if (lblMessages.getText() == "Шах за белите!")
				turn = -1;
			if (lblMessages.getText() == "Шах за черните!")
				turn = 1;

			int a = 0, b = 0;
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					if (field[i][k].figure.type == "king" && field[i][k].figure.team == turn) {
						a = i;
						b = k;
					}
				}
			}
			// System.out.println(a + " " + b);
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					if (field[a][b].figure.validMove(a, b, i, k, turn) && field[i][k].figure.team != turn) {
						moveFigure(a, b, i, k);
						saverTeam = field[a][b].figure.team;
						saverIcon = field[a][b].button.getIcon();
						saverFigure = field[a][b].figure;
						eraseFigure(a, b);
							
						if (check() == true) {
							moveFigure(i, k, a, b);
							field[i][k].figure.team = saverTeam;
							field[i][k].button.setIcon(saverIcon);
							field[i][k].figure = saverFigure;
						} else {
							moveFigure(i, k, a, b);
							field[i][k].figure.team = saverTeam;
							field[i][k].button.setIcon(saverIcon);
							field[i][k].figure = saverFigure;
							return false;
						}
					}
				}
			}
			// System.out.println(a + " " + b);

			int dx, dy;
			if (a < xKiller)
				dx = 1;
			else if (a == xKiller)
				dx = 0;
			else
				dx = -1;

			if (b < yKiller)
				dy = 1;
			else if (b == yKiller)
				dy = 0;
			else
				dy = -1;

			do {
				a = a + dx;
				b = b + dy;
				for (int i = 0; i < 8; i++) {
					for (int k = 0; k < 8; k++) {
						if (field[i][k].figure.validMove(i, k, a, b, field[a][b].figure.team)
								&& jumpOverFigures(i, k, a, b)) {
							moveFigure(i, k, a, b);
							if (check()) {
								moveFigure(a, b, i, k);
							} else {
								moveFigure(a, b, i, k);
								return false;
							}
						}
					}
				}
			} while (a != xKiller && b != yKiller);

			if (turn == -1) {
				lblMessages.setText("Шах и мат за белите!");
				lblMessages.setForeground(new Color(218, 165, 32));
			} else {
				lblMessages.setText("Шах и мат за черните!");
				lblMessages.setForeground(new Color(218, 165, 32));
			}
			return true;
		} else
			return false;
	}
	
	public static boolean stalemate(int turn) {
		if (!check()) {
			int a = 0, b = 0;
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					if (field[i][k].figure.type == "king" && field[i][k].figure.team == turn) {
						a = i;
						b = k;
					}
				}
			}
			int c = 0, d = 0, e = 0;
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					if(Math.max(Math.abs(a - i), Math.abs(b - k)) == 1) {
						d = d +1;
						if (field[a][b].figure.validMove(a, b, i, k, turn) && field[i][k].figure.team != turn) {
							moveFigure(a, b, i, k);
							saverTeam = field[a][b].figure.team;
							saverIcon = field[a][b].button.getIcon();
							saverFigure = field[a][b].figure;
							eraseFigure(a, b);
							
							if (check() == true) {
								moveFigure(i, k, a, b);
								field[i][k].figure.team = saverTeam;
								field[i][k].button.setIcon(saverIcon);
								field[i][k].figure = saverFigure;
								c = c + 1;
							} else {
								moveFigure(i, k, a, b);
								field[i][k].figure.team = saverTeam;
								field[i][k].button.setIcon(saverIcon);
								field[i][k].figure = saverFigure;
								return false;
							}
						} else {
							
						}
					}
				}
			}
			if(c == d) {
				
			}
			lblMessages.setText("Пат!");
			lblMessages.setForeground(new Color(218, 165, 32));
			return true;
		} else
			return false;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Chess");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 20, 1310, 780);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("Tahoma", Font.BOLD, 50));
		lblPlayer1.setBounds(660, 311, 624, 59);
		contentPane.add(lblPlayer1);

		JLabel lblVs = new JLabel("v/s");
		lblVs.setHorizontalAlignment(SwingConstants.CENTER);
		lblVs.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblVs.setBounds(660, 381, 624, 31);
		contentPane.add(lblVs);

		JLabel lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lblPlayer2.setBounds(660, 423, 624, 59);
		contentPane.add(lblPlayer2);

		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				field[i][k] = new Field();
				field[i][k].button.setBounds(50 + k * 75, 100 + i * 75, 75, 75);
				contentPane.add(field[i][k].button);
				if ((i + k) % 2 == 0) {
					field[i][k].button.setBackground(new Color(253, 240, 189));
					field[i][k].button.setForeground(new Color(253, 240, 189));
				} else {
					field[i][k].button.setBackground(new Color(20, 92, 49));
					field[i][k].button.setForeground(new Color(20, 92, 49));
				}
			}
		}
		JPanel panel = new JPanel();
		panel.setBounds(50, 100, 600, 600);
		contentPane.add(panel);

		lblMessages.setForeground(Color.BLACK);
		lblMessages.setFont(new Font("Tahoma", Font.PLAIN, 45));
		lblMessages.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessages.setBounds(10, 12, 1274, 78);
		contentPane.add(lblMessages);

		JPanel panel_Player1 = new JPanel();
		panel_Player1.setBounds(681, 100, 588, 200);
		contentPane.add(panel_Player1);
		panel_Player1.setLayout(null);

		JPanel panel_Player2 = new JPanel();
		panel_Player2.setBounds(681, 500, 588, 200);
		contentPane.add(panel_Player2);
		panel_Player2.setLayout(null);

		field[0][0].button.setIcon(black_rook);
		field[0][0].figure.type = "rook";

		field[0][1].button.setIcon(black_knight);
		field[0][1].figure.type = "knight";

		field[0][2].button.setIcon(black_bishop);
		field[0][2].figure.type = "bishop";

		field[0][3].button.setIcon(black_queen);
		field[0][3].figure.type = "queen";

		field[0][4].button.setIcon(black_king);
		field[0][4].figure.type = "king";

		field[0][5].button.setIcon(black_bishop);
		field[0][5].figure.type = "bishop";

		field[0][6].button.setIcon(black_knight);
		field[0][6].figure.type = "knight";

		field[0][7].button.setIcon(black_rook);
		field[0][7].figure.type = "rook";

		for (int i = 0; i < 8; i++) {
			field[1][i].button.setIcon(black_pawn);
			field[1][i].figure.type = "pawn";
			field[0][i].figure.team = 1;
			field[1][i].figure.team = 1;
		}

		field[7][0].button.setIcon(white_rook);
		field[7][0].figure.type = "rook";

		field[7][1].button.setIcon(white_knight);
		field[7][1].figure.type = "knight";

		field[7][2].button.setIcon(white_bishop);
		field[7][2].figure.type = "bishop";

		field[7][3].button.setIcon(white_queen);
		field[7][3].figure.type = "queen";

		field[7][4].button.setIcon(white_king);
		field[7][4].figure.type = "king";

		field[7][5].button.setIcon(white_bishop);
		field[7][5].figure.type = "bishop";

		field[7][6].button.setIcon(white_knight);
		field[7][6].figure.type = "knight";

		field[7][7].button.setIcon(white_rook);
		field[7][7].figure.type = "rook";

		for (int i = 0; i < 8; i++) {
			field[6][i].button.setIcon(white_pawn);
			field[6][i].figure.type = "pawn";
			field[6][i].figure.team = -1;
			field[7][i].figure.team = -1;
		}
		stage = 0;
		turn = -1;
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				final int x = i, y = k;
				field[i][k].button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						lblMessages.setText("Да играем!");
						lblMessages.setForeground(Color.BLACK);
						if (checkmate()) {
							checkmate();
							return;
						}
						if (stalemate(turn)) {
							stalemate(turn);
							return;
						}
						if (stage == 0) {
							if (field[x][y].figure.team != turn) {
								if (turn == -1) {
									lblMessages.setText("Белите са на ход!");
									lblMessages.setForeground(Color.RED);
								} else {
									lblMessages.setText("Черните са на ход!");
									lblMessages.setForeground(Color.RED);
								}
								return;
							}
							movex = x;
							movey = y;
							stage = 1;
							lblMessages.setText("Да играем!");
							lblMessages.setForeground(Color.BLACK);
						} else if (stage == 1) {
							if (field[x][y].figure.team == turn) {
								movex = x;
								movey = y;
								return;
							}
							if ((!field[movex][movey].figure.validMove(movex, movey, x, y, field[x][y].figure.team))
									|| !(jumpOverFigures(movex, movey, x, y))) {
								lblMessages.setText("Невалиден ход!");
								lblMessages.setForeground(Color.RED);
								return;
							}
							
							moveFigure(movex, movey, x, y);
							saverIcon = field[movex][movey].button.getIcon();
							saverFigure = field[movex][movey].figure;
							saverTeam = field[movex][movey].figure.team;
							eraseFigure(movex, movey);
								
							if (check() == true && field[xKiller][yKiller].figure.team != turn) {
								moveFigure(x, y, movex, movey);
								if(saverTeam != 0) {
									field[x][y].button.setIcon(saverIcon);
									field[x][y].figure = saverFigure;
								}
								lblMessages.setText("Невалиден ход - в шах си!");
								lblMessages.setForeground(Color.RED);
								stage = 0;
								return;
							} 
							if (saverTeam == -1) {
								JLabel lblKilled = new JLabel("");
								lblKilled.setBounds((n1 % 8) * 75, 125 - (n1 / 8) * 75, 75, 75);
								lblKilled.setIcon(saverIcon);
								panel_Player1.add(lblKilled);
								panel_Player1.revalidate();
								panel_Player1.repaint();
								n1 = n1 + 1;
							} else if (saverTeam == 1){
								JLabel lblKilled = new JLabel("");
								lblKilled.setBounds((n2 % 8) * 75, 0 + (n2 / 8) * 75, 75, 75);
								lblKilled.setIcon(saverIcon);
								panel_Player2.add(lblKilled);
								panel_Player2.revalidate();
								panel_Player2.repaint();
								n2 = n2 + 1;
							}
								saverTeam = 0;
							if (field[x][y].figure.type == "pawn" && (x == 0 || x == 7)) {
								field[x][y].figure.type = "queen";
								if (field[x][y].figure.team == -1)
									field[x][y].button.setIcon(white_queen);
								else
									field[x][y].button.setIcon(black_queen);
							}
							if(check()) {
								check();
							} else {
								lblMessages.setText("Да играем!");
								lblMessages.setForeground(Color.BLACK);
							}
							turn = turn * (-1);
							if (turn == -1) {
								lblPlayer1.setFont(new Font("Tahoma", Font.BOLD, 50));
								lblPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 50));
							} else {
								lblPlayer1.setFont(new Font("Tahoma", Font.PLAIN, 50));
								lblPlayer2.setFont(new Font("Tahoma", Font.BOLD, 50));
							}
							stage = 0;
						}
					}
				});
			}
		}
	}
}