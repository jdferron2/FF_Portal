package com.jdf.ff_portal.backend.data;

import java.sql.Date;

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
	private int weight;
	private Date dob;
	private int proRank;
	private SbfRank sbfRank;

	//	private String owner;
	private int currentRank;
	private int startingRank;
//	private int draftedSlot;
	
	public Player() {}
	public Player(int playerId, int jersey, String lname, String fname, String displayName, String team, 
			String position, String height, int weight, Date dob, int proRank){
		this.playerId = playerId;
		this.jersey = jersey;
		this.lname = lname;
		this.fname = fname;
		this.displayName = displayName;
		this.team = team;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.dob = dob;
		this.proRank = proRank;

	}
	
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	public int getJersey() {
		return jersey;
	}

	public void setJersey(int jersey) {
		this.jersey = jersey;
	}
	
	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public String getFname() {
		return fname;
	}
	
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public String getHeight() {
		return height;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public Date getDob() {
		return dob;
	}
	
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
//	public int getCurrentRank() {
//		if (currentRank ==0){
//			currentRank = startingRank;
//		}
//		return currentRank;
//	}
//
//	public void setCurrentRank(int rank) {
//		this.currentRank = rank;
//	}
	
//	public int getStartingRank() {
//		return startingRank;
//	}
//	public void setStartingRank(int startingRank) {
//		this.startingRank = startingRank;
//	}
//	
	public int getProRank() {
		return proRank;
	}
	public void setProRank(int proRank) {
		this.proRank = proRank;
	}
	
	public SbfRank getSbfRank() {
		return sbfRank;
	}
	public void setSbfRank(SbfRank sbfRank) {
		this.sbfRank = sbfRank;
	}
	
//	public void setOwner(String owner){
//		this.owner= owner;
//	}
//	
//	public String getOwner(){
//		return owner;
//	}
	
//	public int getDraftedSlot() {
//		return draftedSlot;
//	}
//	public void setDraftedSlot(int draftedSlot) {
//		this.draftedSlot = draftedSlot;
//	}

//	public int getSbfRankInt(){
//		return getSbfRank().getRank();
//	}
}