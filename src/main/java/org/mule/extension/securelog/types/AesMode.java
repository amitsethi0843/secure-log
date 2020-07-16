package org.mule.extension.securelog.types;

public enum AesMode {
	
	ECB,CBC;
	
	public String getName() {
		return this.name();
	}
}
