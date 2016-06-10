//Class to select opponent: Computer / Human

package loa;

import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.*;

public class SelectPlayer {
	static int pl;
	public static void display() {
        String[] player = {"Computer", "Human"};
        JComboBox combo1 = new JComboBox(player);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Opponent:"));
        panel.add(combo1);
        int result = JOptionPane.showConfirmDialog(null, panel, "Lines of Action : New Game",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if(combo1.getSelectedItem().equals("Computer"))
            	pl = 1;
            else
            	pl = 2;
            
        } else {
            System.out.println("Cancelled");
        }
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