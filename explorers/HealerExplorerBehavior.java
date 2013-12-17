package edu.turtlekit2.warbot.duckingbear.explorers;

import java.util.List;

import edu.turtlekit2.warbot.agents.WarExplorer;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ParticipantBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Soigne l'entitée la plus faible.
 */
public class HealerExplorerBehavior extends AbstractBehavior {
	
	/**
	 * La tâche du FSM en cours d'execution.
	 */
	private Task pTask;
	private EntityKnowledge target;
	
	private static final Task defaultTask = Task.SEARCH_FOOD;
	
	private enum Task {
		SEARCH_FOOD(), GO_HEAL_TARGET();
		
		private Task() {
			//Nothin'
		}
	}

	public HealerExplorerBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		//Default task
		pTask = defaultTask;
		target = null;
	}
	
	private String runFSM(Task state) {
		switch (state) {
		case SEARCH_FOOD:
			return searchFood();
		case GO_HEAL_TARGET:
			return goHealTarget();
		default:
			return runFSM(defaultTask);
		}
	}

	@Override
	public String act() {
		super.act();
		return runFSM(pTask);
	}
	
	private EntityKnowledge getWeakestAllyRocketLauncher() {
		EntityKnowledge res = null;
		for (EntityKnowledge ek : getKnowledgeBase().getAllies()) {
			if (res == null || res.getEnergy() > ek.getEnergy()) {
				if (ek.getType().equals(Names.ROCKET_LAUNCHER)) {
					res = ek;
				}
			}
		}
		return res;
	}
	
	private String goHealTarget() {
		if (getEntity().getBrain().getEnergy() < WarExplorer.MAX_ENERGY) {
			pTask = Task.SEARCH_FOOD;
			return Names.EAT;
		}
		
		target = getWeakestAllyRocketLauncher();
		if (target == null) {
			return Names.IDLE;
		}
		getEntity().getBrain().setHeading(getKnowledgeBase().getHeading(target.getID()));
		if (getKnowledgeBase().getDistance(target.getID()) <= 1) {
			pTask = Task.SEARCH_FOOD;
			getEntity().getBrain().setAgentToGive(target.getID());
			target = null;
			return Names.GIVE;
		}
		return Names.MOVE;
	}
	
	private String searchFood() {
		//Avant toute chose, si le sac est plein et que personne n'a demandé
		//d'aide, on va se poster près de la base
		if (getEntity().getBrain().fullBag()) {
			pTask = Task.GO_HEAL_TARGET;
			return runFSM(pTask);
		}
		
		//On récupère les percepts
		List<Percept> percepts = getEntity().getBrain().getPercepts();
		Percept food = null;
		for (Percept p : percepts) {
			if (p.getType().equals(Names.FOOD)) {
				if (food == null) {
					food = p;
				} else {
					//On garde la food la plus proche
					if (food.getDistance() > p.getDistance()) {
						food = p;
					}
				}
			}
		}
		
		//Si on a trouvé du miam miam, on se dirige vers lui
		if (food != null) {
			getEntity().getBrain().setHeading(food.getAngle());
		}
		//Sinon on a 0.1% de chance de changer de trajectoire
		else if (Math.random() <= (0.1 / 100)) {
			getEntity().getBrain().setRandomHeading();
		}
		//si on est bloqué on fait demi tour
		if (getEntity().getBrain().isBlocked()) {
			getEntity().getBrain().setHeading(getEntity().getBrain().getHeading() - 180);
		}
		
		if ((food != null) && (food.getDistance() <= 1)) { //TODO <= 1 pour ne pas qu'un bot ne bug a tourner autour d'une food. Valeur empirique.
			return Names.TAKE;
		}
		
		return Names.MOVE;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (msg.getMessage().equals("newContrat")) {
			getEntity().getBrain().reply(msg, "acceptContrat", msg.getContent());
			getEntity().setBehavior(new ParticipantBehavior(getEntity(), this, Integer.parseInt(msg.getContent()[0])));
		}
	}

	@Override
	public String getType() {
		return Names.EXPLORER;
	}
}
