package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ParticipantBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.duckingbear.utils.Point;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefenseRocketLauncherBehavior extends AbstractBehavior {
	private Point destination;
	
	public DefenseRocketLauncherBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		destination = null;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (getEntity().getBehavior() == this) {
			if (msg.getMessage().equals("goto")) {
				String[] content = msg.getContent();
				int x = Integer.parseInt(content[0]);
				int y = Integer.parseInt(content[1]);
				destination = new Point(x, y);
			} else if (msg.getMessage().equals("newContrat")) {
				System.out.println("Je suis un RocketLauncher et je reçoi un contrat de " + msg.getSender());
				getEntity().getBrain().reply(msg, "acceptContrat", msg.getContent());
				getEntity().setBehavior(new ParticipantBehavior(getEntity(), this, Integer.parseInt(msg.getContent()[0])));
			}
		} else if (msg.getMessage().equals("newContrat")) {
			getEntity().getBrain().reply(msg, "refuseContrat", msg.getContent());
			getEntity().setBehavior(new ParticipantBehavior(getEntity(), this, Integer.parseInt(msg.getContent()[0])));
		}
	}

	@Override
	public String act() {
		String reflex = super.act();
		if (reflex != null) {
			return reflex;
		}
		
		WarBrain ent = getEntity().getBrain();
		KnowledgeBase kb = getEntity().getKnowledgeBase();
		if (ent.isBlocked()) {
			ent.setRandomHeading();
		}
		
		if (ent.isReloaded()) {
			EntityKnowledge nearest = kb.getBestTarget();
			if (nearest != null) {
				double angle = kb.getBestShootAngle(nearest);
				ent.setAngleTurret((int) angle);
				return Names.FIRE;
			}
		} else if (!ent.isReloading()) {
			return Names.RELOAD;
		}
		if (destination != null) {
			Point here = new Point(getKnowledgeBase().getX(), getKnowledgeBase().getY());
			if (here.distance(destination) < 1) {
				destination = null;
				return Names.IDLE;
			} else {
				getEntity().getBrain().setHeading(here.heading(destination));
			}
			return Names.MOVE;
		}
		return Names.IDLE;
	}
	
	public String getType() {
		return Names.ROCKET_LAUNCHER;
	}
}
