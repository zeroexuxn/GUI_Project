package system.user;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.CouponType;
import system.data.MemberType;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;
import system.user.dialog.InputCouponUseDialog;
import system.user.dialog.InputPointAmtDialog;

public class UsePointOrCouponPanel extends JPanel {
	private char discountType;		// 할인 유형 (P: 포인트, C: 쿠폰)
	private String discountId;		// 포인트를 사용한 회원 ID 혹은 사용한 쿠폰 ID
	private int discountAmt;		// 할인 금액
	
	private String title;			// 상단 안내 메시지 내용
	private String inputEx;			// 입력값 예시 (포인트: 01012345678, 쿠폰: C01012345678000)
	private int limitMin;			// 입력값의 최소 길이 (포인트: 3, 쿠폰: 4)
	private int limitMax;			// 입력값의 최대 길이 (포인트: 11, 쿠폰: 15)
	
	private CustomLabel labelTitle; 					// 상단 안내 메시지
	private CustomLabel labelInput, labelInputEx;	// 입력, 입력 예시 레이블
	private CustomButton[] btnKey;					// 키패드: 숫자키, 지우기(<), 입력 버튼
	private CustomLabel labelError;					// 사용 불가 사유
	private CustomButton btnCancel;					// 취소 버튼
	
	// 생성자
	public UsePointOrCouponPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		labelTitle = new CustomLabel("", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 입력, 입력 예시 레이블
		labelInput = new CustomLabel("", JLabel.CENTER);
		labelInput.toYellow(40, 'T');
		labelInput.setBounds(140, 220, 440, 70);
		this.add(labelInput);
		
		labelInputEx = new CustomLabel("", JLabel.CENTER, 20.0f);
		labelInputEx.setForeground(CustomColor.KIOSK_GRAY_120);
		labelInputEx.setBounds(240, 300, 240, 20);
		this.add(labelInputEx);
		
		// ===== 키패드
		btnKey = new CustomButton[12];
		for(int i = 1; i <= 9; i++) {
			btnKey[i] = new CustomButton(i+"");
			
			int x = 135 + 150 * ((i-1) % 3);
			int y = 350 + 110 * ((i-1) / 3);
			btnKey[i].setBounds(x, y, 150, 110);
		}
		
		btnKey[0] = new CustomButton("0");
		btnKey[10] = new CustomButton("<");
		btnKey[11] = new CustomButton("입력");
		
		btnKey[0].setBounds(285, 680, 150, 110);
		btnKey[10].setBounds(135, 680, 150, 110);
		btnKey[11].setBounds(435, 680, 150, 110);
		
		for(int i = 0; i < btnKey.length; i++) {
			btnKey[i].toWhite(40, 'D');
			btnKey[i].addActionListener(new searchDiscountAmtListener());
			this.add(btnKey[i]);
		}
		
		// ===== 결과 레이블
		labelError = new CustomLabel("", JLabel.CENTER, 25.0f);
		labelError.setForeground(CustomColor.KIOSK_RED);
		labelError.setBounds(135, 820, 450, 60);
		this.add(labelError);
		
		// ===== 취소 버튼
		btnCancel = new CustomButton("취소");
		btnCancel.toBlack(25);
		btnCancel.setBounds(40, 900, 160, 60);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 ID 입력) :: 쿠폰/포인트 여부 선택 단계로 돌아갑니다.");
				
