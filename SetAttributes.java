//Class to initialize game: Board type (5x5 / 6x6), Difficulty Level (Easy / Medium / Hard), Player (Black / White)

package loa;

import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.*;

public class SetAttributes {
	static int bo;
	static int di;
	static int pl;
	public static void display() {
        String[] board = {"5x5", "6x6"};
        String[] difficulty = {"Easy", "Medium", "Hard"};
        String[] player = {"Black", "White"};
        JComboBox combo1 = new JComboBox(board);
        JComboBox combo2 = new JComboBox(difficulty);
        JComboBox combo3 = new JComboBox(player);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Board Layout:"));
        panel.add(combo1);
        panel.add(new JLabel("Select Difficulty Level:"));
        panel.add(combo2);
        panel.add(new JLabel("Select Player:"));
        panel.add(combo3);
        int result = JOptionPane.showConfirmDialog(null, panel, "Lines of Action : New Game",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if(combo1.getSelectedItem().equals("5x5"))
            	bo = 5;
            else
            	bo = 6;
            if(combo2.getSelectedItem().equals("Easy"))
            	di = 2;
            else if(combo2.getSelectedItem().equals("Medium"))
            	di = 4;
            else
            	di = 8;
            if(combo3.getSelectedItem().equals("Black"))
            	pl = 1;
            else
            	pl = 2;
            
        } else {
            System.out.println("Cancelled");
        }
    }
	public static int retBoard(){
		return bo;
	}
	public static int retDifficulty(){
		return di;
	}
	public static int retPlayer(){
		return pl;
	}
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                display();
            }
        });
    }
}