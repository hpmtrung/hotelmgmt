package vn.lotusviet.hotelmgmt.testsuite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"vn.lotusviet.hotelmgmt.web.rest"})
@SuiteDisplayName("Rest controller layer test suite")
public class RestControllerLayerTestSuite {}