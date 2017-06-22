package com.jdf.ff_portal.backend.data;

public class SbfDraftPick {
	private int leagueId;
	private int sbfId;
	private int pick;

	public SbfDraftPick() {}
	public SbfDraftPick(int leagueId, int sbfId, int pick){
		this.leagueId = leagueId;
		this.sbfId = sbfId;
		this.pick = pick;
	}
	
	public int getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	public int getSbfId() {
		return sbfId;
	}
	public void setSbfId(int sbfId) {
		this.sbfId = sbfId;
	}
	public int getPick() {
		return pick;
	}
	public void setPick(int pick) {
		this.pick = pick;
	}
}
