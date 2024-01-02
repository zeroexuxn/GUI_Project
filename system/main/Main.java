package system.main;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.Scanner;

import system.admin.AdminMode;

public class Main {
	public static char currentMode = 'M';						// 현재 실행 모드: M(메인), U(사용자), A(관리자)
	public static KioskFrame f = KioskFrame.getInstance();		// 프레임 객체
	public static Scanner sc = new Scanner(System.in);			// 스캐너 객체
	public static AdminMode admin = AdminMode.getInstance();	// 관리자 모드 객체

	private static boolean fileCheck;				// 주문 처리 파일 확인 결과
	
	// 메인 메소드
	public static void main(String[] args) {
		f.getContentPane().addMouseListener(new EnterAdminModListener());
		f.getContentPane().addMouseMotionListener(new moveFrameListener());
		
		fileCheck = FileCheck.filesCheck();
		f.initialize(fileCheck);	// 주문 처리 파일 확인 결과를 초기화 변수로 전달 (true이면 정상 흐름, false이면 예외 흐름으로 진행)
		
		f.setVisible(true);			// 프레임 객체를 화면에 띄움
	}
	
	// EnterAdminModListener - 마우스 클릭 이벤트 처리
	public static class EnterAdminModListener extends MouseAdapter {
		private int cnt = 0;					// 총 클릭 횟수
		private boolean continuity = false;		// 연속 클릭 여부(true: 연속, false: 연속 아님)
		
		/* 메소드명: mouseClicked
		 * 파라미터: MouseEvent e (마우스 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 관리자 전용 커맨드가 수행되면 창을 숨기고 관리자 모드로 진입한다.
		 * 		   (관리자 전용 커맨드: 좌측 상단의 특정 영역(60px * 60px)을 연속으로 3번 클릭)
		 */
		@Override
        public void mouseClicked(MouseEvent e) {
			int x = e.getX();		// 마우스가 클릭된 위치의 x 좌표값
			int y = e.getY();		// 마우스가 클릭된 위치의 y 좌표값

			if(x < 60 && y < 60 && currentMode == 'M') {	// 지정된 영역이 클릭되었다면 연속 클릭 여부와 클릭 횟수를 업데이트
				continuity = true;
				cnt++;
			} else {				// 지정되지 않은 영역에서 클릭 이벤트 발생 시, 연속 클릭 여부를 변경하고 클릭 횟수를 0으로 초기화
				continuity = false;
				cnt = 0;
			}
			
			if(currentMode == 'M' && continuity && cnt == 3) {
				if(!fileCheck) {	// 주문 처리 파일 확인 결과가 false(예외 흐름) 이면 관리자 모드로 진입하지 않고 시스템 종료
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 메인 화면 :: 주문 처리 파일 오류로 시스템을 종료합니다.");
					
					sc.close();			// 스캐너 객체 닫기
					System.exit(0);		// 시스템 종료
				}
				
				f.setVisible(false);	// 키오스크 프레임 창을 숨김
				
				System.out.println("=======================================");
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 메인 화면 :: 관리자 모드로 진입합니다.");
				System.out.println("=======================================");
				
				// 관리자 비밀번호를 입력받아 일치하는지 확인
				System.out.println("관리자 비밀번호를 입력해주세요.");
				System.out.print(" > ");
				String password = sc.nextLine();
				
				if(admin.adminCheck(password)) {	// 비밀번호 일치
					currentMode = 'A';				// 현재 실행 모드: A(관리자)
					admin.startAdminMode(sc);		// 관리자 모드 실행
					
					if(admin.isExit()) {
						System.out.println("=======================================");
						System.out.println(Calendar.getInstance().getTime().toString() + ":: 메인 화면 :: 관리자 요청으로 시스템을 종료합니다.");
						System.out.println("=======================================");
						
						sc.close();
						System.exit(0);
					}
				} else {	// 관리자 비밀번호 불일치
					System.out.println("관리자 비밀번호가 일치하지 않습니다.");
				}
				
				// 관리자 모드에서 '관리자 모드 종료'를 선택했거나 관리자 비밀번호가 불일치한 경우 아래 문장 수행
				System.out.println("잠시 후 메인 화면으로 돌아갑니다.");
				
				// 잠시 대기
				try { Thread.sleep(1000); }
				catch (InterruptedException IE) { IE.printStackTrace(); }
				
				currentMode = 'M';	// 현재 실행 모드: M(메인 화면)
				
				// 관리자 전용 커맨드 수행 정보 초기화
				cnt = 0;
				continuity = false;
				
				f.setVisible(true);		// 키오스크 프레임 창을 띄움
			}
        }
	}

	// moveFrameListener - 마우스 드래그 앤 드롭 이벤트 처리
	public static class moveFrameListener implements MouseMotionListener {
		private int x, y;	// 좌표값
		
		/* 메소드명: mouseMoved
		 * 파라미터: MouseEvent e (마우스 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 창 위에 올라와 있는 마우스의 화면 상에서의 좌표값을 계산한다.
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			x = e.getXOnScreen();
	        y = e.getYOnScreen();
		}

		/* 메소드명: mouseDragged
		 * 파라미터: MouseEvent e (마우스 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 창에서 드래그 이벤트가 발생한 경우, 드롭된 위치로 창을 이동시킨다.
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = KioskFrame.getInstance().getLocation(); // 컴포넌트의 현재 위치를 반환
			
			p.x += e.getXOnScreen() - x; // 마우스의 이동량이 적용된 새로운 X 좌표값
	        p.y += e.getYOnScreen() - y; // 마우스의 이동량이 적용된 새로운 Y 좌표값

	        KioskFrame.getInstance().setLocation(p);	// 새로운 좌표값으로 윈도우 창을 이동시킴
	        
	        // 드래그 상태가 지속되는 동안 마우스 좌표값을 계산
	        x = e.getXOnScreen();
	        y = e.getYOnScreen();
		}
	}

}