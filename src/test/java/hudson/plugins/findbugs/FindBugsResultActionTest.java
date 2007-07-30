package hudson.plugins.findbugs;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import hudson.model.HealthReport;

import org.junit.After;
import org.junit.Test;

/**
 *  Tests the healthiness report of class {@link FindBugsResultAction}.
 */
// CHECKSTYLE:OFF
public class FindBugsResultActionTest {
    /** Error message. */
    private static final String ERROR_MESSAGE = "Wrong healthiness calculation.";
    private FindBugsResult findBugsResult;

    @Test
    public void testMiddle() {
        HealthReport health = createFixture(true, 50, 150, 100);
        assertEquals(ERROR_MESSAGE, 50, health.getScore());
    }

    @Test
    public void testHigh() {
        HealthReport health = createFixture(true, 50, 150, 20);
        assertEquals(ERROR_MESSAGE, 100, health.getScore());
    }

    @Test
    public void testHighBoundary() {
        HealthReport health = createFixture(true, 50, 150, 50);
        assertEquals(ERROR_MESSAGE, 100, health.getScore());
    }

    @Test
    public void testLow() {
        HealthReport health = createFixture(true, 50, 150, 200);
        assertEquals(ERROR_MESSAGE, 0, health.getScore());
    }

    @Test
    public void testLowBoundary() {
        HealthReport health = createFixture(true, 50, 150, 150);
        assertEquals(ERROR_MESSAGE, 0, health.getScore());
    }

    @Test
    public void test25Percent() {
        HealthReport health = createFixture(true, 0, 100, 75);
        assertEquals(ERROR_MESSAGE, 25, health.getScore());
    }

    @Test
    public void testNoHelthyReport() {
        HealthReport health = createFixture(false, 0, 100, 75);
        assertNull(ERROR_MESSAGE, health);

        assertEquals("Mock test failed", 75, findBugsResult.getNumberOfWarnings()); // so the mock will get invoked once
    }

    /**
     * Creates the test fixture.
     *
     * @param isEnabled
     *            defines whether health reporting is enabled
     * @param min
     *            minimum number of bugs
     * @param max
     *            maximum number of bugs
     * @param actual
     *            actual number of bugs
     * @return the actual healthiness
     */
    private HealthReport createFixture(final boolean isEnabled, final int min, final int max, final int actual) {
        findBugsResult = createMock(FindBugsResult.class);
        expect(findBugsResult.getNumberOfWarnings()).andReturn(actual);

        FindBugsResultAction action = new FindBugsResultAction(null, 0, isEnabled, min, max);
        action.setResult(findBugsResult);
        replay(findBugsResult);
        return action.getBuildHealth();
    }

    @After
    public void verifyMock() {
        verify(findBugsResult);
    }

}

