package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.structures.LightningTests;
import uk.co.automatictester.lightning.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.tests.RespTimeNthPercentileTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;

public class NthPercRespTimeTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "nthPercRespTimeTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        int maxRespTime = getIntegerValueFromElement(element, "maxRespTime");
        int percentile = getPercentile(element, "percentile");
        String description = getTestDescription(element);
        RespTimeNthPercentileTest.Builder builder = new RespTimeNthPercentileTest.Builder(testName, maxRespTime, percentile).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest nthPercRespTimeTest = builder.build();
        LightningTests.add(nthPercRespTimeTest);
    }
}
