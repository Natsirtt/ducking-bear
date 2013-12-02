package edu.turtlekit2.warbot.duckingbear.bases;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.BrainBase;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior extends AbstractBehavior {
	private BrainBase entity;

	
	public DefaultBaseBehavior(BrainBase entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	protected void processMessage(WarMessage msg) {
	}

	@Override
	public String act() {
		super.act();
		return Names.IDLE;
	}

	@Override
	protected KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	protected String getType() {
		return Names.BASE;
	}
}
