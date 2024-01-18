package bg_smg;

public class Figure {

	public int team; // -1 or 1
	public String type;

	Figure() {

	}

	Figure(int team, String type) {
		this.team = team;
		this.type = type;
	}

	public boolean validMove(int a, int b, int c, int d, int teamCD) {
		if (type == "king") {
			return Math.max(Math.abs(a - c), Math.abs(b - d)) == 1;
		} else if (type == "queen") {
			return a + b == c + d || a - b == c - d || a == c || b == d;
		} else if (type == "rook") {
			return a == c || b == d;
		} else if (type == "bishop") {
			return a + b == c + d || a - b == c - d;
		} else if (type == "knight") {
			return (Math.abs(a - c) == 2 && Math.abs(b - d) == 1) || (Math.abs(a - c) == 1 && Math.abs(b - d) == 2);
		} else if (type == "pawn") {
			if (b == d) {
				if (a + team == c && teamCD == 0)
					return true;
				if ((a == 1 || a == 6) && a + 2 * team == c && teamCD == 0)
					return true;
			} else {
				return a + team == c && Math.abs(b - d) == 1 && teamCD != 0;
			}
		}

		return false;
	}

}