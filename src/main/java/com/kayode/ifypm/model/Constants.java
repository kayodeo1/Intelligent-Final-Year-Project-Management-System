/**
 *
 */
package com.kayode.ifypm.model;
import java.util.ResourceBundle;
/**
 *
 */
public class Constants {
	static ResourceBundle rs = ResourceBundle.getBundle("resources.config");
	public static String APP_BASE_NAME;
	public static String UPLOAD_PATH_DIR;
	public static String UPLOAD_PATH_DIR_MAC;
	public static String FILE_UPLOAD_PATH;
	public static String MAIL_NOTIFICATION_SERVICE;
	public static String ISAFE_URL;
	public static String LOG_PATH;

	// Email Configuration
	public static String EMAIL_FROM;

	// Template 1: Course Creation Notification (For Admins)
	public static String EMAIL_TEMPLATE1_SUBJECT;
	public static String EMAIL_TEMPLATE1_BODY;

	// Template 2: Course Published Notification (For Target Audience)
	public static String EMAIL_TEMPLATE2_SUBJECT;
	public static String EMAIL_TEMPLATE2_BODY;

	// Template 3: Course Expiration Warning (For Target Audience)
	public static String EMAIL_TEMPLATE3_SUBJECT;
	public static String EMAIL_TEMPLATE3_BODY;
	
	public static String EMBEDDING_MODEL_NAME;
	public static String EMBEDDING_MODEL_URL;


	

	static {
		APP_BASE_NAME = rs.getString("app.base.name");
		EMBEDDING_MODEL_NAME = rs.getString("app.ai.embeddingmodel.name");
		EMBEDDING_MODEL_URL = rs.getString("app.ai.embeddingmodel.url");

		
	}
}