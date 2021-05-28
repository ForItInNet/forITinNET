package ua.project.forit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService
{
    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender)
    {
        this.mailSender = javaMailSender;
    }

    public void send(String to, String subject, String text)
    {

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom("ForITinNET");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);

                mailSender.send(message);
            }
        };

        thread.start();
    }
}
