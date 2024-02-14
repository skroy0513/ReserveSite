package kr.co.hugetraffic.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "skroy0513@naver.com";
    private static String code;

    public static void getCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        code = key.toString();
    }

    public MimeMessage createMail(String mail) {
        getCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3> 아래의 인증링크를 클릭하여 회원가입을 마무리하세요. </h3>";
            body += "<h2>" +
                    "<a href='http://localhost:8081/user/mail/confirm?email=" +
                    mail + "&code=" + code + "' target='_blank'> 이메일 인증 확인</a>" +
                    "</h2>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public String sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);

        return code;
    }
}
