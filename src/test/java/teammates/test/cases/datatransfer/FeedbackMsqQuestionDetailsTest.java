package teammates.test.cases.datatransfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import teammates.common.datatransfer.FeedbackParticipantType;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.questions.FeedbackMsqQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackMsqResponseDetails;
import teammates.common.datatransfer.questions.FeedbackQuestionType;
import teammates.common.datatransfer.questions.FeedbackResponseDetails;
import teammates.common.util.Const;
import teammates.test.cases.BaseTestCase;
import teammates.test.driver.AssertHelper;

/**
 * SUT: {@link FeedbackMsqQuestionDetails}.
 */
public class FeedbackMsqQuestionDetailsTest extends BaseTestCase {

    @Test
    public void testConstructor_defaultConstructor_fieldsShouldHaveCorrectDefaultValues() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        assertEquals(FeedbackQuestionType.MSQ, msqDetails.getQuestionType());
        assertFalse(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getMsqWeights().isEmpty());
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqWeights_allChoicesNull_weightsListShouldBeEmpty() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.50" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "2.50" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertEquals(msqDetails.getQuestionType(), FeedbackQuestionType.MSQ);
        assertTrue(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getMsqChoices().isEmpty());
        // getMsqWeight() returns empty list as there are no msq choices set.
        assertTrue(msqDetails.getMsqWeights().isEmpty());
        assertFalse(msqDetails.getOtherEnabled());
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqWeights_emptyChoice_weightForInValidChoiceShouldNotBeParsed() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "        " });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.22" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        List<Double> weights = msqDetails.getMsqWeights();
        // As one weight can not be parsed, there will be only one weight in the list
        assertEquals(1, weights.size());
        assertEquals(1.55, weights.get(0));
    }

    @Test
    public void testGetMsqWeights_allWeightsNull_weightsListShouldBeEmpty() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        assertEquals(2, msqDetails.getMsqChoices().size());
        assertEquals(0, msqDetails.getMsqWeights().size());
    }

    @Test
    public void testGetMsqWeights_invalidWeights_weightNotParsed() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "Invalid Weight" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        List<Double> weights = msqDetails.getMsqWeights();
        // As one weight can not be parsed, there will be only one weight in the list
        assertEquals(1, weights.size());
        assertEquals(1.55, weights.get(0));
    }

    @Test
    public void testGetMsqWeights_weightsDisabledValidWeights_weightListShouldBeEmpty() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "off" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.25" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertFalse(msqDetails.hasAssignedWeights());
        // As weights are disabled, getMsqWeights should return an empty list.
        assertTrue(msqDetails.getMsqWeights().isEmpty());
    }

    @Test
    public void testGetMsqWeights_weightsEnabledValidChoicesAndWeights_weightsShouldHaveCorrectValues() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.50" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "2.50" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        assertFalse(msqDetails.getMsqChoices().isEmpty());
        assertEquals(2, msqDetails.getMsqChoices().size());
        List<Double> weights = msqDetails.getMsqWeights();
        assertEquals(2, weights.size());
        assertEquals(1.50, weights.get(0));
        assertEquals(2.50, weights.get(1));
        assertFalse(msqDetails.getOtherEnabled());
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqOtherWeight_weightsEnabledOtherDisabledValidWeights_otherWeightShouldHaveDefaultValue() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "2.57" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.12" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "off" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "3.12" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        assertFalse(msqDetails.getOtherEnabled());
        // As 'other' option is disabled, 'otherWeight' will have default value of 0.0
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqOtherWeight_weightAndOtherEnabledValidWeights_fieldShouldHaveCorrectValue() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "2.57" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.12" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "3.12" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getOtherEnabled());
        assertEquals(3.12, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqOtherWeight_weightsDisabledOtherEnabled_otherWeightShouldHaveDefaultValue() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "off" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "3.12" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertFalse(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getOtherEnabled());
        // As weights is disabled, even though other is enabled, otherWeight will have it's default value.
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqOtherWeight_invalidOtherWeight_otherWeightNotParsed() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "2.57" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.12" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "aa" });

        // Other weight value before editing the question
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getOtherEnabled());
        // As 'aa' is not valid double value, other weight will keep the previous value
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testGetMsqOtherWeight_nullOtherWeight_exceptionThrown() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "2.57" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.12" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        // The following line is commented out, so otherWeight parameter is missing from the requestParams.
        // requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "" });

        assertThrows(AssertionError.class, () -> msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
    }

    @Test
    public void testValidateQuestionDetails_choicesLessThanMinRequirement_errorReturned() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_NOT_ENOUGH_CHOICES
                + Const.FeedbackQuestion.MSQ_MIN_NUM_OF_CHOICES + ".", errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_numberOfChoicesGreaterThanWeights_errorReturned() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.22" });
        // The following msqWeight-1 is commented out, so the number of Weights can become 1 whereas numOfChoices is 2.
        // requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_INVALID_WEIGHT, errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_noValidationError_errorListShouldBeEmpty() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.22" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        assertTrue(msqDetails.hasAssignedWeights());
        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(0, errors.size());
    }

    @Test
    public void testValidateQuestionDetails_negativeWeights_errorsReturned() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.22" });
        // Pass negative weight for choice 1 to check that negative weights are not allowed.
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "-1.55" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_INVALID_WEIGHT, errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_negativeOtherWeight_errorsReturned() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "NONE" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "1.22" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.55" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        // Pass negative weight for 'Other' option to check that negative weights are not allowed.
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "-2" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_INVALID_WEIGHT, errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_duplicateMsqOptions_errorReturned() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("choice 1", "choice 1"));

        List<String> errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_DUPLICATE_MSQ_OPTION, errors.get(0));

        //duplicate cases that has trailing and leading spaces
        msqDetails.setMsqChoices(Arrays.asList("choice 1", " choice 1 "));
        errors = msqDetails.validateQuestionDetails(dummySessionToken);
        assertEquals(1, errors.size());
        assertEquals(Const.FeedbackQuestion.MSQ_ERROR_DUPLICATE_MSQ_OPTION, errors.get(0));

    }

    @Test
    public void testExtractQuestionDetails_weightsEnabledForGenerateOptions_weightShouldRemainDisabled() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        Map<String, String[]> requestParams = new HashMap<>();

        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TYPE, new String[] { "MSQ" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_TEXT, new String[] { "msq question text" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_GENERATED_OPTIONS, new String[] { "STUDENTS" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED, new String[] { "2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-0", new String[] { "Choice 1" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQCHOICE + "-1", new String[] { "Choice 2" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_HAS_WEIGHTS_ASSIGNED, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-0", new String[] { "2.57" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_WEIGHT + "-1", new String[] { "1.12" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQOTHEROPTIONFLAG, new String[] { "on" });
        requestParams.put(Const.ParamsNames.FEEDBACK_QUESTION_MSQ_OTHER_WEIGHT, new String[] { "3.12" });

        assertTrue(msqDetails.extractQuestionDetails(requestParams, FeedbackQuestionType.MSQ));
        // As, weights does not support other generateOptionsFor options then 'NONE',
        // here in this case, even though we assigned weights for 'Generate Options for Student'
        // the weights will remain disabled, and the weights list will remain empty.
        assertFalse(msqDetails.hasAssignedWeights());
        assertTrue(msqDetails.getMsqWeights().isEmpty());
        assertEquals(0.0, msqDetails.getMsqOtherWeight());
    }

    @Test
    public void testValidateQuestionDetails_maxSelectableChoicesMoreThanTotalNumberOfChoice_shouldReturnError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("a", "b"));
        // 'other' is NOT one of the choices
        msqDetails.setOtherEnabled(false);
        msqDetails.setGenerateOptionsFor(FeedbackParticipantType.NONE);
        msqDetails.setHasAssignedWeights(false);
        msqDetails.setMsqOtherWeight(0);
        msqDetails.setMsqWeights(new ArrayList<>());
        msqDetails.setMaxSelectableChoices(3);
        msqDetails.setMinSelectableChoices(Integer.MIN_VALUE);

        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        assertEquals(1, errors.size());
        AssertHelper.assertContains(Const.FeedbackQuestion.MSQ_ERROR_MAX_SELECTABLE_EXCEEDED_TOTAL, errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_maxSelectableChoicesEqualTotalNumberOfChoice_shouldNotReturnError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("a", "b"));
        // 'other' is one of the choices
        msqDetails.setOtherEnabled(true);
        msqDetails.setGenerateOptionsFor(FeedbackParticipantType.NONE);
        msqDetails.setHasAssignedWeights(false);
        msqDetails.setMsqOtherWeight(0);
        msqDetails.setMsqWeights(new ArrayList<>());
        msqDetails.setMaxSelectableChoices(3);
        msqDetails.setMinSelectableChoices(Integer.MIN_VALUE);

        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        assertEquals(0, errors.size());
    }

    @Test
    public void testValidateQuestionDetails_emptyMsqChoice_shouldAddError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        //Setting MsqChoices to be empty
        msqDetails.setMsqChoices(Arrays.asList(""));
        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        StringBuilder fullErrorMessage = new StringBuilder();
        for (String error : errors) {
            fullErrorMessage.append(error + " ");
        }
        assertTrue(errors.size() >= 1);
        //Asserting that partial string of what error is reported when msqChoices is set to empty is present
        assertTrue(fullErrorMessage.toString().contains("Msq options cannot be empty"));
    }

    @Test
    public void testValidateQuestionDetails_hasNoAssignedWeightsButOtherWeightsSetGreaterThanZero_shouldAddError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("a", "b"));
        msqDetails.setOtherEnabled(true);
        msqDetails.setGenerateOptionsFor(FeedbackParticipantType.NONE);
        //setting assigned weights to true
        msqDetails.setHasAssignedWeights(false);
        //Setting MSQ Other Weight to a value different than 1 while weights are not assigned should give an error
        msqDetails.setMsqOtherWeight(1);
        msqDetails.setMsqWeights(new ArrayList<>());
        msqDetails.setMaxSelectableChoices(3);
        msqDetails.setMinSelectableChoices(Integer.MIN_VALUE);

        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        assertTrue(errors.size() >= 1);
        StringBuilder fullErrorMessage = new StringBuilder();
        for (String error : errors) {
            fullErrorMessage.append(error + " ");
        }
        //Specific invalid weight error string
        String invalidWeightErrorString = "The weights for the choices of a Multiple-choice (multiple answers) question must be valid numbers with precision up to 2 decimal places.";
        assertTrue(fullErrorMessage.toString().contains(invalidWeightErrorString));
    }

    @Test
    public void testValidateQuestionDetails_hasAssignedWeightsButOtherNotEnabledAndOtherWeightsSetGreaterThanZero_shouldAddError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("a", "b"));
        //setting otherEnabled to false
        msqDetails.setOtherEnabled(false);
        msqDetails.setGenerateOptionsFor(FeedbackParticipantType.NONE);
        //setting assigned weights to true
        msqDetails.setHasAssignedWeights(true);
        //setting other weight to some value that is not 0 (therefore causing an error)
        msqDetails.setMsqOtherWeight(1);
        msqDetails.setMsqWeights(new ArrayList<>());
        msqDetails.setMaxSelectableChoices(3);
        msqDetails.setMinSelectableChoices(Integer.MIN_VALUE);

        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        assertTrue(errors.size() >= 1);
        StringBuilder fullErrorMessage = new StringBuilder();
        for (String error : errors) {
            fullErrorMessage.append(error + " ");
        }
        //Specific invalid weight error string
        String invalidWeightErrorString = "The weights for the choices of a Multiple-choice (multiple answers) question must be valid numbers with precision up to 2 decimal places.";
        assertTrue(fullErrorMessage.toString().contains(invalidWeightErrorString));
    }

    @Test
    public void testValidateQuestionDetails_minSelectableChoicesLessThanOne_shouldReturnError() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();

        msqDetails.setMsqChoices(Arrays.asList("a", "b"));
        msqDetails.setOtherEnabled(true);
        msqDetails.setGenerateOptionsFor(FeedbackParticipantType.NONE);
        msqDetails.setHasAssignedWeights(false);
        msqDetails.setMsqOtherWeight(0);
        msqDetails.setMsqWeights(new ArrayList<>());
        msqDetails.setMaxSelectableChoices(3);

        // setting minSelectableChoices to less than 1 should produce an error
        msqDetails.setMinSelectableChoices(0);

        List<String> errors = msqDetails.validateQuestionDetails("dummyCourse");
        AssertHelper.assertContains(Const.FeedbackQuestion.MSQ_ERROR_MIN_FOR_MIN_SELECTABLE_CHOICES, errors.get(0));    }

    @Test
    public void testGetQuestionWithExistingResponseSubmissionFormHtml_otherEnabledTest_returnsCorrect() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        FeedbackResponseDetails feedbackResponseDetails = new FeedbackMsqResponseDetails();
        StudentAttributes studentAttributes = StudentAttributes
                .builder("DD2480", "e@e.com")
                .build();
        msqDetails.setOtherEnabled(false);

        //the extra HTML code that should appear if otherEnabled is true
        String expectedHtmlCode = "<tr>\n" +
                "  <td>\n" +
                "    <div class=\"checkbox\">\n" +
                "      <label class=\"bold-label\">\n" +
                "        <input type=\"checkbox\"\n" +
                "            name=\"responsetext-1-1\"\n" +
                "              value=\"\" data-text=\"msqOtherOptionText\">\n" +
                "        Other\n" +
                "      </label>\n" +
                "      <input class=\"form-control margin-top-2px\" type=\"text\" name=\"msqOtherOptionText-1-1\" id=\"msqOtherOptionText-1-1\"\n" +
                "          disabled value=\"\">\n" +
                "      <input type=\"hidden\" name=\"msqIsOtherOptionAnswer-1-1\"\n" +
                "          id=\"msqIsOtherOptionAnswer-1-1\"\n" +
                "          value=\"0\">\n" +
                "    </div>\n" +
                "  </td>\n" +
                "</tr>\n";

        //this is the HTML that should appear during the set conditions if otherEnabled is false
        String fullExpectedOtherFalseHTML = "<p class=\"text-muted\" hidden>\n" +
                "  You need to choose at least -1 options.\n" +
                "</p>\n" +
                "<p class=\"text-muted\" hidden>\n" +
                "  You cannot choose more than -1 options.\n" +
                "</p>\n" +
                "<table>\n" +
                "  <tr>\n" +
                "  <td>\n" +
                "    <div class=\"checkbox\">\n" +
                "      <label class=\"bold-label\">\n" +
                "        <input type=\"checkbox\"\n" +
                "            name=\"responsetext-1-1\"\n" +
                "            \n" +
                "            \n" +
                "            value=\"\">\n" +
                "        <i>None of the above</i>\n" +
                "      </label>\n" +
                "    </div>\n" +
                "  </td>\n" +
                "</tr>\n" +
                "\n" +
                "</table>\n" +
                "<input type=\"hidden\" disabled\n" +
                "    name=\"msqMaxSelectableChoices-1\"\n" +
                "    value=\"-1\">\n" +
                "<input type=\"hidden\" disabled\n" +
                "    name=\"msqMinSelectableChoices-1\"\n" +
                "    value=\"-1\">";

        String fullReceivedHTML = msqDetails.getQuestionWithExistingResponseSubmissionFormHtml(true,1,1,
                "DD2480", 1, feedbackResponseDetails, studentAttributes);

        boolean isWindows = (System.getProperty("os.name").toLowerCase().indexOf("win")>=0);
        if (isWindows){
            assertEquals(fullReceivedHTML.trim().replaceAll("\\r\\n", "\n"), fullExpectedOtherFalseHTML);
        }
        else {
            assertEquals(fullReceivedHTML, fullExpectedOtherFalseHTML);
        }
        msqDetails.setOtherEnabled(true);
        fullReceivedHTML = msqDetails.getQuestionWithExistingResponseSubmissionFormHtml(true,1,1,
                "DD2480", 1, feedbackResponseDetails, studentAttributes);

        //if otherEnabled == true then, the expectedHtmlCode should be a new addition to the fullReceivedHTML
        if (isWindows){
            assertTrue(fullReceivedHTML.trim().replaceAll("\\r\\n", "\n").contains(expectedHtmlCode));
        }
        else {
            assertTrue(fullReceivedHTML.contains(expectedHtmlCode));
        }

    }

    @Test
    public void testGetQuestionWithExistingResponseSubmissionFormHtml__isMinSelectableChoicesEnabledTest_returnsCorrect() {
        FeedbackMsqQuestionDetails msqDetails = new FeedbackMsqQuestionDetails();
        FeedbackResponseDetails feedbackResponseDetails = new FeedbackMsqResponseDetails();
        StudentAttributes studentAttributes = StudentAttributes
                .builder("DD2480", "e@e.com")
                .build();
        //this is the HTML that should appear during the set conditions if otherEnabled is false, as is the default
        String fullExpectedOtherFalseHTML = "<p class=\"text-muted\" hidden>\n" +
                "  You need to choose at least -1 options.\n" +
                "</p>\n" +
                "<p class=\"text-muted\" hidden>\n" +
                "  You cannot choose more than -1 options.\n" +
                "</p>\n" +
                "<table>\n" +
                "  <tr>\n" +
                "  <td>\n" +
                "    <div class=\"checkbox\">\n" +
                "      <label class=\"bold-label\">\n" +
                "        <input type=\"checkbox\"\n" +
                "            name=\"responsetext-1-1\"\n" +
                "            \n" +
                "            \n" +
                "            value=\"\">\n" +
                "        <i>None of the above</i>\n" +
                "      </label>\n" +
                "    </div>\n" +
                "  </td>\n" +
                "</tr>\n" +
                "\n" +
                "</table>\n" +
                "<input type=\"hidden\" disabled\n" +
                "    name=\"msqMaxSelectableChoices-1\"\n" +
                "    value=\"-1\">\n" +
                "<input type=\"hidden\" disabled\n" +
                "    name=\"msqMinSelectableChoices-1\"\n" +
                "    value=\"-1\">";

        String retrievedHTML = msqDetails.getQuestionWithExistingResponseSubmissionFormHtml(true,1,1,
                "DD2480", 1, feedbackResponseDetails, studentAttributes);
        boolean isWindows = (System.getProperty("os.name").toLowerCase().indexOf("win")>=0);
        if(isWindows){
            //asserting that allowing isMinSelectableChoices to be false produces the standard expected HTML
            assertEquals(retrievedHTML.replaceAll("\\r\\n", "\n"), fullExpectedOtherFalseHTML);
        } else{
            assertEquals(retrievedHTML, fullExpectedOtherFalseHTML);
        }

        msqDetails.setMinSelectableChoices(2);
        retrievedHTML = msqDetails.getQuestionWithExistingResponseSubmissionFormHtml(true,1,1,
                "DD2480", 1, feedbackResponseDetails, studentAttributes);

        String expectedChange = "<input type=\"hidden\" disabled\n" +
                "    name=\"msqMaxSelectableChoices-1\"\n" +
                "    value=\"-1\">\n" +
                "<input type=\"hidden\" \n" +
                "    name=\"msqMinSelectableChoices-1\"\n" +
                "    value=\"2\">";
        if(isWindows){
            //asserting that allowing isMinSelectableChoices to be a value >
            // Integer.MIN produces expected addition of choices
            assertTrue(retrievedHTML.replaceAll("\\r\\n", "\n").contains(expectedChange));
        } else{
            assertTrue(retrievedHTML.contains(expectedChange));
        }

        msqDetails.setMinSelectableChoices(10);
        retrievedHTML = msqDetails.getQuestionWithExistingResponseSubmissionFormHtml(true,1,1,
                "DD2480", 1, feedbackResponseDetails, studentAttributes);

        expectedChange ="<input type=\"hidden\" \n" +
                "    name=\"msqMinSelectableChoices-1\"\n" +
                "    value=\"10\">";
        if(isWindows){
            //asserting that changing the value of minSelectableChoices is accurately represented in the returned
            //HTML
            assertTrue(retrievedHTML.replaceAll("\\r\\n", "\n").contains(expectedChange));
        } else{
            assertTrue(retrievedHTML.contains(expectedChange));
        }
    }

}
