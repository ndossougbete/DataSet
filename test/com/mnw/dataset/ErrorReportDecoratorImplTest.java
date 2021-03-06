package com.mnw.dataset;

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * TODO description of this class is missing
 */
public class ErrorReportDecoratorImplTest {


    private static final Throwable ASSERT_THROWABLE = new AssertionError();
    private static final OriginalExceptionWrapper ASSERT_WRAPPER = new OriginalExceptionWrapper(ASSERT_THROWABLE, "");
    private static final Throwable ASSUMPTION_THROWABLE = new AssumptionViolatedException("");
    private static final OriginalExceptionWrapper ASSUMPTION_WRAPPER = new OriginalExceptionWrapper(ASSUMPTION_THROWABLE, "");
    private static final Throwable RUNTIME_THROWABLE = new RuntimeException();
    private static final OriginalExceptionWrapper SERIOUS_WRAPPER = new OriginalExceptionWrapper(RUNTIME_THROWABLE, "");
    private ErrorReportDecoratorImpl sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sut = new ErrorReportDecoratorImpl();
    }

    @Test
    public void givesDataSetTestingEndedWithExceptionAsHeaderIfThereWasAnySeriousExceptionInTheList() {
        // init
        List<OriginalExceptionWrapper> exceptionListWithSeriousException = ImmutableList.of(ASSUMPTION_WRAPPER, ASSERT_WRAPPER,
                                                                                            SERIOUS_WRAPPER, ASSUMPTION_WRAPPER, ASSERT_WRAPPER);

        // run
        final Throwable testFooter = sut.createTestFooter(exceptionListWithSeriousException);

        // verify
        assertTrue(testFooter instanceof DataSetTestingEndedWithException);
    }

    @Test
    public void givesAssumptionViolatedExceptionAsHeaderIfThereWasOnlyAssumptionViolationExceptionInTheList() {
        // init
        List<OriginalExceptionWrapper> exceptionListWithSeriousException = ImmutableList.of(ASSUMPTION_WRAPPER, ASSUMPTION_WRAPPER);

        // run
        final Throwable testFooter = sut.createTestFooter(exceptionListWithSeriousException);

        // verify
        assertTrue(testFooter instanceof AssumptionViolatedException);
    }

    @Test
    public void givesAssertExceptionAsHeaderIfThereWasAssertErrorButNoSeriousErrorInTheList() {
        // init
        List<OriginalExceptionWrapper> exceptionListWithSeriousException = ImmutableList.of(ASSUMPTION_WRAPPER, ASSERT_WRAPPER, ASSERT_WRAPPER,
                                                                                            ASSUMPTION_WRAPPER);

        // run
        final Throwable testFooter = sut.createTestFooter(exceptionListWithSeriousException);

        // verify
        assertTrue(testFooter instanceof AssertionError);
    }
}
