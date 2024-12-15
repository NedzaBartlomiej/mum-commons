package pl.bartlomiej.mumcommons.emailintegration.external.model;

import lombok.Getter;

@Getter
public class LinkedEmail extends Email {
    private final String link;
    private final String linkButtonText;

    public LinkedEmail(String receiverEmail, String title, String message, String link, String linkButtonText) {
        super(receiverEmail, title, message);
        this.link = link;
        this.linkButtonText = linkButtonText;
    }
}
