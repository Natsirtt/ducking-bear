package edu.turtlekit2.warbot.duckingbear.bases;

import java.util.List;

import edu.turtlekit2.warbot.agents.WarBase;
import edu.turtlekit2.warbot.agents.WarRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultBaseBehavior extends AbstractBehavior {
	
	public static final int GROUP_NUMBER = 2;
	public static final int RL_PER_GROUP = 3;
	public static final int EXPLORER_NUMBER = GROUP_NUMBER + 1;
	public static final int ROCKETLAUNCHER_NUMBER = GROUP_NUMBER * RL_PER_GROUP + 1;

	private ManagerBehavior explorerContrat;
	private ManagerBehavior rocketlauncherContrat;
	private ManagerBehavior squadContrat;
	private int explorerTeam;
	private int rocketlauncherTeam;
	
	private boolean sent;
	
	private boolean phase2;
	private boolean phase2Sent;
	
	public DefaultBaseBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		explorerContrat = null;
		rocketlauncherContrat = null;
		squadContrat = null;
		explorerTeam = -1;
		rocketlauncherTeam = -1;
		sent = false;
		phase2 = false;
		phase2Sent = false;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (explorerContrat != null) {
			explorerContrat.processMessage(msg);
		}
		if (rocketlauncherContrat != null) {
			rocketlauncherContrat.processMessage(msg);
		}
		if (squadContrat != null) {
			squadContrat.processMessage(msg);
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
		if (canProduce()) {
			KnowledgeBase kb = getEntity().getKnowledgeBase();
			int exploreCount = kb.getAlliedExplorerCount();
			if (exploreCount < EXPLORER_NUMBER) {
				getEntity().getBrain().setNextAgentCreate(Names.EXPLORER);
				return Names.CREATE;
			} else {
				int rlCount = kb.getAlliedRocketLaucherCount();
				if (rlCount < ROCKETLAUNCHER_NUMBER) {
					getEntity().getBrain().setNextAgentCreate(Names.ROCKET_LAUNCHER);
					return Names.CREATE;
				}
				if ((exploreCount >= EXPLORER_NUMBER) && (rlCount >= ROCKETLAUNCHER_NUMBER)) {
					phase2 = true;
				}
			}
		} else if (canEat()) {
			return Names.EAT;
		}
		
		if (phase2 && !phase2Sent) {
			String cmp = "edu.turtlekit2.warbot.duckingbear.explorers.SquadLeaderExplorerBehavior";
			squadContrat = new ManagerBehavior(getEntity(), this, Names.EXPLORER, GROUP_NUMBER, cmp);
			phase2Sent = true;
		}
		if (squadContrat != null) {
			squadContrat.act();
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
	
	private boolean canProduce() {
		return getEntity().getBrain().getEnergy() > (WarBase.MAX_ENERGY - 200);
	}
	
	private boolean canEat() {
		return !getEntity().getBrain().emptyBag();
	}
}
