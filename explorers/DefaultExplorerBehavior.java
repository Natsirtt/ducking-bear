package edu.turtlekit2.warbot.duckingbear.explorers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultExplorerBehavior extends AbstractBehavior {
	private Entity entity;
	
	public DefaultExplorerBehavior(Entity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public void processMessage(WarMessage msg) {

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
		return Names.EXPLORER;
	}
}
