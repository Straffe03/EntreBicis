package MarioOlaya.Projecte4.EntreBicis.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Lògica per a l'enviament de correus electrònics dins del sistema.
 */
@Service
public class EmailLogic {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailLogic.class);

    /**
     * Envia un correu de recuperació de contrasenya a l'usuari especificat.
     *
     * @param toEmail Correu de destí
     * @param subject Assumpte del correu
     * @param body    Cos del missatge
     */
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("marioentrebicisp4@gmail.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            logger.info("S'ha enviat correctament el correu a {}", toEmail);
        } catch (Exception e) {
            logger.error("Error en enviar el correu a {}: {}", toEmail, e.getMessage());
        }
    }
}
