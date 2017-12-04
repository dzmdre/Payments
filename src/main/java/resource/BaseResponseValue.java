package resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 04.12.2017.
 */
public class BaseResponseValue<T> {
    private ResponseType status;
    private List<String> messages;
    private T data;

    public BaseResponseValue(ResponseType status) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
