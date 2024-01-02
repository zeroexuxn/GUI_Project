package system.admin;


import java.util.Scanner;
import java.util.regex.Pattern;

import system.admin.data.MenuList;


public class AdminMenuProcess {
	//필드
	private static AdminMenuProcess process = new AdminMenuProcess();

	// 생성자
	private AdminMenuProcess() {}
	
	// 메소드
	public static AdminMenuProcess getInstance() {
		return process;
	}
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 스캐너 객체를 통해 입력받은 answer의 값에 따라 메뉴에 대해 수행할 프로세스를 호출
	 * 		   
	 */
	public void startMenuProcess(Scanner sc) {
		// TODO Auto-generated method stub
		char answer;//세부 작업 번호
		while (true) {
			
			System.out.println("[메뉴]에 대해 수행할 세부 작업을 선택해주세요.");
			System.out.println("[1]전체출력 [2]메뉴ID로 조회 [3] 카테고리로 조회 [4]상품 재고로 조회 [5]상품 정보 수정 [6]메뉴 추가 [7]메뉴 활성화/비활성화 [8]카테고리 추가 [9]카테고리 활성화/비활성화 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			if (input.length() > 1) {// 입력이 1글자보다 많은 경우
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");// 안내문 출력
			}
			answer = input.charAt(0);// answer는 input의 첫 번째 글자만 사용
			// B누르면 메인 메뉴로 돌아가기
			if (answer == 'B' || answer == 'b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}
			
			MenuList menuList = new MenuList(sc);//menuList 객체에  Scanner 객체 sc 전달
												 
			
			switch (answer) {
			
			case '1'://전체 출력 (메뉴와 카테고리 선택 가능)
				System.out.println("전체 출력할 항목을 선택해주세요.");
				System.out.println("[1]메뉴 [2]카테고리");
				String choice="";
				while(true) {
					System.out.print(" > ");
					choice=sc.nextLine();
					if (choice.equals("1")) {
						menuList.printWholeMenu();//MenuList 클래스의 printWholeMenu메소드 실행
						break;
					}else if (choice.equals("2")) {
						menuList.printWholeCategories();//printWholeCategories 메소드 호출
						break;
					}else
						System.out.printf("%s선택지를 찾을 수 없습니다. 다시 입력해주세요.\n",choice);
				}
				
				break;
			case '2'://메뉴ID로 조회
				System.out.println("조회할 메뉴 ID를 입력해주세요.(메뉴 ID는 카테고리ID+메뉴순번입니다.)");
				System.out.print(" > ");
				String searchedId = sc.nextLine();
				
				menuList.searchMenuById(searchedId);//MenuList 클래스의 searchMenuById메소드 실행
				break;
			case '3'://카테고리 ID로 조회
				System.out.println("조회할 카테고리ID를 입력해주세요.");
				String searchedCategoryId=null;
				while (true) {
					System.out.print(" > ");
					searchedCategoryId=sc.nextLine();
					if (Pattern.matches("^[a-zA-Z]+$", searchedCategoryId)==true) {
						break;
					}else {
						System.out.println("카테고리ID는 영어로만 구성되어 있어야 합니다. 다시 입력해주세요.");
					}
				}
				
				menuList.searchMenuByCategory(searchedCategoryId);//MenuList 클래스의 searchMenuByCategory메소드 실행
				break;
			case '4'://상품 재고로 조회
				System.out.println("조회할 상품의 재고 범위를 입력하세요.");
				
				int searchedStockMin=0;//조회할 재고의 범위 최솟값
				int searchedStockMax=0;//조회할 재고의 범위 최댓값
				while (true) {//check가 false이면 계속 반복
					System.out.print("최솟값 > ");
					try {
						searchedStockMin=Integer.parseInt(sc.next());
						if (searchedStockMin<0) {
							System.out.println("0 이상의 정수를 입력해주세요.");
						}else break;;//0 이상의 정수형 입력받으면 while문 탈출
						
					} catch (Exception e) {//정수형 외 입력받으면
						// TODO: handle exception
						System.out.println("재고 범위는 숫자로만 입력 가능합니다. 다시 입력해주세요.");
					}
				}
				
				while (true) {
					System.out.print("최댓값 > ");
					try {
						searchedStockMax=Integer.parseInt(sc.next());
						if (searchedStockMax<searchedStockMin) {
							System.out.println("조회하실 재고 범위의 최댓값은 최솟값보다 큰 수로 입력해주세요.");
						}else break;
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("재고 범위는 숫자로만 입력 가능합니다. 다시 입력해주세요.");
					}
				}
				
				menuList.searchMenuByStock(searchedStockMin, searchedStockMax);//MenuList 클래스의 searchMenuByStock메소드 실행
				break;
				
			case '5': //메뉴 정보 수정
				System.out.println("정보를 수정할 메뉴의 메뉴 ID를 입력해주세요. (메뉴 ID는 카테고리ID+메뉴순번입니다.)");
				
				String editMenuId=null;
				while(true) {
					System.out.print(" > ");
					editMenuId=sc.nextLine();
					if (Pattern.matches("^[a-zA-Z0-9]+$", editMenuId)==true) {
						break;
					}else System.out.println("메뉴ID는 영어와 숫자로만 구성되어있어야 합니다. 다시 입력해주세요.");
				}
				System.out.println();
				
				menuList.editMenuInfo(editMenuId);//MenuList 클래스의 editMenuInfo메소드 실행
				break;
			case '6'://메뉴 추가
				menuList.addMenu();//MenuList 클래스의 addMenu메소드 실행
				break;
			case '7'://메뉴 활성화 여부 변경
				System.out.println("활성화 여부를 변경할 메뉴의 ID를 입력해주세요.");
				System.out.print(" > ");
				String toggleMenuId = null;
				boolean idCheck = false;
				while(!idCheck) {
					
					toggleMenuId = sc.nextLine();
					System.out.println();
					idCheck = Pattern.matches("^[a-zA-Z0-9]+$",toggleMenuId);
					if (idCheck == false) {
						System.out.println("메뉴ID는 영어와 숫자로만 구성되어 있어야 합니다. 다시 입력해주세요.");
						System.out.print(" > ");
					}
					
				}
				menuList.toggleMenuEnable(toggleMenuId);//MenuList 클래스의 toggleMenuEnable메소드 실행
				System.out.println();
				break;
			case '8'://카테고리 추가
				menuList.addCategory();//MenuList 클래스의 addCategory 메소드 호출
				System.out.println();
				break;
			case '9'://마테고리 활성화 여부 변경
				System.out.println("활성화 여부를 변경할 카테고리의 카테고리ID를 입력해주세요.");
				String toggleCategoryId=null;
				while(true) {
					System.out.print(" > ");
					toggleCategoryId=sc.nextLine();
					System.out.println();
					if (!Pattern.matches("^[a-zA-Z]+$", toggleCategoryId)) {
						System.out.println("카테고리ID는 영어로만 구성되어 있어야 합니다. 다시 입력해주세요.");
					}else break;
				}
				
				menuList.toggleCategoryEnable(toggleCategoryId);//MeunList 클래스의 toggleCategoryEnable 메소드 호출
				System.out.println();
				break;
			default://1,2,3,4,5,6,7,8,9,B외의 선택지 입력받으면 출력
				System.out.printf("%s 선택지를 찾을 수 없습니다.\n", answer);
				System.out.println();
				break;

			}
		}
	}
}
		