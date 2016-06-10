//Class to select board type: 5x5 / 6x6

package loa;

import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.*;

public class SelectBoard {
	static int bo;
	public static void display() {
        String[] player = {"5x5", "6x6"};
        JComboBox combo1 = new JComboBox(player);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Board Layout:"));
        panel.add(combo1);
        int result = JOptionPane.showConfirmDialog(null, panel, "Lines of Action : New Game",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if(combo1.getSelectedItem().equals("5x5"))
            	bo = 5;
            else
            	bo = 6;
            
        } else {
            System.out.println("Cancelled");
        }
    }
	public static int retBoard(){
		return bo;
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
