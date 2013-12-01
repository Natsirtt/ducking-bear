package edu.turtlekit2.warbot.duckingbear.knowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.turtlekit2.warbot.duckingbear.FakeMessage;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

/**
 * Classe représentant la base de connaisance d'une entité
 *
 */
public class KnowledgeBase {
	private Map<String, SortedMap<Integer, EntityKnowledge>> ennemies;
	private Map<String, SortedMap<Integer, EntityKnowledge>> allies;
	
	private long tick;
	
	public KnowledgeBase() {
		ennemies = new HashMap<>();
		allies = new HashMap<>();
		tick = 0;
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
	
	/**
	 * Permet d'ajouter / mettre à jour des données.
	 * @param msg
	 */
	public void processMessage(WarMessage msg) {
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
	
	public EntityKnowledge getNearestEnnemy() {
		return null;
	}
	
	public EntityKnowledge getMainBase() {
		SortedMap<Integer, EntityKnowledge> bases = allies.get(Names.BASE);
		if ((bases == null) || (bases.isEmpty())) {
			return null;
		}
		Integer first = bases.firstKey();
		
		return bases.get(first);
	}
	
}
