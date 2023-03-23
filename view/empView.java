package aProject.view;

import java.util.List;

import aProject.vo.empVo;

public class empView {
	
	public static void print(List<empVo> emplist) {
		System.out.println("==========직원 정보=========");
		
		for(empVo ee : emplist) {
			System.out.println(ee);
		}
	}
	
	public static void print(empVo emp) {
		System.out.println("==========직원 상세 정보=========");
		if(emp == null) {
			print("직원이 존재하지 않습니다");
		}else {
			System.out.println(emp);
		}
	}
	
	public static void print(String mes) {
		System.out.println("************!알림!*************");
		System.out.println(mes);
	}

	public static void print(double salary) {
		System.out.println("==========직원 정보=========");
		System.out.println("salary : "+salary);
	}

}
