package vn.lotusviet.hotelmgmt.service.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class JasperReportExporter {

  private static final Logger log = LoggerFactory.getLogger(JasperReportExporter.class);

  private JasperPrint jasperPrint;

  public JasperReportExporter() {}

  public JasperReportExporter(JasperPrint jasperPrint) {
    this.jasperPrint = jasperPrint;
  }

  public JasperPrint getJasperPrint() {
    return jasperPrint;
  }

  public void setJasperPrint(JasperPrint jasperPrint) {
    this.jasperPrint = jasperPrint;
  }

  public byte[] exportPdf() {
    JRPdfExporter exporter = new JRPdfExporter();

    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));

    SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
    reportConfig.setSizePageToContent(true);
    reportConfig.setForceLineBreakPolicy(false);

    SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
    exportConfig.setEncrypted(true);
    exportConfig.setAllowedPermissionsHint("PRINTING");

    exporter.setConfiguration(reportConfig);
    exporter.setConfiguration(exportConfig);
    try {
      exporter.exportReport();
    } catch (JRException ex) {
      log.error(ex.getMessage());
    }
    return output.toByteArray();
  }

  public byte[] exportToXls_x(String sheetName) {
    JRXlsxExporter exporter = new JRXlsxExporter();

    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));

    SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
    reportConfig.setSheetNames(new String[] {sheetName});

    exporter.setConfiguration(reportConfig);

    try {
      exporter.exportReport();
    } catch (JRException ex) {
      log.error(ex.getMessage());
    }

    return output.toByteArray();
  }

  public byte[] exportToXls(String fileName, String sheetName) {
    JRXlsExporter exporter = new JRXlsExporter();

    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));

    SimpleXlsReportConfiguration reportConfig = new SimpleXlsReportConfiguration();
    reportConfig.setSheetNames(new String[] {sheetName});

    exporter.setConfiguration(reportConfig);

    try {
      exporter.exportReport();
    } catch (JRException ex) {
      log.error(ex.getMessage());
    }

    return output.toByteArray();
  }
}