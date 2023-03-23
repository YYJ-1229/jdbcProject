package aProject.model;
//DAO: data access object : db 업무 ..crud 여기서 하겠습!

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.oracleUtil;

import aProject.vo.empVo;

public class empDAO {
	Connection connection; // 함수 안으로 안들어가서 초기화 안해줘도 된다~ (자동 초기화)
	PreparedStatement prestatement; // 바인딩 변수를 지원
	Statement statement;
	ResultSet rs; // 조회 했을때 result set 이 생긴다!

	int resultCount; // 영향을 받은 건수

	// 전체 직원 조회 --1
	public List<empVo> select_All() {
		List<empVo> emplist = new ArrayList<>();
		String sql = """
				select EMPLOYEE_ID,
						FIRST_NAME,
						LAST_NAME,
						EMAIL,
						PHONE_NUMBER,
						HIRE_DATE,
						JOB_ID,
						f_tax(SALARY) salary,
						COMMISSION_PCT,
						MANAGER_ID,
						DEPARTMENT_ID
					from employees
				""";
		connection = oracleUtil.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData  meta = rs.getMetaData();
			
			int cnt = meta.getColumnCount();
			for(int i = 1; i <= cnt ; i++) {
				System.out.println("칼럼의 이름 : " + meta.getColumnName(i));
			}
			while (rs.next()) {
				empVo emp = makeEmp(rs); // 덩어리를 만들어서 array list에 담아서 보내기
				emplist.add(emp);
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, statement, connection);
		}
		return emplist;
	}
	// 복습 과제 
	public List<empVo> salary_underDept() {
		List<empVo> emplist = new ArrayList<>();
		String sql = """
				select * 
				from employees , (select department_id , avg(salary) sal
                            from employees
                            group by department_id) inline_emp
				where employees.department_id = inline_emp.department_id
				and employees.salary < inline_emp.sal
				""";
		connection = oracleUtil.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				empVo emp = makeEmp2(rs); // 덩어리를 만들어서 array list에 담아서 보내기
				emplist.add(emp);
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, statement, connection);
		}
		return emplist;
	}
	// 복습과제 2
	public List<empVo> salary_underDept2() {
		List<empVo> emplist = new ArrayList<>();
		String sql = """
				select first_name , salary , department_id 
				from employees e
				where salary < (
					select avg(salary) 
					from employees
						where department_id = e.department_id
)
				""";
		connection = oracleUtil.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				empVo emp = makeEmp(rs); // 덩어리를 만들어서 array list에 담아서 보내기
				emplist.add(emp);
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, statement, connection);
		}
		return emplist;
	}

	// 특정 직원 조회 --2
	public empVo selectById(int empid) {
		empVo emp = null;
		String sql = "select * from employees where employee_id = " + empid;
		connection = oracleUtil.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				emp = makeEmp(rs); // 덩어리를 만들어서 array list에 담아서 보내기
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, statement, connection);
		}

		return emp;
	}

	// 특정 부서의 직원만 조회 하고 싶어! --3
	public List<empVo> selectByDept(int deptid) {
		List<empVo> emplist = new ArrayList<>();
		String sql = "select * from employees where department_id = " + deptid;
		connection = oracleUtil.getConnection();
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				empVo emp = makeEmp(rs); // 덩어리를 만들어서 array list에 담아서 보내기
				emplist.add(emp);
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, statement, connection);
		}
		return emplist;
	}

