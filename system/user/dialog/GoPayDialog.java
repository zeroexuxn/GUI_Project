package system.user.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import system.main.KioskFrame;
import system.user.UserModePanel;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class GoPayDialog extends JDialog {
	private final static String[] CARD_NAME_LIST = {"비씨", "국민", "하나", "삼성", "신한", "현대", "롯데", "씨티", "농협", "우리"}; // 제휴되어 있는 카드사 목록
	
	private CustomLabel labelCountDown;		// 카운트다운 레이블
	
	/* 생성자: GoPayDialog
	 * 파라미터: char payType (결제 유형) (C: 카드 결제, S: 간편 결제)
	 * 기능 설명: 결제 유형에 따라 출력되는 메시지를 다르게 하여 승인 요청 다이얼로그를 띄운다.
	 */
	public GoPayDialog(char payType) {
		super(KioskFrame.getInstance(), "승인 요청 중...", true);

		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제) :: 다이얼로그에서 승인 요청을 진행하고 있습니다.");
		
		this.setSize(400, 230);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		// ===== 현재 진행중인 프로세스에 대한 안내 레이블
		CustomLabel labelStepMsg = new CustomLabel("승인 요청 중입니다.", JLabel.CENTER, 20.0f);
		labelStepMsg.toYellow(20, 'D');
		labelStepMsg.setBounds(0, 0, 400, 40);
		panel.add(labelStepMsg);
		
		// ===== 주의사항 레이블
		String cautionMsg;
		if(payType == 'C') cautionMsg = "결제가 완료될 때까지 카드를 빼지 마세요.";
		else cautionMsg = "결제가 완료될 때까지 휴대전화를 떼지 마세요.";
		
		CustomLabel labelCautionMsg = new CustomLabel(cautionMsg, JLabel.CENTER, 17.0f);
		labelCautionMsg.setBounds(0, 40, 400, 30);
		panel.add(labelCautionMsg);
		
		// ===== 카운트다운 레이블
		labelCountDown = new CustomLabel("4", JLabel.CENTER);
		labelCountDown.setFont(new Font(CustomLabel.fontBMJUA, Font.PLAIN, 40));
		labelCountDown.setBounds(140, 70, 120, 120);
		panel.add(labelCountDown);
		
		// ===== 이미지 레이블
		CustomLabel labelImg = new CustomLabel(new ImageIcon("src/img/paying(90px).gif"));
		labelImg.setBounds(270, 70, 120, 120);
		panel.add(labelImg);
		
		// ===== Timer 객체 생성
		Timer timer = new Timer(1000, new ActionListener() {
			int count = 3;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(count == 0) {
					
					//===============[여기 채우기]==============
					Random random = new Random();	// 난수 생성을 위한 Random 객체
					
					// 카드사명
					int size = CARD_NAME_LIST.length;
					String cardName = CARD_NAME_LIST[random.nextInt(size)];	// 제휴되어 있는 카드사 중 하나
					UserModePanel.salesTotal.setCardName(cardName);
					
					// 카드번호 - 신용카드 결제이면 입력받은 카드번호에 마스킹 처리, 바코드 결제면 임의 생성
					String cardNo = String.format("%06d", random.nextInt(1000000)) + "******" + String.format("%04d", random.nextInt(10000));
					UserModePanel.salesTotal.setCardNo(cardNo);

					// 할부개월
					UserModePanel.salesTotal.setCardQuota("00");
					
					// 승인번호 - 8자리 임의값
					UserModePanel.salesTotal.setAuthCode(String.format("%08d", random.nextInt(100000000)));

					if(UserModePanel.salesTotal.getDiscountId().equals(" ")) {	// 포인트나 쿠폰을 사용하지 않은 경우
						System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제) :: 승인 요청이 완료되어 포인트 적립 단계로 넘어갑니다.");
						
						dispose();
						UserModePanel.payCardOrSimplePanel.setVisible(false);
						UserModePanel.earnPointPanel.setVisible(true);
						UserModePanel.earnPointPanel.initialize();
						
					} else {	// 포인트나 쿠폰을 사용한 경우
						System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(결제) :: 승인 요청이 완료되어 데이터 처리 단계로 넘어갑니다.");
						
						dispose();
						UserModePanel.payCardOrSimplePanel.setVisible(false);
						UserModePanel.updateDataProcess.updateData("");
					}
					//=======================================
					
				}

				labelCountDown.setText(count + "");
				count--;
			}
		});
		timer.start(); // 타이머 시작
		
		this.setVisible(true);
	}
	
}
