package org.mule.extension.securelog.conf;

import org.mule.extension.securelog.types.AesMode;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class AesConf {
	
	@Parameter
	private AesMode aesMode;

	public AesMode getAesMode() {
		return aesMode;
	}

	public void setAesMode(AesMode aesMode) {
		this.aesMode = aesMode;
	}
	
}
