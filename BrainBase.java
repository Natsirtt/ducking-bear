package edu.turtlekit2.warbot.duckingbear;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.duckingbear.utils.Names;
import edu.turtlekit2.warbot.message.WarMessage;

public class BrainBase extends WarBrain{
	
	//Surcharge de setNextAgentCreate afin de palier au bug du moteur
	@Override
	public void setNextAgentCreate(String agent) {
		String rightStr = "";
		switch (agent) {
		case Names.EXPLORER:
			rightStr = "Explorer";
			break;
		case Names.ROCKET_LAUNCHER:
			rightStr = "RocketLauncher";
			break;
		default:
			throw new IllegalArgumentException("Impossible to create a " + agent);
		}
		super.setNextAgentCreate(rightStr);
	}

	public BrainBase(){
		
	}

	@Override
	public String action() {
		
		if(!emptyBag()){
			return "eat";
		}
		
		List<WarMessage> liste = getMessage();
		
		for(WarMessage m : liste){
			reply(m, "ici", null);
		}
		
		if(getEnergy() > 12000){
			setNextAgentCreate("Explorer");
			return "create";
		}
		
		return "idle";
	}

}
