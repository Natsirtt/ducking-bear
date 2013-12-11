package edu.turtlekit2.warbot.duckingbear.explorers;

import java.util.List;

import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Un simple récolteur qui cherche de la nourriture et retourne a la base
 * lorsque le sac est plein.
 * 
 * TODO : ajouter des réflexes comme fuir les ennemis
 * TODO : Meilleur dispatch des recolteurs sur le terrain, trop aléatoire pour le moment.
 */
public class RecolterExplorerBehavior extends AbstractBehavior {
	/**
	 * La tâche du FSM en cours d'exécution.
	 */
	private Task pTask;
	private static final Task defaultTask = Task.SEARCH_FOOD;
	
	private enum Task {
		SEARCH_FOOD(), GO_TO_BASE();
		
		private Task() {
			//Nothing
		}
	}
	
	public RecolterExplorerBehavior(Entity entity) {
		super(entity);
		//Default task
		pTask = Task.SEARCH_FOOD;
	}
	
	private String goToBase() {
		//TODO implémenter une EntityKnowledge.getHeading(myX, myY) ou
		//TODO quelque chose du genre pour avoir le heading vers une
		//TODO EntityKnowledge directement.
		//getEntity().getKnowledgeBase().getMainBase()
		return Names.IDLE;
	}
	
	private String searchFood() {
		//Avant toute chose, si le sac est plein, on change d'état
		if (getEntity().getBrain().fullBag()) {
			pTask = Task.GO_TO_BASE;
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
				
				//Si on a trouver du miam miam, on se dirige vers lui
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
	
	private String runFSM(Task state) {
		switch (state) {
		case SEARCH_FOOD:
			return searchFood();
		case GO_TO_BASE:
			return goToBase();
		default:
			return runFSM(defaultTask);
		}
	}
	
	@Override
	public String act() {
		super.act();
		//Un récolteur va chercher de la nourriture jusqu'à être plein
		//il va alors ramener la nourriture à la base
		return runFSM(pTask);
	}

	@Override
	public void processMessage(WarMessage msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getType() {
		return Names.EXPLORER;
	}
}
