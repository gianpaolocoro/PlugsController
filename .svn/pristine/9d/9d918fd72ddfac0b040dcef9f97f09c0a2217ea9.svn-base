package it.gp.sockets.remote.orvibo.communication;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;



public class MailSender
{

    public static void main(String[] args)
    {
    	String [] recipients = {"gianpaolo.coro@isti.cnr.it","ashtoash@gmail.com"};
    	sendJavaMail("ciao", "test invio mail", recipients);
    
    }

    private final static String mail = "ashtoash80@hotmail.com";
    private final static String testpwd = "Tlundds$1";
    
    public static boolean sendJavaMail(String subject, String body, String[] to){
    	
    	try{
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.live.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mail, testpwd);
					}
				  });
		session.setDebug(true);
        
            Message msg = new javax.mail.internet.MimeMessage(session);
            msg.setFrom(new InternetAddress(mail, "GameMate House Manager"));
            for (String to1:to){
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(to1, to1));
            }
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
            return true;
        } catch (Throwable e) {
        	e.printStackTrace();
        	return false;
        } 
    }
    
  

}
