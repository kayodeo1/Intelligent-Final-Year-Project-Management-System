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

	static {
		APP_BASE_NAME = rs.getString("app.base.name");
		UPLOAD_PATH_DIR = rs.getString("media.upload.dir.path.mac");
		UPLOAD_PATH_DIR_MAC = rs.getString("media.upload.dir.path.mac");
		FILE_UPLOAD_PATH = rs.getString("FILE_UPLOAD_PATH");
		MAIL_NOTIFICATION_SERVICE = rs.getString("notification.email.service");
		ISAFE_URL = rs.getString("isafe.url");
		LOG_PATH = rs.getString("isafe.log");

		// Email Configuration
		EMAIL_FROM = rs.getString("isafe.email.from");

		// Template 1: Course Creation Notification (For Admins)
		EMAIL_TEMPLATE1_SUBJECT = rs.getString("isafe.email.template1.subject");
		EMAIL_TEMPLATE1_BODY = rs.getString("isafe.email.template1.body");

		// Template 2: Course Published Notification (For Target Audience)
		EMAIL_TEMPLATE2_SUBJECT = rs.getString("isafe.email.template2.subject");
		EMAIL_TEMPLATE2_BODY = rs.getString("isafe.email.template2.body");

		// Template 3: Course Expiration Warning (For Target Audience)
		EMAIL_TEMPLATE3_SUBJECT = rs.getString("isafe.email.template3.subject");
		EMAIL_TEMPLATE3_BODY = rs.getString("isafe.email.template3.body");
	}
}