//Main Class: Call constructors of other classes

package loa;

public class Main {
	public static void main(String[] args) {
		int player1, board, difficulty, player2;
		SelectPlayer player = new SelectPlayer();
		player.display();
		player1 = SelectPlayer.retPlayer();
		if (player1 == 1)
		{
			SetAttributes myAttributes = new SetAttributes();
			myAttributes.display();
			board = SetAttributes.retBoard();
			difficulty = SetAttributes.retDifficulty();
			player2 = SetAttributes.retPlayer();
			MachinePlayer game = new MachinePlayer(board, player2,difficulty);
		}
		if (player1 == 2)
		{
			SelectBoard myBoard = new SelectBoard();
			myBoard.display();
			board = SelectBoard.retBoard();
			HumanPlayer game = new HumanPlayer(board, 1);
		}
	}
}