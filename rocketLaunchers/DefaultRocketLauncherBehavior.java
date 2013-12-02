package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import java.util.LinkedList;
import java.util.List;

import edu.turtlekit2.warbot.duckingbear.Behavior;
import edu.turtlekit2.warbot.duckingbear.BrainRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.FakeMessage;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultRocketLauncherBehavior implements Behavior {
	private BrainRocketLauncher entity;
	
	private List<FakeMessage> messages;
	
	private boolean firstStep;
	
	public DefaultRocketLauncherBehavior(BrainRocketLauncher entity) {
		this.entity = entity;
		messages = new LinkedList<>();
		firstStep = true;
	}

	@Override
	public void processMessages() {
		KnowledgeBase kb = entity.getKnowledgeBase();
		List<WarMessage> messages = entity.getMessage();
		for (WarMessage msg : messages) {
			kb.processMessage(new FakeMessage(msg));
		}
		for (FakeMessage msg : this.messages) {
			kb.processMessage(msg);
		}
		this.messages.clear();
	}

	@Override
	public String act() {
		signalAlive();
		
		KnowledgeBase kb = entity.getKnowledgeBase();
		EntityKnowledge base = kb.getMainBase();
		if (base == null) {
			System.out.println("Je suis le rocketlauncher #" + entity.getID() + " et je ne possède pas de base principale");
		} else {
			System.out.println("Je suis le rocketlauncher #" + entity.getID() + " et je possède une base principale #" + base.getID());
			System.out.println("    Je me trouve en (" + kb.getX() + "," + kb.getY() + ")");
		}
		
		if (firstStep) {
			firstStep = false;
			entity.setRandomHeading();
			return Names.MOVE;
		} else {
			return Names.IDLE;
		}
	}
	
	public void signalAlive() {
		KnowledgeBase kb = entity.getKnowledgeBase();
		String[] content = new String[] {String.valueOf(kb.getX()), String.valueOf(kb.getY())};
		broadcastMessage("all", "alive", content);
	}
	
	public void broadcastMessage(String target, String msg, String[] content) {
		entity.broadcastMessage(target, msg, content);
		messages.add(new FakeMessage(entity.getID(), entity.getHeading(), entity.getTeam(), Names.ROCKET_LAUNCHER, msg, content));
	}

}
