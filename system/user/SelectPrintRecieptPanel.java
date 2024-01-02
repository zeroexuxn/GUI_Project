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
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;
import system.user.dialog.WaitNotNReceiptDialog;

public class SelectPrintRecieptPanel extends JPanel {
	
	private String imgSrc = "src/img/img_receipt.png";		// 영수증 출력 예시 이미지
	
	CustomButton btnNoPrint, btnPrint;
	
	// 생성자
	public SelectPrintRecieptPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("영수증 출력 여부를 선택해주세요.", JLabel.CENTER);
		labelTitle.toYellow(30, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 결제 방법
		CustomLabel labelReceipt = new CustomLabel("영수증", JLabel.CENTER, 18.0f);
		labelReceipt.setForeground(CustomColor.KIOSK_GRAY_120);

		labelReceipt.setBounds(300, 300, 120, 30);
		this.add(labelReceipt);
		
		// ===== 출력/미출력 안내 메시지
		CustomLabel labelReqMsg = new CustomLabel("영수증 출력을 원하시면 출력 버튼을 클릭해주세요.", JLabel.CENTER, 20.0f);
		labelReqMsg.setBounds(130, 330, 460, 40);
		this.add(labelReqMsg);
		
		// ===== 예시 사진
		CustomLabel labelExImg = new CustomLabel(new ImageIcon(imgSrc));
		labelExImg.setBounds(80, 280, 560, 360);
		this.add(labelExImg);
		
		// ===== 미출력 버튼
		btnNoPrint = new CustomButton("미출력");
		btnNoPrint.toBlack(25);
		btnNoPrint.addActionListener(new PrinWaitNoNReceiptListener());
		btnNoPrint.setBounds(120, 680, 220, 60);
		this.add(btnNoPrint);
		
		// ===== 출력 버튼
		btnPrint = new CustomButton("출력");
		btnPrint.toGreen(25);
		btnPrint.addActionListener(new PrinWaitNoNReceiptListener());
		btnPrint.setBounds(360, 680, 220, 60);
		this.add(btnPrint);
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
	
	// PrinWaitNoNReceiptListener - btnNoPrint, btnPrint 버튼 클릭 이벤트 처리를 위한 리스너
	public class PrinWaitNoNReceiptListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 영수증 미출력/출력 버튼에 따라 대기번호와 함께 영수증을 출력한다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//===============[여기 채우기]==============
			CustomButton btn = (CustomButton)e.getSource();
			
			System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(영수증 출력 여부 선택) :: 교환권 및 영수증 출력 후, 완료 단계로 넘어갑니다.");
			
			if(btn == btnNoPrint) new WaitNotNReceiptDialog(false);
			else new WaitNotNReceiptDialog(true);
			
			setVisible(false);
			UserModePanel.finishOrderPanel.setVisible(true);
			UserModePanel.finishOrderPanel.initialize(UserModePanel.salesTotal.getWaitNo());		// 대기번호 전달
			UserModePanel.finishOrderPanel.countDown();
			//=======================================
			
		}
	}
}
