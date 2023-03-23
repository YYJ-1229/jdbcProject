package aProject.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//javabeans 기술 : 변수 접근 지정자는private으로 한다. -> setter, getter 한다!
// 기본 생성자가 있어야 한다. 
@NoArgsConstructor
@AllArgsConstructor
@Generated
@Getter
@Setter
@ToString
public class empVo {
	private int employee_id;
	private String first_name;
	private String last_name;
	private String email;
	private String phone_number;
	private Date hire_date;
	private String job_id;
	private double salary;
	private double commission_pct;
	private int manager_id;
	private int department_id;
}
