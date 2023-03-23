package aProject.model;

import java.util.List;

import aProject.vo.empVo;

// service : 업무 로직 담당 : DAO와 controller의 연결 고리~
public class empService {

	empDAO dao = new empDAO();

	public List<empVo> select_All() {
		return dao.select_All();
	}

	public empVo selectById(int empid) {
		return dao.selectById(empid);
	}

	public List<empVo> selectByDept(int deptid) {
		return dao.selectByDept(deptid);
	}

	public List<empVo> selectByCondition(int deptid, String jobid, double salary) {
		return dao.selectByCondition(deptid, jobid, salary);
	}
	
	public List<empVo> salary_underDept(){
		List<empVo> emplist = dao.salary_underDept();
		System.out.println("===size===" + emplist.size());
		return emplist;
	}
	
	public List<empVo> salary_underDept2(){
		return dao.salary_underDept2();
	}
	
	public String empInsert(empVo emp) {
		int result = dao.empInsert(emp);
		
		if(result >= 1) {
			return "입력에 성공하였습니다!";
		}else {
			return "입력 실패!";
		}
	}
	public String empInsertAll(empVo emp) {
		int result = dao.empInsertAll(emp);
		
		if(result >= 1) {
			return "입력에 성공하였습니다!";
		}else {
			return "입력 실패!";
		}
	}
	
	public String empUpdate(empVo emp) {
		int result = dao.empUpdate(emp);
		
		if(result >= 1) {
			return "수정 성공하였습니다!";
		}else {
			return "수정 실패!";
		}
	}
	
	public String empUpdate2(empVo emp) {
		int result = dao.empUpdate2(emp);
		
		if(result >= 1) {
			return "수정 성공하였습니다!";
		}else {
			return "수정 실패!";
		}
	}
	// 한건의 직원을 삭제하기
	public String empDelete(int empid) {
		int result = dao.empDelete(empid);
		
		if(result >= 1) {
			return "수정 성공하였습니다!";
		}else {
			return "수정 실패!";
		}
	}
	
	public double getSalary(int empid) {
		return dao.getSalary(empid);
	}
	public empVo getSalary_name(int empid) {
		return dao.getSalary_name(empid);
	}
}
