package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.ReportNameTemplate;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.model.dto.invoice.*;
import vn.lotusviet.hotelmgmt.service.InvoiceService;
import vn.lotusviet.hotelmgmt.service.ReportService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static vn.lotusviet.hotelmgmt.web.rest.InvoiceController.URL_PREFIX;

@RestController
@Validated
@RequestMapping(URL_PREFIX)
public class InvoiceController extends AbstractController {

  public static final String URL_PREFIX = "/api/v1/invoice";

  private final InvoiceService invoiceService;
  private final ReportService reportService;
  private final ReportNameTemplate reportNameTemplate;

  public InvoiceController(
      InvoiceService invoiceService,
      ReportService reportService,
      ApplicationProperties applicationProperties) {
    this.invoiceService = invoiceService;
    this.reportService = reportService;
    this.reportNameTemplate = applicationProperties.getReportNameTemplate();
  }

  @PortalSecured
  @GetMapping
  public List<CommonInvoiceDto> getCommonInvoices(final @Valid CommonInvoiceFilterDto filterDto) {
    return invoiceService.getCommonInvoices(filterDto);
  }

  @AdminSecured
  @GetMapping("/no_vat")
  public List<MergedInvoiceDto.MergedInvoiceRentalDto> getRentalNotHavingMergedInvoices(
      final @RequestParam LocalDate dateFrom, final @RequestParam LocalDate dateTo) {
    return invoiceService.getRentalNotHavingMergedInvoices(dateFrom, dateTo);
  }

  @AdminSecured
  @LogAround(output = false)
  @PostMapping("/no_vat/merge/preview")
  public ResponseEntity<byte[]> mergeInvoicePreview(
      final @Valid @RequestBody InvoiceMergeRequestDto invoiceMergeRequestDto) {
    final List<Long> rentalIds = invoiceMergeRequestDto.getRentalIds();
    final byte[] content =
        reportService.getMergedInvoicePdf(invoiceService.mergeInvoicePreview(rentalIds));

    return createFileByteResponse(content, "invoice-merge-preview.pdf");
  }

  @AdminSecured
  @LogAround(output = false)
  @PostMapping("/no_vat/merge")
  public ResponseEntity<byte[]> mergeInvoice(
      final @Valid @RequestBody InvoiceMergeRequestDto invoiceMergeRequestDto) {
    final List<Long> rentalIds = invoiceMergeRequestDto.getRentalIds();
    final MergedInvoiceDto mergedInvoiceDto = invoiceService.mergeInvoice(rentalIds);
    final byte[] content = reportService.getMergedInvoicePdf(mergedInvoiceDto);
    return createFileByteResponse(
        content, String.format("invoice-merge-%d.pdf", mergedInvoiceDto.getId()));
  }

  @LogAround(output = false)
  @GetMapping("/{invoiceId}/{invoiceType}/pdf")
  public ResponseEntity<byte[]> getInvoicePdf(
      final @PathVariable long invoiceId, final @PathVariable InvoiceType invoiceType) {
    byte[] content;
    if (invoiceType.equals(InvoiceType.MERGED)) {
      final MergedInvoiceDto mergedInvoiceDto = invoiceService.getMergedInvoiceById(invoiceId);
      content = reportService.getMergedInvoicePdf(mergedInvoiceDto);
    } else {
      final InvoiceDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
      content = reportService.getInvoicePdf(invoiceDto);
    }
    return createFileByteResponse(
        content, String.format(reportNameTemplate.getInvoiceReport(), invoiceId) + ".pdf");
  }
}