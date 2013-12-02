package edu.turtlekit2.warbot.duckingbear.explorers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.BrainExplorer;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultExplorerBehavior extends AbstractBehavior {
	private BrainExplorer entity;
	
	public DefaultExplorerBehavior(BrainExplorer entity) {
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
		return Names.EXPLORER;
	}
}
