package org.mule.extension.securelog.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.mule.extension.securelog.conf.SecureLogConfiguration;
import org.mule.extension.securelog.types.LogLevel;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureLogOperations {

	@Parameter
	@Optional
	private LogLevel logLevel;
	
	@Parameter
	private boolean logMessage = true;
	
	private static final Logger logger= LoggerFactory.getLogger(SecureLogOperations.class);
	private static String CHAR_SET_UTF8 = "UTF-8";
	private static String AES = "AES";
	private static String CIPHER = null;
    private byte[] iv = new byte[16];
	
	@MediaType(value = ANY,strict = false)
	public String encryptAES(@Config SecureLogConfiguration config, 
			@Expression(ExpressionSupport.SUPPORTED) String stringData,@Expression(ExpressionSupport.SUPPORTED) @Optional String preText
			) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
//		switch(config.getAesConf().getAesMode()) {
//		case ECB:
		CIPHER = "AES/ECB/PKCS5Padding";
//			break;
//		case CBC:
//			CIPHER = "AES/CBC/PKCS5Padding";
//			break;
//		}
		
		StringBuilder msg = new StringBuilder();
		if(preText != null) {
			msg.append(preText).append(" ");
		}
		SecretKeySpec newKey = new SecretKeySpec(config.getKey().getBytes(CHAR_SET_UTF8), AES);
		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(Cipher.ENCRYPT_MODE, newKey);
		byte[] cipherText = cipher.doFinal(stringData.getBytes(CHAR_SET_UTF8));
		String encryptedData = Base64.getEncoder().encodeToString(cipherText);
		msg.append(encryptedData);
		if(logMessage) {
			logMsg(msg.toString());
		}
		return msg.toString();
	}
	
	@MediaType(value = ANY,strict = false)
	public String decryptAES(@Config SecureLogConfiguration config, 
			@Expression(ExpressionSupport.SUPPORTED) String data,@Expression(ExpressionSupport.SUPPORTED) @Optional String preText
			) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
//		switch(config.getAesConf().getAesMode()) {
//		case ECB:
			CIPHER = "AES/ECB/PKCS5Padding";
//			break;
//		case CBC:
//			CIPHER = "AES/CBC/PKCS5Padding";
//			break;
//		}
		
		StringBuilder msg = new StringBuilder();
		if(preText != null) {
			msg.append(preText).append(" ");
		}
		SecretKeySpec newKey = new SecretKeySpec(config.getKey().getBytes(CHAR_SET_UTF8), AES);
		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(Cipher.DECRYPT_MODE, newKey);
		byte[] cipherText = Base64.getDecoder().decode(data.getBytes(CHAR_SET_UTF8));
		byte[] decodedBytes = cipher.doFinal(cipherText);
		String decrytedStr = new String(decodedBytes);
		msg.append(decrytedStr);
		if(logMessage) {
			logMsg(msg.toString());
		}
		return msg.toString();
	}
	
	@Ignore
	public void logMsg(String message) {
		switch(this.logLevel) {
		case INFO:
			logger.info(message);
			break;
		case DEBUG:
			logger.debug(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		}
	}
}
