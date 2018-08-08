package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;

import static uk.co.automatictester.lightning.constants.PerfMonColumns.VALUE_INDEX;

public class ServerSideTest extends LightningTest {

    private static final String GREATER_THAN_MESSAGE = "Average value > %s";
    private static final String LESS_THAN_MESSAGE = "Average value < %s";
    private static final String BETWEEN_MESSAGE = "Average value between %s and %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average value = %s";

    private final String hostAndMetric;
    private final ServerSideTestType subtype;
    private int dataEntriesCount;
    private final long metricValueA;
    private long metricValueB;
    private String expectedResultMessage;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServerSideTest(String name, String type, ServerSideTestType subtype, String description, String hostAndMetric, long metricValueA, long metricValueB) {
        this(name, type, subtype, description, hostAndMetric, metricValueA);
        this.metricValueB = metricValueB;
    }

    public ServerSideTest(String name, String type, ServerSideTestType subtype, String description, String hostAndMetric, long metricValueA) {
        super(name, type, description);
        this.subtype = subtype;
        this.hostAndMetric = hostAndMetric;
        this.metricValueA = metricValueA;
        this.expectedResultMessage = getExpectedResultMessage();
    }

    @Override
    public void execute(ArrayList<String[]> originalDataEntries) {
        try {
            PerfMonEntries dataEntries = filterDataEntries((PerfMonEntries) originalDataEntries);
            dataEntriesCount = dataEntries.size();
            calculateActualResult(dataEntries);
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    public PerfMonEntries filterDataEntries(PerfMonEntries originalPerfMonEntries) {
        return originalPerfMonEntries.getEntriesWith(getHostAndMetric());
    }

    @Override
    public String getTestExecutionReport() {
        return String.format("Test name:            %s%n" +
                        "Test type:            %s%n" +
                        "Test subtype:         %s%n" +
                        "%s" +
                        "Host and metric:      %s%n" +
                        "Expected result:      %s%n" +
                        "Actual result:        %s%n" +
                        "Entries count:        %s%n" +
                        "Test result:          %s%n",
                getName(),
                getType(),
                getSubType(),
                getDescriptionForReport(),
                getHostAndMetric(),
                getExpectedResultDescription(),
                getActualResultDescription(),
                getDataEntriesCount(),
                getResultForReport());
    }

    @Override
    public void printTestExecutionReport() {
        logger.info(getTestExecutionReport());
    }

    public String getHostAndMetric() {
        return hostAndMetric;
    }

    public int getDataEntriesCount() {
        return dataEntriesCount;
    }

    public String getSubType() {
        return subtype.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private String getExpectedResultMessage() {
        switch (subtype) {
            case GREATER_THAN:
                return GREATER_THAN_MESSAGE;
            case BETWEEN:
                return BETWEEN_MESSAGE;
            default:
                return LESS_THAN_MESSAGE;
        }
    }

    private void calculateActualResult(PerfMonEntries perfMonEntries) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (String[] transaction : perfMonEntries) {
            String elapsed = transaction[VALUE_INDEX];
            ds.addValue(Double.parseDouble(elapsed));
        }
        actualResult = (int) ds.getMean();
    }

    private void calculateTestResult() {
        switch (subtype) {
            case GREATER_THAN:
                calculateTestResultForGreaterThan();
                break;
            case LESS_THAN:
                calculateTestResultForLessThan();
                break;
            case BETWEEN:
                calculateTestResultForBetween();
                break;
        }
    }

    private void calculateTestResultForGreaterThan() {
        expectedResultDescription = String.format(expectedResultMessage, metricValueA);
        if (actualResult > metricValueA) {
            result = TestResult.PASS;
        } else {
            result = TestResult.FAIL;
        }
    }

    private void calculateTestResultForLessThan() {
        expectedResultDescription = String.format(expectedResultMessage, metricValueA);
        if (actualResult < metricValueA) {
            result = TestResult.PASS;
        } else {
            result = TestResult.FAIL;
        }
    }

    private void calculateTestResultForBetween() {
        expectedResultDescription = String.format(expectedResultMessage, metricValueA, metricValueB);
        if ((actualResult > metricValueA) && (actualResult < metricValueB)) {
            result = TestResult.PASS;
        } else {
            result = TestResult.FAIL;
        }
    }
}