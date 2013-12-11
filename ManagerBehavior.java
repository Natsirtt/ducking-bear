package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class ManagerBehavior extends AbstractBehavior {
	public static int contract_number = 0;
	
	private Behavior oldBehavior;
	private Entity entity;
	private String type;
	private int number; // Le nombre d'unités que l'on veut
	private int contratID;
	private boolean sent; // Le contrat à-t-il été envoyé
	private int answer; // Le nombre de réponses
	private int positiveAnswers; // Le nombre de réponses
	private String newBehavior;
	
	public ManagerBehavior(Entity entity, Behavior oldBehavior, String type, int number, String newBehavior) {
		super(entity);
		this.oldBehavior = oldBehavior;
		this.entity = entity;
		this.type = type;
		this.number = number;
		sent = false;
		contratID = contract_number++;
		answer = 0;
		positiveAnswers = 0;
		this.newBehavior = newBehavior;
	}

	@Override
	public String act() {
		super.act();
		if (!sent) {
			sendContract();
			sent = true;
		} else {
			KnowledgeBase kb = getKnowledgeBase();
			int count = 0;
			if (type == Names.EXPLORER) {
				count = kb.getAlliedExplorerCount();
			} else if (type == Names.ROCKET_LAUNCHER) {
				count = kb.getAlliedRocketLaucherCount();
			} else {
				count = kb.getAlliedBaseCount();
			}
			if (count <= answer) {
				entity.setBehavior(oldBehavior);
			}
		}

		return Names.IDLE;
	}

	@Override
	public void processMessage(WarMessage msg) {
		oldBehavior.processMessage(msg);
		if (msg.getMessage().equals("acceptContrat")) {
			if (positiveAnswers < number) {
				entity.getBrain().reply(msg, "acceptParticipant", msg.getContent());
			} else {
				entity.getBrain().reply(msg, "refuseParticipant", msg.getContent());
			}
			answer++;
			positiveAnswers++;
		} else if (msg.getMessage().equals("refuseContrat")) {
			answer++;
		} else if (msg.getMessage().equals("newContrat")) {
			entity.getBrain().reply(msg, "refuseContrat", msg.getContent());
		}
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return oldBehavior.getKnowledgeBase();
	}

	@Override
	public String getType() {
		return oldBehavior.getType();
	}
	
	private void sendContract() {
		String[] content = new String[]{String.valueOf(contratID), newBehavior};
		entity.getBrain().broadcastMessage(type, "newContrat", content);
	}
}
