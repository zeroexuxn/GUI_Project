package system.user.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.OrderedMenuType;
import system.main.KioskFrame;
import system.user.UserModePanel;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;

public class InputCouponUseDialog extends JDialog {
	private boolean flag;		// 예외값 체크 (true: 예외 발생 X, false: 예외 발생)
	private int useCouponAmt;	// 사용할 쿠폰 금액
	
	/* 생성자: InputCouponUseDialog
	 * 파라미터: 없음
	 * 기능 설명: 쿠폰 사용 여부를 입력받기 위한 다이얼로그를 띄운다.
	 */
	public InputCouponUseDialog(int couponAmt) {
		super(KioskFrame.getInstance(), "쿠폰 사용 여부 입력", true);
		
		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트/쿠폰 ID 입력) :: 다이얼로그에서 쿠폰 사용 여부를 입력받고 있습니다.");
		
		this.setSize(510, 260);
		this.setResizable(false);
		this.setLocationRelativeTo(KioskFrame.getInstance());
		
		JPanel panel = new JPanel();
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(null);
		this.add(panel);
		
		String info = "쿠폰 금액: " + String.format("%,d원", couponAmt) 
						+ " / 결제 금액: " + String.format("%,d원", OrderedMenuType.getTOTAL_AMT());
		CustomLabel labelInfo = new CustomLabel(info, JLabel.LEFT, 20.f);
		labelInfo.setBounds(30, 10, 450, 40);
		panel.add(labelInfo);
		
		CustomLabel labelMsg1 = new CustomLabel("- 쿠폰을 사용하시려면 하단의 사용 버튼을 클릭해주세요.", JLabel.LEFT, 17.0f);
		labelMsg1.setBounds(30, 50, 450, 40);
		panel.add(labelMsg1);
		
		CustomLabel labelMsg2 = new CustomLabel("- 쿠폰 사용을 원하지 않을 경우 우측 상단의 X를 클릭해주세요.", JLabel.LEFT, 17.0f);
		labelMsg2.setBounds(30, 70, 450, 40);
		panel.add(labelMsg2);
		
		CustomButton btnUse = new CustomButton("사용");
		btnUse.toWhite(20, 'D');
		btnUse.setBounds(215, 110, 80, 40);
		btnUse.addActionListener(new ActionListener() {		// 사용 버튼 클릭 처리를 위한 이벤트 리스너
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트 객체)
			 * 반환값: 없음
			 * 기능 설명: 사용 버튼이 클릭되면 쿠폰 금액 혹은 결제 금액(쿠폰 금액이 결제 금액보다 더 큰 경우)을 저장하고 다이얼로그를 종료한다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				useCouponAmt = (couponAmt <= OrderedMenuType.getTOTAL_AMT())? couponAmt: OrderedMenuType.getTOTAL_AMT();
				dispose();
			}
		});
		panel.add(btnUse);
		
		// 쿠폰 금액이 결제 금액보다 더 큰 경우 차액은 반환되지 않는다는 안내문 표시
		if(couponAmt > OrderedMenuType.getTOTAL_AMT()) {
			CustomLabel labelCaution = new CustomLabel("※쿠폰 사용 후 남은 금액은 반환되지 않습니다.※", JLabel.CENTER, 17.0f);
			labelCaution.setForeground(CustomColor.KIOSK_RED);
			labelCaution.setBounds(30, 160, 450, 40);
			panel.add(labelCaution);
		}
		
		// 윈도우 이벤트 발생 시 처리를 위한 이벤트 리스너
		this.addWindowListener(new WindowAdapter() {
			/* 메소드명: windowClosing
			 * 파라미터: WindowEvent e (윈도우 이벤트 객체)
			 * 반환값: 없음
			 * 기능 설명: 다이얼로그 창 우측 상단의 X 버튼이 클릭되면 쿠폰 금액으 0으로 처리
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				useCouponAmt = 0;
			}
		});
		
		this.setVisible(true);
	}
	
	/* 메소드명: getUseCouponAmt
	 * 파라미터: 없음
	 * 반환값: int useCouponAmt (쿠폰 금액 혹은 결제 금액(쿠폰 금액이 결제 금액보다 더 큰 경우))
	 * 기능 설명: useCouponAmt 값을 반환
	 */
	public int getUseCouponAmt() {
		return useCouponAmt;
	}
	
}
