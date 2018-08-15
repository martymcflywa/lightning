package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.structures.LightningTests;
import uk.co.automatictester.lightning.tests.ServerSideBetweenTest;
import uk.co.automatictester.lightning.tests.ServerSideGreaterThanTest;
import uk.co.automatictester.lightning.tests.ServerSideLessThanTest;
import uk.co.automatictester.lightning.tests.base.ServerSideTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;

public class ServerSideTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "serverSideTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        ServerSideTest test = null;
        ServerSideTestType subType = getSubType(element);

        switch (subType) {
            case BETWEEN:
                int lowerThreshold = getIntegerValueFromElement(element, "metricValueA");
                int upperThreshold = getIntegerValueFromElement(element, "metricValueB");
                ServerSideBetweenTest.Builder betweenBuilder = new ServerSideBetweenTest.Builder(testName, lowerThreshold, upperThreshold);
                betweenBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    betweenBuilder.withHostAndMetric(hostAndMetric);
                }
                test = betweenBuilder.build();
                break;
            case GREATER_THAN:
                int greatherThanThreshold = getIntegerValueFromElement(element, "metricValueA");
                ServerSideGreaterThanTest.Builder greatherThanBuilder = new ServerSideGreaterThanTest.Builder(testName, greatherThanThreshold);
                greatherThanBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    greatherThanBuilder.withHostAndMetric(hostAndMetric);
                }
                test = greatherThanBuilder.build();
                break;
            case LESS_THAN:
                int lessThanThreshold = getIntegerValueFromElement(element, "metricValueA");
                ServerSideLessThanTest.Builder lessThanBuilder = new ServerSideLessThanTest.Builder(testName, lessThanThreshold);
                lessThanBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    lessThanBuilder.withHostAndMetric(hostAndMetric);
                }
                test = lessThanBuilder.build();
                break;
        }

        LightningTests.add(test);
    }
}