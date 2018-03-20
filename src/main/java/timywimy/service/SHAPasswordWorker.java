package timywimy.service;

public interface SHAPasswordWorker {

    String generatePasswordHash(String password);

    boolean validatePassword(String password, String saltPepperHash);
}
