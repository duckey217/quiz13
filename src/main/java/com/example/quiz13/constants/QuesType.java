package com.example.quiz13.constants;

public enum QuesType {
	
	
	SINGLE("Single"),//
	MULTI("Multi"),//
	TEXT("Text"),//
	QUES_TYPE_ERROR("Param type error");

	
	private String type;

	private QuesType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static boolean checkType(String inputType) {
		for(QuesType item :QuesType.values()) {
			if(inputType.equalsIgnoreCase(item.getType())) {
				return true;
			}
		}
		return false;
	}
	
}
