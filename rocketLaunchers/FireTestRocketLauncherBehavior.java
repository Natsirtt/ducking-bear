package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class FireTestRocketLauncherBehavior extends AbstractBehavior {
	private Entity entity;
			
	public FireTestRocketLauncherBehavior(Entity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public void processMessage(WarMessage msg) {

	}

	@Override
	public String act() {
		super.act();
		WarBrain ent = entity.getEntity();
		KnowledgeBase kb = entity.getKnowledgeBase();
		if (ent.isBlocked()) {
			ent.setRandomHeading();
		}
		
		System.out.println("Je suis le rocket launcher #" + 
							entity.getEntity().getID() + " et je cherche quelqu'un à tuer en " +
							kb.getX() + " " + kb.getY());
		
		if (ent.isReloaded()) {
			EntityKnowledge nearest = kb.getNearestEnnemy();
			if (nearest != null) {
				System.out.println("Je suis #" + ent.getID() + " et j'attaque #" + nearest.getID());
				int angle = getAngle(kb.getX(), nearest.getX(), kb.getY(), nearest.getY());
				ent.setAngleTurret(angle);
				return Names.FIRE;
			}
		} else if (!ent.isReloading()) {
			return Names.RELOAD;
		}
		
		return Names.MOVE;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	public String getType() {
		return Names.ROCKET_LAUNCHER;
	}
	
	private int getAngle(double x1, double x2, double y1, double y2){
		int angle = 0;
        
		double radian = Math.atan2((y2 - y1), (x2 - x1));

        angle = (int) Math.toDegrees(radian);
        
        return angle;
	}
}
