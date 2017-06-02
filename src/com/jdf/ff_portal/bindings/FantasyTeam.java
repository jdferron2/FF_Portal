package com.jdf.ff_portal.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  
 
public class FantasyTeam {
	private String owner;
	private int draftPosition;
	private List<Player> players;

	public FantasyTeam() {}
	public FantasyTeam(String owner, int draftPosition, List<Player> players){
		this.owner = owner;
		this.draftPosition = draftPosition;
		this.players = players;
	}
	
	@XmlAttribute  (name="owner")
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@XmlAttribute  (name="draftPosition")
	public int getDraftPosition() {
		return draftPosition;
	}
	public void setDraftPosition(int draftPosition) {
		this.draftPosition = draftPosition;
	}
	
	@XmlElement(name="Player")
    public List<Player> getPlayers() {
        if (players == null) {
        	players = new ArrayList<Player>();
        }
        return this.players;
    }
	
	public void setPlayers(List<Player> players){
		this.players = players;
		
	}
	
	public void addPlayer(Player player){
		getPlayers().add(player);
	}

}

