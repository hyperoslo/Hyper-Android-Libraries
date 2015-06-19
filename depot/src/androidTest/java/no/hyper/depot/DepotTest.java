package no.hyper.depot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;

/*
THIS MOTHERFUCKING PIECE OF TESTING SHIT DOES NOT EVEN PRETEND
TO WORK! IT'S A MILLION TIMES EASIER TO JUST NOT WRITE BUGS!
 */

@RunWith(MockitoJUnitRunner.class)
public class DepotTest {

    @Mock
    Context mockContext;

    private static final String TEST_STRING = "Such String";
    private static final String STRING_FILENAME = "teststring.txt";



    @Test
    public void testDepot() {

        Depot.with(mockContext).store(STRING_FILENAME, TEST_STRING);

        assertTrue(Depot.with(mockContext).contains(STRING_FILENAME));

        String result = Depot.with(mockContext).retrieve(STRING_FILENAME);
        assertThat(result, is(TEST_STRING));

    }
}
