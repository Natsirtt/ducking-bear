package edu.turtlekit2.warbot.duckingbear;

import java.util.LinkedList;
import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Classe abstraite d'un comportement.
 * Contient le code commun à tous les comportements (Les primitives vitales).
 * Exemple : détection d'ennemie, signalement, etc...
 * @author Benoit
 *
 */
public abstract class AbstractBehavior implements Behavior {
	
	private Entity entity;
	private List<FakeMessage> messages;
	private int teamNumber;
	
	public AbstractBehavior(Entity entity, int teamNumber) {
		this.entity = entity;
		this.teamNumber = teamNumber;
		messages = new LinkedList<>();
	}

	private void processMessages() {
		KnowledgeBase kb = getKnowledgeBase();
		List<WarMessage> messages = entity.getBrain().getMessage();
		for (WarMessage msg : messages) {
			kb.processMessage(new FakeMessage(msg));
			/*
			 * On utilise "getEntity().getBehavior()" plutot que "this"
			 *   au cas ou le comportement aurait change durant 
			 *   le traitement des messages.
			 */
			getEntity().getBehavior().processMessage(msg);
		}
		for (FakeMessage msg : this.messages) {
			kb.processMessage(msg);
		}
		this.messages.clear();
	}

	@Override
	public String act() {
		processMessages();
		// Au cas ou le comportement aurait change durant le traitement des messages
		if (getEntity().getBehavior() != this) {
			return getEntity().getBehavior().act();
		}
			
		List<Percept> percepts = entity.getBrain().getPercepts();
		for (Percept percept : percepts) {
			if (!percept.getTeam().equals(entity.getBrain().getTeam()) && 
					!percept.getType().equals(Names.FOOD)) {
				signalEnnemy(percept);
			}
		}
		signalAlive();
		return processReflexes();
	}
	
	protected String processReflexes() {
		if (!getEntity().getBrain().emptyBag()) {
			return Names.EAT;
		}
		return null;
	}
	
	private void signalEnnemy(Percept percept) {
		KnowledgeBase kb = getKnowledgeBase();
		int x = kb.getX() + (int) (Math.cos(Math.toRadians(percept.getAngle())) * percept.getDistance());
		int y = kb.getY() + (int) (Math.sin(Math.toRadians(percept.getAngle())) * percept.getDistance());
		
		String[] content = new String[] {
				String.valueOf(percept.getId()),
				percept.getTeam(),
				percept.getType(),
				String.valueOf(x), 
				String.valueOf(y)};
		broadcastMessage("all", "ennemy", content);
	}
	
	private void signalAlive() {
		KnowledgeBase kb = getKnowledgeBase();
		String[] content = new String[] {
				String.valueOf(kb.getX()), 
				String.valueOf(kb.getY()), 
				String.valueOf(getTeamNumber()),
				String.valueOf(entity.getBrain().getEnergy())};
		broadcastMessage("all", "alive", content);
	}

	public void broadcastMessage(String target, String msg, String[] content) {
		WarBrain ent = entity.getBrain();
		ent.broadcastMessage(target, msg, content);
		messages.add(new FakeMessage(ent.getID(), ent.getHeading(), ent.getTeam(), getType(), msg, content));
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	protected Entity getEntity() {
		return this.entity;
	}
	
	public int getTeamNumber() {
		return teamNumber;
	}
}
