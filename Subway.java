package subway;

import java.io.*;
import java.util.*;

public class Subway {
	
	static int visit[][] = new int[400][400];
	static int lu[] = new int[200];
	static int lu2[] = new int[200];
	static int min = 100;

	public static void main(String[] args) throws IOException {
		
		ArrayList<Station> station = new ArrayList<Station>();			//站点集合
		int[][] bian = new int[400][400];								//边
		ArrayList<Line> line = new ArrayList<Line>();					//线路集合
		
		File file = new File("C:/Users/乐乐/Desktop/地铁线路信息.txt");
		
		if(file.exists()) {
			getShuju(file,station,bian,line);							//导入数据
		}
		else
			System.out.print("不存在");
		
		Scanner s = new Scanner(System.in);
		
		System.out.print("请输入起始站： ");
		String start = s.next();
		System.out.print("请输入终点站： ");
		String end = s.next();
		
		int n1 = getNum(station, start);
		if(n1 == -1) {
			System.out.println("不存在该起点");
		}
		int n2 = getNum(station, end);
		if(n2 == -1) {
			System.out.println("不存在该终点");
		}
		
		
		if(n1 != -1 & n2 != -1) {
			digist(station,n1,n2,bian,0);									//DSF算法
			
			System.out.println("最少站数： " +min);
			
			shuChu(n1,station,line);
		}
		
		

	}
	
	public static void getShuju(File file, ArrayList<Station> station,int[][] bian, ArrayList<Line> Line) throws IOException {
		
		String node = "UTF-8";
		
		FileInputStream fin = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fin,node);
        BufferedReader br = new BufferedReader(reader);
		
		int num = 22;										//线路总数
		
		for(int i = 0;i < num; i++) {
			String token = br.readLine();
			String[] tokens = token.split(" ");
			
			String line = tokens[0];						//先储存线路信息
			Line li = new Line(line);
			Line.add(li);
			
			for(int j = 1;j < tokens.length; j++) {
				Station st = new Station(tokens[j]);
				int n = getNum( station, tokens[j]);		//得到站点编号
				if( n < 0 ) {								//判断站点是否存在
					st.setLine(line);
					station.add(st);
					n = getNum( station, tokens[j]);
				}
				else {
					if( !station.get(n).getLine().contains(line))	//判断站点是否已储存该线路
						station.get(n).setLine(line);
				}
					
				int n1 = getNum( station, tokens[j-1]);				//得到前后站点的编号
				int n2 = -1;
				if(j != tokens.length - 1)							//若当前站点不为末尾，则有下一个站点的编号
					n2 = getNum( station, tokens[j+1]);
				
				if(j == 1) {										//根据当前站点是首位，末尾还是其他，加入不同的边
					if(n2 < 0) {
						bian[n][station.size()] = 1;
						bian[station.size()][n] = 1;
					}
					else {
						bian[n][n2] = 1;
						bian[n2][n] = 1;
					}
				}
				else if(j == tokens.length - 1) {
					bian[n][n1] = 1;
					bian[n1][n] = 1;
				}
				else {
					if(n2 < 0) {
						bian[n][station.size()] = 1;
						bian[station.size()][n] = 1;
					}
					else {
						bian[n][n2] = 1;
						bian[n2][n] = 1;
					}
					bian[n][n1] = 1;
					bian[n1][n] = 1;
				}
				
			}
			
			
		}
		br.close();
	}
	
	public static int getNum(ArrayList<Station> station, String name) {
		for(int i = 0;i < station.size(); i++) {			//遍历所有的站点，找到站点的编号
			if(station.get(i).getName().equals( name))
				return i;
		}
		return -1;											//若不存在该站点，返回-1
	}
	
	public static void digist( ArrayList<Station> station, int start, int end, int[][] bian,int juli) {
		
		if(juli <= min) {									//判断此时距离是否过大
			if(start == end) {								//如果首尾相同，则到站
				if(juli < min) {
					min = juli;								//改变距离最小值
					for(int i = 0;i < min; i++)				
						lu2[i] = lu[i];						//更新当前最小路线
				}
				return;
			}
		
			for(int i = 0;i < 400; i++)
				if(bian[start][i] == 1)
					if( visit[start][i] == 0) {				//判断是否走过当前路线
						visit[start][i] = 1;
						visit[i][start] = 1;
						lu[juli] = i;
						digist(station,i,end,bian,juli + 1);//迭代
						visit[start][i] = 0;
						visit[i][start] = 0;
					}
		}
	}
	
	public static void shuChu(int n1,ArrayList<Station> station, ArrayList<Line> Line) {
		
		int l = -1;
		
		for(int i = 0;i < station.get(n1).getLine().size(); i++)
			for(int j = 0;j < station.get(lu2[0]).getLine().size(); j++)
				if(station.get(n1).getLine().get(i).equals(station.get(lu2[0]).getLine().get(j))) {		//判断起始的线路
					System.out.println(station.get(n1).getLine().get(i));								//输出线路
					for(int m = 0;m < Line.size(); m++) {												//遍历得到起始线路的编号
						if(Line.get(m).getName().equals(station.get(n1).getLine().get(i))) {
							l = m;
							break;
						}
					}
				}
		System.out.print(station.get(n1).getName() + "  ");												//输出站点
		
		
		
		for(int k = 0;k < min - 1; k++) {																//从第一个站点开始
			
			System.out.print(station.get(lu2[k]).getName() + "  ");										//输出当前站点
			
			int flag = 0;
			for(int i = 0;i < station.get(lu2[k + 1]).getLine().size(); i++)							//判断下一个站点是否在当前线路上
				if(station.get(lu2[k + 1]).getLine().get(i).equals(Line.get(l).getName()))
					flag = 1;
			
			if(flag == 0) {																				//若没在
				System.out.println();
				for(int i = 0;i < station.get(lu2[k + 1]).getLine().size(); i++)
					for(int j = 0;j < station.get(lu2[k + 2]).getLine().size(); j++) 
						if( station.get(lu2[k + 1]).getLine().get(i).equals(station.get(lu2[k + 2]).getLine().get(j))) {		//根据下一站和下下站判断需要换乘的线路
							System.out.println("-->" + station.get(lu2[k + 1]).getLine().get(i));								//输出换乘的线路
							for(int m = 0;m < Line.size(); m++) {
								if(Line.get(m).getName().equals(station.get(lu2[k + 1]).getLine().get(i))) {					//更新线路编号、
									l = m;
									break;
								}
							}
						}
			}
		}			
		System.out.print(station.get(lu2[min - 1]).getName() + "  ");															//输出终点站
	}
}


