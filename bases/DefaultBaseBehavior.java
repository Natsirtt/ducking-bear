package edu.turtlekit2.warbot.duckingbear.bases;

import java.util.List;

import edu.turtlekit2.warbot.agents.WarRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior extends AbstractBehavior {

	private ManagerBehavior explorerContrat;
	private ManagerBehavior rocketlauncherContrat;
	private int explorerTeam;
	private int rocketlauncherTeam;
	
	private boolean sent;
	
	public DefaultBaseBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		explorerContrat = null;
		rocketlauncherContrat = null;
		explorerTeam = -1;
		rocketlauncherTeam = -1;
		sent = false;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (explorerContrat != null) {
			explorerContrat.processMessage(msg);
		}
		if (rocketlauncherContrat != null) {
			rocketlauncherContrat.processMessage(msg);
		}
	}

	@Override
	public String act() {
		super.act();
		if (explorerTeam == -1) {
			if (explorerContrat != null) {
				explorerContrat.act();
				if (explorerContrat.isOver()) {
					explorerTeam = explorerContrat.getContratID();
				}
			} else {
				String cmp = "edu.turtlekit2.warbot.duckingbear.explorers.RecolterExplorerBehavior";
				explorerContrat = new ManagerBehavior(getEntity(), this, Names.EXPLORER, 5, cmp);	
			}
		}
		if (rocketlauncherTeam == -1) {
			if (rocketlauncherContrat != null) {
				rocketlauncherContrat.act();
				if (rocketlauncherContrat.isOver()) {
					rocketlauncherTeam = rocketlauncherContrat.getContratID();
				}
			} else {
				String cmp = "edu.turtlekit2.warbot.duckingbear.rocketLaunchers.DefenseRocketLauncherBehavior";
				rocketlauncherContrat = new ManagerBehavior(getEntity(), this, Names.ROCKET_LAUNCHER, 5, cmp);	
			}
		} else if (!sent) {
			List<EntityKnowledge> lek = getEntity().getKnowledgeBase().getTeam(rocketlauncherTeam);
			if (lek.size() > 0) {
				double dt = 360.0 / lek.size();
				double a = 0;
				for (EntityKnowledge ke : lek) {
					int x = (int) (Math.cos(Math.toRadians(a)) * WarRocketLauncher.RADIUS);
					int y = (int) (Math.sin(Math.toRadians(a)) * WarRocketLauncher.RADIUS);
					String[] content = new String[] {
						String.valueOf(x),
						String.valueOf(y)
					};
					getEntity().getBrain().sendMessage(ke.getID(), "goto", content);
					a += dt;
				}
				sent = true;
			}
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
