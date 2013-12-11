package edu.turtlekit2.warbot.duckingbear.rocketLaunchers;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ParticipantBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class DefaultRocketLauncherBehavior extends AbstractBehavior {
			
	public DefaultRocketLauncherBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
	}

	@Override
	public void processMessage(WarMessage msg) {
		//TODO les variables type "newContrat", "acceptContrat" etc... font partie
		//TODO du protocole des contrats. Il nous faudrait 1/une rapide spécification
		//TODO histoire d'être tous d'accord, mais surtout des constantes pour ne pas faire d'erreur
		if (msg.getMessage().equals("newContrat")) {
			getEntity().getBrain().reply(msg, "acceptContrat", msg.getContent());
			getEntity().setBehavior(new ParticipantBehavior(getEntity(), this));
		}
	}

	@Override
	public String act() {
		super.act();
		
		return Names.IDLE;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return getEntity().getKnowledgeBase();
	}
	
	public String getType() {
		return Names.ROCKET_LAUNCHER;
	}
	
}
