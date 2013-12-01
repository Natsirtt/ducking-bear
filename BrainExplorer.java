package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.explorers.DefaultExplorerBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;

public class BrainExplorer extends WarBrain{
	private Behavior behavior;
	private KnowledgeBase knowledgeBase;
	
	public BrainExplorer(){
		behavior = new DefaultExplorerBehavior(this);
		knowledgeBase = new KnowledgeBase();
	}
	
	@Override
	public String action() {
		behavior.processMessages();
		String action = behavior.act();
		knowledgeBase.tick();
		return action;
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

}
