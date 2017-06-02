package com.jdf.ff_portal.UI.elements;


import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;

public class SortableTable {//extends Table {
	private static final long serialVersionUID = 1L;
	private boolean updating = false;
	
//	public boolean isUpdating() {
//		return updating;
//	}
//	public void setUpdating(boolean updating) {
//		this.updating = updating;
//	}
	public SortableTable() {
//		setDragMode(TableDragMode.ROW);
//		setSelectable(true);
//		this.setDropHandler( new DropHandler() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public AcceptCriterion getAcceptCriterion() {
//				return AcceptAll.get();
//			}
//
//			@Override
//			public void drop(DragAndDropEvent event) {
//				Transferable t = event.getTransferable();
//				Object sourceItemId = t.getData("itemId");
//
//				AbstractSelectTargetDetails dropData = (AbstractSelectTargetDetails)event.getTargetDetails();
//				Object targetItemId = dropData.getItemIdOver();
//
//				switch(dropData.getDropLocation()) {
//				case BOTTOM:
//					moveAfter(targetItemId, sourceItemId);
//					break;
//				case MIDDLE:
//				case TOP:
//					final Object prevItemId = prevItemId(targetItemId);
//					if (prevItemId == sourceItemId) break;
//					moveAfter(prevItemId, sourceItemId);
//					break;
//				}
//			}
//		});
	}
//	public Object moveAfter(Object targetItemId, Object sourceItemId) {
//		if(sourceItemId == null)
//			return null;
//		setUpdating(true);
//		Item sourceItem = getItem(sourceItemId);
//
//		Object[] propertyIds = getContainerPropertyIds().toArray();
//		int size = propertyIds.length;
//		Object[][] properties = new Object[size][2];
//
//		// backup source item properties and values
//		for(int i = 0; i < size; i++) {
//			Object propertyId = propertyIds[i];
//			Object value = sourceItem.getItemProperty(propertyId).getValue();
//			properties[i][0] = propertyId;
//			properties[i][1] = value;
//		}
//		removeItem(sourceItemId);
//		Item item = addItemAfter(targetItemId, sourceItemId);
//
//		// restore source item properties and values
//		for(int i = 0; i < size; i++) {
//			Object propertyId = properties[i][0];
//			Object value = properties[i][1];
//			item.getItemProperty(propertyId).setValue(value);
//		}
//		
//		int rowNum = 1;
//		for (Object itemId : this.getItemIds().toArray()){
//			Item currentItem = getItem(itemId);
//			currentItem.getItemProperty("Current Rank").setValue(rowNum);
//			
//			rowNum++;
//			
//		}
//		
//		this.select(sourceItemId);
//		//this.setCurrentPageFirstItemId(test);
//		
//		setUpdating(false);
//		return sourceItemId;
//	}
//	
//	public void rankUpdated(int newRank, Object updatedItemId){
//		setUpdating(true);
//		System.out.println("new rank:" + newRank);
//		int rowNum=1;
//		for (Object currentItemId : this.getItemIds().toArray()){
//			if(updatedItemId.equals(currentItemId)){
//				continue;
//			}
//			Item currentItem = getItem(currentItemId);
//			int currentRank = (Integer) currentItem.getItemProperty("Current Rank").getValue();
//			if (currentRank >= newRank){
//				currentItem.getItemProperty("Current Rank").setValue(currentRank+1);
//			}else{
//				currentItem.getItemProperty("Current Rank").setValue(currentRank-1);
//			}
//			
//			rowNum++;
//		}
//		this.select(updatedItemId);
//		setUpdating(false);
//	}
}
