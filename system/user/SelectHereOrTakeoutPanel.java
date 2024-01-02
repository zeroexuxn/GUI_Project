package system.user;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.user.custom.CustomButton;
import system.user.custom.CustomLabel;
import system.user.dialog.EnterMenuFailDialog;

public class SelectHereOrTakeoutPanel extends JPanel {
	private CustomButton btnHere, btnTakeout;	// 매장, 포장 버튼
	
	// 버튼에 들어갈 이미지 아이콘 경로
	private String iconHereSrc = "src/img/icon_ForHere.png";
	private String iconTakeoutSrc = "src/img/icon_Take-out.png";
	
	// 생성자
	public SelectHereOrTakeoutPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("매장 이용 방법을 선택해주세요.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 매장 / 포장 버튼
		String strHere = "<html><div style=\"text-align:center\">매장<br><font size=6>For Here</font><div></html>";
		btnHere = new CustomButton(strHere, new ImageIcon(iconHereSrc));
		btnHere.toWhite(40, 'D');
		btnHere.addActionListener(new HereOrTakeoutListener());
		btnHere.setBounds(120, 360, 220, 260);
		this.add(btnHere);
		
		String strTakeout = "<html><div style=\"text-align:center\">포장<br><font size=6>Take-out</font><div></html>";
		btnTakeout = new CustomButton(strTakeout, new ImageIcon(iconTakeoutSrc));
		btnTakeout.toWhite(40, 'D');
		btnTakeout.addActionListener(new HereOrTakeoutListener());
		btnTakeout.setBounds(380, 360, 220, 260);
		this.add(btnTakeout);
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
	
	// HereOrTakeoutListener - 버튼 클릭 이벤트 처리
	public class HereOrTakeoutListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 클릭된 매장/포장 버튼에 따라 주문 유형 정보를 저장한 후, 주문 진행을 위한 메뉴 패널을 띄운다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			//===============[여기 채우기]==============
			CustomButton btn = (CustomButton)e.getSource();
			
			if(btn == btnHere) UserModePanel.salesTotal.setType("매장");
			else UserModePanel.salesTotal.setType("포장");
			
			// 활성화 상태의 메뉴가 있어 주문 진행이 가능한 경우에만 메뉴 화면을 띄움
			if(UserModePanel.menuPanel.isAnyMenuExists()) {
				System.out.println("=======================================");
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(매장/포장 선택) :: 주문 단계로 진입합니다.");
				
				setVisible(false);
				
				UserModePanel.menuPanel.setVisible(true);
				UserModePanel.menuPanel.initialize();
				
			} else {	// 활성화 상태의 메뉴가 없어 주문 진행이 불가능한 경우 안내 메시지가 적힌 다이얼로그를 띄움
				System.out.println("=======================================");
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(매장/포장 선택) :: 활성화 상태의 메뉴가 없습니다.");
				
				new EnterMenuFailDialog();
				
			}
			//=======================================
			
		}
	}
}