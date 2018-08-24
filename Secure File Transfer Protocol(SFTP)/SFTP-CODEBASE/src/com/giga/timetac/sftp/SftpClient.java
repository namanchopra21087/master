package com.giga.timetac.sftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpClient {

	private static JSch jsch = null;
	private static Session session = null;
	private static Channel channel = null;
	private static ChannelSftp channelSftp = null;

	public SftpClient() {
	}

	public static String copyToSftp(String inputDir, String destDir,
			String fileStartsWith) {
		try {
			StringBuffer sb = new StringBuffer("");
			Calendar midnightToday = new GregorianCalendar();
			midnightToday.set(Calendar.HOUR_OF_DAY, 0);
			midnightToday.set(Calendar.MINUTE, 0);
			midnightToday.set(Calendar.SECOND, 0);
			midnightToday.set(Calendar.MILLISECOND, 0);

			File directory = new File(inputDir);
			File[] files = directory.listFiles();
			for (File file : files) {
				Date lastMod = new Date(file.lastModified());
				if (file.getName().startsWith(fileStartsWith)
						&& lastMod.compareTo(midnightToday.getTime()) > 0) {
					channelSftp.put(file.toString(), destDir);
					sb.append("File >> " + file + " Copied Successfully.");
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
	}

	public static void sendEmail(String fileName, String errorMsg,Address[] toAddr)
			throws AddressException {

		String host = "183.81.161.125";
		final String user = "systems@gmg.my";// change accordingly
		final String password = "A1b2C3d4E5";// change accordingly

		String company = fileName;
		
		if ("NMC".equals(fileName))
			company = "GIGA HQ";

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(
				props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				});

		// Compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			// message.addRecipient(Message.RecipientType.TO, new
			// InternetAddress(to));
			message.addRecipients(Message.RecipientType.TO, toAddr);
			message.setSubject("TimeTac File Transfer to RAMCO SFTP has failed @ "+new Date());
			message.setText(company
					+ "TimeTac File Transfer to RAMCO SFTP has failed due to "
					+ errorMsg + " . Please contact GIGA IT.");

			// send the message
			Transport.send(message);

			System.out.println("Email has been sent successfully.");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("logfile.txt", true)));
		String fileNameStartsWith = "";
		Address[] toAddr = null;
		try {
			out.println("===========================================================================================================");
			out.println("Batch Started @ " + new Date());

			ResourceBundle bundle = ResourceBundle.getBundle("SFTPProperties");
			String hostname = bundle.getString("hostname");
			String port = bundle.getString("port");
			String username = bundle.getString("username");
			String password = bundle.getString("password");
			String srcDir = bundle.getString("inputFolder");
			String destDir = bundle.getString("destinationFolder");
			fileNameStartsWith = bundle.getString("fileNameStartsWith");
			
			String emailAddr[] = bundle.getString("email").split(",");
			toAddr = new Address[emailAddr.length];
			
			if (emailAddr != null && emailAddr.length > 0) {
				for (int i = 0; i < emailAddr.length; i++) {
					toAddr[i] = new InternetAddress(emailAddr[i].trim());
				}
			}
			jsch = new JSch();
			session = jsch.getSession(username, hostname,
					Integer.parseInt(port));

			session.setPassword(password);

			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;

			// upload the files to SFTP
			if (channelSftp == null || session == null
					|| !session.isConnected() || !channelSftp.isConnected()) {
				throw new Exception(
						"Connection to server is closed. Open it first.");
			}

			// Copy to SFTP
			String message = copyToSftp(srcDir, destDir, fileNameStartsWith);

			out.println(message);
			out.println("Batch Ended.@ " + new Date());

		} catch (Exception e) {
			out.println("Exception occured. "+e.getMessage());
			e.printStackTrace();
			sendEmail(fileNameStartsWith, e.getMessage(),toAddr);
		} finally {
			try {
				if (channelSftp != null) {
					channelSftp.disconnect();
				}
				if (channel != null) {
					channel.disconnect();
				}
				if (session != null) {
					session.disconnect();
				}
				if(out != null)
					out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
