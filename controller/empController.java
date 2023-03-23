package aProject.controller;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import aProject.model.empService;
import aProject.view.empView;
import aProject.vo.DateUtil;
import aProject.vo.empVo;
//수정했음~!~
public class empController {
	public static void main(String[] args) {
		empService empService = new empService();
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			
			System.out.println("1. select All");
			System.out.println("2. select by id");
			System.out.println("3. select by dept");
			System.out.println("4. select by condition (deptid, jobid, salary)");
			System.out.println("5 and 5-1. salary which is under it's avg(salary)");
			System.out.println("6. insert employee");
			System.out.println("7. insert employee with all details");
			System.out.println("8. edit employee with all details");
			System.out.println("9. edit more datails ");
			System.out.println("10. delete employee ");
			System.out.println("11. select employee's salary ");
			System.out.println("12. select employee's salary and name :  ");
			System.out.println("exit 입력시 종료");
			
			System.out.println("============작업 선택============");
			String ch = sc.next();
			switch (ch) {
				case "1" : {
					empView.print(empService.select_All());
					break;
				}
				case "2" : {
					System.out.print("조회할 직원 번호 : ");
					int emp_id = sc.nextInt();
					empView.print(empService.selectById(emp_id));
					break;
				}
				case "3" : {
					System.out.print("조회할 팀 번호 : ");
					int dept_id = sc.nextInt();
					empView.print(empService.selectByDept(dept_id));
					break;
				}
				case "4" : {
					System.out.print("조회할 팀 번호 : ");
					int dept_id = sc.nextInt();
					System.out.print("조회할 직업 id : ");
					String job_id = sc.next();
					System.out.print("조회할 급여 : ");
					double salary = sc.nextDouble();
					empView.print(empService.selectByCondition(dept_id, job_id, salary));
					break;
				}case "5" : {
					List <empVo> emplist = empService.salary_underDept();
					System.out.println("5 size" + emplist.size());
					
					for(empVo vv : emplist) {
						System.out.println(vv);
					}
					break;
				}case "5-1" : {
					empView.print(empService.select_All());
					break;
				}case "6" : {
					System.out.print("name : ");
					String name = sc.next();
					System.out.print("email : ");
					String email = sc.next();
					System.out.print("job_id : ");
					String job_id = sc.next();
					
					empVo emp = new empVo();
					emp.setLast_name(name);
					emp.setEmail(email);
					emp.setJob_id(job_id);
					
					empView.print(empService.empInsert(emp));
					
					break;
				}
				case "7" : {
					System.out.print("first_name : ");
					String first_name = sc.next();
					
					System.out.print("last_name : ");
					String last_name = sc.next();
					
					System.out.print("email : ");
					String email = sc.next();
					
					System.out.print("Phone : ");
					String phone = sc.next();
					
					System.out.print("Manager : ");
					int manager = sc.nextInt();
					
					System.out.print("department_id : ");
					int department_id = sc.nextInt();
					
					System.out.print("Commission : ");
					Double commision_pct = sc.nextDouble();
					
					System.out.print("Salary : ");
					Double salary = sc.nextDouble();
					
					System.out.print("job_id : ");
					String job_id = sc.next();
					
					System.out.print("hire_date 'yyy/mm/dd' : ");
					String dd = sc.next();
					Date date = DateUtil.convert_date(dd);
					
					empVo emp = new empVo();
					emp.setLast_name(last_name);
					emp.setFirst_name(first_name);
					emp.setHire_date(date);
					emp.setPhone_number(phone);
					emp.setSalary(salary);
					emp.setEmail(email);
					emp.setManager_id(manager);
					emp.setDepartment_id(department_id);
					emp.setCommission_pct(commision_pct);
					emp.setJob_id(job_id);
					
					empView.print(empService.empInsertAll(emp));
					
					break;
				}case "8" : {
					
					empVo ee = makeEmp2(sc);
					empView.print(empService.empUpdate(ee));
				
					break;
				} case "9" : { 
					System.out.print("update emp_id : ");
					int emp_id = sc.nextInt();
					
					empView.print(empService.selectById(emp_id));
					empVo ee = makeEmp3(sc, emp_id);
					empView.print(empService.empUpdate2(ee));
					break;
				}
				case "10" : { 
					System.out.print("delete emp_id : ");
					int emp_id = sc.nextInt();
					empView.print(empService.selectById(emp_id));
					
					System.out.print("confirm delete y / n : ");
					String c = sc.next();
					if(c.equals("y")) {
						empService.empDelete(emp_id);
					}else {
						System.out.println("삭제되지 않았습니다.");
					}
					break;
				}
				case "11" : {
					System.out.print("조회할 직원 번호 : ");
					int emp_id = sc.nextInt();
					empView.print(empService.getSalary(emp_id));
					break;
				}
				case "12" : {
					System.out.print("조회할 직원 번호 : ");
					int emp_id = sc.nextInt();
					empView.print(empService.getSalary_name(emp_id));
					break;
				}
			}
			
			if(ch.equals("exit")) break;
			
		}
		System.out.println("종료 되었습니다.");
	}

	private static empVo makeEmp3(Scanner sc, int emp_id) {
		
		System.out.print("update email : ");
		String email = sc.next();
		System.out.print("update department_id : ");
		int department_id = sc.nextInt();
		System.out.print("update job_id : ");
		String job_id = sc.next();
		System.out.print("update salary : ");
		Double salary = sc.nextDouble();
		System.out.print("update date 'yyy/MM/dd' : ");
		String ndd = sc.next();
		Date date = DateUtil.convert_date(ndd);
		System.out.print("update manager_id : ");
		int manager_id = sc.nextInt();
		
		empVo emp = new empVo();
		emp.setEmployee_id(emp_id);
		emp.setEmail(email);
		emp.setDepartment_id(department_id);
		emp.setJob_id(job_id);
		emp.setSalary(salary);
		emp.setHire_date(date);
		emp.setManager_id(manager_id);
		return emp;
	}

	private static empVo makeEmp2(Scanner sc ) {
		System.out.print("update emp_id : ");
		int emp_id = sc.nextInt();
		
		System.out.print("update email : ");
		String email = sc.next();
		System.out.print("update department_id : ");
		int department_id = sc.nextInt();
		System.out.print("update job_id : ");
		String job_id = sc.next();
		System.out.print("update salary : ");
		Double salary = sc.nextDouble();
	
		
		empVo emp = new empVo();
		emp.setEmployee_id(emp_id);
		emp.setEmail(email);
		emp.setDepartment_id(department_id);
		emp.setJob_id(job_id);
		emp.setSalary(salary);
		
		return emp;
		
	}
	

}
