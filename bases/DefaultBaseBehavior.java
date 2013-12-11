package edu.turtlekit2.warbot.duckingbear.bases;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior extends AbstractBehavior {
	private boolean sent;

	
	public DefaultBaseBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		sent = false;
	}

	@Override
	public void processMessage(WarMessage msg) {
	}

	@Override
	public String act() {
		super.act();
		
		/*if (!sent) {
			String cmp = "edu.turtlekit2.warbot.duckingbear.rocketLaunchers.FireTestRocketLauncherBehavior";
			entity.setBehavior(new ManagerBehavior(getEntity(), this, Names.ROCKET_LAUNCHER, 5, cmp));
			sent = true;
		}*/
		
		if (!sent) {
			String cmp = "edu.turtlekit2.warbot.duckingbear.explorers.RecolterExplorerBehavior";
			getEntity().setBehavior(new ManagerBehavior(getEntity(), this, Names.EXPLORER, 5, cmp));
			sent = true;
		}
		
		return Names.IDLE;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return getEntity().getKnowledgeBase();
	}
	
	public String getType() {
		return Names.BASE;
	}
}
