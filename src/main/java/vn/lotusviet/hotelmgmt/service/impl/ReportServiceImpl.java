package vn.lotusviet.hotelmgmt.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceType;
import vn.lotusviet.hotelmgmt.model.dto.invoice.MergedInvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.report.RoomOccupancyReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.SuiteTurnOverStatsReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.ReservationMonthStatsRecord;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationRepository;
import vn.lotusviet.hotelmgmt.repository.report.ReportRepository;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.ReportService;
import vn.lotusviet.hotelmgmt.service.report.AbstractReport.ReportTable;
import vn.lotusviet.hotelmgmt.service.report.AbstractReport.ReportTable.Cell;
import vn.lotusviet.hotelmgmt.service.report.JasperReportExporter;
import vn.lotusviet.hotelmgmt.service.report.POIStyleGenerator;
import vn.lotusviet.hotelmgmt.service.report.POIStyleGenerator.CustomCellStyle;
import vn.lotusviet.hotelmgmt.service.report.TurnOverReport;
import vn.lotusviet.hotelmgmt.util.CurrencyUtil;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;
import vn.lotusviet.hotelmgmt.util.JacksonUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static vn.lotusviet.hotelmgmt.service.report.POIStyleGenerator.CustomCellStyle.*;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

  private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

  private final ReportRepository reportRepository;
  private final POIStyleGenerator styleGenerator;
  private final EmployeeService employeeService;
  private final JasperReportExporter reportExporter;
  private final ReservationRepository reservationRepository;
  private final JasperReport invoiceReport;
  private final JasperReport vatInvoiceReport;
  private final JasperReport mergedInvoiceReport;
  private final JasperReport roomOccupancyReport;
  private final JasperReport reservationMonthStatsReport;
  private final JasperReport suiteTurnOverStatsReport;

  public ReportServiceImpl(
      ReportRepository reportRepository,
      POIStyleGenerator styleGenerator,
      EmployeeService employeeService,
      JasperReportExporter reportExporter,
      ReservationRepository reservationRepository) {
    this.reportRepository = reportRepository;
    this.styleGenerator = styleGenerator;
    this.employeeService = employeeService;
    this.reportExporter = reportExporter;
    this.reservationRepository = reservationRepository;

    compileJasperReport("service-usage-detail-subreport.jrxml");
    compileJasperReport("promotions-subreport.jrxml");
    invoiceReport = compileJasperReport("novat-invoice-master-report.jrxml");
    vatInvoiceReport = compileJasperReport("vat-invoice-master-report.jrxml");
    mergedInvoiceReport = compileJasperReport("merged-invoice-master-report.jrxml");
    roomOccupancyReport = compileJasperReport("room-occupancy-report.jrxml");
    reservationMonthStatsReport = compileJasperReport("reservation-month-stats-report.jrxml");
    suiteTurnOverStatsReport = compileJasperReport("suite-turnover-stats-report.jrxml");
  }

  @Override
  @LogAround(output = false)
  public TurnOverYearReportDto getTurnOverOfYear(final int year) {
    return reportRepository.getTurnOverReportData(year);
  }

  @Override
  @LogAround(output = false)
  public byte[] getTurnOverYearReportXlsx(final int year) {
    final TurnOverYearReportDto data = reportRepository.getTurnOverReportData(year);

    final TurnOverReport report =
        new TurnOverReport(
            year, employeeService.getAuditedLogin().getFullName(), LocalDate.now(), data);

    final Workbook wb = new XSSFWorkbook();

    var styles = styleGenerator.prepareStyles(wb);

    var sheet = wb.createSheet(String.format("BCDT-%d", year));

    setColumnsWidth(sheet);
    createHeaderPart(sheet, styles, report.getMainTitle(), 0);
    createMetaPart(sheet, styles, report.getReportMetaInfo(), 2);
    createTableReport(sheet, styles, report.getReportTables(), 6);

    var out = new ByteArrayOutputStream();
    try {
      wb.write(out);
      out.close();
      wb.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return out.toByteArray();
  }

  @Override
  @LogAround(output = false)
  public byte[] getReservationMonthStatsReportPdf(
      final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);

    final Map<String, Object> params = getReportCommonParams(dateFrom, dateTo);
    final List<ReservationMonthStatsRecord> records =
        reservationRepository.getReservationMonthStatsRecordsFromDate(dateFrom, dateTo);

    reportExporter.setJasperPrint(
        createJasperPrintFromCollection(reservationMonthStatsReport, params, records));

    return reportExporter.exportPdf();
  }

  @Override
  @LogAround(output = false)
  public byte[] getInvoicePdf(final InvoiceDto invoiceDto) {
    Objects.requireNonNull(invoiceDto);

    final CustomerDto owner = Objects.requireNonNull(invoiceDto.getOwner());

    final Map<String, Object> params = new HashMap<>();

    params.put("invoice_id", invoiceDto.getId());
    params.put("created_at_string", invoiceDto.getCreatedAtString());
    params.put("customer_name", invoiceDto.getCustomerName());
    params.put("customer_tax_code", invoiceDto.getTaxCode());
    params.put("customer_address", owner.getAddress());
    params.put("discount_amount", invoiceDto.getDiscountAmount());
    params.put("payment_method", invoiceDto.getPaymentMethodName());
    params.put("detail_total", invoiceDto.getDetailTotal());
    params.put("total_letters", CurrencyUtil.convertToReadableText(invoiceDto.getTotal()));
    params.put("deposit_amount", invoiceDto.getDepositAmount());
    params.put("total", invoiceDto.getTotal());

    reportExporter.setJasperPrint(
        createJasperPrintFromCollection(
            invoiceDto.getInvoiceType().equals(InvoiceType.VAT) ? vatInvoiceReport : invoiceReport,
            params,
            invoiceDto.getDetails()));

    return reportExporter.exportPdf();
  }

  @Override
  @LogAround(output = false)
  public byte[] getRoomOccupancyReportPdf(final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);

    final Map<String, Object> params = getReportCommonParams(dateFrom, dateTo);
    final RoomOccupancyReportDto data =
        reportRepository.getRooomOccupancyReportData(dateFrom, dateTo);

    reportExporter.setJasperPrint(
        createJasperPrintFromCollection(roomOccupancyReport, params, data.getRecords()));

    return reportExporter.exportPdf();
  }

  @Override
  @LogAround(output = false)
  public byte[] getSuiteTurnOverStatsReportPdf(final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);

    final Map<String, Object> params = getReportCommonParams(dateFrom, dateTo);
    final SuiteTurnOverStatsReportDto data =
        reportRepository.getSuiteTurnOverStatsReportDate(dateFrom, dateTo);

    reportExporter.setJasperPrint(
        createJasperPrintFromCollection(suiteTurnOverStatsReport, params, data.getRecords()));

    return reportExporter.exportPdf();
  }

  @Override
  @LogAround(output = false)
  public byte[] getMergedInvoicePdf(final MergedInvoiceDto mergedInvoiceDto) {
    Objects.requireNonNull(mergedInvoiceDto);

    final Map<String, Object> params = new HashMap<>();
    params.put("invoice_id", mergedInvoiceDto.getId());
    params.put("created_at_string", mergedInvoiceDto.getCreatedAtString());

    final long total = mergedInvoiceDto.getTotal();
    params.put("total", total);
    params.put("total_letters", CurrencyUtil.convertToReadableText(total));

    reportExporter.setJasperPrint(
        createJasperPrintFromCollection(
            mergedInvoiceReport, params, mergedInvoiceDto.getRentals()));

    return reportExporter.exportPdf();
  }

  private Map<String, Object> getReportCommonParams(
      final LocalDate dateFrom, final LocalDate dateTo) {
    final Map<String, Object> params = new HashMap<>();
    final Employee createdBy = employeeService.getAuditedLogin();
    params.put("createdBy_fullname", createdBy.getFullName());
    params.put("dateFrom", DatetimeUtil.formatLocalDate(dateFrom));
    params.put("dateTo", DatetimeUtil.formatLocalDate(dateTo));
    return params;
  }

  private JasperReport compileJasperReport(final String templateName) {
    JasperReport report;
    try {
      InputStream reportStream = getClass().getResourceAsStream("/templates/jrxml/" + templateName);
      report = JasperCompileManager.compileReport(reportStream);
      // JRSaver.saveObject(report, ".jasper/" + templateName.replace(".jrxml", ".jasper"));
      JRSaver.saveObject(report, templateName.replace(".jrxml", ".jasper"));
    } catch (JRException ex) {
      throw new JasperReportFillException(ex.getMessage());
    }
    return report;
  }

  private JasperPrint createJasperPrintFromCollection(
      final JasperReport report, final Map<String, Object> params, final Collection<?> collection) {
    Objects.requireNonNull(report);
    Objects.requireNonNull(params);
    Objects.requireNonNull(collection);

    log.debug(
        "Create printer with parameters {} and collection {}",
        JacksonUtil.toString(params),
        JacksonUtil.toString(collection));

    try {
      return JasperFillManager.fillReport(
          report, params, new JRBeanCollectionDataSource(collection));
    } catch (JRException ex) {
      throw new JasperReportFillException(ex.getMessage());
    }
  }

  private void setColumnsWidth(final Sheet sheet) {
    for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
      sheet.setColumnWidth(columnIndex, 256 * 18);
    }
  }

  private void createHeaderPart(
      final Sheet sheet,
      final Map<CustomCellStyle, CellStyle> styles,
      final String title,
      final int startRow) {
    var row = sheet.createRow(startRow);
    var cell = row.createCell(0);
    cell.setCellStyle(styles.get(MAIN_TITLE));
    cell.setCellValue(title.toUpperCase());
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
  }

  private void createMetaPart(
      final Sheet sheet,
      final Map<CustomCellStyle, CellStyle> styles,
      final Map<String, Object> meta,
      final int startRow) {
    int rowIndex = 0;
    for (Map.Entry<String, Object> entry : meta.entrySet()) {
      var row = sheet.createRow(startRow + rowIndex);

      var keyCell = row.createCell(0);
      keyCell.setCellStyle(styles.get(LEFT_BOLD_ALIGNED));
      keyCell.setCellValue(entry.getKey());

      var valueCell = row.createCell(1);
      valueCell.setCellStyle(styles.get(LEFT_ALIGNED));
      valueCell.setCellValue(entry.getValue().toString());

      rowIndex++;
    }
  }

  private void createTableReport(
      final Sheet sheet,
      final Map<CustomCellStyle, CellStyle> styles,
      final ReportTable[] tables,
      final int startRow) {
    int tableIndex = 0;
    int tableStartRow = startRow;
    for (final ReportTable table : tables) {
      // Header
      final int headerRowIndex = tableStartRow + tableIndex++;
      var headerRow = sheet.createRow(headerRowIndex);

      int columnIndex = 0;
      for (Cell<Object> header : table.getHeaders()) {
        columnIndex = createCell(sheet, styles, columnIndex, headerRowIndex, headerRow, header);
      }

      // Body
      int bodyRowIndex = headerRowIndex + 1;

      for (Cell<Object>[] row : table.getRows()) {
        final var bodyRow = sheet.createRow(bodyRowIndex);
        columnIndex = 0;
        for (Cell<Object> c : row) {
          columnIndex = createCell(sheet, styles, columnIndex, bodyRowIndex, bodyRow, c);
        }
        bodyRowIndex++;
      }

      tableStartRow = bodyRowIndex + 1;
    }
  }

  private int createCell(
      final Sheet sheet,
      final Map<CustomCellStyle, CellStyle> styles,
      int columnIndex,
      final int bodyRowIndex,
      final Row bodyRow,
      final Cell<Object> c) {
    var cell = bodyRow.createCell(columnIndex);
    cell.setCellStyle(styles.get(c.getStyle()));
    if (c.getValue() instanceof String) {
      cell.setCellValue(c.getValue().toString());
    } else if (c.getValue() instanceof Integer) {
      cell.setCellValue((int) c.getValue());
    } else if (c.getValue() instanceof Long) {
      cell.setCellValue((long) c.getValue());
    } else if (c.getValue() instanceof LocalDate) {
      cell.setCellValue((LocalDate) c.getValue());
    }
    if (c.getMergeColumnNum() != 0) {
      CellRangeAddress region =
          new CellRangeAddress(
              bodyRowIndex, bodyRowIndex, columnIndex, columnIndex + c.getMergeColumnNum() - 1);
      sheet.addMergedRegion(region);
      if (c.getStyle().name().contains("BORDER")) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
      }
      columnIndex += c.getMergeColumnNum() - 1;
    }
    columnIndex++;
    return columnIndex;
  }

  private static class JasperReportFillException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JasperReportFillException(String message) {
      super(message);
    }

    public JasperReportFillException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}