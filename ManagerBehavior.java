package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class ManagerBehavior extends AbstractBehavior {
	public static int contract_number = 0;
	public static final int CONTRAT_TIMEOUT = 10;
	
	private Entity entity;
	private String type;
	private int number; // Le nombre d'unités que l'on veut
	private int contratID;
	private Behavior oldBehavior;
	private boolean sent; // Le contrat à-t-il été envoyé
	private int answer; // Le nombre de réponses
	private int positiveAnswers; // Le nombre de réponses
	private String newBehavior;
	private boolean isOver;
	
	private int timeout;
	
	public ManagerBehavior(Entity entity, Behavior oldBehavior, String type, int number, String newBehavior) {
		super(entity, oldBehavior.getTeamNumber());
		this.entity = entity;
		this.type = type;
		this.oldBehavior = oldBehavior;
		this.number = number;
		sent = false;
		contratID = contract_number++;
		answer = 0;
		positiveAnswers = 0;
		isOver = false;
		this.newBehavior = newBehavior;
		timeout = 0;
	}

	@Override
	public String act() {
		if (!isOver()) {
			if (getKnowledgeBase().getActiveContrat(type) == contratID) {
				System.out.println("Je suis le contrat " + contratID + " sur des " + type + " et je suis actif");
				if (!sent) {
					sendContract();
					sent = true;
					timeout = CONTRAT_TIMEOUT;
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
					if ((count <= answer) || (timeout <= 0)) {
						isOver = true;
						String[] content = new String[]{
							type,
							String.valueOf(contratID)
						};
						getEntity().getBehavior().broadcastMessage("all", "endContract", content);
					}
					timeout--;
				}
			} else {
				System.out.println("Je suis le contrat " + contratID + " sur des " + type + " et je ne suis pas actif");
				System.out.println("  Le contrat actif est " + getKnowledgeBase().getActiveContrat(type));
				String[] content = new String[]{
					type,
					String.valueOf(contratID)
				};
				getEntity().getBehavior().broadcastMessage("all", "beginContract", content);
			}
		}
		return Names.IDLE;
	}

	@Override
	public void processMessage(WarMessage msg) {
		if (!isOver()) {
			if (msg.getMessage().equals("acceptContrat")) {
				if (Integer.parseInt(msg.getContent()[0]) != contratID) {
					return;
				}
				if (positiveAnswers < number) {
					entity.getBrain().reply(msg, "acceptParticipant", msg.getContent());
				} else {
					entity.getBrain().reply(msg, "refuseParticipant", msg.getContent());
				}
				answer++;
				positiveAnswers++;
			} else if (msg.getMessage().equals("refuseContrat")) {
				if (Integer.parseInt(msg.getContent()[0]) != contratID) {
					return;
				}
				answer++;
			} else if (msg.getMessage().equals("newContrat")) {
				if (Integer.parseInt(msg.getContent()[0]) != contratID) {
					return;
				}
				entity.getBrain().reply(msg, "refuseContrat", msg.getContent());
			}
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
	
	public boolean isOver() {
		return isOver;
	}
	
	public int getContratID() {
		return contratID;
	}
	
	private void sendContract() {
		String[] content = new String[]{String.valueOf(contratID), newBehavior};
		entity.getBrain().broadcastMessage(type, "newContrat", content);
	}
}
