package edu.turtlekit2.warbot.duckingbear.explorers;

import java.util.List;

import edu.turtlekit2.warbot.agents.WarExplorer;
import edu.turtlekit2.warbot.agents.WarRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ParticipantBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

/**
 * Soigne l'entit�e la plus faible.
 */
public class HealerExplorerBehavior extends AbstractBehavior {
	
	/**
	 * La t�che du FSM en cours d'execution.
	 */
	private Task pTask;
	private EntityKnowledge target;
	private int distanceToGo;
	private boolean isGoingFarFarAway;
	
	private static final Task DEFAULT_TASK = Task.SEARCH_FOOD;
	
	private enum Task {
		SEARCH_FOOD(), GO_HEAL_TARGET();
		
		private Task() {
			//Nothin'
		}
	}

	public HealerExplorerBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		//Default task
		pTask = DEFAULT_TASK;
		target = null;
		isGoingFarFarAway = false;
		distanceToGo = 0;
	}
	
	private String runFSM(Task state) {
		switch (state) {
		case SEARCH_FOOD:
			return searchFood();
		case GO_HEAL_TARGET:
			return goHealTarget();
		default:
			return runFSM(DEFAULT_TASK);
		}
	}

	@Override
	public String act() {
		String reflex = super.act();
		if (reflex != null) {
			return reflex;
		}
		return runFSM(pTask);
	}
	
	private EntityKnowledge getWeakestAllyRocketLauncher() {
		EntityKnowledge res = null;
		for (EntityKnowledge ek : getKnowledgeBase().getAllies()) {
			if (res == null || (res.getEnergy() > ek.getEnergy()) && ek.getEnergy() > 0) {
				if (getKnowledgeBase().isInformationReliable(ek)) {
					if (ek.getType().equals(Names.ROCKET_LAUNCHER)) {
						res = ek;
					}
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
			getEntity().getBrain().setAgentToGive(target.getID());
			if (getEntity().getBrain().emptyBag()) {
				target = null;
				pTask = Task.SEARCH_FOOD;
				return Names.IDLE;
			}
			return Names.GIVE;
		}
		return Names.MOVE;
	}
	
	private String searchFood() {
		//Avant toute chose, si le sac est plein et que personne n'a demand�
		//d'aide, on va se poster pr�s de la base
		if (getEntity().getBrain().fullBag()) {
			pTask = Task.GO_HEAL_TARGET;
			return runFSM(pTask);
		}
		
		//On r�cup�re les percepts
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
		
		//Si on a trouv� du miam miam, on se dirige vers lui
		if (food != null) {
			getEntity().getBrain().setHeading(food.getAngle());
		}
		//Sinon on a 0.1% de chance de changer de trajectoire
		else if (Math.random() <= (0.1 / 100)) {
			getEntity().getBrain().setRandomHeading();
		}
		//si on est bloqu� on fait demi tour
		if (getEntity().getBrain().isBlocked()) {
			getEntity().getBrain().setHeading(getEntity().getBrain().getHeading() - 180);
			goFarFarAway(60);
		}
		
		if ((food != null) && (food.getDistance() <= WarFood.MAX_DISTANCE_TAKE)) {
			return Names.TAKE;
		}
		
		return Names.MOVE;
	}
	
	private void goFarFarAway(int ticksToMoveForward) {
		distanceToGo = ticksToMoveForward;
		isGoingFarFarAway = true;
	}
	
	@Override
	protected String processReflexes() {
		String old = super.processReflexes();
		if (old != null && !old.equals(Names.EAT)) {
			return old;
		}
		if (isGoingFarFarAway) {
			distanceToGo--;
			if (distanceToGo <= 0) {
				isGoingFarFarAway = false;
			}
			return Names.MOVE;
		}
		//On reste a distance raisonnable d'un tank ennemi
		EntityKnowledge nearestEnnemy = getKnowledgeBase().getNearestEnnemy();
		if (nearestEnnemy != null && nearestEnnemy.getType().equals(Names.ROCKET_LAUNCHER)) {
			if (getKnowledgeBase().getDistance(nearestEnnemy.getID()) <= WarRocketLauncher.RADIUS + 8) {
				getEntity().getBrain().setHeading(getKnowledgeBase().getHeading(nearestEnnemy.getID()) + 180);
				return Names.MOVE;
			}
		}
		return null;
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
