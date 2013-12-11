package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.waritems.WarRocket;

public class FireTestRocketLauncherBehavior extends AbstractBehavior {			
	public FireTestRocketLauncherBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
	}

	@Override
	public void processMessage(WarMessage msg) {

	}

	@Override
	public String act() {
		super.act();
		WarBrain ent = getEntity().getBrain();
		KnowledgeBase kb = getEntity().getKnowledgeBase();
		if (ent.isBlocked()) {
			ent.setRandomHeading();
		}
		
		System.out.println("Je suis le rocket launcher #" + 
							getEntity().getBrain().getID() + " et je cherche quelqu'un à tuer en " +
							kb.getX() + " " + kb.getY());
		
		if (ent.isReloaded()) {
			EntityKnowledge nearest = kb.getNearestEnnemy();
			if (nearest != null) {
				System.out.println("Je suis #" + ent.getID() + " et j'attaque #" + nearest.getID());
				int angle = getAngle(kb.getX(), nearest.getX(), kb.getY(), nearest.getY());
				ent.setAngleTurret(angle);
				if (nearest.getDistance(getKnowledgeBase().getX(), getKnowledgeBase().getY()) <= 23 * WarRocket.SPEED + 10) {//10 pour avoir une marge //TODO passer de 23 à WarRocket.AUTONOMY lorsque warbot sera corrigé
					return Names.FIRE;
				}
			}
		} else if (!ent.isReloading()) {
			return Names.RELOAD;
		}
		
		return Names.MOVE;
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
