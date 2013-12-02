package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.BrainRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultRocketLauncherBehavior extends AbstractBehavior {
	private BrainRocketLauncher entity;
			
	public DefaultRocketLauncherBehavior(BrainRocketLauncher entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	protected void processMessage(WarMessage msg) {

	}

	@Override
	public String act() {
		super.act();
		KnowledgeBase kb = entity.getKnowledgeBase();
		
		if (entity.isReloaded()) {
			EntityKnowledge nearest = kb.getNearestEnnemy();
			if (nearest != null) {
				int angle = getAngle(kb.getX(), nearest.getX(), kb.getY(), nearest.getY());
				entity.setAngleTurret(angle);
				return Names.FIRE;
			}
		} else if (!entity.isReloading()) {
			return Names.RELOAD;
		}
		
		return Names.IDLE;
	}

	@Override
	protected KnowledgeBase getKnowledgeBase() {
		return entity.getKnowledgeBase();
	}
	
	protected String getType() {
		return Names.ROCKET_LAUNCHER;
	}
	
	private int getAngle(double x1, double x2, double y1, double y2){
		int angle = 0;
        
		double radian = Math.atan2((y2 - y1), (x2 - x1));

        angle = (int) Math.toDegrees(radian);
        
        return angle;
	}
}
