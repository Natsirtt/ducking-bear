package edu.turtlekit2.warbot.duckingbear;


public class FakeMessage {
	
	private int						_angle = 0;
	private int						_sender = 0;
	private String					_senderTeam = "";
	private String					_type = "";
	private String					_message = "";
	private String[]				_content = null;
	
	public FakeMessage(int id, int angle, String team, String type, String msg, String[] content){
		_angle = angle;
		_sender = id;
		_senderTeam = team;
		_type = type;
		_message = msg;
		_content = content;
	}
	
	/**
	 * Methode permettant de renvoyer l'angle de la direction de l'envoyeur du message. 
	 * 
	 * @return {@code int} - l'angle de la direction de l'envoyeur du message
	 */
	public int getAngle(){
		return _angle;
	}
	
	/**
	 * Methode permettant de renvoyer la distance entre l'envoyeur et le recepteur
	 * du message.
	 * 
	 * @return {@code int} - la distance entre l'envoyeur et le recepteur
	 */
	public int getDistance(){
		return 0;
	}
	
	/**
	 * Methode renvoyant le numero de l'agent envoyeur du message.
	 * 
	 * @return {@code int} - le numero de l'agent envoyeur du message
	 */
	public int getSender(){
		return _sender;
	}
	
	/**
	 * Methode renvoyant l'equipe a laquelle l'agent envoyeur appartient.
	 * 
	 * @return {@code String} - l'equipe a laquelle l'agent envoyeur appartient
	 */
	public String getSenderTeam(){
		return _senderTeam;
	}
	
	/**
	 * Methode renvoyant le type de l'agent envoyeur du message.
	 * 
	 * @return {@code String} - le type de l'agent envoyeur du message
	 * @deprecated
	 */
	public String getType(){
		return _type;
	}
	/**
	 * Methode renvoyant le type de l'agent envoyeur du message.
	 * 
	 * @return {@code String} - le type de l'agent envoyeur du message
	 */
	
	public String getSenderType(){
		return _type;
	}
	
	/**
	 * Methode permettant de recuperer le message envoye.
	 * 
	 * @return {@code String} - le message envoye.
	 */
	public String getMessage(){
		return _message;
	}
	
	/**
	 * Methode permettant de recuperer le message envoye.
	 * 
	 * @return {@code String} - le message envoye.
	 */
	public String getPerformative(){
		return _message;
	}
	
	/**
	 * Methode permettant de recuperer les differentes informations 
	 * transmises.
	 * 
	 * @return {@code String[]} - les differentes informations 
	 * transmises sous forme d'un tableau de chaine de caractere
	 */
	public String[] getContent(){
		return _content;
	}
	
	/**
	 * Methode permettant de retourner pour l'affichage, l'ensemble 
	 * des informations concernant le message.
	 */
	public String toString(){
		String retour = "";
		
		retour = "--- Message : "+getType()+" "+getSender()+" - Equipe : "+getSenderTeam()+" - "+getAngle()+"° "+getDistance()+" => "+getMessage();
		
		return retour;
	}

}
