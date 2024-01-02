package system.user.dialog;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.main.KioskFrame;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class MenuAddFailDialog extends JDialog {
	
	/* 생성자: MenuAddFailDialog
	 * 파라미터: 없음
	 * 기능 설명: 총 주문 수량이 50인 상태에서 메뉴를 추가하려고 할 때 안내 문구가 적힌 다이얼로그를 띄운다.
	 */
	public MenuAddFailDialog() {
		super(KioskFrame.getInstance(), "최대 주문 수량 초과", true);
		
		this.setSize(460, 280);
		this.setResizable(false);
		this.setLocationRelativeTo(this.getParent());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		CustomLabel labelImg = new CustomLabel(new ImageIcon("src/img/request_fail.gif"));
		labelImg.setBounds(155, 10, 150, 110);
		panel.add(labelImg);
		
		String msg = "<html><div style=\"text-align:center\">주문 가능한 수량이 초과되었습니다.<br>"
					+ "(최대 주문 가능 수량: 50)<br><br>"
					+ "메뉴를 추가하시려면 주문 목록에서 메뉴를 삭제하시거나<br>"
					+ "수량을 감소시킨 후 다시 진행하시기 바랍니다.</div></html>";
		
		CustomLabel labelMsg = new CustomLabel(msg, JLabel.CENTER, 15.0f);
		labelMsg.setBounds(0, 125, 460, 110);
		panel.add(labelMsg);

		this.setVisible(true);
	}
}
