package poc.mongo.mongoapp.testhelpers;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

public class AssertResponse {

    private AssertResponse() {}

    public static void assertResponse(final String actual, final String expected) throws JSONException {

        JSONAssert.assertEquals(expected, actual, true);

    }

}