// 특정 부서 + 특정 job + salary 이상인~ 직원 찾기 --4
	public List<empVo> selectByCondition(int deptid, String jobid, double salary) {
		List<empVo> emplist = new ArrayList<>();
		String sql = "select * from employees" + " where department_id = ?" + " and job_id = ?" + " and salary >= ?";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			prestatement.setInt(1, deptid);
			prestatement.setString(2, jobid);
			prestatement.setDouble(3, salary);
			rs = prestatement.executeQuery();
			while (rs.next()) {
				empVo emp = makeEmp(rs); // 덩어리를 만들어서 array list에 담아서 보내기
				emplist.add(emp);
			}
		} catch (SQLException e) {
		} finally {
			oracleUtil.dbDisconnect(rs, prestatement, connection);
		}
		return emplist;
	}
	
	// 신규 직원을 등록! insert 하고 싶다!
	public int empInsert(empVo emp) {
		String sql = """
				insert into employees (employee_id, last_name, email, hire_date, job_id)
				values(seq_id.nextval, ? , ?, sysdate, ?)
				""";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			
			prestatement.setString(1, emp.getLast_name());
			prestatement.setString(2, emp.getEmail());
			prestatement.setString(3, emp.getJob_id());
			
			resultCount = prestatement.executeUpdate(); // DML문장을 실행한다. 영향을 받은 건수가 리턴
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			oracleUtil.dbDisconnect(null, prestatement, connection);
			oracleUtil.dbDisconnect(null, statement, null);
		}
		return resultCount;
	}
	
	public int empInsertAll(empVo emp) {
		String sql = """
				insert into employees 
				values(seq_id.nextval, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)
				""";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			
			prestatement.setString(1, emp.getFirst_name());
			prestatement.setString(2, emp.getLast_name());
			prestatement.setString(3, emp.getEmail());
			prestatement.setString(4, emp.getPhone_number());
			prestatement.setDate(5, emp.getHire_date());
			prestatement.setString(6, emp.getJob_id());
			prestatement.setDouble(7, emp.getSalary());
			prestatement.setDouble(8, emp.getCommission_pct());
			prestatement.setInt(9, emp.getManager_id());
			prestatement.setInt(10, emp.getDepartment_id());
			
			resultCount = prestatement.executeUpdate(); // DML문장을 실행한다. 영향을 받은 건수가 리턴
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCount = -1;
		} finally {
			oracleUtil.dbDisconnect(null, prestatement, connection);
			oracleUtil.dbDisconnect(null, statement, null);
		}
		return resultCount;
	}
	
	public int empUpdate(empVo emp) {
		String sql = """
				update employees
				set email = ?, department_id = ?, job_id = ? , salary = ?
				where employee_id = ?
				""";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			
			prestatement.setString(1, emp.getEmail());
			prestatement.setInt(2, emp.getDepartment_id());
			prestatement.setString(3, emp.getJob_id());
			prestatement.setDouble(4, emp.getSalary());
			prestatement.setInt(5, emp.getEmployee_id());
			

			resultCount = prestatement.executeUpdate(); // DML문장을 실행한다. 영향을 받은 건수가 리턴
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCount = -1;
		} finally {
			oracleUtil.dbDisconnect(null, prestatement, connection);
			oracleUtil.dbDisconnect(null, statement, null);
		}
		return resultCount;
	}
	
	public int empUpdate2(empVo emp) {
		String sql = """
				update employees
				set email = ?, department_id = ?, job_id = ? , salary = ?, hire_Date = ?, manager_id = ?
				where employee_id = ?
				""";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			
			prestatement.setString(1, emp.getEmail());
			prestatement.setInt(2, emp.getDepartment_id());
			prestatement.setString(3, emp.getJob_id());
			prestatement.setDouble(4, emp.getSalary());
			prestatement.setDate(5, emp.getHire_date());
			prestatement.setInt(6, emp.getManager_id());
			prestatement.setInt(7, emp.getEmployee_id());
		

			resultCount = prestatement.executeUpdate(); // DML문장을 실행한다. 영향을 받은 건수가 리턴
			System.out.println("업데이트 결과 " + resultCount);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCount = -1;
		} finally {
			oracleUtil.dbDisconnect(null, prestatement, connection);
			oracleUtil.dbDisconnect(null, statement, null);
		}
		return resultCount;
	}
	//한건의 직원을 삭제하기 : key만 알면 된다! 
	public int empDelete(int empid) {
		String sql = """
				delete from employees where employee_id = ?
				""";
		connection = oracleUtil.getConnection();
		try {
			prestatement = connection.prepareStatement(sql);
			
			prestatement.setInt(1, empid);

			resultCount = prestatement.executeUpdate(); // DML문장을 실행한다. 영향을 받은 건수가 리턴
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCount = -1;
		} finally {
			oracleUtil.dbDisconnect(null, prestatement, connection);
			oracleUtil.dbDisconnect(null, statement, null);
		}
		System.out.println("삭제결과 " + resultCount);
		return resultCount;
	}

	public double getSalary(int empid) {
		String sql ="{call sp_salary(?, ?)}";
		connection = oracleUtil.getConnection();
		CallableStatement callableStatement; // sp지원
		double salary = 0;
		try {
			callableStatement = connection.prepareCall(sql);
			callableStatement.setInt(1, empid);
			callableStatement.registerOutParameter(2, Types.DOUBLE);
			callableStatement.execute(); //result set(변화)이 있으면 true ,
			// update는 false 
			salary = callableStatement.getDouble(2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salary;
	}
	public empVo getSalary_name(int empid) {
		String sql ="{call sp_salary2(?, ?, ?)}";
		connection = oracleUtil.getConnection();
		CallableStatement callableStatement; // sp지원
		empVo emp = new empVo();
		try {
			callableStatement = connection.prepareCall(sql);
			callableStatement.setInt(1, empid);
			callableStatement.registerOutParameter(2, Types.DOUBLE);//result set(변화)이 있으면 true ,
			// update는 false 
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute(); 
			
			emp.setSalary(callableStatement.getDouble(2));
			emp.setFirst_name(callableStatement.getString(3));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emp;
	}
	private empVo makeEmp(ResultSet rs) throws SQLException {
		empVo emp = new empVo();
		emp.setCommission_pct(rs.getDouble("commission_pct"));
		emp.setDepartment_id(rs.getInt("department_id"));
		emp.setEmail(rs.getString("email"));
		emp.setEmployee_id(rs.getInt("employee_id"));
		emp.setFirst_name(rs.getString("first_name"));
		emp.setHire_date(rs.getDate("hire_date"));
		emp.setJob_id(rs.getString("job_id"));
		emp.setLast_name(rs.getString("last_name"));
		emp.setManager_id(rs.getInt("manager_id"));
		emp.setPhone_number(rs.getString("phone_number"));
		emp.setSalary(rs.getDouble("commission_pct"));

		return emp;
	}
	
	private empVo makeEmp2(ResultSet rs) throws SQLException {
		empVo emp = new empVo();
		emp.setDepartment_id(rs.getInt("department_id"));
		emp.setEmployee_id(rs.getInt("employee_id"));
		emp.setFirst_name(rs.getString("first_name"));
		emp.setSalary(rs.getDouble("commission_pct"));

		return emp;
	}


}
