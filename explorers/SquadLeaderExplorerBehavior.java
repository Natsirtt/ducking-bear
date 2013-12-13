package edu.turtlekit2.warbot.duckingbear.explorers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.bases.DefaultBaseBehavior;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class SquadLeaderExplorerBehavior extends AbstractBehavior {
	// Le numéro de la team qu'on controle
	private int myteam;
	
	private ManagerBehavior membersContrat = null;

	public SquadLeaderExplorerBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		
		myteam = -1;
		membersContrat = null;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (getEntity().getBehavior() == this) {
			if (membersContrat != null) {
				membersContrat.processMessage(msg);
			}
		}
	}
	
	public String act() {
		super.act();
		
		System.out.println("#" + getEntity().getBrain().getID() + " Je suis un leader !");
		
		if (myteam == -1) {
			if (membersContrat == null) {
				String cmp = "edu.turtlekit2.warbot.duckingbear.rocketLaunchers.SquadMemberRocketLauncherBehavior";
				membersContrat = new ManagerBehavior(
						getEntity(), this, 
						Names.ROCKET_LAUNCHER, DefaultBaseBehavior.RL_PER_GROUP, cmp);
				System.out.println("#" + getEntity().getBrain().getID() + " J'envoie mes contrats !");
			} else {
				membersContrat.act();
				if (membersContrat.isOver()) {
					myteam = membersContrat.getContratID();
				}
			}
		} else {
			String[] content = new String[] {
				String.valueOf(myteam),
				String.valueOf(getTeamNumber())
			};
			broadcastMessage("all", "leader", content);
		}
		if (getEntity().getBrain().isBlocked()) {
			getEntity().getBrain().setRandomHeading();
		}
		return Names.MOVE;
	}

	@Override
	public String getType() {
		return Names.EXPLORER;
	}
}
