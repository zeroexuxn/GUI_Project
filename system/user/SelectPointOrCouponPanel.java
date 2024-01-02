package system.user;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.user.SelectHereOrTakeoutPanel.HereOrTakeoutListener;
import system.user.SelectPointOrCouponPanel.PointOrCouponListener;
import system.user.custom.CustomButton;
import system.user.custom.CustomLabel;

public class SelectPointOrCouponPanel extends JPanel {
	private CustomButton btnPoint, btnCoupon;	// 포인트, 쿠폰 버튼
	private CustomButton btnCancel;				// 취소 버튼
	private CustomButton btnNotUse;				// 미사용 버튼
	
	// 버튼에 들어갈 이미지 아이콘 경로
	private String iconPointSrc = "src/img/icon_Point.png";
	private String iconCouponSrc = "src/img/icon_Coupon.png";
	
	// 생성자
	public SelectPointOrCouponPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("포인트/쿠폰 사용 여부를 선택해주세요.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 포인트 / 쿠폰 버튼
		String strPoint = "<html><div style=\"text-align:center\">포인트<br><font size=6>Point</font><div></html>";
		btnPoint = new CustomButton(strPoint, new ImageIcon(iconPointSrc));
		btnPoint.toWhite(40, 'D');
		btnPoint.addActionListener(new PointOrCouponListener());
		btnPoint.setBounds(120, 360, 220, 260);
		this.add(btnPoint);
		
		String strCoupon = "<html><div style=\"text-align:center\">쿠폰<br><font size=6>Coupon</font><div></html>";
		btnCoupon = new CustomButton(strCoupon, new ImageIcon(iconCouponSrc));
		btnCoupon.toWhite(40, 'D');
		btnCoupon.addActionListener(new PointOrCouponListener());
		btnCoupon.setBounds(380, 360, 220, 260);
		this.add(btnCoupon);
		
		// ===== 취소 버튼
		btnCancel = new CustomButton("취소");
		btnCancel.toBlack(25);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 여부 선택) :: 주문 단계로 돌아갑니다.");
				
				setVisible(false);
				UserModePanel.menuPanel.setVisible(true);
				//=======================================
				
			}
		});
		btnCancel.setBounds(40, 900, 160, 60);
		this.add(btnCancel);
		
		// ===== 미사용 버튼
		btnNotUse = new CustomButton("미사용");
		btnNotUse.toGreen(25);
		btnNotUse.addActionListener(new PointOrCouponListener());
		btnNotUse.setBounds(520, 900, 160, 60);
		this.add(btnNotUse);
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
	
	// PointOrCouponListener - btnPoint, btnCoupon, btnNotUse 버튼 클릭 이벤트 처리
	public class PointOrCouponListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 1) 포인트/쿠폰 버튼 클릭 시 포인트/쿠폰 사용을 위한 패널을 띄운다.
		 * 		   2) 미사용 버튼 클릭 시 세부 주문 내역을 보여주는 패널을 띄운다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//===============[여기 채우기]==============
			char discountType = 'N';
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			if(btn == btnNotUse) {
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 여부 선택) :: 주문 세부 내역 단계로 진입합니다.");
				
				UserModePanel.salesTotal.setDiscountId(" ");
				
				setVisible(false);
				UserModePanel.orderDetailsPanel.setVisible(true);
				UserModePanel.orderDetailsPanel.initialize(0);
			}
			
			else if(btn == btnPoint) {
				discountType = 'P';
				UserModePanel.salesTotal.setPayMethod("포인트");
			}
			else {
				discountType = 'C';
				UserModePanel.salesTotal.setPayMethod("쿠폰");
			}
			
			if(discountType != 'N') {
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 여부 선택) :: 포인트/쿠폰 ID 입력 단계로 진입합니다.");
				
				setVisible(false);
				UserModePanel.usePointOrCouponPanel.setVisible(true);
				UserModePanel.usePointOrCouponPanel.initialize(discountType);
			}
			//=======================================

		}
	}
	
}