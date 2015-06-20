package no.hyper.depot;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;


public class DepotTest extends InstrumentationTestCase{

    private Context context;

    private static final String TEST_STRING = "Such String";
    private static final String TEST_STRING_NL = "Such String\nOn two lines\n";
    private static final String STRING_FILENAME = "teststring.txt";

    @Override
    protected void setUp() throws Exception {
        context = getInstrumentation().getContext();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    public void testSaveAndRetrieveStringAsObject() {

        Depot.with(context).store(STRING_FILENAME, TEST_STRING);

        assertTrue(Depot.with(context).contains(STRING_FILENAME));
        String result = (String) Depot.with(context).retrieve(STRING_FILENAME);
        assertEquals(TEST_STRING, result);

    }

    public void testSaveAndRetrievePlainString() {
        Depot.with(context).storeString(STRING_FILENAME, TEST_STRING);
        assertTrue(Depot.with(context).contains(STRING_FILENAME));
        String result = Depot.with(context).retrieveString(STRING_FILENAME);
        assertEquals(TEST_STRING, result);
    }

    public void testSaveAndRetrieveStringWithNewline() {
        Depot.with(context).storeString(STRING_FILENAME, TEST_STRING_NL);
        assertTrue(Depot.with(context).contains(STRING_FILENAME));
        String result = Depot.with(context).retrieveString(STRING_FILENAME);
        assertEquals(TEST_STRING_NL, result);

        //Also test the general object saving method
        Depot.with(context).store(STRING_FILENAME, TEST_STRING_NL);
        assertTrue(Depot.with(context).contains(STRING_FILENAME));
        result = (String) Depot.with(context).retrieve(STRING_FILENAME);
        assertEquals(TEST_STRING_NL, result);
    }

}
