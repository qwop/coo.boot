package coo.boot.demo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.boot.core.entity.User;
import coo.boot.demo.enums.Sex;
import coo.core.hibernate.search.ArrayBridge;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.security.entity.ResourceEntity;

/**
 * 职员。
 */
@Entity
@Table(name = "Employee")
@Indexed(index = "Employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XStreamAlias("employee")
@JsonIgnoreProperties({"creator", "modifier"})
public class Employee extends ResourceEntity<User> {
  private static final long serialVersionUID = 5408086709494267860L;
  /** 关联部门 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "companyId")
  @IndexedEmbedded(includePaths = "name")
  @JsonBackReference
  private Company company;
  /** 姓名 */
  @NotBlank
  @Field(analyze = Analyze.NO)
  private String name;
  /** 年龄 */
  @NotNull
  @Range(min = 1, max = 999)
  private Integer age = 18;
  /** 性别 */
  @Type(type = "IEnum")
  @Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
  private Sex sex = Sex.MALE;
  /** 兴趣爱好 */
  @Type(type = "Array")
  @Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = ArrayBridge.class))
  private String[] interests = new String[] {};

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public String[] getInterests() {
    return interests;
  }

  public void setInterests(String[] interests) {
    this.interests = interests;
  }
}
