package org.mule.extension.securelog.conf;

import org.mule.extension.securelog.internal.SecureLogOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(SecureLogOperations.class)
public class SecureLogConfiguration {

  @Parameter
  @Optional
  private AesConf aesConf;
  
  @Parameter
  private String key;

  public AesConf getAesConf() {
		return aesConf;
  }
	
  public String getKey() {
		return key;
  }

  
}
