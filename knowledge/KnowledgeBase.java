package edu.turtlekit2.warbot.duckingbear.knowledge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.turtlekit2.warbot.duckingbear.FakeMessage;
import edu.turtlekit2.warbot.duckingbear.utils.Names;

/**
 * Classe représentant la base de connaisance d'une entité
 *
 */
public class KnowledgeBase {
	private Map<String, SortedMap<Integer, EntityKnowledge>> ennemies;
	private Map<String, SortedMap<Integer, EntityKnowledge>> allies;
	
	int id;
	String type;
	
	private int x;
	private int y;
	
	private long tick;
	
	public KnowledgeBase(String type) {
		ennemies = new HashMap<>();
		allies = new HashMap<>();
		tick = 0;
		x = 0;
		y = 0;
		this.id = 0;
		this.type = type;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Incrémente le tick de la base de connaisance
	 */
	public void tick() {
		tick++;
	}
	
	public long getTick() {
		return tick;
	}
	public void processMessage(FakeMessage msg) {
		if (msg.getMessage().equals("alive")) {
			SortedMap<Integer, EntityKnowledge> a = allies.get(msg.getSenderType());
			if (a == null) {
				a = new TreeMap<Integer, EntityKnowledge>();
				allies.put(msg.getSenderType(), a);
			}
			EntityKnowledge ent = a.get(msg.getSender());
			if (ent == null) {
				ent = new EntityKnowledge(msg, getTick());
				a.put(msg.getSender(), ent);
			} else {
				ent.update(msg, getTick());
			}
			if (msg.getSenderType().equals(Names.BASE)) {
				checkMainBase(msg);
			}
		} else if (msg.getMessage().equals("ennemy")) {
			String[] content = msg.getContent();
			int id = Integer.parseInt(content[0]);
			String type = content[2];
			
			SortedMap<Integer, EntityKnowledge> a = ennemies.get(type);
			if (a == null) {
				a = new TreeMap<Integer, EntityKnowledge>();
				ennemies.put(type, a);
			}
			EntityKnowledge ent = a.get(id);
			if (ent == null) {
				ent = new EntityKnowledge(msg, getTick());
				a.put(id, ent);
			} else {
				ent.update(msg, getTick());
			}
		}
	}
	
	private void checkMainBase(FakeMessage msg) {
		EntityKnowledge mainBase = getMainBase();
		if ((mainBase != null) && (msg.getSender() == mainBase.getID())) {
			x = (int) -(Math.cos(Math.toRadians(msg.getAngle())) * msg.getDistance());
			y = (int) -(Math.sin(Math.toRadians(msg.getAngle())) * msg.getDistance());
		}
	}
	
	public EntityKnowledge getNearestEnnemy() {
		EntityKnowledge nearest = null;
		int minDistance = Integer.MAX_VALUE;
		for (Map<Integer, EntityKnowledge> map : ennemies.values()) {
			for (EntityKnowledge ek : map.values()) {
				if (ek.getLastUpdateDuration(tick) < 2) {
					int distance = ek.getDistance(getX(), getY());
					if (distance < minDistance) {
						minDistance = distance;
						nearest = ek;
					}
				}
			}
		}
		return nearest;
	}
	
	public EntityKnowledge getMainBase() {
		SortedMap<Integer, EntityKnowledge> bases = allies.get(Names.BASE);
		if ((bases == null) || (bases.isEmpty())) {
			return null;
		}
		Integer first = bases.firstKey();
		
		return bases.get(first);
	}
	/**
	 * Renvoie le nombre de rocket-launcher dans l'équipe.
	 * @return
	 */
	public int getAlliedRocketLaucherCount() {
		SortedMap<Integer, EntityKnowledge> a = allies.get(Names.ROCKET_LAUNCHER);
		if (a == null) {
			return 0;
		}
		int count = 0;
		for (EntityKnowledge ek : a.values()) {
			if (ek.getLastUpdateDuration(tick) < 2) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Renvoie le nombre d'explorer dans l'équipe.
	 * @return
	 */
	public int getAlliedExplorerCount() {
		SortedMap<Integer, EntityKnowledge> a = allies.get(Names.EXPLORER);
		if (a == null) {
			return 0;
		}
		int count = 0;
		for (EntityKnowledge ek : a.values()) {
			if (ek.getLastUpdateDuration(tick) < 2) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Renvoie le nombre de base dans l'équipe.
	 * @return
	 */
	public int getAlliedBaseCount() {
		SortedMap<Integer, EntityKnowledge> a = allies.get(Names.BASE);
		if (a == null) {
			return 0;
		}
		int count = 0;
		for (EntityKnowledge ek : a.values()) {
			if (ek.getLastUpdateDuration(tick) < 2) {
				count++;
			}
		}
		return count;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public EntityKnowledge getEntity(int id) {
		for (SortedMap<Integer, EntityKnowledge> map : allies.values()) {
			for (EntityKnowledge ke : map.values()) {
				if (ke.getID() == id) {
					return ke;
				}
			}
		}
		for (SortedMap<Integer, EntityKnowledge> map : ennemies.values()) {
			for (EntityKnowledge ke : map.values()) {
				if (ke.getID() == id) {
					return ke;
				}
			}
		}
		return null;
	}
	
	/**
	 * Renvoie la direction vers une entité.
	 * @param id L'id de l'entité vers laquelle on se dirige.
	 * @return
	 */
	public int getHeading(int id) {
		int angle = 0;
		EntityKnowledge ke = getEntity(id);
		if (ke != null) {
			double radian = Math.atan2((ke.getY() - y), (ke.getX() - x));
			angle = (int) Math.toDegrees(radian);
			return angle;
		}
		return 0;
	}
	
	/**
	 * Renvoie la direction vers une entité.
	 * @param id L'id de l'entité vers laquelle on se dirige.
	 * @return
	 */
	public int getDistance(int id) {
		int dst = 0;
		EntityKnowledge ke = getEntity(id);
		if (ke != null) {
			dst = (int) Math.sqrt((ke.getY() - y) * (ke.getY() - y) + (ke.getX() - x) * (ke.getX() - x));
			return dst;
		}
		return 0;
	}
	
	public List<EntityKnowledge> getTeam(int id) {
		List<EntityKnowledge> lek = new LinkedList<EntityKnowledge>();
		for (SortedMap<Integer, EntityKnowledge> map : allies.values()) {
			for (EntityKnowledge ke : map.values()) {
				if (ke.getTeamNumber() == id) {
					lek.add(ke);
				}
			}
		}
		return lek;
	}
	
	public String toString() {
		String s = "";
		s += type + "#" + id + "\n";
		s += "x : " + getX() + ", y : " + getY() + "\n";
		s += "Ennemies : " + ennemies + "\n";
		s += "Alliés : " + allies + "\n";
		return s;
	}
}
