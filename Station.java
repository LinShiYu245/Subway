package subway;

import java.util.*;

public class Station {
	
	private String name;											//站点的名字
	private ArrayList<String> line = new ArrayList<String>();		//记录站点在哪几个线路
	
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
