package bridge;

import java.util.ArrayList;

import merapi.messages.Message;

public class MsgData extends Message {

	private int status;
	@SuppressWarnings("rawtypes")
	private ArrayList objectData;
	private String sender;

	public MsgData() {
		super(MsgHandler.MESSAGE);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getObjectData() {
		return objectData;
	}
	
	@SuppressWarnings("rawtypes")
	public void setObjectData(ArrayList objectData) {
		this.objectData = objectData;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void clearObjectData() {
		objectData.clear();
	}
}
