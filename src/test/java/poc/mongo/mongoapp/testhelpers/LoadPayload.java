package poc.mongo.mongoapp.testhelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class LoadPayload {

    private LoadPayload() {}

    public static String loadJson(final String filePath) throws IOException, JSONException {

        final InputStream fileStream = LoadPayload.class.getClassLoader().getResourceAsStream(filePath);

        if(Objects.nonNull(fileStream)) {

            final JSONObject jsonObject = new JSONObject(new String(fileStream.readAllBytes()));

            return jsonObject.toString();
        }

        throw new IOException("Could not read file.");
    }

}
