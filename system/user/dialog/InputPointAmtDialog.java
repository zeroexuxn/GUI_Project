package system.user.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.OrderedMenuType;
import system.main.KioskFrame;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class InputPointAmtDialog extends JDialog {
	private boolean flag = true;	// 예외값 체크 (true: 정상 흐름, false: 예외 발생)
	private int usePointAmt;		// 사용할 포인트 금액
	
	/* 생성자: InputPointAmtDialog
	 * 파라미터: 없음
	 * 기능 설명: 사용할 포인트 금액을 입력받기 위한 다이얼로그 띄운다.
	 */
	public InputPointAmtDialog(int pointAmt) {
		super(KioskFrame.getInstance(), "포인트 사용 금액 입력", true);
		
		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 ID 입력) :: 다이얼로그에서 포인트 금액을 입력받고 있습니다.");
		
		this.setSize(540, 360);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		String info = "현재 보유 포인트: " + String.format("%,dp", pointAmt)
		+ " / 결제 금액: " + String.format("%,d원", OrderedMenuType.getTOTAL_AMT());
		CustomLabel labelInfo = new CustomLabel(info, JLabel.LEFT, 20.0f);
		labelInfo.setBounds(20, 10, 480, 40);
		panel.add(labelInfo);
		
		CustomLabel labelMsg1 = new CustomLabel("- 사용하실 포인트 금액을 입력해주세요.(최소 100p, 최대 99,900p)", JLabel.LEFT, 17.0f);
		labelMsg1.setBounds(20, 50, 480, 40);
		panel.add(labelMsg1);
		
		CustomLabel labelMsg2 = new CustomLabel("- 포인트 사용을 원하지 않을 경우 우측 상단의 X를 클릭해주세요.", JLabel.LEFT, 17.0f);
		labelMsg2.setBounds(20, 70, 480, 40);
		panel.add(labelMsg2);
		
		// 입력 레이블
		CustomLabel labelInput = new CustomLabel("00");
		labelInput.toWhite(17);
		labelInput.setBorder(BorderFactory.createLineBorder(CustomColor.KIOSK_GRAY_120));
		labelInput.setBounds(20, 110, 480, 40);
		panel.add(labelInput);
		
		// 숫자 입력을 위한 키패드
		CustomButton[] btnKey = new CustomButton[12];
		for(int i = 1; i <= 9; i++) {
			btnKey[i] = new CustomButton(i+"");
			
			int x = 20 + 80 * ((i-1) % 5);
			int y = 160 + 70 * ((i-1) / 5);
			btnKey[i].setBounds(x, y, 80, 70);
		}
		
		btnKey[0] = new CustomButton("0");
		btnKey[10] = new CustomButton("C");
		btnKey[11] = new CustomButton("입력");

		btnKey[0].setBounds(340, 230, 80, 70);
		btnKey[10].setBounds(420, 160, 80, 70);
		btnKey[11].setBounds(420, 230, 80, 70);
		btnKey[11].setEnabled(false);
		
		// 버튼 클릭 이벤트 처리를 위한 리스너
		ActionListener listener = new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e
			 * 반환값: 없음
			 * 기능 설명: 1) C 버튼 클릭 시 입력 레이블에 기본값(00) 외의 값이 입력되어 있으면 기본값으로 입력 레이블 내용 초기화하고, 입력 버튼 비활성화한다.
			 * 		   2) 숫자키 버튼 클릭 시 입력 레이블 내용의 뒤에서 3번째 자리에 입력된 숫자를 추가하고, 입력 버튼 활성화한다.
			 * 		   3) 입력 버튼 클릭 시 입력된 값이 사용 가능한 값인지 확인하여 예외처리를 진행하고, 사용 가능한 값인 경우 그 값을 저장한다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
				
				if (!flag) { // 이전에 예외가 발생해서 입력 레이블에 오류 메시지가 작성되어 있는 경우
					labelInput.setText("00");
					labelInput.setForeground(CustomColor.KIOSK_BLACK);
					flag = true;	// 정상 흐름으로 진행될 수 있도록 변경 
				}
				
				int length = labelInput.getText().length() - 2;  // 00을 뺀 입력값
				
				if(btn.getText().equals("C") && labelInput.getText().length() > 2) { // 기본값(00) 외의 값이 입력된 상태에서 'C' 버튼 클릭 시
					labelInput.setText("00");		// 입력 레이블 초기화
					btnKey[11].setEnabled(false);	// 입력 버튼 비활성화
				}
				
				if(length < 3) {	// 기본값 "00" 제외 최대 3자리만 추가적으로 입력 가능
					if(!(btn.getText().equals("입력") || btn.getText().equals("C"))) {	// 숫자키
						String text = labelInput.getText().substring(0, length);
						labelInput.setText(text + btn.getText() + "00");				// 입력 레이블 내용의 뒤에서 3번째 자리에 입력된 숫자 추가
						
						btnKey[11].setEnabled(true);									// 입력 버튼 활성화
					}
				}

				if(btn.getText().equals("입력")) {
					int amt = Integer.parseInt(labelInput.getText());	// 입력한 포인트 사용 금액 가져오기
					int min; 											// 포인트 금액과 결제 금액 중 더 작은 값
					
					if(pointAmt < OrderedMenuType.getTOTAL_AMT())
						min = pointAmt;
					else
						min = OrderedMenuType.getTOTAL_AMT();
					
					if(100 <= amt && amt <= min) {		// 입력된 값이 사용한 가능한 값이면
						usePointAmt = amt;			// 입력된 값 저장
						dispose();					// 다이얼로그 창 종료
						
					} else if(amt == 0) {	// 사용할 포인트 금액이 0이라면
						flag = false;	// 정상 흐름이 아닌 것으로 변경
						
						labelInput.setText("포인트 금액은 100p 이상으로 입력해주세요.");
						labelInput.setForeground(CustomColor.KIOSK_RED);
						btnKey[11].setEnabled(false);
						
					} else { // 입력된 값이 사용 불가능한 값이면
						flag = false;	// 정상 흐름이 아닌 것으로 변경
						
						// 입력 레이블에 빨간색 글씨로 안내 메시지 출력 후 입력 버튼 비활성화
						labelInput.setText("보유하신 포인트 혹은 결제 금액보다 작은 값으로 입력해주세요.");
						labelInput.setForeground(CustomColor.KIOSK_RED);
						btnKey[11].setEnabled(false);
					}
				}
			}
		};
		
		// 숫자키, C, 입력 버튼: 디자인 적용, 이벤트 리스너 등록, 패널에 추가
		for(int i = 0; i < btnKey.length; i++) {
			btnKey[i].toWhite(17, 'D');
			btnKey[i].addActionListener(listener);
			panel.add(btnKey[i]);
		}
		
		// 윈도우 이벤트 발생 시 처리를 위한 이벤트 리스너
		this.addWindowListener(new WindowAdapter() {
			/* 메소드명: windowClosing
			 * 파라미터: WindowEvent e (윈도우 이벤트 객체)
			 * 반환값: 없음
			 * 기능 설명: 다이얼로그 창 우측 상단의 X 버튼이 클릭되면 사용할 포인트 금액을 0으로 처리
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				usePointAmt = 0;
			}
		});

		this.setVisible(true);
	}
	
	/* 메소드명: getUsePointAmt
	 * 파라미터: 없음
	 * 반환값: int usePointAmt (사용할 포인트 금액)
	 * 기능 설명: usePointAmt 값을 반환
	 */
	public int getUsePointAmt() {
		return usePointAmt;
	}
}
