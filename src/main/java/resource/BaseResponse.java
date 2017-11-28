package resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 29.11.2017.
 */
public class BaseResponse {

    private ResponseType status;
    private List<String> messages;

    public BaseResponse(ResponseType status) {
        this.status = status;
        messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public ResponseType getStatus() {
        return status;
    }

    public void setStatus(ResponseType status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
