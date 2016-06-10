//Class to handle Human Opponent

package loa;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class HumanPlayer extends JFrame{
	private Container pane;
	Values values;
	private static int flag=0,index_flag=0, player_flag, move_flag=0,valid_moveflag=0,opposite_player_flag;
	private static JLabel Piece;
	private static JPanel old_panel; 
	private JPanel[][] panels;
	static int rows,columns;
	static Color[][] oldcolours;
	
	private class Values{
		private int[][] array;
		private int[][] validmove;
		private int[][] visitedarray;
		private int row,column;
		private Values(int rows,int columns)
		{
			row = rows;
			column = columns;
			array = new int[row][column];
			validmove = new int[row][column];
			visitedarray = new int[row][column];
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
				{
					array[i][j] = 0;
					validmove[i][j] = 0;
					visitedarray[i][j] = 0;
				}
			}
		}
		
		public void setValidMovestoZero()
		{
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
				{
					validmove[i][j] = 0;
					visitedarray[i][j] = 0;
				}
			}
		}
		
		public void setValidMoves(int r,int c)
		{
			int Vcount = 0, Hcount = 0, DcountRight = 0, DcountLeft = 0;
			int Vmoveup = 0, Vmovedown = 0;
			int Hmoveright = 0, Hmoveleft = 0;
			
			//Set Vertical Moves
			for(int i=0;i<row;i++)
			{
				if(array[i][c]!=0)
					Vcount++;
			}
			Vmoveup = r - Vcount;
			if((Vmoveup)>=0)
			{
				int jump_flag = 0;
				for(int i=r;i>Vmoveup;i--)
				{
					if(array[i][c]==opposite_player_flag)
						jump_flag = 1;
				}
				if(jump_flag == 0)
				{
					if(array[Vmoveup][c]!=player_flag)
						validmove[Vmoveup][c] = 1;
				}
			}
			Vmovedown = r + Vcount;
			if((Vmovedown)<=(row-1))
			{
				int jump_flag = 0;
				for(int i=r;i<Vmovedown;i++)
				{
					if(array[i][c]==opposite_player_flag)
						jump_flag = 1;
				}
				if(jump_flag == 0)
				{
					if(array[Vmovedown][c]!=player_flag)
						validmove[Vmovedown][c] = 1;
				}
			}
			
			//Set Horizontal Valid Moves
			for(int j=0;j<column;j++)
			{
				if(array[r][j]!=0)
					Hcount++;
			}
			Hmoveleft = c - Hcount;
			if((Hmoveleft)>=0)
			{
				int jump_flag = 0;
				for(int i=c;i>Hmoveleft;i--)
				{
					if(array[r][i]==opposite_player_flag)
						jump_flag = 1;
				}
				if(jump_flag==0)
				{
					if(array[r][Hmoveleft]!=player_flag)
						validmove[r][Hmoveleft] = 1;
				}
			}
			Hmoveright = c + Hcount;
			if((Hmoveright)<=(column-1))
			{
				int jump_flag = 0;
				for(int i=c;i<Hmoveright;i++)
				{
					if(array[r][i]==opposite_player_flag)
						jump_flag = 1;
				}
				if(jump_flag == 0)
				{
					if(array[r][Hmoveright]!=player_flag)
						validmove[r][Hmoveright] = 1;
				}
			}
			
			//Set Diagonal Valid Moves
			int k=c;
			for(int i=r;i<row;i++)
			{
				if(k>(column-1))
					break;
				if(array[i][k]!=0)
					DcountRight++;
				k++;
			}
			k=c-1;
			for(int i=(r-1);i>=0;i--)
			{
				if(k<0)
					break;
				if(array[i][k]!=0)
					DcountRight++;
				k--;
			}
			int row_down = r+DcountRight;
			int column_right = c+DcountRight;
			if((row_down)<=(row-1)&&(column_right)<=(column-1))
			{
				int jump_flag = 0,z=c;
				for(int i=r;i<row_down;i++)
				{
					if(z==column_right)
						break;
					if(array[i][z]==opposite_player_flag)
						jump_flag = 1;
					z++;
				}
				if(jump_flag == 0)
				{
					if(array[row_down][column_right]!=player_flag)
						validmove[row_down][column_right] = 1;
				}
			}
			int row_up = r-DcountRight;
			int column_left = c-DcountRight;
			if((row_up)>=0&&(column_left)>=0)
			{
				int jump_flag =0,z=c;
				for(int i=r;i>row_up;i--)
				{
					if(z==column_left)
						break;
					if(array[i][z]==opposite_player_flag)
						jump_flag = 1;
					z--;
				}
				if(jump_flag==0)
				{
					if(array[row_up][column_left]!=player_flag)
						validmove[row_up][column_left] = 1;
				}
			}
			k=c;
			for(int i=r;i>=0;i--)
			{
				if(k>(column-1))
					break;
				if(array[i][k]!=0)
					DcountLeft++;
				k++;
			}
			k=c-1;
			for(int i=r+1;i<row;i++)
			{
				if(k<0)
					break;
				if(array[i][k]!=0)
					DcountLeft++;
				k--;
			}
			int rowdown = r+DcountLeft;
			int columnleft = c-DcountLeft;
			if((rowdown)<=(row-1)&&(columnleft)>=0)
			{
				int jump_flag =0,z=c;
				for(int i=r;i<rowdown;i++)
				{
					if(z==columnleft)
						break;
					if(array[i][z]==opposite_player_flag)
						jump_flag = 1;
					z--;
				}
				if(jump_flag==0)
				{
					if(array[rowdown][columnleft]!=player_flag)
						validmove[rowdown][columnleft] = 1;
				}
			}
			int rowup = r-DcountLeft;
			int columnright = c+DcountLeft;
			if((rowup)>=0&&(columnright)<=(column-1))
			{
				int jump_flag =0,z=c;
				for(int i=r;i>rowup;i--)
				{
					if(z==columnright)
						break;
					if(array[i][z]==opposite_player_flag)
						jump_flag = 1;
					z++;
				}
				if(jump_flag==0)
				{
					if(array[rowup][columnright]!=player_flag)
						validmove[rowup][columnright] = 1;
				}
			}
		}
		
		//Check Region Connectivity
		public void CheckRegionConnect(int r,int c)
		{
			if(r<0||r>(row-1))
				return;
			if(c<0||c>(column-1))
				return;
			if(array[r][c]==player_flag&&visitedarray[r][c]!=player_flag)
			{
				visitedarray[r][c] = player_flag;
			    CheckRegionConnect(r+1,c);
			    CheckRegionConnect(r-1,c);
				CheckRegionConnect(r,c+1);
				CheckRegionConnect(r,c-1);
				CheckRegionConnect(r+1,c+1);
				CheckRegionConnect(r+1,c-1);
				CheckRegionConnect(r-1,c+1);
				CheckRegionConnect(r-1,c-1);
			}
		}
		
		//Check Win condition
		public int CheckWin()
		{
			int countarray = 0, countvisitedarray = 0;
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
				{
					if(array[i][j]==player_flag)
						countarray++;
					if(visitedarray[i][j]==player_flag)
						countvisitedarray++;
				}
			}
			if(countarray==countvisitedarray)
				return 1;
			else
				return 0;
		}
	}
	
	public HumanPlayer(int r, int player)
	{
		rows = r;
		columns = r;
		player_flag = player;
		if(player_flag==1)
			opposite_player_flag = 2;
		else
			opposite_player_flag = 1;
		Color c1 = new Color(148,148,148);
		Color c2 = new Color(223,223,233);
		this.setTitle("Lines of Action");
		this.setSize(600, 600);
		pane = this.getContentPane();
		pane.setLayout(new GridLayout(rows,columns));
		panels = new JPanel[rows][columns];
		values = new Values(rows,columns);
		oldcolours = new Color[rows][columns];
		
		Color temp;
		for(int i=0;i<rows;i++)
		{
			if(i%2==0)
				temp = c1;
			else
				temp = c2;
			for(int j=0;j<columns;j++)
			{
				panels[i][j] = new JPanel();
				panels[i][j].setBackground(temp);
				oldcolours[i][j] = temp;
				if(temp.equals(c1))
					temp = c2;
				else
					temp = c1;
				if((i==0&&j>0&&j<columns-1)||(i==rows-1&&j>0&&j<columns-1))
				{
					JLabel BlackPiece = new JLabel( new ImageIcon("Black_Piece.png") );
					panels[i][j].add(BlackPiece);
					values.array[i][j] = 1;
				}
				else if((j==0&&i>0&&i<rows-1)||(j==columns-1&&i>0&&i<rows-1))
				{
					JLabel WhitePiece = new JLabel( new ImageIcon("White_Piece.png") );
					panels[i][j].add(WhitePiece);
					values.array[i][j] = 2;
				}
				panels[i][j].addMouseListener(new MouseHandler());
				pane.add(panels[i][j]);
			}
		}
		addWindowListener(new WindowHandler());
		setVisible(true);
		setLocation(400, 150);
	}
	
	//Handle Mouse Events
	private class MouseHandler extends MouseAdapter
	{
		 public void mousePressed(MouseEvent e) {
			 JPanel panel = (JPanel) e.getComponent();
			 if(flag==0)
			 {
				 if(panel.getComponentCount()!=0)
				 {
					 for(int i=0;i<rows;i++)
					 {
						 for(int j=0;j<columns;j++)
						 {
							 if(panels[i][j]==panel&&values.array[i][j]==player_flag)
							 {
								 index_flag = values.array[i][j];
								 values.setValidMoves(i, j);
								 values.array[i][j] = 0;
								 move_flag = 1;
							 }
						 }
					 }
					 if(move_flag==1)
					 {
						 for(int i=0;i<rows;i++)
						 {
							 for(int j=0;j<columns;j++)
							 {
								 if(values.validmove[i][j]==1)
									 panels[i][j].setBackground(Color.RED);
							 }
						 }
						 Piece = (JLabel) panel.getComponent(0);
						 panel.setBackground(Color.YELLOW);
						 old_panel = panel;
						 flag = 1;
						 move_flag = 0;
					 }
				 }
			 }
			 else if(flag==1)
			 {
				 if(panel==old_panel)
				 {
					 for(int i=0;i<rows;i++)
					 {
						 for(int j=0;j<columns;j++)
						 {
							 if(panels[i][j]==panel)
								 values.array[i][j] = player_flag;
						 }
					 }
					 for(int i=0;i<rows;i++)
					 {
					 	for(int j=0;j<columns;j++)
					 		panels[i][j].setBackground(oldcolours[i][j]);
				     }
					 flag = 0;
					 values.setValidMovestoZero();
				 }
				 else
				 {
					 int r=0,c=0;
					 if(Piece!=null)
					 {
						 for(int i=0;i<rows;i++)
						 {
							 for(int j=0;j<columns;j++)
							 {
								 if(panels[i][j]==panel&&values.validmove[i][j]==1)
								 {
									 r=i;
									 c=j;
									 values.array[i][j] = index_flag;
									 values.CheckRegionConnect(i, j);
									 index_flag = 0;
									 valid_moveflag = 1;
								 }
							 }
						 }
						 if(valid_moveflag==1)
						 {
							if(panel.getComponentCount()!=0)
							{
								panel.removeAll();
								values.array[r][c] = player_flag;
							}
							old_panel.remove(Piece);
						 	panel.add(Piece);
						 	flag = 0;
						 	valid_moveflag = 0;
						 	for(int i=0;i<rows;i++)
						 	{
						 		for(int j=0;j<columns;j++)
						 			panels[i][j].setBackground(oldcolours[i][j]);
						 	}
						 	if(values.CheckWin()==1)
						 	{
						 		for(int i=0;i<rows;i++)
						 		{
						 			for(int j=0;j<columns;j++)
						 			{
						 				if(values.visitedarray[i][j]!=0)
						 					panels[i][j].setBackground(Color.green);
						 			}
						 		}
						 		Object[] options = {"Exit"};
						 		ImageIcon icon = new ImageIcon("Winner.jpg");
						 		int n = JOptionPane.showOptionDialog(panels[rows/2][columns/2],
						 			    "You Won!!",
						 			    "Game Over",
						 			    JOptionPane.YES_NO_OPTION,
						 			    JOptionPane.QUESTION_MESSAGE,
						 			    icon,     //do not use a custom Icon
						 			    options,  //the titles of buttons
						 			    options[0]); //default button title
						 		if(n==0)
						 			System.exit(0);
						 	}
						 	System.out.println("Waiting for Player "+opposite_player_flag);
						 	if(player_flag==1)
						 	{
						 		player_flag = 2;
						 		opposite_player_flag = 1;
						 	}
						 	else
						 	{
						 		player_flag = 1;
						 		opposite_player_flag = 2;
						 	}
						 	values.setValidMovestoZero();
						 }
					 }
				 }
			 } 
			 revalidate();
			 repaint();
		 }
	}
	
	//Handle Window Closing
	private class WindowHandler extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent arg0) {
			System.exit(0);
		}
	}
}