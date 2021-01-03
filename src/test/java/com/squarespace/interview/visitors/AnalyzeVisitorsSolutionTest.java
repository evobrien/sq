package com.squarespace.interview.visitors;

import kotlin.NotImplementedError;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.squarespace.interview.TestUtils.loadTestResource;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnalyzeVisitorsSolutionTest {

  /* TESTS */

  @Test
  public void testRun000() throws IOException {
    String inputData = "visitors/input000.txt";
    String expectedData = "visitors/output000.txt";
    assertTestRun(inputData, expectedData);
  }

  @Test
  public void testRun001() throws IOException {
    String inputData = "visitors/input001.txt";
    String expectedData = "visitors/output001.txt";
    assertTestRun(inputData, expectedData);
  }

  @Test
  public void testRun002() throws IOException {
    String inputData = "visitors/input002.txt";
    String expectedData = "visitors/output002.txt";
    assertTestRun(inputData, expectedData);
  }

  @Test
  public void testRun003() throws IOException {
    String inputData = "visitors/input003.txt";
    String expectedData = "visitors/output003.txt";
    assertTestRun(inputData, expectedData);
  }

  /* TEST HELPERS */

  /**
   * Runs the current implementation the AnalyzeVisitorsSolution problem with
   * the inputs of the given input data and copares them to the outputs of the
   * given expected data.
   */
  private void assertTestRun(String inputData, String expectedResultsFilename) throws IOException {
    try (InputStream input = loadTestResource(inputData)) {
      long[] expectedResults = loadExpectedResults(expectedResultsFilename);
      long[] actualResults = runActualImplementation(processInput(input));

      assertNotNull("Error in output: Expected non-null array of results.", actualResults);
      assertEquals("Error in output: Expected results array with 3 longs",
              expectedResults.length, actualResults.length);
      assertArrayEquals("Result mismatch.", expectedResults, actualResults);

       /*//verbose logging
       for (int i = 0; i < expectedResults.length; i++) {
           System.out.printf("expected value: [%d] == actual value: [%d]\n",
              expectedResults[i], actualResults[i]);
       }*/
    }
  }

  /**
   * Given a filename for a file on the classpath containing expected results
   * data.
   *
   * @param expectedData Expected data in the format:
   *      <code>metricName:\s+metricValue\n</code>
   * @return A 3-element array of long corresponding to
   *      0: page views, 1: unique visitors, 2: view sessions
   * @throws IOException If given file is not found or unable to be
   *      successfully read.
   */
  private static long[] loadExpectedResults(String expectedData) throws IOException {
    List<String> lines;
    InputStream stream = loadTestResource(expectedData);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
      lines = br.lines().collect(Collectors.toList());
    }
    long[] expectedResults = new long[lines.size()];
    int i = 0;
    for (String line : lines) {
      String[] parsedLine = line.split("\\s+");
      expectedResults[i++] = Long.parseLong(parsedLine[1]);
    }

    return expectedResults;
  }

  /**
   * Given a {@link java.lang.String} file on the classpath containing input
   * test data, parse that into a {@link List} of {@link WebsiteVisit}.
   *
   * @param input Test data in the format <code>visitorId\s+timestamp\n</code>
   * @return Parsed test data.
   */
  private static List<WebsiteVisit> processInput(InputStream input) {
    Scanner in = new Scanner(input);
    List<WebsiteVisit> inputList = new ArrayList<>();
    while (in.hasNextLine()) {
      String line = in.nextLine();
      String[] split = line.split(" ");
      String visitorId = split[0];
      long timestamp = Long.parseLong(split[1]);
      inputList.add(new WebsiteVisit(visitorId, timestamp));
    }
    return inputList;
  }

  /**
   * Runs the given data through both available implementation files, with
   * preference given to Kotlin.
   */
  private static long[] runActualImplementation(Iterable<WebsiteVisit> websiteVisits) {
    try {
      return AnalyzeVisitorsSolutionKt.processPageViews(websiteVisits);
    } catch (NotImplementedError e) {
      return AnalyzeVisitorsSolutionJava.processPageViews(websiteVisits);
    }
  }
}
