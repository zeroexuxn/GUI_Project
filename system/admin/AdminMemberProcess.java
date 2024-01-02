package system.admin;

import java.util.Scanner;
import java.util.regex.Pattern;

import system.admin.data.MemberList;

public class AdminMemberProcess {
	private static AdminMemberProcess process=new AdminMemberProcess();
	
	private AdminMemberProcess() {}
	
	public static AdminMemberProcess getInstance() {
		return process;
	}
	
	/* 메소드명: start
	 * 파라미터: Scanner sc
	 * 		  sc: main()에서 생성한 스캐너 객체
	 * 반환값: 없음
	 * 기능 설명: 스캐너 객체를 통해 입력받은 answer의 값에 따라 회원 관리에 대해 수행할 프로세스를 호출   
	 */
	public void startMemberProcess(Scanner sc) {
		// TODO Auto-generated method stub
		char answer;//세부 작업 번호
		while (true) {
			
			System.out.println("[회원 관리]에 대해 수행할 세부 작업을 선택해주세요.");
			System.out.println("[1]회원 목록 전체 출력 [2]회원 ID로 조회 [3]생일로 조회 [B]메인 메뉴로 돌아가기");
			System.out.print(" > ");
			String input = sc.next();
			sc.nextLine();
			if (input.length() > 1) {// 입력이 1글자보다 많으면
				System.out.println("입력이 1글자를 초과하셨습니다. 첫 번째 글자만 사용됩니다.");// 안내문 출력
			}
			answer = input.charAt(0);// answer는 input의 첫 번째 글자만 사용
			// B누르면 메인 메뉴로 돌아가기
			if (answer == 'B' || answer == 'b') {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			}

			MemberList memberList = new MemberList();

			switch (answer) {
			case '1': // 회원 목록 전체 출력
				memberList.printWholeMember();//memberList클래스의 printWholeMember메소드 호출
				break;
			case '2': // 회원 ID로 조회
				System.out.println("조회할 회원 ID를 입력해주세요. (핸드폰 번호 11자리 입력)");
				String searchedMemberId=null;
				while(true) {
					System.out.print(" > ");
					searchedMemberId = sc.nextLine();
					if (!Pattern.matches("^[0-9]+$",searchedMemberId)) {
						System.out.println("회원ID는 숫자로만 구성되어 있습니다. 다시 입력해주세요.");
					}else break;
				}
				memberList.searchMemberById(searchedMemberId);//memberList클래스의 searchMemberById메소드 호출
				break;
			case '3': // 생일로 조회
				System.out.println("조회할 회원의 생일을 입력해주세요. (MMDD 네자리로 입력)");
				String searchedBirth=null;
				while (true) {
					System.out.print(" > ");
					searchedBirth=sc.nextLine();
					if (!Pattern.matches("^[0-9]+$",searchedBirth)) {
						System.out.println("MMDD 형식에 맞게 숫자로 입력해주세요.");
					}else break;
				}
				memberList.searchMemberByBirth(searchedBirth);//memberList클래스의 searchMemberByBirth메소드 호출
				break;
			default: // 1, 2, 3, B 외의 선택지 입력받으면
				System.out.printf("%s 선택지를 찾을 수 없습니다.", answer);
				break;
			}
		}
	}
}
