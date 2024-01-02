package system.user;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import system.data.OrderedMenuType;
import system.main.KioskFrame;
import system.main.Main;
import system.user.custom.CustomLabel;

public class FinishOrderPanel extends JPanel {
	private String waitNo;
	
	private CustomLabel labelWaitNo;		// 대기번호
	private CustomLabel labelGoMain;		// 카운트다운 내용이 포함된 레이블
	
	// 생성자
	public FinishOrderPanel() {
		this.setLayout(null);
		
		// ===== 안내 메시지
		CustomLabel labelTitle = new CustomLabel("주문이 완료되었습니다.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 대기번호
		labelWaitNo = new CustomLabel("", JLabel.CENTER);
		labelWaitNo.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 45));
		labelWaitNo.setBounds(160, 220, 400, 45);
		this.add(labelWaitNo);
		
		// ===== 이미지
		CustomLabel labelImg = new CustomLabel(new ImageIcon("src/img/order_finish.gif"));
		labelImg.setBounds(200, 290, 320, 320);
		this.add(labelImg);
		
		// ===== 카운트다운 내용이 포함된 레이블
		labelGoMain = new CustomLabel("", JLabel.CENTER);
		labelGoMain.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 35));
		labelGoMain.setBounds(135, 610, 450, 80);
		this.add(labelGoMain);
		
		// ===== 이미지 (뒷 배경)
		CustomLabel labelImgBg = new CustomLabel(new ImageIcon("src/img/img_finish.png"));
		labelImgBg.setBounds(80, 270, 560, 440);
		this.add(labelImgBg);
	}
	
	/* 메소드명: paintComponent
	 * 파라미터: Graphics g (그래픽 객체)
	 * 반환값: 없음
	 * 기능 설명: 지정된 배경 이미지로 배경을 채운다.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon img = new ImageIcon("src/img/bg.png");	// 배경 이미지
		Image background = img.getImage();
		g.drawImage(background, 0, 0, this);
	}
	
	/* 메소드명: initialize
	 * 파라미터: String waitNo (대기번호)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 대기번호가 화면에 출력될 수 있도록 변수에 저장한다.
	 */
	public void initialize(String waitNo) {
		// ===== 대기번호
		this.waitNo = waitNo;
		labelWaitNo.setText("대기번호 : " + waitNo);
		
		// ===== 카운트다운 내용이 포함된 레이블
		labelGoMain.setText("<html><div style=\"text-align:center\">5초 후<br>메인 화면으로 돌아갑니다.</div></html>");
	}
	
	/* 메소드명: countDown
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 5초 후 메인 화면으로 돌아가기 위해 카운트다운을 수행하며 레이블에 남은 초를 표시한다.
	 */
	public void countDown() {
		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(완료) :: 모든 주문이 완료되어 5초 후 메인 화면으로 돌아갑니다.");
		
		// ===== Timer 객체 생성
		Timer timer = new Timer(1000, new ActionListener() {
			int count = 4;
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
			 * 반환값: 없음
			 * 기능 설명: 남은 시간이 0이 되면 주문 정보를 모두 초기화한 후 메인 화면을 띄운다
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if(count == 0) {
					((Timer) e.getSource()).stop();
					
					//===============[여기 채우기]==============
					setVisible(false);
					UserModePanel.selectHereOrTakeoutPanel.setVisible(true);
					KioskFrame.userModePanel.setVisible(false);
					KioskFrame.userModePanel.initialize();
					OrderedMenuType.reset();
					KioskFrame.mainPanel.setVisible(true);
					Main.currentMode = 'M';
					//=======================================
				}

				labelGoMain.setText("<html><div style=\"text-align:center\">" + count + "초 후<br>메인 화면으로 돌아갑니다.</div></html>");
				count--;
			}
		});
		timer.start(); // 타이머 시작
	}
}
