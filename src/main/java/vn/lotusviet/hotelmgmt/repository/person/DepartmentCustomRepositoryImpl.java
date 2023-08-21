package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.stats.DepartmentEmployeeStatsRecord;
import vn.lotusviet.hotelmgmt.model.entity.person.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Transactional(readOnly = true)
public class DepartmentCustomRepositoryImpl extends AbstractRepository
    implements DepartmentCustomRepository {

  @Override
  public List<DepartmentEmployeeStatsRecord> getDepartmentEmployeeStatsRecords() {
    return (List<DepartmentEmployeeStatsRecord>)
        execProc(
                "Usp_LayThongKeSoNhanVienTheoBoPhan",
                (RowMapper<DepartmentEmployeeStatsRecord>)
                    (rs, rowNum) -> getDepartmentEmployeeStatsRecordFromRs(rs))
            .getResultSet();
  }

  private DepartmentEmployeeStatsRecord getDepartmentEmployeeStatsRecordFromRs(ResultSet rs)
      throws SQLException {
    return new DepartmentEmployeeStatsRecord(
        rs.getString(Department.COL_TEN_BP), rs.getInt("SO_NV"));
  }
}