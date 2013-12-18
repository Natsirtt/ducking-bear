package edu.turtlekit2.warbot.duckingbear.knowledge;

import edu.turtlekit2.warbot.duckingbear.FakeMessage;

/**
 * Classe représentant une donnée de la base de connaissance.
 *  Note : Les données correspondent à la dernière fois que l'entité
 *         a été vue.
 */
public class EntityKnowledge {
	private int id;
	private String team;
	private String type;
	private int x;
	private int y;
	private int teamNumber;
	// Le tick de dernière mise à jour
	private long tick;
	private int energy;
	private double heading;
	
	public EntityKnowledge(FakeMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.energy = -1;
		this.tick = tick;
		String[] content = msg.getContent();
		if (msg.getMessage().equals("alive")) {
			id = msg.getSender();
			team = msg.getSenderTeam();
			type = msg.getSenderType();
			x = Integer.parseInt(content[0]);
			y = Integer.parseInt(content[1]);
			teamNumber = Integer.parseInt(content[2]);
			energy = Integer.parseInt(content[3]);
			heading = Double.parseDouble(content[4]);
		} else if (msg.getMessage().equals("ennemy")) {
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			energy = Integer.parseInt(content[5]);
			heading = Double.parseDouble(content[6]);
			teamNumber = 0;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void update(FakeMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.tick = tick;
		String[] content = msg.getContent();
		if (msg.getMessage() == "alive") {
			x = Integer.parseInt(content[0]);
			y = Integer.parseInt(content[1]);
			teamNumber = Integer.parseInt(content[2]);
			energy = Integer.parseInt(content[3]);
			heading = Double.parseDouble(content[4]);
		} else if (msg.getMessage() == "ennemy") {
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			heading = Double.parseDouble(content[5]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public String toString() {
		String s = "";
		s += type + "#" + id + " ";
		s += "x : " + getX() + ", y : " + getY() + "";
		return s;
	}
	
	public long getTick() {
		return tick;
	}
	
	public int getID() {
		return id;
	}
	
	public String getTeam() {
		return team;
	}
	
	public String getType() {
		return type;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	/**
	 * Renvoie la position x de l'entité.
	 * Note : la position est relative à la base principale.
	 */
	public int getX() {
		return x;
	}
	/**
	 * Renvoie la position y de l'entité.
	 * Note : la position est relative à la base principale.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Renvoie le nombre de ticks depuis la dernière mise à jour.
	 * @return
	 */
	public long getLastUpdateDuration(long actualTick) {
		return actualTick - tick;
	}
	
	public int getDistance(int x2, int y2) {
		int squareDistance = (x2-x)*(x2-x) + (y2-y)*(y2-y);
		return (int) Math.sqrt(squareDistance);
	}
	
	public int getTeamNumber() {
		return teamNumber;
	}
	
	public double getHeading() {
		return heading;
	}
}
