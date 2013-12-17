package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class SquadMemberRocketLauncherBehavior extends AbstractBehavior {
	
	private int leader;

	public SquadMemberRocketLauncherBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		leader = -1;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (msg.getMessage().equals("leader")) {
			String[] content = msg.getContent();
			if (Integer.parseInt(content[0]) == getTeamNumber()) {
				leader = Integer.parseInt(content[1]);
			}
		}
	}
	
	public String act() {
		super.act();
		System.out.println("#" + getEntity().getBrain().getID() + " Je suis un SquadMember ! "
				+ " Mon Equipe : " + getTeamNumber() + " Mon leader : " + leader);
		if (leader != -1) {
			KnowledgeBase kb = getKnowledgeBase();
			int heading = kb.getHeading(leader);
			getEntity().getBrain().setHeading(heading);
			return Names.MOVE;
		}
		return Names.IDLE;
	}

	@Override
	public String getType() {
		return Names.ROCKET_LAUNCHER;
	}

}
