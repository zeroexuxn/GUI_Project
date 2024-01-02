package system.user.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.OrderedMenuType;
import system.main.KioskFrame;
import system.main.Main;
import system.user.UserModePanel;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class InputGoMainDialog extends JDialog {
	
	/* 생성자: InputGoMainDialog
	 * 파라미터: 없음
	 * 기능 설명: 주문 목록에 메뉴가 있을 때 메인 화면을 돌아가기 위한 버튼을 클릭한 경우 주문 목록 초기화 안내문이 작성된 다이얼로그를 띄운다.
	 */
	public InputGoMainDialog() {
		super(KioskFrame.getInstance(), "주문 목록 초기화 안내", true);
		
		this.setSize(515, 210);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		CustomLabel labelMsg = new CustomLabel("메인 화면으로 돌아가면 모든 주문 목록이 초기화됩니다.", JLabel.CENTER, 20.0f);
		labelMsg.setBounds(0, 10, 500, 40);
		panel.add(labelMsg);
		
		CustomLabel labelMsg1 = new CustomLabel("계속 진행하시려면 하단의 진행 버튼을 클릭해주세요.", JLabel.CENTER, 17.0f);
		labelMsg1.setBounds(0, 70, 500, 30);
		panel.add(labelMsg1);
		
		CustomLabel labelMsg2 = new CustomLabel("주문 목록 초기화를 원하지 않을 경우 우측 상단의 X를 클릭해주세요.", JLabel.CENTER, 17.0f);
		labelMsg2.setBounds(0, 90, 500, 30);
		panel.add(labelMsg2);
		
		CustomButton btnContinue = new CustomButton("진행");
		btnContinue.toWhite(20, 'D');
		btnContinue.setBounds(215, 120, 85, 40);
		btnContinue.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트 객체)
			 * 반환값: 없음
			 * 기능 설명: 진행 버튼이 클릭되면 모든 주문 정보를 모두 초기화한 후 메인 화면을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문) :: 메인 화면으로 돌아갑니다.");
				
				dispose();
				
				UserModePanel.menuPanel.setVisible(false);
				UserModePanel.selectHereOrTakeoutPanel.setVisible(true);
				KioskFrame.userModePanel.setVisible(false);
				KioskFrame.userModePanel.initialize();
				OrderedMenuType.reset();
				KioskFrame.mainPanel.setVisible(true);
				Main.currentMode = 'M';
			}
		});
		panel.add(btnContinue);
		
		this.setVisible(true);
	}
}
