package system.main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import system.user.custom.CustomButton;

public class MainPanel extends JPanel {
	private String imgBackground;	// 배경 이미지

	// 생성자
	public MainPanel(boolean check) {
		this.setLayout(null);
		
		if(check) {	// 정상 흐름(주문 처리 파일이 모두 존재)
			imgBackground = "src/img/bg_main.png";
			
			// ==== 주문 시작을 위한 버튼
			String strClick = "<html>주문하시려면 여기를 <style=\"font-size:40px\"><font color=#FFC843>클릭</font></style>해주세요.</html>";
			CustomButton btnClick = new CustomButton(strClick);
			btnClick.toGreen(40);
			
			btnClick.addActionListener(new ActionListener() {	// 버튼 클릭 이벤트 처리를 위한 리스너 등록
				/* 메소드명: actionPerformed
				 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
				 * 반환값: 없음
				 * 기능 설명: 주문 진행을 위한 버튼이 클릭되면 사용자 모드로 진입한다.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("=======================================");
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 메인 화면 :: 사용자 모드로 진입합니다.");
					
					Main.currentMode = 'U';							// 현재 실행 모드: U(사용자)
					KioskFrame.mainPanel.setVisible(false);			// 메인 화면 패널 숨김
					KioskFrame.userModePanel.setVisible(true);		// 사용자 모드 패널 띄움
				}
			});
			
			btnClick.setBounds(0, 720, 720, 300);
			this.add(btnClick);
			
		} else { // 예외(주문 처리 파일이 1개라도 존재하지 않음)
			imgBackground = "src/img/bg_main_fail.png";
		}
	}
	
	/* 메소드명: paintComponent
	 * 파라미터: Graphics g (그래픽 객체)
	 * 반환값: 없음
	 * 기능 설명: 지정된 배경 이미지로 배경을 채운다.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon img = new ImageIcon(imgBackground);			// 배포판 배경 이미지
		Image background = img.getImage();
		g.drawImage(background, 0, 0, this);
	}
}
