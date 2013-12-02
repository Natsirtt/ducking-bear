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
	
	private WarBrain entity;
	private List<FakeMessage> messages;
	
	public AbstractBehavior(WarBrain entity) {
		this.entity = entity;
		messages = new LinkedList<>();
	}

	private void processMessages() {
		KnowledgeBase kb = getKnowledgeBase();
		List<WarMessage> messages = entity.getMessage();
		for (WarMessage msg : messages) {
			kb.processMessage(new FakeMessage(msg));
			processMessage(msg);
		}
		for (FakeMessage msg : this.messages) {
			kb.processMessage(msg);
		}
		this.messages.clear();
	}

	@Override
	public String act() {
		processMessages();
		List<Percept> percepts = entity.getPercepts();
		for (Percept percept : percepts) {
			if (!percept.getTeam().equals(entity.getTeam()) && 
					!percept.getType().equals(Names.FOOD)) {
				signalEnnemy(percept);
			}
		}
		signalAlive();
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
		String[] content = new String[] {String.valueOf(kb.getX()), String.valueOf(kb.getY())};
		broadcastMessage("all", "alive", content);
	}

	public void broadcastMessage(String target, String msg, String[] content) {
		entity.broadcastMessage(target, msg, content);
		messages.add(new FakeMessage(entity.getID(), entity.getHeading(), entity.getTeam(), getType(), msg, content));
	}
	
	abstract protected KnowledgeBase getKnowledgeBase();
	
	abstract protected String getType();
	
	abstract protected void processMessage(WarMessage msg);

}
