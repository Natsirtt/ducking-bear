package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.rocketLaunchers.DefaultRocketLauncherBehavior;
import edu.turtlekit2.warbot.duckingbear.utils.Names;

public class BrainRocketLauncher extends WarBrain {
	private Behavior behavior;
	private KnowledgeBase knowledgeBase;
	
	public BrainRocketLauncher() {
		behavior = new DefaultRocketLauncherBehavior(this);
		knowledgeBase = new KnowledgeBase(Names.ROCKET_LAUNCHER);
	}
	
	@Override
	public String action() {
		knowledgeBase.setID(getID());
		behavior.processMessages();
		String action = behavior.act();
		knowledgeBase.tick();
		return action;
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

}
