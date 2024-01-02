package system.user.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.main.KioskFrame;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class InputBirthInfoDialog extends JDialog {
	public static final int[] LAST_DAY_OF_MONTH = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // 월별 마지막 날짜
	
	private boolean flag = true;	// 예외값 체크 (true: 정상 흐름, false: 예외 발생)
	private String birth;			// 생일 정보
	
	/* 생성자: InputBirthInfoDialog
	 * 파라미터: 없음
	 * 기능 설명: 생일 정보를 입력받기 위한 다이얼로그를 띄운다.
	 */
	public InputBirthInfoDialog() {
		super(KioskFrame.getInstance(), "생일 정보 입력", true);
		
		this.setSize(540, 360);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		CustomLabel labelMsg = new CustomLabel("고객님의 생일을 입력해주세요 (ex. 8월 18일 => 0818)", 20.0f);
		labelMsg.setBounds(20, 10, 480, 40);
		panel.add(labelMsg);
		
		CustomLabel labelMsg1 = new CustomLabel("- 생일 입력을 원하지 않을 경우 0000을 입력해주세요.", JLabel.LEFT, 17.0f);
		labelMsg1.setBounds(20, 50, 480, 40);
		panel.add(labelMsg1);
		
		CustomLabel labelMsg2 = new CustomLabel("- 포인트 적립을 원하지 않을 경우 우측 상단의 X를 클릭해주세요.", JLabel.LEFT, 17.0f);
		labelMsg2.setBounds(20, 70, 480, 40);
		panel.add(labelMsg2);
		
		// 입력 레이블
		CustomLabel labelInput = new CustomLabel("");
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
				CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
				
				if (!flag) { // 이전에 예외가 발생해서 입력 레이블에 오류 메시지가 작성되어 있는 경우
					labelInput.setText("");
					labelInput.setForeground(CustomColor.KIOSK_BLACK);
					flag = true;	// 정상 흐름으로 진행될 수 있도록 변경 
				}
				
				int length = labelInput.getText().length();
				
				if (btn.getText().equals("C") && labelInput.getText().length() > 0) {	// 값이 입력된 상태에서 'C' 버튼 클릭 시
					labelInput.setText("");			// 입력 레이블 초기화
					btnKey[11].setEnabled(false);	// 입력 버튼 비활성화
				}
				
				if (length < 4) {	// 4자리로 입력 제한
					if (!(btn.getText().equals("입력") || btn.getText().equals("C"))) {	// 숫자키
						String text = labelInput.getText();
						labelInput.setText(text + btn.getText());	// 입력 레이블에 입력된 숫자 추가
					}
					
					if(labelInput.getText().length() < 4)	// 입력 레이블에 입력된 숫자가 4자리 미만이면
						btnKey[11].setEnabled(false);		// 입력 버튼 비활성화
					else
						btnKey[11].setEnabled(true);
				}
				
				if(btn.getText().equals("입력")) {
					String birthday = labelInput.getText();
					
					int month = Integer.parseInt(birthday.substring(0, 2)); 	// 월
					int day = Integer.parseInt(birthday.substring(2)); 		// 일
					
					if(birthday.equals("0000")) {
						birth = labelInput.getText();
						dispose();
					}
					
					// 입력된 월(month)의 값이 1~12 범주를 벗어나는 경우
					if (month < 1 || month > 12) {
						flag = false;
						labelInput.setText("형식이 잘못되었습니다.");
						labelInput.setForeground(CustomColor.KIOSK_RED);
						btnKey[11].setEnabled(false);

					// 입력된 일(day)의 값이 일의 범위를 초과하는 경우: 유효하지 않은 날짜
					} else if (day < 1 || day > LAST_DAY_OF_MONTH[month - 1]) {
						flag = false;
						labelInput.setText("형식이 잘못되었습니다.");
						labelInput.setForeground(CustomColor.KIOSK_RED);
						btnKey[11].setEnabled(false);
					} else {
						birth = labelInput.getText();
						dispose();
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
			 * 기능 설명: 다이얼로그 창 우측 상단의 X 버튼이 클릭되면 생일 정보가 저장되어야 할 birth 변수에 "CANCEL" 저장
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				birth = "CANCEL";
			}
		});

		this.setVisible(true);
	}
	
	/* 메소드명: getBirth
	 * 파라미터: 없음
	 * 반환값: String birth (입력된 생일 정보)
	 * 기능 설명: birth 값을 반환
	 */
	public String getBirth() {
		return birth;
	}
}
