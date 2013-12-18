package edu.turtlekit2.warbot.duckingbear.explorers;

import edu.turtlekit2.warbot.agents.WarRocketLauncher;
import edu.turtlekit2.warbot.duckingbear.AbstractBehavior;
import edu.turtlekit2.warbot.duckingbear.Entity;
import edu.turtlekit2.warbot.duckingbear.ManagerBehavior;
import edu.turtlekit2.warbot.duckingbear.bases.DefaultBaseBehavior;
import edu.turtlekit2.warbot.duckingbear.knowledge.EntityKnowledge;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class SquadLeaderExplorerBehavior extends AbstractBehavior {
	
	private enum Task {
		EXPLORE_RANDOMLY(), ANIHILATE_NEAREST_ENNEMY(), ANIHILATE_NEAREST_ENNEMY_BASE();
		
		private Task() {
			//Nothin'
		}
	}
	
	// Le num√©ro de la team qu'on controle
	private int myteam;
	private Task pTask;
	private static final Task DEFAULT_TASK = Task.EXPLORE_RANDOMLY;
	
	private ManagerBehavior membersContrat = null;

	public SquadLeaderExplorerBehavior(Entity entity, int teamNumber) {
		super(entity, teamNumber);
		
		myteam = -1;
		membersContrat = null;
		pTask = DEFAULT_TASK;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (getEntity().getBehavior() == this) {
			if (membersContrat != null) {
				membersContrat.processMessage(msg);
			}
		}
	}
	
	private String runFSM(Task state) {
		switch (state) {
		case EXPLORE_RANDOMLY:
			return randomExploration();
		case ANIHILATE_NEAREST_ENNEMY:
			return nearestEnnemyAnihilation();
		case ANIHILATE_NEAREST_ENNEMY_BASE:
			return nearestEnnemyBaseAnihilation();
		default:
			return runFSM(DEFAULT_TASK);
		}
	}
	
	private String randomExploration() {
		//On a 0.5% de chance de changer de trajectoire
		if (Math.random() <= (0.5 / 100)) {
			getEntity().getBrain().setRandomHeading();
		}
		return Names.MOVE;
	}
	
	private String nearestEnnemyAnihilation() {
		return Names.IDLE;
	}
	
	private String nearestEnnemyBaseAnihilation() {
		return Names.IDLE;
	}
	
	public String act() {
		String reflex = super.act();
		
		System.out.println("#" + getEntity().getBrain().getID() + " Je suis un leader ! Mon equipe #" + myteam);
		checkAdministrationStuff();
		
		if (reflex != null && !reflex.equals(Names.EAT)) { //TODO un squad leader devrait pouvoir manger tout son contenu mais Áa bug... processReflexes renvoi TOUJOURS eat ?!!
			return reflex;
		}
		
		return runFSM(pTask);
	}
	
	@Override
	protected String processReflexes() {
		String old = super.processReflexes();
		if (old != null) {
			return old;
		}
		if (getEntity().getBrain().isBlocked()) {
			getEntity().getBrain().setRandomHeading();
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
	
	private void checkAdministrationStuff() {
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
				String.valueOf(getEntity().getBrain().getID())
			};
			String s = "";
			for (int i = 0; i < content.length; i++) {
				s += " " + content[i];
			}
			System.out.println("# " + getEntity().getBrain().getID() + " j'envoie un message a mes membres : " + s);
			broadcastMessage("all", "leader", content);
		}
	}

	@Override
	public String getType() {
		return Names.EXPLORER;
	}
}
