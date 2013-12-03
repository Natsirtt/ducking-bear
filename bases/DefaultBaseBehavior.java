package edu.turtlekit2.warbot.duckingbear.bases;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior extends AbstractBehavior {
	private Entity entity;
	
	private boolean sent;

	
	public DefaultBaseBehavior(Entity entity) {
		super(entity);
		this.entity = entity;
		sent = false;
	}

	@Override
	public void processMessage(WarMessage msg) {
	}

	@Override
	public String act() {
		super.act();
		
		if (!sent) {
			String cmp = "edu.turtlekit2.warbot.duckingbear.rocketLaunchers.FireTestRocketLauncherBehavior";
			entity.setBehavior(new ManagerBehavior(entity, this, Names.ROCKET_LAUNCHER, 2, cmp));
			sent = true;
		}
		return Names.IDLE;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	public String getType() {
		return Names.BASE;
	}
}
