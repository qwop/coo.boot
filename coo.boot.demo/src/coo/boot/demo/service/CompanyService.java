package coo.boot.demo.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.boot.demo.entity.Company;
import coo.boot.demo.model.CompanyExtendInfo;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 公司管理。
 */
@Service
public class CompanyService {
  @Resource
  private Dao<Company> companyDao;
  @Resource
  private MessageSource messageSource;

  /**
   * 获取所有公司列表。
   * 
   * @return 返回所有公司列表。
   */
  @Transactional(readOnly = true)
  public List<Company> getAllCompany() {
    return companyDao.searchAll("name", true, SortField.Type.STRING);
  }

  /**
   * 获取公司。
   * 
   * @param companyId 公司ID
   * @return 返回公司。
   */
  @Transactional(readOnly = true)
  public Company getCompany(String companyId) {
    return companyDao.get(companyId);
  }

  /**
   * 新增公司。
   * 
   * @param company 公司
   */
  @Transactional
  @DetailLog(target = "company", code = "company.add.log", vars = "company.name",
      type = LogType.NEW)
  public void createCompany(Company company) {
    if (!companyDao.isUnique(company, "name")) {
      messageSource.thrown("company.name.exist");
    }
    companyDao.save(company);
  }

  /**
   * 更新公司。
   * 
   * @param company 公司
   */
  @Transactional
  @DetailLog(target = "company", code = "company.edit.log", vars = "company.name",
      type = LogType.ALL)
  public void updateCompany(Company company) {
    if (!companyDao.isUnique(company, "name")) {
      messageSource.thrown("company.name.exist");
    }
    Company origCompany = companyDao.get(company.getId());
    BeanUtils.copyFields(company, origCompany);
  }

  /**
   * 删除公司。
   * 
   * @param company 公司
   */
  @Transactional
  @SimpleLog(entityId = "company.id", code = "company.delete.success")
  public void deleteCompany(Company company) {
    companyDao.remove(company);
  }

  /**
   * 自动创建公司，用于定时任务。
   */
  @Transactional
  public Company autoCreateCompany() {
    Company company = new Company();
    company.setName("c" + DateUtils.format(new Date(), DateUtils.SECOND_N));
    company.setFoundDate(new Date());
    CompanyExtendInfo extendInfo = new CompanyExtendInfo();
    extendInfo.setTel("12345678");
    company.setExtendInfo(extendInfo);
    createCompany(company);
    return company;
  }
}
