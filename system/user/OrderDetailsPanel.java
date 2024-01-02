package system.user;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.OrderedMenuType;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class OrderDetailsPanel extends JPanel {
	private int discountAmt;
	
	private CustomLabel[] labelMenuName, labelQuantity, labelTotalPrice;		// 주문 목록 내 메뉴명, 수량, 금액
	private CustomButton btnScrollUp, btnScrollDown;							// 주문 목록 스크롤 버튼 (▲, ▼)
	private CustomLabel labelTotalCnt, labelTotalAmt;						// 합계 수량, 합계 금액 레이블
	private CustomLabel labelDiscountAmt;									// 할인 금액 레이블
	private CustomLabel labelRealTotalAmt;									// 할인 금액이 적용되어 실제 결제가 이루어질 금액
	private CustomButton btnCancel, btnPay;									// 취소, 결제 버튼
	
	private int viewIndex = 0;	// 화면에 보여지는 주문 목록 내 메뉴 중 가장 처음에 있는 메뉴의 인덱스
	
	// 생성자
	public OrderDetailsPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("주문 세부 내역을 확인해주세요.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 주문 목록 헤더 (메뉴명, 수량, 금액)
		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(CustomColor.KIOSK_YELLOW);
		panelHeader.setLayout(null);
		panelHeader.setBounds(60, 200, 600, 40);
		this.add(panelHeader);
		
		CustomLabel labelHeaderMenuName = new CustomLabel("메뉴", JLabel.CENTER);
		labelHeaderMenuName.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelHeaderMenuName.setBounds(10, 0, 360, 40);
		panelHeader.add(labelHeaderMenuName);
		
		CustomLabel labelHeaderQuantity = new CustomLabel("수량", JLabel.CENTER);
		labelHeaderQuantity.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelHeaderQuantity.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.white));
		labelHeaderQuantity.setBounds(370, 0, 90, 40);
		panelHeader.add(labelHeaderQuantity);
		
		CustomLabel labelHeaderTotalPrice = new CustomLabel("금액", JLabel.CENTER);
		labelHeaderTotalPrice.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelHeaderTotalPrice.setBounds(460, 0, 130, 40);
		panelHeader.add(labelHeaderTotalPrice);
		
		// ===== 주문 목록 (메뉴명, 수량, 금액)
		JPanel panelCartView = new JPanel();
		panelCartView.setBounds(60, 240, 600, 400);
		panelCartView.setBackground(Color.WHITE);
		panelCartView.setLayout(null);
		this.add(panelCartView);
		
		labelMenuName = new CustomLabel[10];
		labelQuantity = new CustomLabel[10];
		labelTotalPrice = new CustomLabel[10];
		
		for(int i=0; i<10; i++) {
			labelMenuName[i] = new CustomLabel("", 17.0f);
			labelMenuName[i].setBounds(10, 40 * i, 360, 40);
			labelMenuName[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CustomColor.KIOSK_GRAY_242));
			panelCartView.add(labelMenuName[i]);
			
			labelQuantity[i] = new CustomLabel("", JLabel.CENTER, 17.0f);
			labelQuantity[i].setBounds(370, 40 * i, 90, 40);
			labelQuantity[i].setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, CustomColor.KIOSK_GRAY_242));
			panelCartView.add(labelQuantity[i]);
			
			labelTotalPrice[i] = new CustomLabel("", JLabel.RIGHT, 17.0f);
			labelTotalPrice[i].setBounds(460, 40 * i, 130, 40);
			labelTotalPrice[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CustomColor.KIOSK_GRAY_242));
			panelCartView.add(labelTotalPrice[i]);
		}
		
		// ===== 주문 목록 스크롤 버튼 (▲, ▼)
		btnScrollUp = new CustomButton("▲");
		btnScrollUp.toScrollDesign();
		btnScrollUp.addActionListener(new updateCartListener());
		btnScrollUp.setBounds(280, 650, 40, 40);
		this.add(btnScrollUp);
		
		btnScrollDown = new CustomButton("▼");
		btnScrollDown.toScrollDesign();
		btnScrollDown.addActionListener(new updateCartListener());
		btnScrollDown.setBounds(400, 650, 40, 40);
		this.add(btnScrollDown);

		// ===== 결과 (합계, 할인, 최종)
		JPanel panelResult = new JPanel();
		panelResult.setBounds(60, 700, 600, 120);
		panelResult.setBackground(Color.WHITE);
		panelResult.setLayout(null);
		this.add(panelResult);
		
		// --- 합계
		CustomLabel labelTotal = new CustomLabel("합계");
		labelTotal.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelTotal.setBounds(10, 0, 360, 40);
		panelResult.add(labelTotal);
		
		labelTotalCnt = new CustomLabel("", JLabel.CENTER);
		labelTotalCnt.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelTotalCnt.setBounds(370, 0, 90, 40);
		panelResult.add(labelTotalCnt);
		
		labelTotalAmt = new CustomLabel("", JLabel.RIGHT);
		labelTotalAmt.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelTotalAmt.setBounds(460, 0, 130, 40);
		panelResult.add(labelTotalAmt);
		
		// --- 할인
		CustomLabel labelDiscount = new CustomLabel("할인");
		labelDiscount.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelDiscount.setBounds(10, 40, 450, 40);
		panelResult.add(labelDiscount);
		
		labelDiscountAmt = new CustomLabel("", JLabel.RIGHT);
		labelDiscountAmt.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 22));
		labelDiscountAmt.setBounds(460, 40, 130, 40);
		panelResult.add(labelDiscountAmt);
		
		// --- 최종
		CustomLabel labelRealTotal = new CustomLabel("총 결제 금액");
		labelRealTotal.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 25));
		labelRealTotal.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CustomColor.KIOSK_GRAY_120));
		labelRealTotal.setBounds(10, 80, 450, 40);
		panelResult.add(labelRealTotal);
		
		labelRealTotalAmt = new CustomLabel("", JLabel.RIGHT);
		labelRealTotalAmt.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 25));
		labelRealTotalAmt.setForeground(CustomColor.KIOSK_RED);
		labelRealTotalAmt.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CustomColor.KIOSK_GRAY_120));
		labelRealTotalAmt.setBounds(460, 80, 130, 40);
		panelResult.add(labelRealTotalAmt);
		
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
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문 세부 내역) :: 주문 단계로 돌아갑니다.");
				
				setVisible(false);
				UserModePanel.menuPanel.setVisible(true);
				//=======================================
				
			}
		});
		btnCancel.setBounds(40, 900, 160, 60);
		this.add(btnCancel);
		
		// ===== 결제 버튼
		btnPay = new CustomButton("결제");
		btnPay.toGreen(25);
		btnPay.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
			 * 반환값: 없음
			 * 기능 설명: 결제 버튼이 클릭되면 결제를 진행할 수 있도록 결제수단 패널을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//===============[여기 채우기]==============
				if((OrderedMenuType.getTOTAL_AMT() - discountAmt) != 0) {	// 결제할 금액이 있는 경우 결제 단계로 넘어감
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문 세부 내역) :: 결제 수단 선택 단계로 넘어갑니다.");
					
					UserModePanel.salesTotal.setTotalAmt(OrderedMenuType.getTOTAL_AMT() - discountAmt);
					
					setVisible(false);
					UserModePanel.selectPayMethodPanel.setVisible(true);
					
				} else {	// 총 결제 금액이 0원이면 결제(결제 수단 선택, 승인 요청) 프로세스를 진행하지 않읍
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문 세부 내역) :: 데이터 처리 단계로 넘어갑니다.");
					
					UserModePanel.salesTotal.setTotalAmt(0);	// 거래 금액
					
					// 거래 일시
					Date today = new Date();
					Locale currentLocale = new Locale("KOREAN", "KOREA");
					String pattern = "yyyyMMddHHmmss";
					SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
					
					String transDt = formatter.format(today);
					UserModePanel.salesTotal.setTransDt(transDt);
					
					setVisible(false);
					UserModePanel.updateDataProcess.updateData("");
				}
				//=======================================
				
			}
		});
		btnPay.setBounds(520, 900, 160, 60);
		this.add(btnPay);
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
	 * 파라미터: int discountAmt (할인 금액: 사용할 포인트 혹은 쿠폰의 금액)
	 * 반환값: 없음
	 * 기능 설명: 결제 진행 전 주문 목록에 담긴 메뉴의 내용(메뉴명, 수량, 금액)과 포인트/쿠폰 사용 여부에 따라 할인 금액이 적용된 총 결제 금액 정보로 내용을 초기화한다.
	 */
	public void initialize(int discountAmt) {
		viewIndex = 0;
		this.discountAmt = discountAmt;
		
		// ===== 주문 목록 (메뉴명, 수량, 금액)
		for(int i=0; i<10; i++) {
			if(i < UserModePanel.cart.size()) {
				labelMenuName[i].setVisible(true);
				labelQuantity[i].setVisible(true);
				labelTotalPrice[i].setVisible(true);
				
				labelMenuName[i].setText(UserModePanel.cart.get(i).getName());
				labelQuantity[i].setText(UserModePanel.cart.get(i).getQuantity() + "");
				labelTotalPrice[i].setText(String.format("%,d", UserModePanel.cart.get(i).getTotalPrice()));
				
			} else {
				labelMenuName[i].setVisible(false);
				labelQuantity[i].setVisible(false);
				labelTotalPrice[i].setVisible(false);
			}
		}
		
		// ===== 주문 목록 스크롤 버튼 (▲, ▼)
		btnScrollUp.setEnabled(false);	// 초기화 시 0번째 메뉴를 가리키므로 이전 메뉴가 없어 ▲ 버튼 비활성화
		
		if(UserModePanel.cart.size() > 10) btnScrollDown.setEnabled(true);	// 주문 목록의 개수가 10개를 초과한 경우에만 ▼ 버튼 활성화
		else btnScrollDown.setEnabled(false);
		
		// ===== 합계
		labelTotalCnt.setText(OrderedMenuType.getTOTAL_QUANTITY() + "");				// 합계 수량
		labelTotalAmt.setText(String.format("%,d", OrderedMenuType.getTOTAL_AMT()));	// 합계 금액
		
		// ===== 할인 (금액)
		
		if(discountAmt != 0) labelDiscountAmt.setText(String.format("-%,d", discountAmt));
		else labelDiscountAmt.setText("");
		
		// ===== 총 결제 금액
		labelRealTotalAmt.setText(String.format("%,d원", OrderedMenuType.getTOTAL_AMT() - discountAmt));		// 총 결제 금액	

	}
	
	// updateCartListener - btnScrollUp, btnScrolDown 버튼 클릭 이벤트 처리
	public class updateCartListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 주문 목록 스크롤 버튼 (▲, ▼)이 클릭될 때마다 주문 목록에 있는 이전/다음 메뉴를 보여준다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			// ▲ 버튼이 클릭되면 이전 메뉴를 화면 상에서 가장 첫번째 메뉴로 가리키고, ▼ 버튼이 클릭되면 다음 메뉴를 화면 상에서 가장 첫번째 메뉴로 가리킴
			if(btn == btnScrollUp) viewIndex--;
			else viewIndex++;
			
			for(int i=0; i<10; i++) {
				// 출력할 내용이 있는 경우 (ex. 전체 주문 메뉴가 3개일 때, 0~2번째 줄에는 주문 메뉴의 내용이 출력되어야 함)
				if(viewIndex+i < UserModePanel.cart.size()) {
					labelMenuName[i].setVisible(true);
					labelQuantity[i].setVisible(true);
					labelTotalPrice[i].setVisible(true);
					
					labelMenuName[i].setText(UserModePanel.cart.get(viewIndex+i).getName());
					labelQuantity[i].setText(UserModePanel.cart.get(viewIndex+i).getQuantity() + "");
					labelTotalPrice[i].setText(String.format("%,d", UserModePanel.cart.get(viewIndex+i).getTotalPrice()));
					
				// 주문 목록이 없거나 출력할 내용이 없는 경우 (ex. 전체 주문 메뉴가 3개일 때, 3~4번째 줄에는 내용이 없음)
				} else if (UserModePanel.cart.size() == 0 || viewIndex+i >= UserModePanel.cart.size()){
					labelMenuName[i].setVisible(false);
					labelQuantity[i].setVisible(false);
					labelTotalPrice[i].setVisible(false);
				}
			}
			
			// 현재 보여지는 주문 목록 기준으로 이전 내용이 없다면 ▲ 버튼을 비활성화하고, 아니라면 활성화
			if(viewIndex == 0) btnScrollUp.setEnabled(false);
			else btnScrollUp.setEnabled(true);
			
			// 현재 보여지는 주문 목록 기준으로 다음 내용이 없다면 ▼ 버튼을 비활성화하고, 아니라면 활성화
			if(viewIndex + 10 < UserModePanel.cart.size()) btnScrollDown.setEnabled(true);
			else btnScrollDown.setEnabled(false);
		}
	}
}
