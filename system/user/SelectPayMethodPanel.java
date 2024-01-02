package system.user;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.user.custom.CustomButton;
import system.user.custom.CustomLabel;

public class SelectPayMethodPanel extends JPanel {
	private CustomButton btnCard;			// 신용카드 버튼
	private CustomButton[] btnSimplePay;		// 간편 결제 버튼 배열
	private CustomButton btnCancel;			// 취소 버튼
	
	private String iconCardSrc = "src/img/icon_Card.png";	// 버튼에 들어갈 이미지 아이콘 경로
	
	// {간편결제 이름 : 이미지 아이콘 경로}로 매핑되어 있는 맵 (화면 배치 상 최대 9종류의 간편결제 제공 가능)
	private LinkedHashMap<String, String> simplePayMap = new LinkedHashMap<String, String>() {
		{
			put("카카오페이", "src/img/icon_KakaoPay.png");
			put("페이코", "src/img/icon_Payco.png");
			put("네이버페이", "src/img/icon_NaverPay.png");
			put("제로페이", "src/img/icon_ZeroPay.png");
			put("삼성페이", "src/img/icon_SamsungPay.png");
			put("페이북", "src/img/icon_Paybooc.png");
		}
	};
	
	// 생성자
	public SelectPayMethodPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("결제 수단을 선택해주세요.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 카드 결제 레이블
		CustomLabel labelCard = new CustomLabel("카드 결제", 22.0f);
		labelCard.setBounds(80, 220, 100, 40);
		this.add(labelCard);
		
		// ===== 신용카드 버튼
		btnCard = new CustomButton("신용카드", new ImageIcon(iconCardSrc));
		btnCard.toWhite(25, 'D');
		btnCard.addActionListener(new payMethodListener());
		btnCard.setBounds(80, 260, 160, 120);
		this.add(btnCard);
		
		// ===== 간편 결제 레이블
		CustomLabel labelSimplePay = new CustomLabel("간편 결제", 22.0f);
		labelSimplePay.setBounds(80, 420, 100, 40);
		this.add(labelSimplePay);
		
		// ===== 간편 결제 버튼
		btnSimplePay = new CustomButton[simplePayMap.size()];	// 현재 지정되어 있는 간편 결제 목록에 따라 동적으로 배열 생성
		int index = 0;
		for (Map.Entry entry : simplePayMap.entrySet()) {
			btnSimplePay[index] = new CustomButton((String)entry.getKey(), new ImageIcon((String)entry.getValue()));
			btnSimplePay[index].toWhite(25, 'D');
			btnSimplePay[index].addActionListener(new payMethodListener());
			
			int x = 80 + 200 * (index % 3);
			int y = 460 + 140 * (index / 3);
			btnSimplePay[index].setBounds(x, y, 160, 120);
			this.add(btnSimplePay[index]);
			
			index++;
		}
		
		// ===== 취소 버튼
		btnCancel = new CustomButton("취소");
		btnCancel.toBlack(25);
		btnCancel.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
			 * 반환값: 없음
			 * 기능 설명: 취소 버튼이 클릭되면 주문을 계속 진행할 수 있도록 메뉴 패널을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제 수단 선택) :: 주문 단계로 돌아갑니다.");
				
				setVisible(false);
				UserModePanel.menuPanel.setVisible(true);
				//=======================================
				
			}
		});
		btnCancel.setBounds(40, 900, 160, 60);
		this.add(btnCancel);
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
	
	// payMethodListener - btnCard, btnSimplePay 버튼 클릭 이벤트 처리
	public class payMethodListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//===============[여기 채우기]==============
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제 수단 선택) :: 결제 단계로 넘어갑니다.");
			
			if(btn == btnCard) {
				UserModePanel.salesTotal.setPayMethod("신용카드");
				
				setVisible(false);
				
				UserModePanel.payCardOrSimplePanel.setVisible(true);
				UserModePanel.payCardOrSimplePanel.initialize('C');
				
			} else {
				UserModePanel.salesTotal.setPayMethod("간편결제");
				
				setVisible(false);
				
				UserModePanel.payCardOrSimplePanel.setVisible(true);
				UserModePanel.payCardOrSimplePanel.initialize('S');
			}
			//=======================================
			
		}
	}
}