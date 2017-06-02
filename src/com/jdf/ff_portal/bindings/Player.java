package com.jdf.ff_portal.bindings;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

public class Player {
	private int playerId;
	private int active;
	private int jersey;
	private String lname;
	private String fname;
	private String displayName;
	private String team;
	private String position;
	private String height;
	private String weight;
	private String dob;
	private String college;
	private String owner;
	private int currentRank;
	private int startingRank;
	private int draftedSlot;
	
	public Player() {}
	public Player(int playerId, int active, int jersey, String lname, String fname, String displayName, String team, 
			String position, String height, String weight, String dob, String college){
		this.playerId = playerId;
		this.active = active;
		this.jersey = jersey;
		this.lname = lname;
		this.fname = fname;
		this.displayName = displayName;
		this.team = team;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.dob = dob;
		this.college = college;
	}
	
	@XmlAttribute  (name="active")
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	@XmlAttribute  (name="jersey")
	public int getJersey() {
		return jersey;
	}

	public void setJersey(int jersey) {
		this.jersey = jersey;
	}
	
	@XmlAttribute  (name="lname")
	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}
	
	@XmlAttribute  (name="fname")
	public String getFname() {
		return fname;
	}
	
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	@XmlAttribute  (name="displayName")
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@XmlAttribute  (name="team")
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	@XmlAttribute  (name="position")
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	@XmlAttribute  (name="height")
	public String getHeight() {
		return height;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	@XmlAttribute  (name="weight")
	public String getWeight() {
		return weight;
	}
	
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	@XmlAttribute  (name="dob")
	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	@XmlAttribute  (name="college")
	public String getCollege() {
		return college;
	}
	
	public void setCollege(String college) {
		this.college = college;
	}
	
	@XmlAttribute  (name="playerId")
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	
	@XmlAttribute  (name="currentRank")
	public int getCurrentRank() {
		if (currentRank ==0){
			currentRank = startingRank;
		}
		return currentRank;
	}

	public void setCurrentRank(int rank) {
		this.currentRank = rank;
	}
	
	@XmlAttribute  (name="startingRank")
	public int getStartingRank() {
		return startingRank;
	}
	public void setStartingRank(int startingRank) {
		this.startingRank = startingRank;
	}
	
	public void setOwner(String owner){
		this.owner= owner;
	}
	
	public String getOwner(){
		return owner;
	}
	
	@XmlAttribute  (name="draftedSlot")
	public int getDraftedSlot() {
		return draftedSlot;
	}
	public void setDraftedSlot(int draftedSlot) {
		this.draftedSlot = draftedSlot;
	}

	public Player getThisPlayer(){
		return this;
	}
}