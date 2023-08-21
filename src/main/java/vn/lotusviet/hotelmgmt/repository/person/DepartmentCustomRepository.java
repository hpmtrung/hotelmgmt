package vn.lotusviet.hotelmgmt.repository.person;

import vn.lotusviet.hotelmgmt.model.dto.stats.DepartmentEmployeeStatsRecord;

import java.util.List;

public interface DepartmentCustomRepository {

   List<DepartmentEmployeeStatsRecord> getDepartmentEmployeeStatsRecords();

}