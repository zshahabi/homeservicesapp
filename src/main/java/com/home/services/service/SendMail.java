package com.home.services.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

@Component
public final class SendMail
{

    public static final String CONTENT_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TEXT_HTML = "text/html";
    private final Environment environment;

    public final Random random;

    public SendMail(Environment environment)
    {
        this.environment = environment;
        random = new Random();
    }

    private boolean validContentType(final String contentType)
    {
        return (contentType.equals(CONTENT_TEXT_PLAIN) || contentType.equals(CONTENT_TEXT_HTML));
    }

    public boolean send(final String to , final String subject , final String msg , final String contentType)
    {
        if (!validContentType(contentType) || (to == null || to.isEmpty()) || (subject == null || subject.isEmpty()) || (msg == null || msg.isEmpty()))
            return false;

        Properties properties = new Properties();

        properties.put("mail.smtp.auth" , environment.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable" , environment.getProperty("mail.smtp.starttls.enable"));
        properties.put("mail.smtp.host" , environment.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.port" , environment.getProperty("mail.smtp.port"));

        final Session session = Session.getInstance(properties , new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(environment.getProperty("mail.username") , environment.getProperty("mail.password"));
            }
        });

        try
        {
            final Address from = new InternetAddress(environment.getProperty("mail.username"));
            final Address toAddress = new InternetAddress(to);

            final Message message = new MimeMessage(session);
            message.addFrom(new Address[]{from});
            message.setRecipient(Message.RecipientType.TO , toAddress);
            message.setContent("" , contentType);
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            return true;

        }
        catch (MessagingException ignored)
        {
            return false;
        }
    }

    public Environment environment()
    {
        return environment;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SendMail) obj;
        return Objects.equals(this.environment , that.environment);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(environment);
    }

    @Override
    public String toString()
    {
        return "SendMail[" +
                "environment=" + environment + ']';
    }

    public Environment getEnvironment()
    {
        return environment;
    }
}
