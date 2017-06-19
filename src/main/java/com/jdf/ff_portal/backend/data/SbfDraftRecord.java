package com.jdf.ff_portal.backend.data;

import java.sql.Timestamp;;

public class SbfDraftRecord {
	protected int leagueId;
	protected int sbfId;
	protected int playerId;
	protected int slotDrafted;
	protected Timestamp timeDrafted;
	
	public SbfDraftRecord(int leagueId, int sbfId, int playerId, int slotDrafted, Timestamp timeDrafted){
		this.leagueId = leagueId;
		this.sbfId = sbfId;
		this.playerId = playerId;
		this.slotDrafted = slotDrafted;
		this.timeDrafted = timeDrafted;
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
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getSlotDrafted() {
		return slotDrafted;
	}
	public void setSlotDrafted(int slotDrafted) {
		this.slotDrafted = slotDrafted;
	}
	public Timestamp getTimeDrafted() {
		return timeDrafted;
	}
	public void setTimeDrafted(Timestamp timeDrafted) {
		this.timeDrafted = timeDrafted;
	}

}
