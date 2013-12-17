package edu.turtlekit2.warbot.duckingbear.knowledge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.turtlekit2.warbot.agents.WarExplorer;
import edu.turtlekit2.warbot.agents.WarRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.FakeMessage;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.duckingbear.utils.Point;
import edu.turtlekit2.warbot.waritems.WarRocket;

/**
 * Classe représentant la base de connaisance d'une entité
 *
 */
public class KnowledgeBase {
	private Map<String, SortedMap<Integer, EntityKnowledge>> ennemies;
	private Map<String, SortedMap<Integer, EntityKnowledge>> allies;
	
	private Map<String, Integer> activeContracts;
	
	int id;
	String type;
	
	private int x;
	private int y;
	
	private long tick;
	
	/**
	 * 
	 * @param type Le type de l'unité possédant cette base de connaissances.
	 */
	public KnowledgeBase(String type) {
		ennemies = new HashMap<>();
		allies = new HashMap<>();
		tick = 0;
		x = 0;
		y = 0;
		this.id = 0;
		this.type = type;
		activeContracts = new HashMap<>();
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
		} else if (msg.getMessage().equals("beginContract")) {
			String[] content = msg.getContent();
			setActiveContract(content[0], Integer.parseInt(content[1]));
		} else if (msg.getMessage().equals("endContract")) {
			String[] content = msg.getContent();
			setActiveContract(content[0], -1);
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
	
	public List<EntityKnowledge> getAllies() {
		List<EntityKnowledge> res = new LinkedList<>();
		for (SortedMap<Integer, EntityKnowledge> map : allies.values()) {
			res.addAll(map.values());
		}
		return res;
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
	
	public String getType() {
		return type;
	}
	
	/**
	 * Renvoie l'ID du contrat actif pour un certain type d'unités.
	 * @param type Le type d'unité du contrat.
	 * @return -1 si aucun contrat n'est actif.
	 */
	public int getActiveContrat(String type) {
		Integer id = activeContracts.get(type);
		if (id == null) {
			return -1;
		}
		return id;
	}
	
	private void setActiveContract(String type, int id) {
		int activeContract = getActiveContrat(type);
		if (id == -1) {
			activeContracts.put(type, null);
		} else if ((activeContract == -1) || (activeContract > id)) {
			activeContracts.put(type, id);
		}
	}
	
	public EntityKnowledge getBestTarget() {
		int radius = (int) (WarRocket.AUTONOMY * WarRocket.SPEED);
		EntityKnowledge target = null;
		
		for (Map<Integer, EntityKnowledge> map : ennemies.values()) {
			for (EntityKnowledge ek : map.values()) {
				if ((ek.getLastUpdateDuration(tick) < 5) && (ek.getDistance(x, y) < radius)) {
					if ((target == null) || (ek.getEnergy() < target.getEnergy())) {
						target = ek;
					}
				}
			}
		}
		return target;
	}
	
	public double getBestShootAngle(EntityKnowledge target) {
		double nbTick = 6.0; // Le nombre de tick d'avance que l'on calcule
		
		double speed = 0.0;
		if (target.getType().equals(Names.EXPLORER)) {
			speed = WarExplorer.SPEED;
		} else if (target.getType().equals(Names.ROCKET_LAUNCHER)) {
			speed = WarRocketLauncher.SPEED;
		}
		
		int dx = (int) (Math.cos(Math.toRadians(target.getHeading())) * speed * nbTick);
		int dy = (int) (Math.sin(Math.toRadians(target.getHeading())) * speed * nbTick);
		System.out.println(dx + " " + dy + " " + speed);
		
		Point p = new Point(target.getX() + dx, target.getY() + dy);
		Point here = new Point(x, y);
		
		
		return here.heading(p);
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
