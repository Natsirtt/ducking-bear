package edu.turtlekit2.warbot.duckingbear;

import java.lang.reflect.InvocationTargetException;

import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class ParticipantBehavior extends AbstractBehavior {	
	private Behavior oldBehavior;
	private Entity entity;
	private int contratID;
	
	public ParticipantBehavior(Entity entity, Behavior oldBehavior, int contratID) {
		super(entity, oldBehavior.getTeamNumber());
		this.oldBehavior = oldBehavior;
		this.entity = entity;
		this.contratID = contratID;
	}

	@Override
	public String act() {
		super.act();

		return Names.IDLE;
	}

	@Override
	public void processMessage(WarMessage msg) {
		oldBehavior.processMessage(msg);
		if (msg.getMessage().equals("acceptParticipant")) {
			if (Integer.parseInt(msg.getContent()[0]) != contratID) {
				return;
			}
			String[] content = msg.getContent();
			try {
				Behavior b = (Behavior) Class.forName(content[1])
											.getConstructor(Entity.class, int.class)
												.newInstance(entity, Integer.parseInt(content[0]));
				entity.setBehavior(b);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				System.err.println("Le cast du behavior a échoué");
				e.printStackTrace();
			}
		} else if (msg.getMessage().equals("refuseParticipant")) {
			if (Integer.parseInt(msg.getContent()[0]) != contratID) {
				return;
			}
			entity.setBehavior(oldBehavior);
		} else if (msg.getMessage().equals("newContrat")) {
			if (Integer.parseInt(msg.getContent()[0]) != contratID) {
				return;
			}
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
	
}
