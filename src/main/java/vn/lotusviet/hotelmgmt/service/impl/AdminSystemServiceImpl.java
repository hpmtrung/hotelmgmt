package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.BusinessRuleException;
import vn.lotusviet.hotelmgmt.model.dto.stats.DepartmentEmployeeStatsRecord;
import vn.lotusviet.hotelmgmt.model.dto.stats.ReservationMonthStatsRecord;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsRecord;
import vn.lotusviet.hotelmgmt.model.dto.system.AdminDashboardDataDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleUpdateDto;
import vn.lotusviet.hotelmgmt.model.entity.system.BusinessRule;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationRepository;
import vn.lotusviet.hotelmgmt.repository.misc.BusinessRuleRepository;
import vn.lotusviet.hotelmgmt.repository.person.CustomerRepository;
import vn.lotusviet.hotelmgmt.repository.person.DepartmentRepository;
import vn.lotusviet.hotelmgmt.repository.person.EmployeeRepository;
import vn.lotusviet.hotelmgmt.repository.room.RoomRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteRepository;
import vn.lotusviet.hotelmgmt.service.AdminSystemService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.mapper.BusinessRuleMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AdminSystemServiceImpl implements AdminSystemService {

  private final CustomerRepository customerRepository;
  private final SuiteRepository suiteRepository;
  private final RoomRepository roomRepository;
  private final DepartmentRepository departmentRepository;
  private final ReservationRepository reservationRepository;
  private final EmployeeRepository employeeRepository;
  private final EmployeeService employeeService;
  private final BusinessRuleRepository businessRuleRepository;
  private final BusinessRuleMapper businessRuleMapper;
  private final CacheManager cacheManager;

  public AdminSystemServiceImpl(
      CustomerRepository customerRepository,
      SuiteRepository suiteRepository,
      RoomRepository roomRepository,
      DepartmentRepository departmentRepository,
      ReservationRepository reservationRepository,
      EmployeeRepository employeeRepository,
      EmployeeService employeeService,
      BusinessRuleRepository businessRuleRepository,
      BusinessRuleMapper businessRuleMapper,
      CacheManager cacheManager) {
    this.customerRepository = customerRepository;
    this.suiteRepository = suiteRepository;
    this.roomRepository = roomRepository;
    this.departmentRepository = departmentRepository;
    this.reservationRepository = reservationRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
    this.businessRuleRepository = businessRuleRepository;
    this.businessRuleMapper = businessRuleMapper;
    this.cacheManager = cacheManager;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public AdminDashboardDataDto getDashboardData() {
    long totalCustomer = customerRepository.count();
    short totalSuite = (short) suiteRepository.count();
    short totalRoom = (short) roomRepository.count();
    short totalEmployee = (short) employeeRepository.count();
    List<DepartmentEmployeeStatsRecord> departmentEmployeeStatsRecords =
        departmentRepository.getDepartmentEmployeeStatsRecords();
    List<RoomStatusStatsRecord> roomStatusStatsRecords = roomRepository.getRoomStatusStatsRecords();
    List<ReservationMonthStatsRecord> reservationMonthStatsRecords =
        reservationRepository.getReservationMonthStatsRecordsCurrentYear();

    return new AdminDashboardDataDto()
        .setTotalCustomer(totalCustomer)
        .setTotalSuite(totalSuite)
        .setTotalRoom(totalRoom)
        .setTotalEmployee(totalEmployee)
        .setDepartmentEmployeeStatsRecords(departmentEmployeeStatsRecords)
        .setRoomStatusStatsRecords(roomStatusStatsRecords)
        .setReservationMonthStatsRecords(reservationMonthStatsRecords);
  }

  @Override
  @LogAround
  public List<BusinessRuleDto> updateBusinessRules(final BusinessRuleUpdateDto ruleUpdateDto) {
    Objects.requireNonNull(ruleUpdateDto);
    final List<BusinessRuleUpdateDto.RuleUpdate> updateDtos = ruleUpdateDto.getRuleUpdates();
    final List<BusinessRule> rules = new ArrayList<>();
    for (final BusinessRuleUpdateDto.RuleUpdate dto : updateDtos) {
      final BusinessRule rule =
          businessRuleRepository
              .findById(dto.getId())
              .orElseThrow(() -> new BusinessRuleException("Rule not found"));
      if (!rule.getValue().equals(dto.getValue())) {
        rule.setValue(dto.getValue());
        rule.setEditedBy(employeeService.getAuditedLogin());
        rules.add(rule);
      }
    }
    if (!rules.isEmpty()) {
      clearCache();
    }
    return businessRuleMapper.toDto(businessRuleRepository.saveAll(rules));
  }

  private void clearCache() {
    final Cache findByKeyCache =
        cacheManager.getCache(BusinessRuleRepository.FIND_BY_KEY_CACHE_NAME);
    if (findByKeyCache != null) {
      findByKeyCache.clear();
    }
  }
}