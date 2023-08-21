package vn.lotusviet.hotelmgmt.testsuite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"vn.lotusviet.hotelmgmt.repository"})
@SuiteDisplayName("Repository layer test suite")
public class RepositoryLayerTestSuite {}