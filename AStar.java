public class AStar {
	static int map[][] = new int[10][10];
	static int wall[][] = {{1,1},{1,2},{1,3},{1,4},{1,5},{1,6},{1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,7},{8,7},{8,6},{9,9}};
	static int start[] = {0,2};
	static int end[] = {8,5};
	static int G_value[][] = new int[10][10];
	static int L_value[][] = new int[10][10];
	static int F_value[][] = new int[10][10];
	static int parent[][][] = new int[10][10][2];
	static int open_list[][] = new int[100][2];
	static int closed_list[][] = new int[100][2];
	static int e1, e2;
	public static void main(String args[]){
		for(int i = 0; i < open_list.length; i++){
			open_list[i][0] = -1;
			closed_list[i][0] = -1;
			open_list[i][1] = -1;
			closed_list[i][1] = -1;
		}
		for(int i = 0; i < L_value.length; i++){//L value calculations
			for(int j = 0; j < L_value[i].length; j++){
				L_value[i][j] = (end[0]-i)+(end[1]-j);
			}
		}
		find_path(map, start, end);// finding path
		pave_path(start, end); // placing path values
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if(in_wall(i,j))
					System.out.print(2 + " ");
				else
					System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		pave_path(start, end);
		//System.out.println(parent[2][6][0]+" "+parent[2][6][1]);
		//for(int i = 0; i < map.length; i++){
			//for(int j = 0; j < map[i].length; j++){
				//System.out.println("{" + i + ", " + j + "}, {" + parent[i][j][0] + " " + parent[i][j][1]);
			//}
		//}	
	}
	
	public static int[][] find_path(int m[][],int s[], int e[]){
		int x = 0;
		int y = 0;
		int current_x = s[0];
		int current_y = s[1];
		m[s[0]][s[1]] = 1;
		G_value[s[0]][s[1]] = 0;
		add_to_close(s[0],s[1]);
		while(true){
			for(int i = 0; i < 4; i++){
				switch(i){
					case 0:
						x = current_x+1;
						y = current_y;
						break;
					case 1:
						x = current_x-1;
						y = current_y;
						break;
					case 2:
						x = current_x;
						y = current_y+1;
						break;
					case 3:
						x = current_x;
						y = current_y-1;
						break;
				}
				
				
				if(!in_wall(x, y) && (x < m.length && y < m[current_x].length) && !in_closed(x,y) && x > -1 && y > -1){
					if(!in_open(x,y)){
						parent[x][y][0] = current_x;
						parent[x][y][1] = current_y;
						G_value[x][y] = G_value[parent[x][y][0]][parent[x][y][1]]+1;
						F_value[x][y] = G_value[x][y]+L_value[x][y];
						add_to_open(x,y);
					}
					else{
						if(G_value[current_x][current_y]+1 < G_value[x][y]){
							parent[x][y][0] = current_x;
							parent[x][y][1] = current_y;
							G_value[x][y] = G_value[current_x][current_y]+1;
							F_value[x][y] = G_value[x][y] + L_value[x][y];
						}
					}
				}
			}
			int temp[] = find_smallest(open_list);
			//System.out.println(temp[0]+" "+temp[1]);
			remove_from_open(temp);
			add_to_close(temp[0], temp[1]);
			current_x = temp[0];
			current_y = temp[1];
			if(current_x == end[0] && current_y == end[1])
				break;
			
		}
		pave_path(s, e);
		return m;
	}
	public static void add_to_open(int col1, int col2){
		int i = 0;
		while(true){
			if(open_list[i][0] == -1){
				open_list[i][0]=col1;
				open_list[i][1]=col2;
				break;
			}
			i++;
		}
	}
	public static void add_to_close(int col1, int col2){
		int i = 0;
		while(true){
			if(closed_list[i][0] == -1){
				closed_list[i][0] = col1;
				closed_list[i][1] = col2;
				break;
			}
			i++;
		}
	}
	public static void remove_from_open(int open[]){
		int i = 0;
		while(true){
			if(open_list[i][0] == open[0] && open_list[i][1] == open[1]){
				open_list[i][0] = -1;
				open_list[i][1] = -1;
				break;
			}
			i++;
		}
	}
	public static boolean in_wall(int col1, int col2){// walll checking
		for(int i = 0; i < wall.length; i++){
			if(wall[i][0] == col1 && wall[i][1] == col2)
				return true;
		}
		return false;
	}
	public static boolean in_open(int col1, int col2){// is present in open list
		for(int i = 0; i < open_list.length; i++){
			if(open_list[i][0] == col1 && open_list[i][1] == col2)
				return true;
		}
		return false;
	}
	public static boolean in_closed(int col1, int col2){//checking whether co-ordinate is prensent in closed list or not
		for(int i = 0; i < closed_list.length; i++){
			if(closed_list[i][0] == col1 && closed_list[i][1] == col2)
				return true;
		}
		return false;
	}
	public static int[] find_smallest(int open[][]){//finding smallest F value co-ordinate from open list
		int i = 0;
		while(open[i][0] == -1 && open[i][1] == -1)
			i++;
		int smallest = F_value[open[i][0]][open[i][1]];
		int temp[] = {open[i][0],open[i][1]};
		for(i = 1; i < open.length; i++){
			if(open[i][0] != -1 && open[i][1] != -1){
				if(F_value[open[i][0]][open[i][1]] < smallest){
					smallest = F_value[open[i][0]][open[i][1]];
					temp[0] = open[i][0];
					temp[1] = open[i][1];
				}	
			}
		}	
		return temp;
	}
	public static void pave_path(int s[], int e[]){//back track though parents by placing "1"
		//System.out.println(e[0]+" "+e[1]);
		map[e[0]][e[1]] = 1;
		if(parent[e[0]][e[1]][0] == s[0] && parent[e[0]][e[1]][1] == s[1])
			return;
		else{
			e1 = e[0];
			e2 = e[1];
			//System.out.println(parent[e[0]][e[1]][0]+" "+parent[e[0]][e[1]][1]);
			//System.out.print(parent[e[0]][e[1]][0]+" ");
			e[0] = parent[e1][e2][0];
			//System.out.println(e[0]);
			//System.out.print(parent[e[0]][e[1]][1]+" ");
			e[1] = parent[e1][e2][1];
			//System.out.println(e[1]);
			//System.out.println(e[0]+" "+e[1]);
			pave_path(s, e);
		}	
	}
}