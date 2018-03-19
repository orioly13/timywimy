package timywimy.service;

public interface MailService {
    //registers user and returns session UUID
   void sendRegisterEmail(String recepient,Integer code);

}
