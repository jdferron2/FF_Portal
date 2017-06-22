package com.jdf.ff_portal.backend.data;

public class SbfRank {
	protected int sbfId;
	protected int playerId;
	protected int rank;
	protected boolean flagForUpdate;
	public SbfRank(int sbfId, int playerId, int rank){
		this.sbfId = sbfId;
		this.playerId = playerId;
		this.rank=rank;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public boolean isFlagForUpdate() {
		return flagForUpdate;
	}
	public void setFlagForUpdate(boolean flagForUpdate) {
		this.flagForUpdate = flagForUpdate;
	}
}
