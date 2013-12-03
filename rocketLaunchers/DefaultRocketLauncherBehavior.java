package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ParticipantBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultRocketLauncherBehavior extends AbstractBehavior {
	private Entity entity;
			
	public DefaultRocketLauncherBehavior(Entity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (msg.getMessage().equals("newContrat")) {
			entity.getEntity().reply(msg, "acceptContrat", msg.getContent());
			entity.setBehavior(new ParticipantBehavior(entity, this));
		}
	}

	@Override
	public String act() {
		super.act();
		
		return Names.IDLE;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	public String getType() {
		return Names.ROCKET_LAUNCHER;
	}
	
}
