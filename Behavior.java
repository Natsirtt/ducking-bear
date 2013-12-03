package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;
import edu.turtlekit2.warbot.message.WarMessage;


public interface Behavior {
	/**
	 * Agit en fonction du comportement.
	 * @return L'action à éxécuter.
	 */
	String act();
	
	public void processMessage(WarMessage msg);
	
	public KnowledgeBase getKnowledgeBase();
	
	public String getType();
}
