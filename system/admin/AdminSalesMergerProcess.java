package system.admin;

import java.util.Scanner;

import system.admin.data.SalesDataMerger;

public class AdminSalesMergerProcess {
	
	private static AdminSalesMergerProcess process=new AdminSalesMergerProcess();
	
	private AdminSalesMergerProcess() {}
	
	public static AdminSalesMergerProcess getInstance() {
		return process;
	}
	//주석 달아야 함
	public void startSalesMergerProcess(Scanner sc) {
		char answer;//세부 작업 번호
		while(true) {
			
			System.out.println("데이터를 통합할 매출 내역 데이터를 선택하세요.");
			System.out.println("[1]연도별 상세 매출 내역 데이터 [2]월별 상세 매출 내역 데이터 [3]월별 통합 매출 내역 데이터 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			
			String input=sc.next();
			sc.nextLine();
			if (input.length()>1) {// 입력이 1글자보다 많은 경우
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");// 안내문 출력
			}
			answer=input.charAt(0);// answer는 input의 첫 번째 글자만 사용
			
			if (answer=='B' || answer=='b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			
			switch (answer) {
			case '1':
				System.out.println("몇 년도의 상세 매출 내역 데이터를 통합하시겠습니까?");
				
				int year=0;
				while (true) {
					System.out.print(" > ");
					try {
						year=Integer.parseInt(sc.nextLine());
						if (year<0 || year>3000) {
							System.out.println("0 이상의 연도를 입력해주세요.");
						}else break;
					} catch (NullPointerException e) {
						// TODO: handle exception
						System.out.println("연도는 숫자로 입력하셔야 합니다. 다시 입력해주세요.");
					}
					
				}
				
				SalesDataMerger.mergeMonthlySalesByYear(year);
				break;
			
			case '2':
				System.out.println("몇 년도 몇 월의 상세 매출 내역 데이터를 통합하시겠습니까?");
				year=0;
				int month=0;
				while (true) {
					System.out.println("연도를 입력하세요.");
					System.out.print(" > ");
					try {
						year=Integer.parseInt(sc.nextLine());
						if (year<0 || year>3000) {
							System.out.println("0 이상의 연도를 입력해주세요.");
						}else break;
					} catch (NullPointerException e) {
						// TODO: handle exception
						System.out.println("연도는 숫자로 입력하셔야 합니다. 다시 입력해주세요.");
					}
					
				}
				while (true) {
					System.out.println("월을 입력하세요.");
					System.out.print(" > ");
					try {
						month=Integer.parseInt(sc.nextLine());
						if (month<0 || month>12) {
							System.out.println("존재하지 않는 월입니다. 다시 입력해주세요.");
						}else break;
					} catch (NullPointerException e) {
						// TODO: handle exception
						System.out.println("월은 숫자로 입력하셔야 합니다. 다시 입력해주세요.");
					}
					
				}
				
				SalesDataMerger.mergeDailySalesByMonth(year, month);
				break;
			
			case '3':
				System.out.println("몇 년도 몇 월의 상세 매출 내역 데이터를 통합하시겠습니까?");
				year=0;
				month=0; // month2 변수명 변경해야함
				while (true) {
					System.out.println("연도를 입력하세요.");
					System.out.print(" > ");
					try {
						year=Integer.parseInt(sc.nextLine());
						if (year<0 || year>3000) {
							System.out.println("0 이상의 연도를 입력해주세요.");
						}else break;
					} catch (NullPointerException e) {
						// TODO: handle exception
						System.out.println("연도는 숫자로 입력하셔야 합니다. 다시 입력해주세요.");
					}
					
				}
				while (true) {
					System.out.println("월을 입력하세요.");
					System.out.print(" > ");
					try {
						month=Integer.parseInt(sc.nextLine());
						if (month<0 || month>12) {
							System.out.println("존재하지 않는 월입니다. 다시 입력해주세요.");
						}else break;
					} catch (NullPointerException e) {
						// TODO: handle exception
						System.out.println("월은 숫자로 입력하셔야 합니다. 다시 입력해주세요.");
					}
					
				}
				
				SalesDataMerger.mergeDailySalesTotalByMonth(year, month);
				break;
			default:
				break;
			}
		}
	}


}
