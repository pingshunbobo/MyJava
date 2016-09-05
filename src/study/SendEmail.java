package study;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
 
public class SendEmail
{
   public static void main(String [] args){   
      // �ռ��˵�������
      String to = "pingshunbobo@sina.cn";
 
      // �����˵�������
      String from = "wubo@pingshunbobo.com";
 
      // ָ�������ʼ�������Ϊ localhost
      String host = "192.168.1.103";
 
      // ��ȡϵͳ����
      Properties properties = System.getProperties();
 
      // �����ʼ�������
      properties.setProperty("mail.smtp.host", host);
 
      // ��ȡĬ��session����
      Session session = Session.getDefaultInstance(properties);
 
      try{
         // ����Ĭ�ϵ� MimeMessage ����
         MimeMessage message = new MimeMessage(session);
 
         // Set From: ͷ��ͷ�ֶ�
         message.setFrom(new InternetAddress(from));
 
         // Set To: ͷ��ͷ�ֶ�
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));
 
         // Set Subject: ͷ��ͷ�ֶ�
         message.setSubject("This is the Subject Line!");
 
         // ������Ϣ��
         message.setText("This is actual message");
 
         // ������Ϣ
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
}