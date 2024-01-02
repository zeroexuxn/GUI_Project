package system.user;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;
import system.user.dialog.GoPayDialog;

public class PayCardOrSimplePanel extends JPanel {
	private char payType;
	
	private String title;
	private String payMethod;
	private String reqMsg;
	
	private CustomLabel labelTitle; 						// 상단 안내 메시지
	private CustomLabel labelPayMethod, labelReqMsg; 	// 결제 방법, 승인 요청 메시지
	private CustomLabel labelTotalAmt;					// 결제 금액
	private CustomLabel labelExImg;						// 예시 이미지
	
	// 안내 이미지 경로
	private String imgCardSrc = "src/img/img_card.png";			// 카드 결제
	private String imgSimpleSrc = "src/img/img_simple.png";		// 간편 결제
	
	CustomButton btnCancel, btnRequest;
	
	// 생성자
	public PayCardOrSimplePanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		labelTitle = new CustomLabel("", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 결제 방법
		labelPayMethod = new CustomLabel("", JLabel.CENTER, 18.0f);
		labelPayMethod.setForeground(CustomColor.KIOSK_GRAY_120);
		labelPayMethod.setBounds(300, 300, 120, 30);
		this.add(labelPayMethod);
		
		// ===== 승인 요청 메시지
		labelReqMsg = new CustomLabel("", JLabel.CENTER, 20.0f);
		labelReqMsg.setBounds(110, 330, 500, 40);
		this.add(labelReqMsg);
		
		// ===== 결제 금액
		labelTotalAmt = new CustomLabel("", JLabel.CENTER);
		labelTotalAmt.setFont(new Font(CustomLabel.fontBMJUA, JLabel.CENTER, 30));
		labelTotalAmt.setBounds(120, 570, 480, 50);
		this.add(labelTotalAmt);
		
		// ===== 예시 이미지
		labelExImg = new CustomLabel();
		labelExImg.setBounds(80, 280, 560, 360);
		this.add(labelExImg);

		// ===== 취소 버튼
		btnCancel = new CustomButton("취소");
		btnCancel.toBlack(25);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제) :: 승인 요청이 취소되어 결제 수단 선택 단계로 돌아갑니다.");
				
				setVisible(false);
				UserModePanel.selectPayMethodPanel.setVisible(true);
				//=======================================
				
			}
		});
		btnCancel.setBounds(120, 680, 220, 60);
		this.add(btnCancel);
		
		// ===== 승인 요청 버튼
		btnRequest = new CustomButton("승인 요청");
		btnRequest.toGreen(25);
		btnRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				//===============[여기 채우기]==============
				// 거래 일시
				Date today = new Date();
				Locale currentLocale = new Locale("KOREAN", "KOREA");
				String pattern = "yyyyMMddHHmmss";
				SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
				
				String transDt = formatter.format(today);
				UserModePanel.salesTotal.setTransDt(transDt);
				
				new GoPayDialog(payType);
				//=======================================
			}
		});
		btnRequest.setBounds(360, 680, 220, 60);
		this.add(btnRequest);	

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
	 * 파라미터: char payType
	 * 반환값: 없음
	 * 기능 설명: 전달받은 결제 유형에 따라 다른 내용이 출력될 수 있도록 내용을 초기화한다.
	 */
	public void initialize(char payType) {
		this.payType = payType;
		
		if(payType == 'C') {
			title = "다음 그림과 같이 카드 투입구에 카드를 넣어주세요.";
			payMethod = "카드 결제";
			reqMsg = "카드를 넣은 후 화면의 승인 요청 버튼을 클릭해주세요.";
			
			labelExImg.setIcon(new ImageIcon(imgCardSrc));
			
		} else {
			title = "다음 그림과 같이 NFC 리더기에 휴대전화를 태그해주세요.";
			payMethod = "간편 결제";
			reqMsg = "휴대전화를 태그한 후 화면의 승인 요청 버튼을 클릭해주세요.";
			
			labelExImg.setIcon(new ImageIcon(imgSimpleSrc));
		}
		
		// ===== 상단 안내 메시지
		labelTitle.setText(title);
		
		// ===== 결제 방법
		labelPayMethod.setText(payMethod);

		// ===== 승인 요청 메시지
		labelReqMsg.setText(reqMsg);
		
		// ===== 결제 금액
		labelTotalAmt.setText(String.format("결제금액 : %,d원", UserModePanel.salesTotal.getTotalAmt()));
	}
	
}
