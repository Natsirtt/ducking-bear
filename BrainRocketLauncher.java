package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.rocketLaunchers.DefaultRocketLauncherBehavior;

public class BrainRocketLauncher extends WarBrain {
	private Behavior behavior;
	private KnowledgeBase knowledgeBase;
	
	public BrainRocketLauncher() {
		behavior = new DefaultRocketLauncherBehavior(this);
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
