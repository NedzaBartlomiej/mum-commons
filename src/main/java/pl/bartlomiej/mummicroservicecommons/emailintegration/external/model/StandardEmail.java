package pl.bartlomiej.mummicroservicecommons.emailintegration.external.model;

public class StandardEmail extends Email {
    public StandardEmail(String receiverEmail, String title, String message) {
        super(receiverEmail, title, message);
    }
}