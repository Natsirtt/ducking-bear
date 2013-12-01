package edu.turtlekit2.warbot.duckingbear.bases;

import java.util.List;

import edu.turtlekit2.warbot.duckingbear.Behavior;
import edu.turtlekit2.warbot.duckingbear.BrainBase;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior implements Behavior {
	private BrainBase entity;
	
	public DefaultBaseBehavior(BrainBase entity) {
		this.entity = entity;
	}

	@Override
	public void processMessages() {
		KnowledgeBase kb = entity.getKnowledgeBase();
		List<WarMessage> messages = entity.getMessage();
		for (WarMessage msg : messages) {
			kb.processMessage(msg);
		}
	}

	@Override
	public String act() {
		entity.broadcastMessage(null, "alive", new String[0]);
		
		KnowledgeBase kb = entity.getKnowledgeBase();
		EntityKnowledge base = kb.getMainBase();
		if (base == null) {
			System.out.println("Je suis la base #" + entity.getID() + " et je ne possède pas de base principale");
		} else {
			System.out.println("Je suis la base #" + entity.getID() + " et je possède une base principale #" + base.getID());
		}
			
		return Names.IDLE;
	}

}
