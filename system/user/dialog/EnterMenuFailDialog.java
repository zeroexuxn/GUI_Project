package system.user.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.main.KioskFrame;
import system.main.Main;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class EnterMenuFailDialog extends JDialog {
	
	/* 생성자: EnterMenuFailDialog
	 * 파라미터: 없음
	 * 기능 설명: 활성화 상태의 메뉴가 없어 주문 진행이 불가능한 경우 메인 화면으로 이동하거나 관리자에게 문의할 수 있도록 유도하는 다이얼로그를 띄운다.
	 */
	public EnterMenuFailDialog() {
		super(KioskFrame.getInstance(), "요청 실패", true);
		
		this.setSize(480, 320);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		CustomLabel labelImg = new CustomLabel(new ImageIcon("src/img/request_fail.gif"));
		labelImg.setBounds(165, 10, 150, 110);
		panel.add(labelImg);
		
		CustomLabel labelMsg = new CustomLabel("현재 시스템이 원활하지 않습니다.", JLabel.CENTER, 20.0f);
		labelMsg.setBounds(0, 125, 480, 40);
		panel.add(labelMsg);
		
		CustomLabel labelMsg1 = new CustomLabel("관리자에게 문의해주세요.", JLabel.CENTER, 17.0f);
		labelMsg1.setBounds(0, 165, 480, 20);
		panel.add(labelMsg1);
		
		CustomLabel labelMsg2 = new CustomLabel("하단의 확인 버튼을 클릭하시면 메인 화면으로 돌아갑니다.", JLabel.CENTER, 17.0f);
		labelMsg2.setBounds(0, 200, 480, 30);
		panel.add(labelMsg2);
		
		CustomButton btnOk = new CustomButton("확인");
		btnOk.toWhite(20, 'D');
		btnOk.setBounds(200, 230, 80, 40);
		btnOk.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트 객체)
			 * 반환값: 없음
			 * 기능 설명: 확인 버튼이 클릭되면 메인 화면을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
				KioskFrame.userModePanel.setVisible(false);
				KioskFrame.userModePanel.initialize();
				KioskFrame.mainPanel.setVisible(true);
				Main.currentMode = 'M';
			}
		});
		panel.add(btnOk);
		
		this.setVisible(true);
	}
}
