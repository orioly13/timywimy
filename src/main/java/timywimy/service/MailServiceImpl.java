package timywimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    private String emailFrom;

    @Autowired
    public MailServiceImpl(@Value("${api.email.from}") String emailFrom) {
        Assert.notNull(emailFrom, "EmailPattern should be provided");
        this.emailFrom = emailFrom;
    }

    @Override
    public void sendRegisterEmail(String recipient, Integer code) {

        Properties props = new Properties();
        //todo here go properties (localhost:25 by default, should be something else if trying to test it)
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Welcome to TimyWimy, the greatest time-management service (according to it's creator)\n " +
                "Here is your entrance code:" + code;

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailFrom, "No-Reply"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient, "Mr. Recipient"));
            msg.setSubject("Thanks for registering in TimyWimy");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
