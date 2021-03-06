package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.bases.DefaultBaseBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;

public class BrainBase extends WarBrain implements Entity {
	private Behavior behavior;
	private KnowledgeBase knowledgeBase;
	
	public BrainBase() {
		behavior = new DefaultBaseBehavior(this, 0);
		knowledgeBase = new KnowledgeBase(Names.BASE);
	}

	@Override
	public String action() {
		knowledgeBase.setID(getID());
		String action = behavior.act();
		knowledgeBase.tick();
		return action;
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
	
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
	
	// UTILITAIRES
	
	//Surcharge de setNextAgentCreate afin de palier au bug du moteur
	@Override
	public void setNextAgentCreate(String agent) {
		String rightStr = "";
		switch (agent) {
		case Names.EXPLORER:
			rightStr = "Explorer";
			break;
		case Names.ROCKET_LAUNCHER:
			rightStr = "RocketLauncher";
			break;
		default:
			throw new IllegalArgumentException("Impossible to create a " + agent);
		}
		super.setNextAgentCreate(rightStr);
	}

	@Override
	public WarBrain getBrain() {
		return this;
	}

	@Override
	public Behavior getBehavior() {
		return behavior;
	}
}
