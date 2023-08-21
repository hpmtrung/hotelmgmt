package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.BusinessRuleException;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey;
import vn.lotusviet.hotelmgmt.model.entity.system.BusinessRule;
import vn.lotusviet.hotelmgmt.repository.misc.BusinessRuleRepository;
import vn.lotusviet.hotelmgmt.service.SystemService;
import vn.lotusviet.hotelmgmt.service.mapper.BusinessRuleMapper;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

  private final BusinessRuleRepository businessRuleRepository;
  private final BusinessRuleMapper businessRuleMapper;

  public SystemServiceImpl(
      BusinessRuleRepository businessRuleRepository, BusinessRuleMapper businessRuleMapper) {
    this.businessRuleRepository = businessRuleRepository;
    this.businessRuleMapper = businessRuleMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BusinessRuleDto> getAllBusinessRules() {
    return businessRuleMapper.toDto(businessRuleRepository.findAll());
  }

  @Override
  @Cacheable(
      value = BusinessRuleRepository.FIND_BY_KEY_CACHE_NAME,
      key = "#key.id",
      condition = "#key != null")
  @LogAround
  @Transactional(readOnly = true)
  public <T> T getBusinessRuleValue(@NonNull BusinessRuleKey key, @NonNull Class<T> resultClass) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(resultClass);

    final BusinessRule rule =
        businessRuleRepository
            .findById(key.getId())
            .orElseThrow(() -> new BusinessRuleException("Not found for key " + key));

    if (resultClass.equals(String.class)) {
      return resultClass.cast(rule.getValue());
    } else if (resultClass.equals(Integer.class)) {
      return resultClass.cast(Integer.parseInt(rule.getValue()));
    } else if (resultClass.equals(Long.class)) {
      return resultClass.cast(Long.parseLong(rule.getValue()));
    } else {
      throw new BusinessRuleException("Class cast not supported");
    }
  }
}