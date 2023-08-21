package vn.lotusviet.hotelmgmt.testsuite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({
  "vn.lotusviet.hotelmgmt.core",
  "vn.lotusviet.hotelmgmt.util",
  "vn.lotusviet.hotelmgmt.security"
})
@SuiteDisplayName("Core API test suite")
public class CoreAPITestSuite {}