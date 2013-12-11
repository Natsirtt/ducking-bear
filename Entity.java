package edu.turtlekit2.warbot.duckingbear;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.knowledge.KnowledgeBase;

/**
 * Interface pour obtenir un comportement commun aux différente unités.
 */
public interface Entity {
	KnowledgeBase getKnowledgeBase();
	void setBehavior(Behavior behavior);
	WarBrain getBrain();
}
