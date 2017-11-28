package utll;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by computer on 29.11.2017.
 */
public class ValidationParamsUtil {


    private final static Logger LOGGER = LoggerFactory.getLogger(ValidationParamsUtil.class);


    public static JsonObject getJsonFromRequest(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            return data;
        } catch (IOException | JsonSyntaxException | JsonIOException e ) {
            LOGGER.error("Bad request params", e);
        }
        return null;
    }

    public static <T> void sendResponse(HttpServletResponse response, T data, Type type) {
        if(data != null) {
            response.setContentType("application/json; charset=UTF-8");
            try {
                Gson gson = new Gson();
                response.getWriter().print(gson.toJson(data, type));
                response.getWriter().flush();
            } catch (IOException e) {
                LOGGER.error("IOException in populateWithJSON", e);
            }
        }

    }

}
