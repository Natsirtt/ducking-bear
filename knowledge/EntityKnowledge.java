package edu.turtlekit2.warbot.duckingbear.knowledge;

import edu.turtlekit2.warbot.duckingbear.FakeMessage;
import edu.turtlekit2.warbot.message.WarMessage;

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
	private int angle;
	// Le tick de dernieère mise à jour
	private long tick;
	
	public EntityKnowledge(WarMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.tick = tick;
		if (msg.getMessage().equals("alive")) {
			id = msg.getSender();
			team = msg.getSenderTeam();
			type = msg.getSenderType();
			x = 0; // A modifier
			y = 0; // A modifier
			angle = msg.getAngle();
		} else if (msg.getMessage().equals("ennemy")) {
			String[] content = msg.getContent();
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			angle = Integer.parseInt(content[5]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public EntityKnowledge(FakeMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.tick = tick;
		if (msg.getMessage().equals("alive")) {
			id = msg.getSender();
			team = msg.getSenderTeam();
			type = msg.getSenderType();
			x = 0; // A modifier
			y = 0; // A modifier
			angle = msg.getAngle();
		} else if (msg.getMessage().equals("ennemy")) {
			String[] content = msg.getContent();
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			angle = Integer.parseInt(content[5]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void update(WarMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.tick = tick;
		if (msg.getMessage() == "alive") {
			x = 0; // A modifier
			y = 0; // A modifier
			angle = msg.getAngle();
		} else if (msg.getMessage() == "ennemy") {
			String[] content = msg.getContent();
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			angle = Integer.parseInt(content[5]);
		} else {
			throw new IllegalArgumentException();
		}
	}
	public void update(FakeMessage msg, long tick) {
		if (msg == null) {
			throw new IllegalArgumentException();
		}
		this.tick = tick;
		if (msg.getMessage() == "alive") {
			x = 0; // A modifier
			y = 0; // A modifier
			angle = msg.getAngle();
		} else if (msg.getMessage() == "ennemy") {
			String[] content = msg.getContent();
			id = Integer.parseInt(content[0]);
			team = content[1];
			type = content[2];
			x = Integer.parseInt(content[3]);
			y = Integer.parseInt(content[4]);
			angle = Integer.parseInt(content[5]);
		} else {
			throw new IllegalArgumentException();
		}
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
	 * Renvoie la direction de l'entité
	 */
	public int getAngle() {
		return angle;
	}
	
	/**
	 * Renvoie le nombre de ticks depuis la dernière mise à jour.
	 * @return
	 */
	public long getLastUpdateDuration(long actualTick) {
		return actualTick - tick;
	}
}
