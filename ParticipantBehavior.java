package edu.turtlekit2.warbot.duckingbear;

import java.lang.reflect.InvocationTargetException;

import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class ParticipantBehavior extends AbstractBehavior {	
	private Behavior oldBehavior;
	private Entity entity;
	
	public ParticipantBehavior(Entity entity, Behavior oldBehavior) {
		super(entity);
		this.oldBehavior = oldBehavior;
		this.entity = entity;
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
			String[] content = msg.getContent();
			try {
				Behavior b = (Behavior)Class.forName(content[1]).getConstructor(Entity.class).newInstance(entity);
				entity.setBehavior(b);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (msg.getMessage().equals("refuseParticipant")) {
			entity.setBehavior(oldBehavior);
		} else if (msg.getMessage().equals("newContrat")) {
			entity.getEntity().reply(msg, "refuseContrat", msg.getContent());
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
