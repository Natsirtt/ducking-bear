package edu.turtlekit2.warbot.duckingbear;

public interface Behavior {
	/**
	 * Traite tous les messages reçus. Peut changer le comportement en fonction.
	 */
	void processMessages();
	
	/**
	 * Agit en fonction du comportement.
	 * @return L'action à éxécuter.
	 */
	String act();
}
