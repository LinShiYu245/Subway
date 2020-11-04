package subway;

import java.util.*;

public class Station {
	
	private String name;											//վ�������
	private ArrayList<String> line = new ArrayList<String>();		//��¼վ�����ļ�����·
	
	public Station(String name) {									
		this.name = name;
	}
	
	public void setLine(String line) {
		this.line.add(line);
	}
	
	public ArrayList<String> getLine() {
		return line;
	}
	
	public String getName() {
		return name;
	}

}