				setVisible(false);
				UserModePanel.selectPointOrCouponPanel.setVisible(true);
				//=======================================
				
			}
		});
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
	
	/* 메소드명: initialize
	 * 파라미터: char discountType (P(포인트), C(쿠폰))
	 * 반환값: 없음
	 * 기능 설명: 전달받은 할인 유형에 따라 다른 내용이 출력될 수 있도록 내용을 초기화한다.
	 */
	public void initialize(char discountType) {
		this.discountType = discountType;
		this.discountId = "";
		this.discountAmt = 0;
		
		if(discountType == 'P') {	// 포인트
			title = "포인트를 사용할 회원의 휴대전화 번호를 입력해주세요.";
			inputEx = "01012345678";
			limitMin = 3;
			limitMax = 11;
		} else {					// 쿠폰
			title = "사용할 쿠폰 번호를 입력해주세요.";
			inputEx = "C01012345678000";
			limitMin = 4;
			limitMax = 15;
		}
		
		// ===== 상단 안내 메시지
		labelTitle.setText(title);
		
		// ===== 입력, 입력 예시 레이블
		labelInput.setText(inputEx.substring(0, limitMin));
		labelInputEx.setText("예시) " + inputEx);
		
		// ===== 키패드
		btnKey[11].setEnabled(false);
		
		// ===== 결과 레이블
		labelError.setText("");
	}

	/* 메소드명: serachPointAmt
	 * 파라미터: String memberId (포인트를 조회할 회원 ID)
	 * 반환값: int pointAmt (사용 가능한 포인트를 가지고 있는 경우의 포인트 금액, 조회에 실패하거나 사용할 수 없는 경우 0)
	 * 기능 설명: 전달받은 회원 ID를 이용해 회원 목록에서 회원을 조회한 후 보유한 포인트를 반환한다.
	 */
	private int serachPointAmt(String memberId) {
		BufferedReader in;	// 입력 스트림
		
		try {
			System.out.println("회원 목록을 읽기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 회원 파일이 없는 경우
			System.out.println("회원 파일이 존재하지 않습니다.");
			labelError.setText("회원 조회에 실패하였습니다.");
			return 0;
		}
		
		String s;				// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;			// 구분자로 분리한 문자열이 저장될 임시 배열
		int pointAmt = -9999;	// 포인트 금액 변수 (초기값: -9999)
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					System.out.println(">>> 회원 조회 성공");
					pointAmt = Integer.parseInt(tmp[MemberType.INDEX_POINT]);
					break;
				}
			}
			
			in.close();	// 스트림 닫기
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			labelError.setText("회원 조회에 실패하였습니다.");
			return 0;
		}
		
		if(pointAmt == -9999) {
			System.out.println(">>> 회원 조회 실패");
			labelError.setText("회원 정보를 찾을 수 없습니다.");
			return 0;
		}
		
		if(pointAmt < 100) {
			System.out.println();
			String text = "<html><div style=\"text-align:center\">포인트는 최소 100p부터 사용 가능합니다.<br>"
					+ "<font size=5>(현재 보유 포인트: " + pointAmt + "p)</font></div></html>";
			labelError.setText(text);
			return 0;
		}
		
		return pointAmt;
	}

	/* 메소드명: searchCouponAmt
	 * 파라미터: String couponId (조회할 쿠폰 ID)
	 * 반환값: int coupontAmt (조회에 성공한 쿠폰이 사용 가능한 상태일 때의 쿠폰 금액, 조회에 실패하거나 사용할 수 없는 경우 0)
	 * 기능 설명: 전달받은 쿠폰 ID를 이용해 쿠폰 목록에서 쿠폰을 조회한 후 금액을 반환한다.
	 */
	private int searchCouponAmt(String couponId) {
		BufferedReader in;	// 입력 스트림
		
		try {
			System.out.println("쿠폰 목록을 읽기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 쿠폰 파일이 없는 경우
			System.out.println("쿠폰 파일이 존재하지 않습니다.");
			labelError.setText("쿠폰 조회에 실패하였습니다.");
			return 0;
		}
		
		String s;					// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;				// 구분자로 분리한 문자열이 저장될 임시 배열
		int couponAmt = -9999;		// 쿠폰 금액 변수 (초기값: -9999)
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[CouponType.INDEX_COUPON_ID].equals(couponId)) {
				
					if(tmp[CouponType.INDEX_USED].equals("Y")) {	// 이미 사용된 쿠폰인 경우
						System.out.println(">>> 쿠폰 조회 성공 (사용 불가)");
						couponAmt = -1;
						
					} else {	// 사용된 적이 없는 쿠폰
						System.out.println(">>> 쿠폰 조회 성공 (사용 가능)");
						couponAmt = Integer.parseInt(tmp[CouponType.INDEX_PRICE]);
					}
						
					break;
				}
			}
			
			in.close();	// 스트림 닫기
			
		} catch(IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			labelError.setText("쿠폰 조회에 실패하였습니다.");
			return 0;
		}
		
		if(couponAmt == -9999) {
			System.out.println(">>> 쿠폰 조회 실패");
			labelError.setText("쿠폰 정보를 찾을 수 없습니다.");
			return 0;
		}
		
		if(couponAmt == -1) {
			labelError.setText("이미 사용된 쿠폰입니다.");
			return 0;
		}
		
		return couponAmt;
	}
	
	// searchDiscountAmtListener - btnKey 버튼 클릭 이벤트 처리
	private class searchDiscountAmtListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 1) 입력 버튼이 클릭되면 포인트/쿠폰 금액을 조회한 후 사용할 포인트/쿠폰 금액이 0이 아닌 경우 주문 세부 내역 출력 화면을 띄운다.
		 * 		   2) < 버튼이 클릭되면 기본값(포인트-010, 쿠폰-C0101) 외의 내용이 있을 때 뒤에서부터 순차적으로 지운다.
		 * 		   3) 입력할 수 있는 값이 남아 있는 상태(ex. 입력되어 있는 휴대전화 번호가 11자리 미만이거나 쿠폰 ID가 15자리 미만인 경우)라면
		 * 			  숫자키 버튼이 입력되었을 때 입력 레이블의 내용에 입력된 숫자를 추가한다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			int length = labelInput.getText().length();		// 입력 레이블에 입력된 텍스트의 길이
			
			if(btn.getText().equals("입력")) {
				discountId = labelInput.getText();			// 조회할 회원 ID 혹은 쿠폰 ID
				UserModePanel.salesTotal.setDiscountId(discountId);
				
				if(discountType == 'P') {
					// 포인트 사용 금액을 입력받기 위한 다이얼로그를 호출하고 포인트 금액을 받아옴
					int availableAmt = serachPointAmt(discountId);		// 보유 포인트 금액
					if(availableAmt != 0) discountAmt = new InputPointAmtDialog(availableAmt).getUsePointAmt();
					
				} else {
					// 쿠폰 사용 여부를 입력받기 위한 다이얼로그를 호출하고 쿠폰 금액을 받아옴
					int availableAmt = searchCouponAmt(discountId);		// 쿠폰 금액
					if(availableAmt != 0) discountAmt = new InputCouponUseDialog(availableAmt).getUseCouponAmt();
				}
				
				if(discountAmt != 0) {		// 포인트/쿠폰 사용 금액이 있는 경우
					
					//===============[여기 채우기]==============
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 ID 입력) :: 주문 세부 내역 단계로 진입합니다.");
					
					setVisible(false);
					UserModePanel.orderDetailsPanel.setVisible(true);
					UserModePanel.orderDetailsPanel.initialize(discountAmt);
					//=======================================
					
				}
			}
			
			// 지우기 버튼 (<)이 클릭되었고, 기본값 외의 데이터가 입력되어 있는 경우
			if(btn.getText().equals("<") && labelInput.getText().length() > limitMin) {
				// 마지막 문자를 지운 값으로 입력 레이블의 내용 초기화
				String str = labelInput.getText();
				labelInput.setText(str.substring(0,str.length()-1));
				
				btnKey[11].setEnabled(false);	// 입력 버튼 비활성화	 (지우기 버튼이 클릭되면 입력 레이블의 내용은 항상 limitMax 이므로)
				labelError.setText("");			// 결과 레이블의 내용 초기화
			}
			
			if(length < limitMax) {		// 입력해야 할 값이 남아 있는 상태
				if(!(btn.getText().equals("입력") || btn.getText().equals("<"))) {			// 클릭된 버튼이 숫자키인 경우
					labelInput.setText(labelInput.getText() + btn.getText());				// 입력 레이블의 내용 끝에 숫자 추가
					
					// 입력 레이블의 길이가 limitMax 미만이면 입력 버튼 비활성화, 아니면 입력 버튼 활성화
					if(labelInput.getText().length() < limitMax) btnKey[11].setEnabled(false);
					else btnKey[11].setEnabled(true);
				}
			}
		}
	}

}