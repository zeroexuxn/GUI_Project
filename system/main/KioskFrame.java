package system.main;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;

import system.user.UserModePanel;

public class KioskFrame extends JFrame {
	
	public static MainPanel mainPanel;				// 메인 패널 (사용자 모드, 관리자 모드로 분기 처리)
	public static UserModePanel userModePanel;		// 사용자 모드 패널 (매장/포장 선택 - 주문 - 포인트/쿠폰 사용 - 주문 세부 내역 - 결제 - 포인트 적립 - 대기번호/영수증 출력)
	
	// 싱글톤 객체 생성
	private static KioskFrame kioskFrame = new KioskFrame();
	
	// 생성자
	private KioskFrame() {
		this.setSize(720, 1000);		// 프레임 크기 (720px * 1000px)
		this.setResizable(false);		// 프레임 크기 변경 불가
		this.setUndecorated(true);		// 프레임 타이틀바 제거
		this.setAlwaysOnTop(true);		// 항상 최상단 창으로 띄우기
		
		// 현재 화면의 중앙에 창 띄우기
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int leftTopX = centerPoint.x - this.getWidth()/2;
		int leftTopY = centerPoint.y - this.getHeight()/2;
		this.setLocation(leftTopX, leftTopY);
	}

	// 싱글톤 객체 반환 메소드
	public static KioskFrame getInstance() {
		return kioskFrame;
	}

	/* 메소드명: initialize
	 * 파라미터: boolean check (true: 파일이 모두 존재하여 정상 흐름으로 진행 가능, false: 파일이 1개 이상 존재하지 않아 예외 흐름으로 진행)
	 * 반환값: 없음
	 * 기능 설명: 파라미터로 받은 정상/예외 여부에 따라 필요한 패널을 생성하고 프레임에 추가한다.
	 */
	public void initialize(boolean check) {
		mainPanel = new MainPanel(check);
		mainPanel.setBounds(0, 0, 720, 1000);
		this.add(mainPanel);
		
		if(check) {
			userModePanel = UserModePanel.getInstance();
			userModePanel.setVisible(false);
			userModePanel.setBounds(0, 0, 720, 1000);
			this.add(userModePanel);
		}
	}
}
