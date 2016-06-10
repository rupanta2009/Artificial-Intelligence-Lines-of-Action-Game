//Class to perform search based on Alpha-Beta Pruning Algorithm

package loa;

import java.util.ArrayList;

public class Search {

	public int flag,opposite_flag;
	static int root_instance,node_count=0,computer_flag,maxvalue=0,maxdepth,count_eval,count_prune_max,count_prune_min,difficulty_level;
	public int[][] dataarray;
	public int[][] board_array;
	public int minimax,terminal_value,depth,node_value,alpha,beta;
	static int[][] visitedarray,choosearray;
	static int[] EvalMaxArray,EvalMinArray;
	static int temp_node_count, temp_node_value;
	static ArrayList<Search> firstlevelchildren;
	
	public Search(int row,int column,int[][] array,int comp_flag,int depthofnode,int terminal_state,int alpha,int beta,int level_cutoff)
	{
		this.flag = comp_flag;
		if(this.flag==1)
			this.opposite_flag = 2;
		else
			this.opposite_flag = 1;
		this.depth = depthofnode;
		this.dataarray = new int[row][column];
		this.board_array = new int[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				board_array[i][j] = array[i][j];
				dataarray[i][j] = array[i][j];
			}
		}
		if(root_instance==0)
		{
			computer_flag = flag;
			visitedarray = new int[row][column];
			choosearray = new int[row][column];
			EvalMaxArray = new int[8];
			EvalMinArray = new int[8];
			int tempeval = 100,tempeval2 = -100;
			for(int i=0;i<8;i++)
			{
				EvalMaxArray[i] = tempeval;
				EvalMinArray[i] = tempeval2;
				tempeval= tempeval - 10;
				tempeval2= tempeval2 + 10;
			}
			firstlevelchildren = new ArrayList<Search>();
			difficulty_level = level_cutoff;
			root_instance++;
		}
		if(flag == computer_flag)
			minimax = 1;                 //Max Player
		else
			minimax = -1;                //Min Player
		terminal_value = terminal_state;         //Set the utility values for a state
		this.alpha = alpha;
		this.beta = beta;
		node_count++;
		if(this.depth==1)
			firstlevelchildren.add(this);
		//System.out.println(node_count);
	}
	
	//Alpha-Beta Search Algorithm
	public void AlphaBetaSearch(int row,int column)
	{
		long startTime = System.currentTimeMillis();
		this.node_value = MaxValue(row,column,this);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Value: "+this.node_value);
		printvalues();
		System.out.println();
	}
	
	//Max Funtion
	public static int MaxValue(int row,int column,Search node)
	{
		if(node.depth>maxdepth)
			maxdepth = node.depth;
		if(node.terminal_value!=0)
			return node.terminal_value;
		if(node.depth>=difficulty_level)
			return node.Eval(row, column);
		node.node_value = Integer.MIN_VALUE;
		
		int[][] validmoves = new int[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				if(node.board_array[i][j]==node.flag)
				{
					validmoves = node.setValidMoves(i,j,row,column);
					for(int k=0;k<row;k++)
					{
						for(int z=0;z<column;z++)
						{
							if(validmoves[k][z]!=0)
							{
								int temp = node.board_array[k][z];
								int temp2 = node.board_array[i][j];
								node.board_array[i][j] = 0;
								node.board_array[k][z] = validmoves[k][z];
								int tobesent,terminal_state=0;
								if(node.flag==1)
									tobesent = 2;
								else
									tobesent = 1;
							
								//Check Win for computer piece
								node.CheckRegionConnect(k,z,node.flag,row,column,node.board_array);
								if(node.CheckWin(node.flag,row,column,node.board_array)==1)
								{
									if(node.flag==computer_flag)     //Max Player Utility value
										terminal_state = 100;
									else
										terminal_state = -100;       //Min Player Utility value
								}
							
								//Check win for opposition piece
								node.setVisitedtoZero(row,column);
								for(int q=0;q<row;q++)
								{
									int temp_flag = 0;
									for(int w=0;w<column;w++)
									{
										if(node.board_array[q][w]==node.opposite_flag)
										{
											temp_flag = 1;
											node.CheckRegionConnect(q,w,node.opposite_flag,row,column,node.board_array);
											break;
										}
									}
									if(temp_flag==1)
										break;
								}
								if(node.CheckWin(node.opposite_flag,row,column,node.board_array)==1)
								{
									if(node.opposite_flag==computer_flag)      //Max player utility value
										terminal_state = 100;
									else
										terminal_state = -100;                 //Min player utility value
								}
								node.setVisitedtoZero(row,column);
								if(node.depth==0)
								{
									if(temp_node_count==0)
									{
										temp_node_count++;
										temp_node_value = node.node_value;
									}
								}
								node.node_value = Math.max(node.node_value,MinValue(row,column,new Search(row,column,node.board_array,tobesent,node.depth+1,terminal_state,node.alpha,node.beta,0)));
								if(node.depth==0)
								{
									if(node.node_value>temp_node_value)
									{
										temp_node_value = node.node_value;
										for(int o=0;o<row;o++)
										{
											for(int p=0;p<column;p++)
												choosearray[o][p] = node.board_array[o][p];
										}
									}
								}
								if(node.node_value>=node.beta)
								{
									count_prune_max++;
									return node.node_value;
								}
								node.alpha = Math.max(node.alpha, node.node_value);
								node.board_array[k][z] = temp;
								node.board_array[i][j] = temp2;
							}
						}
					}
				}
			}
		}
		return node.node_value;
	}
	
	//Min Funtion
	public static int MinValue(int row,int column,Search node)
	{
		if(node.depth>maxdepth)
			maxdepth = node.depth;
		if(node.terminal_value!=0)
			return node.terminal_value;
		node.node_value = Integer.MAX_VALUE;
		int[][] validmoves = new int[row][column];
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				if(node.board_array[i][j]==node.flag)
				{	
					validmoves = node.setValidMoves(i,j,row,column);
					for(int k=0;k<row;k++)
					{
						for(int z=0;z<column;z++)
						{
							if(validmoves[k][z]!=0)
							{
								int temp = node.board_array[k][z];
								int temp2 = node.board_array[i][j];
								node.board_array[i][j] = 0;
								node.board_array[k][z] = validmoves[k][z];
								int tobesent,terminal_state=0;
								if(node.flag==1)
									tobesent = 2;
								else
									tobesent = 1;
							
								//Check Win for computer piece
								node.CheckRegionConnect(k,z,node.flag,row,column,node.board_array);
								if(node.CheckWin(node.flag,row,column,node.board_array)==1)
								{
									if(node.flag==computer_flag)
										terminal_state = 100;             //Max player utility value
									else
										terminal_state = -100;            //Min player utility value
								}
								
								//Check win for opposition piece
								node.setVisitedtoZero(row,column);
								for(int q=0;q<row;q++)
								{
									int temp_flag = 0;
									for(int w=0;w<column;w++)
									{	
										if(node.board_array[q][w]==node.opposite_flag)
										{
											temp_flag = 1;
											node.CheckRegionConnect(q,w,node.opposite_flag,row,column,node.board_array);
											break;
										}
									}
									if(temp_flag==1)
										break;
								}
								if(node.CheckWin(node.opposite_flag,row,column,node.board_array)==1)
								{
									if(node.opposite_flag==computer_flag)
										terminal_state = 100;                    //Max player utility value
									else
										terminal_state = -100;                   //Min player utility value
								}
								node.setVisitedtoZero(row,column);
							
								node.node_value = Math.min(node.node_value, MaxValue(row,column,new Search(row,column,node.board_array,tobesent,node.depth+1,terminal_state,node.alpha,node.beta,0)));
								if(node.node_value<=node.alpha)
								{
									count_prune_min++;
									return node.node_value;
								}
								node.beta = Math.min(node.beta, node.node_value);
								node.board_array[k][z] = temp;
								node.board_array[i][j] = temp2;
							}
						}
					}
				}
			}
		}
		return node.node_value;
	}
	
	//Evaluation function
	public int Eval(int row,int column)
	{
		count_eval++;
		setVisitedtoZero(row,column);
		int regioncountmax = 0,regioncountmin = 0;
		
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				if(this.board_array[i][j]==this.flag&&visitedarray[i][j]==0)
				{
					CheckRegionConnect(i,j,this.flag,row,column,this.board_array);
					regioncountmax++;
				}
			}
		}
		setVisitedtoZero(row,column);
		
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
			{
				if(this.board_array[i][j]==this.opposite_flag&&visitedarray[i][j]==0)
				{
					CheckRegionConnect(i,j,this.opposite_flag,row,column,this.board_array);
					regioncountmin++;
				}
			}
		}
		setVisitedtoZero(row,column);
		
		if(regioncountmax<regioncountmin)
			return EvalMaxArray[regioncountmax-1];
		else if(regioncountmin<regioncountmax)
			return EvalMinArray[regioncountmin - 1];
		else
			return 0;
	}
	
	//Set Valid Moves for a function
	public int[][] setValidMoves(int r,int c,int row,int column)
	{
		int Vcount = 0, Hcount = 0, DcountRight = 0, DcountLeft = 0;
		int Vmoveup = 0, Vmovedown = 0;
		int Hmoveright = 0, Hmoveleft = 0;
		int[][] validmove = new int[row][column];
		
	
		//Set Vertical Valid Moves
		for(int i=0;i<row;i++)
		{
			if(dataarray[i][c]!=0)
				Vcount++;
		}
		
		//Check Vertical up
		Vmoveup = r - Vcount;
		if((Vmoveup)>=0)
		{
			int jump_flag = 0;
			for(int i=r;i>Vmoveup;i--)
			{
				if(dataarray[i][c]==opposite_flag)
					jump_flag = 1;
			}
			if(jump_flag == 0)
			{
				if(dataarray[Vmoveup][c]!=flag)
					validmove[Vmoveup][c] = flag;
			}
		}
		
		//Check Vertical Down
		Vmovedown = r + Vcount;
		if((Vmovedown)<=(row-1))
		{
			int jump_flag = 0;
			for(int i=r;i<Vmovedown;i++)
			{
				if(dataarray[i][c]==opposite_flag)
					jump_flag = 1;
			}
			if(jump_flag == 0)
			{
				if(dataarray[Vmovedown][c]!=flag)
					validmove[Vmovedown][c] = flag;
			}
		}
		
		//Set Horizontal Valid Moves
		for(int j=0;j<column;j++)
		{
			if(dataarray[r][j]!=0)
				Hcount++;
		}
		
		//Check Horizontal left
		Hmoveleft = c - Hcount;
		if((Hmoveleft)>=0)
		{
			int jump_flag = 0;
			for(int i=c;i>Hmoveleft;i--)
			{
				if(dataarray[r][i]==opposite_flag)
					jump_flag = 1;
			}
			if(jump_flag==0)
			{
				if(dataarray[r][Hmoveleft]!=flag)
					validmove[r][Hmoveleft] = flag;
			}
		}
		
		//Check Horizontal right
		Hmoveright = c + Hcount;
		if((Hmoveright)<=(column-1))
		{
			int jump_flag = 0;
			for(int i=c;i<Hmoveright;i++)
			{
				if(dataarray[r][i]==opposite_flag)
					jump_flag = 1;
			}
			if(jump_flag == 0)
			{
				if(dataarray[r][Hmoveright]!=flag)
					validmove[r][Hmoveright] = flag;
			}
		}
		
		//Set Diagonal Valid Moves
		//Set One Diagonal
		int k=c;
		for(int i=r;i<row;i++)
		{
			if(k>(column-1))
				break;
			if(dataarray[i][k]!=0)
				DcountRight++;
			k++;
		}
		k=c-1;
		for(int i=(r-1);i>=0;i--)
		{
			if(k<0)
				break;
			if(dataarray[i][k]!=0)
				DcountRight++;
			k--;
		}
		
		//Check Diagonal down and right
		int row_down = r+DcountRight;
		int column_right = c+DcountRight;
		if((row_down)<=(row-1)&&(column_right)<=(column-1))
		{
			int jump_flag = 0,z=c;
			for(int i=r;i<row_down;i++)
			{
				if(z==column_right)
					break;
				if(dataarray[i][z]==opposite_flag)
					jump_flag = 1;
				z++;
			}
			if(jump_flag == 0)
			{
				if(dataarray[row_down][column_right]!=flag)
					validmove[row_down][column_right] = flag;
			}
		}
		
		//Check Diagonal up and left
		int row_up = r-DcountRight;
		int column_left = c-DcountRight;
		if((row_up)>=0&&(column_left)>=0)
		{
			int jump_flag =0,z=c;
			for(int i=r;i>row_up;i--)
			{
				if(z==column_left)
					break;
				if(dataarray[i][z]==opposite_flag)
					jump_flag = 1;
				z--;
			}
			if(jump_flag==0)
			{
				if(dataarray[row_up][column_left]!=flag)
					validmove[row_up][column_left] = flag;
			}
		}
		
		//Set Second Diagonal
		k=c;
		for(int i=r;i>=0;i--)
		{
			if(k>(column-1))
				break;
			if(dataarray[i][k]!=0)
				DcountLeft++;
			k++;
		}
		k=c-1;
		for(int i=r+1;i<row;i++)
		{
			if(k<0)
				break;
			if(dataarray[i][k]!=0)
				DcountLeft++;
			k--;
		}
		
		//Check Diagonal down and left
		int rowdown = r+DcountLeft;
		int columnleft = c-DcountLeft;
		if((rowdown)<=(row-1)&&(columnleft)>=0)
		{
			int jump_flag =0,z=c;
			for(int i=r;i<rowdown;i++)
			{
				if(z==columnleft)
					break;
				if(dataarray[i][z]==opposite_flag)
					jump_flag = 1;
				z--;
			}
			if(jump_flag==0)
			{
				if(dataarray[rowdown][columnleft]!=flag)
					validmove[rowdown][columnleft] = flag;
			}
		}
		
		//Check Diagonal Up and Right
		int rowup = r-DcountLeft;
		int columnright = c+DcountLeft;
		if((rowup)>=0&&(columnright)<=(column-1))
		{
			int jump_flag =0,z=c;
			for(int i=r;i>rowup;i--)
			{
				if(z==columnright)
					break;
				if(dataarray[i][z]==opposite_flag)
					jump_flag = 1;
				z++;
			}
			if(jump_flag==0)
			{
				if(dataarray[rowup][columnright]!=flag)
					validmove[rowup][columnright] = flag;
			}
		}
		return validmove;
	}
	
	//Check For region Connectivity
	public void CheckRegionConnect(int r,int c,int PieceFlag,int row,int column,int[][] array)
	{
		if(r<0||r>(row-1))
			return;
		if(c<0||c>(column-1))
			return;
		if(array[r][c]==PieceFlag&&visitedarray[r][c]!=PieceFlag)
		{
			visitedarray[r][c] = PieceFlag;
		    CheckRegionConnect(r+1,c,PieceFlag,row,column,array);
		    CheckRegionConnect(r-1,c,PieceFlag,row,column,array);
			CheckRegionConnect(r,c+1,PieceFlag,row,column,array);
			CheckRegionConnect(r,c-1,PieceFlag,row,column,array);
			CheckRegionConnect(r+1,c+1,PieceFlag,row,column,array);
			CheckRegionConnect(r+1,c-1,PieceFlag,row,column,array);
			CheckRegionConnect(r-1,c+1,PieceFlag,row,column,array);
			CheckRegionConnect(r-1,c-1,PieceFlag,row,column,array);
		}
	}
			
	//Check For win condition
	public int CheckWin(int PieceFlag,int row,int column,int[][] array)
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
			
	public void setVisitedtoZero(int row,int column)
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<column;j++)
				visitedarray[i][j] = 0;
		}
	}
	
	public void setBoard()
	{
		choosearray = firstlevelchildren.get(0).dataarray;
		maxvalue = firstlevelchildren.get(0).node_value;
		for(int k=1;k<firstlevelchildren.size();k++)
		{
			if(firstlevelchildren.get(k).node_value>maxvalue)
			{
				choosearray = firstlevelchildren.get(k).dataarray;
				maxvalue = firstlevelchildren.get(k).node_value;
			}
		}
	}
	
	public void clearoutput()
	{
		node_count = 0;
	 	maxvalue = 0;
	 	maxdepth = 0;
	 	count_eval = 0;
	 	count_prune_max = 0;
	 	count_prune_min = 0;
	 	root_instance = 0;
	 	difficulty_level = 0;
	 	firstlevelchildren.clear();
	 	temp_node_value = 0;
	 	temp_node_count = 0;
	}
	
	public void printvalues()
	{
		System.out.println("Maximum depth of game tree reached: "+maxdepth);
		System.out.println("Total number of nodes generated: "+node_count);
		System.out.println("Number of times evaluation function called within MAX function: "+count_eval);
		System.out.println("Number of times evaluation function called within MIN function: 0");
		System.out.println("Number of times pruning occured in MAX function: "+count_prune_max);
		System.out.println("Number of times pruning occured in MIN function: "+count_prune_min);
	}
}