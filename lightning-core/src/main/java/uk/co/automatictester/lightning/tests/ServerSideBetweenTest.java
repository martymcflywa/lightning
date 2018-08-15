package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.base.ServerSideTest;

public class ServerSideBetweenTest extends ServerSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average value between %s and %s";

    private long lowerThreshold;
    private long upperThreshold;

    private ServerSideBetweenTest(String testName, long lowerThreshold, long upperThreshold) {
        super(testName);
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, lowerThreshold, upperThreshold);
    }

    protected void calculateTestResult() {
        if ((actualResult > lowerThreshold) && (actualResult < upperThreshold)) {
            result = TestResult.PASS;
        } else {
            result = TestResult.FAIL;
        }
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class Builder {
        private String hostAndMetric;
        private long lowerThreshold;
        private long upperThreshold;
        private String testName;
        private String description;

        public Builder(String testName, long lowerThreshold, long upperThreshold) {
            this.testName = testName;
            this.lowerThreshold = lowerThreshold;
            this.upperThreshold = upperThreshold;
        }

        public ServerSideBetweenTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ServerSideBetweenTest.Builder withHostAndMetric(String hostAndMetric) {
            this.hostAndMetric = hostAndMetric;
            return this;
        }

        public ServerSideBetweenTest build() {
            ServerSideBetweenTest test = new ServerSideBetweenTest(testName, lowerThreshold, upperThreshold);
            test.description = this.description;
            test.hostAndMetric = this.hostAndMetric;
            return test;
        }
    }
}