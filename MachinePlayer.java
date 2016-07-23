//Class to handle Computer Opponent

package loa;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MachinePlayer extends JFrame{
	
	public Container pane;
	ValueClass values;
	public static int flag=0,index_flag=0,player_flag,move_flag=0,valid_moveflag=0,opposite_player_flag,temp_flag,computer_move_flag=0,difficulty_level=0;
	public static JLabel Piece;
	public static JPanel old_panel; 
	public JPanel[][] panels;
	static int rows,columns;
	static Color[][] oldcolours;
	String UserPiece,OppositePiece;
	Search Searchtree;
	
	public class ValueClass{
		public int[][] array;
		public int[][] validmove;
		public int[][] visitedarray;
		public int row,column;
		public ValueClass(int rows,int columns)
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
		
		public void printValidMoves()
		{
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
					System.out.print(validmove[i][j]+" ");
				System.out.println();
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
			
			//Set Vertical Valid Moves
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
		public void CheckRegionConnect(int r,int c,int PieceFlag)
		{
			if(r<0||r>(row-1))
				return;
			if(c<0||c>(column-1))
				return;
			if(array[r][c]==PieceFlag&&visitedarray[r][c]!=PieceFlag)
			{
				visitedarray[r][c] = PieceFlag;
			    CheckRegionConnect(r+1,c,PieceFlag);
			    CheckRegionConnect(r-1,c,PieceFlag);
				CheckRegionConnect(r,c+1,PieceFlag);
				CheckRegionConnect(r,c-1,PieceFlag);
				CheckRegionConnect(r+1,c+1,PieceFlag);
				CheckRegionConnect(r+1,c-1,PieceFlag);
				CheckRegionConnect(r-1,c+1,PieceFlag);
				CheckRegionConnect(r-1,c-1,PieceFlag);
			}
		}
		
		//Check Win condition
		public int CheckWin(int PieceFlag)
		{
			int countarray = 0, countvisitedarray = 0;
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
				{
					if(array[i][j]==PieceFlag)
						countarray++;
					if(visitedarray[i][j]==PieceFlag)
						countvisitedarray++;
				}
			}
			if(countarray==countvisitedarray)
				return 1;
			else
				return 0;
		}
		public void printVisitedArray()
		{
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<column;j++)
					System.out.print(visitedarray[i][j]+" ");
				System.out.println();
			}
		}
	}
	
	public MachinePlayer(int r,int b_or_w,int difficulty)
	{
		rows=r;
		columns=r;
		player_flag = b_or_w;
		if(player_flag==1)
		{
			opposite_player_flag = 2;
			UserPiece = new String("Black Piece");
			OppositePiece = new String("White Piece");
		}
		else
		{
			opposite_player_flag = 1;
			UserPiece = new String("White Piece");
			OppositePiece = new String("Black Piece");
		}
		difficulty_level = difficulty;
		Color c1 = new Color(148,148,148);
		Color c2 = new Color(223,223,233);
		this.setTitle("Lines of Action");
		this.setSize(600, 600);
		pane = this.getContentPane();
		pane.setLayout(new GridLayout(rows,columns));
		panels = new JPanel[rows][columns];
		values = new ValueClass(rows,columns);
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
		if(player_flag==2)
		{
			Search SearchTree = new Search(rows,columns,values.array,opposite_player_flag,0,0,-Integer.MIN_VALUE,Integer.MAX_VALUE,difficulty_level);
		 	SearchTree.AlphaBetaSearch(rows, columns);
		 	SearchTree.clearoutput();
		 	makemove();
		}
	}
	
	//Handle Mouse Events
	public class MouseHandler extends MouseAdapter
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
									 values.CheckRegionConnect(i, j,player_flag);
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
						 	computer_move_flag = 1;
						 	for(int i=0;i<rows;i++)
						 	{
						 		for(int j=0;j<columns;j++)
						 			panels[i][j].setBackground(oldcolours[i][j]);
						 	}
						 	
						 	//Check Win for current player
						 	if(values.CheckWin(player_flag)==1)
						 	{
						 		for(int i=0;i<rows;i++)
						 		{
						 			for(int j=0;j<columns;j++)
						 			{
						 				if(values.visitedarray[i][j]!=0)
						 					panels[i][j].setBackground(Color.GREEN);
						 			}
						 		}
						 		String temp;
						 		if(player_flag == 1)
						 			temp = new String("Black Piece");
						 		else
						 			temp = new String("White Piece");
						 		Object[] options = {"Exit"};
						 		ImageIcon icon = new ImageIcon("Winner.jpg");
						 		int n = JOptionPane.showOptionDialog(panels[rows/2][columns/2],
						 			    "Player with "+temp+" Wins!!",
						 			    "Game Over",
						 			    JOptionPane.YES_NO_OPTION,
						 			    JOptionPane.QUESTION_MESSAGE,
						 			    icon,     //do not use a custom Icon
						 			    options,  //the titles of buttons
						 			    options[0]); //default button title
						 		if(n==0)
						 		{
						 			dispose();
						 			System.exit(0);
						 		}
						 	}
						 	
						 	//Check Win for opponent player
						 	values.setValidMovestoZero();
						 	int temp_count = 0;
						 	for(int i=0;i<rows;i++)
						 	{
						 		for(int j=0;j<columns;j++)
						 		{
						 			if(values.array[i][j]==opposite_player_flag&&temp_count==0)
						 			{
						 				temp_count++;
						 				values.CheckRegionConnect(i, j, opposite_player_flag);
						 			}
						 		}
						 	}
						 	if(values.CheckWin(opposite_player_flag)==1)
						 	{
						 		for(int i=0;i<rows;i++)
						 		{
						 			for(int j=0;j<columns;j++)
						 			{
						 				if(values.visitedarray[i][j]!=0)
						 					panels[i][j].setBackground(Color.GREEN);
						 			}
						 		}
						 		String temp;
						 		if(opposite_player_flag == 1)
						 			temp = new String("Black Piece");
						 		else
						 			temp = new String("White Piece");
						 		Object[] options = {"Exit"};
						 		ImageIcon icon = new ImageIcon("Winner.jpg");
						 		int n = JOptionPane.showOptionDialog(panels[rows/2][columns/2],
						 			    "Player with "+temp+" Wins!!",
						 			    "Game Over",
						 			    JOptionPane.YES_NO_OPTION,
						 			    JOptionPane.QUESTION_MESSAGE,
						 			    icon,     //do not use a custom Icon
						 			    options,  //the titles of buttons
						 			    options[0]); //default button title
						 		if(n==0)
						 		{
						 			dispose();
						 			System.exit(0);
						 		}
						 	}
						 	values.setValidMovestoZero();
						 }
					 }
				 }
			 } 
			 revalidate();
			 repaint();
			 if(computer_move_flag==1)
			 {
				 Search SearchTree = new Search(rows,columns,values.array,opposite_player_flag,0,0,Integer.MIN_VALUE,Integer.MAX_VALUE,difficulty_level);
				 SearchTree.AlphaBetaSearch(rows, columns);
				 SearchTree.clearoutput();
				 makemove();
			 }
		 }
	}
	
	//AI Move Method
	public void makemove()
	{
		int row1=0,column1=0,row2=0,column2=0,count=0;
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				if(values.array[i][j]!=Search.choosearray[i][j])
				{
					if(count==0)
					{
						row1 = i;
						column1 = j;
						count++;
					}
					else
					{
						row2 = i;
						column2 = j;
					}
				}
			}
		}
		values.array[row1][column1] = Search.choosearray[row1][column1];
		values.array[row2][column2] = Search.choosearray[row2][column2];
		
		if(values.array[row1][column1]==0)
		{
			if(panels[row1][column1].getComponentCount()!=0)
				panels[row1][column1].remove(0);
		}
		else if(values.array[row1][column1]==1)
		{
			if(panels[row1][column1].getComponentCount()!=0)
				panels[row1][column1].remove(0);
				JLabel BlackPiece = new JLabel( new ImageIcon("Black_Piece.png") );
				panels[row1][column1].add(BlackPiece);
		}
		else
		{
			if(panels[row1][column1].getComponentCount()!=0)
				panels[row1][column1].remove(0);
			JLabel WhitePiece = new JLabel( new ImageIcon("White_Piece.png") );
			panels[row1][column1].add(WhitePiece);
		}
		
		if(values.array[row2][column2]==0)
		{
			if(panels[row2][column2].getComponentCount()!=0)
				panels[row2][column2].remove(0);
		}
		else if(values.array[row2][column2]==1)
		{
			if(panels[row2][column2].getComponentCount()!=0)
				panels[row2][column2].remove(0);
			JLabel BlackPiece = new JLabel( new ImageIcon("Black_Piece.png") );
			panels[row2][column2].add(BlackPiece);
		}
		else
		{
			if(panels[row2][column2].getComponentCount()!=0)
				panels[row2][column2].remove(0);
			JLabel WhitePiece = new JLabel( new ImageIcon("White_Piece.png") );
			panels[row2][column2].add(WhitePiece);
		}
		
		//Check Win for Human player
	 	values.setValidMovestoZero();
	 	int temp_count = 0;
	 	for(int i=0;i<rows;i++)
	 	{
	 		for(int j=0;j<columns;j++)
	 		{
	 			if(values.array[i][j]==player_flag&&temp_count==0)
	 			{
	 				temp_count++;
	 				values.CheckRegionConnect(i, j, player_flag);
	 			}
	 		}
	 	}
	 	if(values.CheckWin(player_flag)==1)
	 	{
	 		for(int i=0;i<rows;i++)
	 		{
	 			for(int j=0;j<columns;j++)
	 			{
	 				if(values.visitedarray[i][j]!=0)
	 					panels[i][j].setBackground(Color.GREEN);
	 			}
	 		}
	 		String temp;
	 		if(player_flag == 1)
	 			temp = new String("Black Piece");
	 		else
	 			temp = new String("White Piece");
	 		Object[] options = {"Exit"};
	 		ImageIcon icon = new ImageIcon("Winner.jpg");
	 		int n = JOptionPane.showOptionDialog(panels[rows/2][columns/2],
	 			    "Player with "+temp+" Wins!!",
	 			    "Game Over",
	 			    JOptionPane.YES_NO_OPTION,
	 			    JOptionPane.QUESTION_MESSAGE,
	 			    icon,     //do not use a custom Icon
	 			    options,  //the titles of buttons
	 			    options[0]); //default button title
	 		if(n==0)
	 		{
	 			dispose();
	 		}
	 		else
	 			System.exit(0);
	 	}
	 	//Check Win for opponent player
	 	values.setValidMovestoZero();
	 	int temp_count2 = 0;
	 	for(int i=0;i<rows;i++)
	 	{
	 		for(int j=0;j<columns;j++)
	 		{
	 			if(values.array[i][j]==opposite_player_flag&&temp_count2==0)
	 			{
	 				temp_count2++;
	 				values.CheckRegionConnect(i, j, opposite_player_flag);
	 			}
	 		}
	 	}
	 	if(values.CheckWin(opposite_player_flag)==1)
	 	{
	 		for(int i=0;i<rows;i++)
	 		{
	 			for(int j=0;j<columns;j++)
	 			{
	 				if(values.visitedarray[i][j]!=0)
	 					panels[i][j].setBackground(Color.GREEN);
	 			}
	 		}
	 		String temp;
	 		if(opposite_player_flag == 1)
	 			temp = new String("Black Piece");
	 		else
	 			temp = new String("White Piece");
	 		Object[] options = {"Exit"};
	 		ImageIcon icon = new ImageIcon("Winner.jpg");
	 		int n = JOptionPane.showOptionDialog(panels[rows/2][columns/2],
	 			    "Player with "+temp+" Wins!!",
	 			    "Game Over",
	 			    JOptionPane.YES_NO_OPTION,
	 			    JOptionPane.QUESTION_MESSAGE,
	 			    icon,     //do not use a custom Icon
	 			    options,  //the titles of buttons
	 			    options[0]); //default button title
	 		if(n==0)
	 		{
	 			dispose();
	 			System.exit(0);
	 		}
	 	}
	 	
		revalidate();
		repaint();
		computer_move_flag = 0;
	}
	//Handle Window Closing
	public class WindowHandler extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
	}
}
