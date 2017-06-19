package com.jdf.ff_portal.backend.data;

public class SbfTeam {
	private String ownerName;
	private int draftSlot;
	private int sbfId;

	public SbfTeam() {}
	public SbfTeam(String owner, int draftPosition, int id){
		this.ownerName = owner;
		this.draftSlot = draftPosition;
		this.sbfId = id;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String owner) {
		this.ownerName = owner;
	}
	
	public int getDraftSlot() {
		return draftSlot;
	}
	public void setDraftSlot(int draftPosition) {
		this.draftSlot = draftPosition;
	}
	
	public int getSbfId() {
		return sbfId;
	}
	public void setSbfId(int sbfId) {
		this.sbfId = sbfId;
	}

}
