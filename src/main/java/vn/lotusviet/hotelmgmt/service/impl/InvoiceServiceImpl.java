package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.entity.InvoiceNotFoundException;
import vn.lotusviet.hotelmgmt.exception.entity.RentalNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.invoice.*;
import vn.lotusviet.hotelmgmt.model.dto.invoice.MergedInvoiceDto.MergedInvoiceRentalDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.InvoiceRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.MergedInvoiceRepository;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.InvoiceService;
import vn.lotusviet.hotelmgmt.service.RentalService;
import vn.lotusviet.hotelmgmt.service.factory.InvoiceFactory;
import vn.lotusviet.hotelmgmt.service.mapper.EmployeeMapper;
import vn.lotusviet.hotelmgmt.service.mapper.InvoiceMapper;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

  private final RentalService rentalService;
  private final InvoiceRepository invoiceRepository;
  private final MergedInvoiceRepository mergedInvoiceRepository;
  private final InvoiceFactory invoiceFactory;
  private final InvoiceMapper invoiceMapper;
  private final RentalRepository rentalRepository;
  private final EmployeeService employeeService;
  private final EmployeeMapper employeeMapper;

  public InvoiceServiceImpl(
      RentalService rentalService,
      InvoiceRepository invoiceRepository,
      MergedInvoiceRepository mergedInvoiceRepository,
      InvoiceFactory invoiceFactory,
      InvoiceMapper invoiceMapper,
      RentalRepository rentalRepository,
      EmployeeService employeeService,
      EmployeeMapper employeeMapper) {
    this.rentalService = rentalService;
    this.invoiceRepository = invoiceRepository;
    this.mergedInvoiceRepository = mergedInvoiceRepository;
    this.invoiceFactory = invoiceFactory;
    this.invoiceMapper = invoiceMapper;
    this.rentalRepository = rentalRepository;
    this.employeeService = employeeService;
    this.employeeMapper = employeeMapper;
  }

  @Override
  @LogAround(output = false, jsonInput = true)
  @Transactional(readOnly = true)
  public List<CommonInvoiceDto> getCommonInvoices(final CommonInvoiceFilterDto filterDto) {
    Objects.requireNonNull(filterDto);

    final List<CommonInvoiceDto> invoiceDtos = new ArrayList<>();
    final InvoiceType invoiceType = filterDto.getInvoiceType();

    final LocalDateTime dateFrom = DatetimeUtil.atStartOfDate(filterDto.getDateFrom());
    final LocalDateTime dateTo = DatetimeUtil.atEndOfDate(filterDto.getDateTo());

    final boolean allOfInvoices = invoiceType == null;

    if (allOfInvoices || invoiceType.equals(InvoiceType.NO_VAT)) {
      invoiceDtos.addAll(
          invoiceMapper.toCommonInvoice(
              invoiceRepository.findByCreatedAtBetweenAndTaxCodeIsNull(dateFrom, dateTo)));
    }

    if (allOfInvoices || invoiceType.equals(InvoiceType.VAT)) {
      invoiceDtos.addAll(
          invoiceMapper.toCommonInvoice(
              invoiceRepository.findByCreatedAtBetweenAndTaxCodeIsNotNull(dateFrom, dateTo)));
      invoiceDtos.addAll(
          invoiceMapper.toCommonInvoiceFromMerge(
              mergedInvoiceRepository.findByCreatedAtBetween(dateFrom, dateTo)));
    }

    invoiceDtos.sort(comparing(CommonInvoiceDto::getCreatedAt));

    return invoiceDtos;
  }

  @Override
  @LogAround(jsonOutput = true)
  public MergedInvoiceDto mergeInvoice(final List<Long> rentalIds) {
    Objects.requireNonNull(rentalIds);

    if (rentalIds.isEmpty()) {
      throw new IllegalArgumentException();
    }

    final List<Rental> rentals = new ArrayList<>();
    final List<MergedInvoiceRentalDto> invoiceRentalDtos = new ArrayList<>();

    for (long rentalId : rentalIds) {
      final Rental rental = rentalService.getRentalById(rentalId);
      rentals.add(rental);
      MergedInvoiceRentalDto invoiceRentalDto = invoiceMapper.toMergedInvoiceRentalDto(rental);
      invoiceRentalDto.setTotal(getInvoiceTotalOfRentalById(rentalId));
      invoiceRentalDtos.add(invoiceRentalDto);
    }

    long invoiceTotal =
        invoiceRentalDtos.stream().mapToLong(MergedInvoiceRentalDto::getTotal).sum();

    final MergedInvoice mergedInvoice =
        invoiceFactory.createMergedInvoice(LocalDateTime.now(), rentals, invoiceTotal);

    mergedInvoiceRepository.save(mergedInvoice);

    MergedInvoiceDto mergedInvoiceDto = invoiceMapper.toMergedInvoiceDto(mergedInvoice);
    mergedInvoiceDto.setRentals(invoiceRentalDtos);

    return mergedInvoiceDto;
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public MergedInvoiceDto mergeInvoicePreview(final List<Long> rentalIds) {
    Objects.requireNonNull(rentalIds);

    if (rentalIds.isEmpty()) {
      throw new IllegalArgumentException();
    }

    final List<MergedInvoiceRentalDto> mergedInvoiceRentalDtos = new ArrayList<>();

    for (long rentalId : rentalIds) {
      final Rental rental = rentalService.getRentalById(rentalId);
      final MergedInvoiceRentalDto invoiceRental = invoiceMapper.toMergedInvoiceRentalDto(rental);
      invoiceRental.setTotal(getInvoiceTotalOfRentalById(rentalId));
      mergedInvoiceRentalDtos.add(invoiceRental);
    }

    final long invoiceTotal =
        mergedInvoiceRentalDtos.stream().mapToLong(MergedInvoiceRentalDto::getTotal).sum();

    return new MergedInvoiceDto()
        .setCreatedAt(LocalDateTime.now())
        .setCreatedBy(employeeMapper.toEmployeeDtoWithIdAndName(employeeService.getAuditedLogin()))
        .setRentals(mergedInvoiceRentalDtos)
        .setTotal(invoiceTotal);
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public InvoiceDto getInvoiceById(final long invoiceId) {
    return invoiceMapper.toInvoiceDto(getInvoiceEntityById(invoiceId));
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public MergedInvoiceDto getMergedInvoiceById(final long invoiceId) {
    return mergedInvoiceRepository
        .findById(invoiceId)
        .map(
            invoice -> {
              final MergedInvoiceDto mergedInvoiceDto = invoiceMapper.toMergedInvoiceDto(invoice);
              for (final MergedInvoiceRentalDto rental : mergedInvoiceDto.getRentals()) {
                rental.setTotal(getInvoiceTotalOfRentalById(rental.getId()));
              }
              return mergedInvoiceDto;
            })
        .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
  }

  @Override
  @Transactional(readOnly = true)
  @LogAround(output = false)
  public List<MergedInvoiceRentalDto> getRentalNotHavingMergedInvoices(
      final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);

    return rentalRepository
        .findAllNotHasMergedInvoice(
            DatetimeUtil.atStartOfDate(dateFrom), DatetimeUtil.atEndOfDate(dateTo))
        .stream()
        .map(
            rental ->
                invoiceMapper
                    .toMergedInvoiceRentalDto(rental)
                    .setTotal(getInvoiceTotalOfRentalById(rental.getId())))
        .collect(Collectors.toList());
  }

  private int getInvoiceTotalOfRentalById(final long rentalId) {
    if (!rentalRepository.existsById(rentalId)) {
      throw new RentalNotFoundException(rentalId);
    }
    return invoiceRepository.getInvoiceTotalFromRentalId(rentalId);
  }

  private Invoice getInvoiceEntityById(long id) {
    return invoiceRepository
        .findById(id)
        .map(
            invoice -> {
              invoice.getRentalDetails().forEach(rentalService::setRentalDetailPromotions);
              return invoice;
            })
        .orElseThrow(() -> new InvoiceNotFoundException(id));
  }
}