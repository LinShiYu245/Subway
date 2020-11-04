package subway;

import java.io.*;
import java.util.*;

public class Subway {
	
	static int visit[][] = new int[400][400];
	static int lu[] = new int[200];
	static int lu2[] = new int[200];
	static int min = 100;

	public static void main(String[] args) throws IOException {
		
		ArrayList<Station> station = new ArrayList<Station>();			//վ�㼯��
		int[][] bian = new int[400][400];								//��
		ArrayList<Line> line = new ArrayList<Line>();					//��·����
		
		File file = new File("C:/Users/����/Desktop/������·��Ϣ.txt");
		
		if(file.exists()) {
			getShuju(file,station,bian,line);							//��������
		}
		else
			System.out.print("������");
		
		Scanner s = new Scanner(System.in);
		
		System.out.print("��������ʼվ�� ");
		String start = s.next();
		System.out.print("�������յ�վ�� ");
		String end = s.next();
		
		int n1 = getNum(station, start);
		if(n1 == -1) {
			System.out.println("�����ڸ����");
		}
		int n2 = getNum(station, end);
		if(n2 == -1) {
			System.out.println("�����ڸ��յ�");
		}
		
		
		if(n1 != -1 & n2 != -1) {
			digist(station,n1,n2,bian,0);									//DSF�㷨
			
			System.out.println("����վ���� " +min);
			
			shuChu(n1,station,line);
		}
		
		

	}
	
	public static void getShuju(File file, ArrayList<Station> station,int[][] bian, ArrayList<Line> Line) throws IOException {
		
		String node = "UTF-8";
		
		FileInputStream fin = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fin,node);
        BufferedReader br = new BufferedReader(reader);
		
		int num = 22;										//��·����
		
		for(int i = 0;i < num; i++) {
			String token = br.readLine();
			String[] tokens = token.split(" ");
			
			String line = tokens[0];						//�ȴ�����·��Ϣ
			Line li = new Line(line);
			Line.add(li);
			
			for(int j = 1;j < tokens.length; j++) {
				Station st = new Station(tokens[j]);
				int n = getNum( station, tokens[j]);		//�õ�վ����
				if( n < 0 ) {								//�ж�վ���Ƿ����
					st.setLine(line);
					station.add(st);
					n = getNum( station, tokens[j]);
				}
				else {
					if( !station.get(n).getLine().contains(line))	//�ж�վ���Ƿ��Ѵ������·
						station.get(n).setLine(line);
				}
					
				int n1 = getNum( station, tokens[j-1]);				//�õ�ǰ��վ��ı��
				int n2 = -1;
				if(j != tokens.length - 1)							//����ǰվ�㲻Ϊĩβ��������һ��վ��ı��
					n2 = getNum( station, tokens[j+1]);
				
				if(j == 1) {										//���ݵ�ǰվ������λ��ĩβ�������������벻ͬ�ı�
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
		for(int i = 0;i < station.size(); i++) {			//�������е�վ�㣬�ҵ�վ��ı��
			if(station.get(i).getName().equals( name))
				return i;
		}
		return -1;											//�������ڸ�վ�㣬����-1
	}
	
	public static void digist( ArrayList<Station> station, int start, int end, int[][] bian,int juli) {
		
		if(juli <= min) {									//�жϴ�ʱ�����Ƿ����
			if(start == end) {								//�����β��ͬ����վ
				if(juli < min) {
					min = juli;								//�ı������Сֵ
					for(int i = 0;i < min; i++)				
						lu2[i] = lu[i];						//���µ�ǰ��С·��
				}
				return;
			}
		
			for(int i = 0;i < 400; i++)
				if(bian[start][i] == 1)
					if( visit[start][i] == 0) {				//�ж��Ƿ��߹���ǰ·��
						visit[start][i] = 1;
						visit[i][start] = 1;
						lu[juli] = i;
						digist(station,i,end,bian,juli + 1);//����
						visit[start][i] = 0;
						visit[i][start] = 0;
					}
		}
	}
	
	public static void shuChu(int n1,ArrayList<Station> station, ArrayList<Line> Line) {
		
		int l = -1;
		
		for(int i = 0;i < station.get(n1).getLine().size(); i++)
			for(int j = 0;j < station.get(lu2[0]).getLine().size(); j++)
				if(station.get(n1).getLine().get(i).equals(station.get(lu2[0]).getLine().get(j))) {		//�ж���ʼ����·
					System.out.println(station.get(n1).getLine().get(i));								//�����·
					for(int m = 0;m < Line.size(); m++) {												//�����õ���ʼ��·�ı��
						if(Line.get(m).getName().equals(station.get(n1).getLine().get(i))) {
							l = m;
							break;
						}
					}
				}
		System.out.print(station.get(n1).getName() + "  ");												//���վ��
		
		
		
		for(int k = 0;k < min - 1; k++) {																//�ӵ�һ��վ�㿪ʼ
			
			System.out.print(station.get(lu2[k]).getName() + "  ");										//�����ǰվ��
			
			int flag = 0;
			for(int i = 0;i < station.get(lu2[k + 1]).getLine().size(); i++)							//�ж���һ��վ���Ƿ��ڵ�ǰ��·��
				if(station.get(lu2[k + 1]).getLine().get(i).equals(Line.get(l).getName()))
					flag = 1;
			
			if(flag == 0) {																				//��û��
				System.out.println();
				for(int i = 0;i < station.get(lu2[k + 1]).getLine().size(); i++)
					for(int j = 0;j < station.get(lu2[k + 2]).getLine().size(); j++) 
						if( station.get(lu2[k + 1]).getLine().get(i).equals(station.get(lu2[k + 2]).getLine().get(j))) {		//������һվ������վ�ж���Ҫ���˵���·
							System.out.println("-->" + station.get(lu2[k + 1]).getLine().get(i));								//������˵���·
							for(int m = 0;m < Line.size(); m++) {
								if(Line.get(m).getName().equals(station.get(lu2[k + 1]).getLine().get(i))) {					//������·��š�
									l = m;
									break;
								}
							}
						}
			}
		}			
		System.out.print(station.get(lu2[min - 1]).getName() + "  ");															//����յ�վ
	}
}


