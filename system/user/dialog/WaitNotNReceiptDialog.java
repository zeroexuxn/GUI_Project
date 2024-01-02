package system.user.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import system.data.SalesDetailType;
import system.main.KioskFrame;
import system.user.UserModePanel;
import system.user.custom.CustomColor;

// 2023-08-09: JTextArea에서 영문, 숫자, 공백, 한글의 폭이 일정하지 않아 대기번호, 영수증 출력 시 양식이 일정하지 않는 문제가 있음

public class WaitNotNReceiptDialog extends JDialog {
	// 기본 상점 정보
	private static final String STORE_NAME = "대우커피 상봉역점";					// 상호
	private static final String STORE_LICENSE_NO = "123-45-67890";			// 사업자번호
	private static final String CEO = "홍길동";								// 대표자명
	private static final String TEL = "02-000-0000";						// 전화번호
	private static final String ADDRESS = "서울특별시 중랑구 망우로 291 상봉빌딩 6층";	// 주소
	
	private static final Font font = new Font("Malgun Gothic", Font.PLAIN, 14);
	
	private static int waitNoTextLine = 0;	// 교환권 내용의 라인 수
	private static int receiptTextLine = 0;	// 영수증 내용의 라인 수
	
	/* 생성자: ExchTktNRececDialog
	 * 파라미터: boolean print (영수증 출력 여부) (true: 출력, false: 미출력)
	 * 기능 설명: 교환권, 영수증(출력 여부에 따라) 내용이 포함된 다이얼로그를 모달리스로 띄운다.
	 */
	public WaitNotNReceiptDialog(boolean print) {
		super(KioskFrame.getInstance(), "", false);
		
		if(print) System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(영수증 출력 여부 선택) :: 다이얼로그에서 교환권과 영수증을 출력하고 있습니다.");
		else System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(영수증 출력 여부 선택) :: 다이얼로그에서 교환권을 출력하고 있습니다.");
		
		this.setSize(480, 750);
		this.setResizable(false);
		
		// 부모창 옆에 다이얼로그 창을 띄움
		int x = this.getParent().getX() + this.getParent().getWidth();
        int y = this.getParent().getY();
        this.setLocation(x, y);
		
        // 배경 패널
		JPanel panel = new JPanel();
		int height;	// 패널 높이 계산을 위한 변수

		// 대기번호 출력용 텍스트 영역
		JTextArea waitNoArea = new JTextArea(toWaitNoText());
		Dimension waitNoAreaSize = new Dimension(280, waitNoTextLine * 19); // 대기번호 영역의 크기 지정
		waitNoArea.setPreferredSize(waitNoAreaSize);
		waitNoArea.setBorder(BorderFactory.createLineBorder(CustomColor.KIOSK_BLACK));
		waitNoArea.setFont(font);
		waitNoArea.setEditable(false);
		panel.add(waitNoArea);
		height = 10 + (int)waitNoAreaSize.getHeight() + 20;
		
		// 영수증 출력용 텍스트 영역
		if(print) {
			JTextArea receiptArea = new JTextArea(toReceiptText());
			Dimension receiptAreaSize = new Dimension(420, receiptTextLine * 19); // 대기번호 영역의 크기 지정
			receiptArea.setPreferredSize(receiptAreaSize);
			receiptArea.setBorder(BorderFactory.createLineBorder(CustomColor.KIOSK_BLACK));
			receiptArea.setFont(font);
			receiptArea.setEditable(false);
			panel.add(receiptArea);
			
			height += 10 + (int)receiptAreaSize.getHeight();
		}

		// 패널 크기 및 기타 옵션 설정
		Dimension panelSize = new Dimension(490, height);
		panel.setPreferredSize(panelSize);
		panel.setBackground(CustomColor.KIOSK_GRAY_242);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		this.add(panel, BorderLayout.CENTER);
		
		// 스크롤 페인
		JScrollPane pane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(pane);
		
		this.setVisible(true);
	}
	
	/* 메소드명: toWaitNoText
	 * 파라미터: 없음
	 * 반환값: String text (대기번호 내용)
	 * 기능 설명: 대기번호 영역에 들어갈 내용을 문자열로 생성하여 반환한다.
	 */
	public String toWaitNoText() {
		waitNoTextLine = 0;

		// 대기번호 내용
		String text = "[교환권]\n";
		text += UserModePanel.salesTotal.getType() + "\n";
		text += toTransDtTemplate(UserModePanel.salesTotal.getTransDt()) + "\n";
		text += "===========================\n";
		text += "대기번호: " + UserModePanel.salesTotal.getWaitNo() + "\n";
		text += "===========================\n";
		text += "메뉴명\t\t 수량\n";
		text += "--------------------------------------------\n";
		for(SalesDetailType salesDetail : UserModePanel.salesDetailList) {
			if(!salesDetail.getType().equals("M")) continue;
			
			text += String.format("%-20s", salesDetail.getName());
			text += String.format("        %2d", salesDetail.getQuantity()) + "\n";
		}
		text += "===========================\n\n";
		
		// 대기번호용 텍스트 영역의 높이값을 설정하기 위한 라인수 계산
		for(char c : text.toCharArray()) {
			if(c == '\n') waitNoTextLine++;
		}
		waitNoTextLine++;
		
		return text;
	}
	
	/* 메소드 설명: toReceiptText
	 * 파라미터: 없음
	 * 반환값: String text (영수증 내용)
	 * 기능 설명: 영수증 영역에 들어갈 내용을 문자열로 생성하여 반환한다.
	 */
	public String toReceiptText() {
		receiptTextLine = 0;
		
		// 영수증 내용
		String text = "[영수증]\n";
		text += UserModePanel.salesTotal.getType() + "\n";
		text += toTransDtTemplate(UserModePanel.salesTotal.getTransDt()) + "\n";
		text += "=========================================\n";
		text += "상호: " + STORE_NAME + "\n";
		text += "사업자번호: " + STORE_LICENSE_NO + "\n";
		text += "대표: " + CEO + "\n";
		text += "TEL: " + TEL + "\n";
		text += "주소: " + ADDRESS + "\n";
		text += "\n";
		text += "#" + UserModePanel.salesTotal.getOrderNo() + "\n";
		text += "=========================================\n";
		text += "메뉴명\t\t 단가   수량\t  금액\n";
		text += "--------------------------------------------------------------------\n";
		
		for(SalesDetailType salesDetail : UserModePanel.salesDetailList) {
			text += String.format("%-20s   ", salesDetail.getName());
			text += String.format("%,8d    %2d   %,8d", salesDetail.getPrice(), salesDetail.getQuantity(), salesDetail.getTotalPrice()) + "\n";
		}
		
		text += "=========================================\n";
		text += "합계금액\t\t\t" + String.format("%,8d", UserModePanel.salesTotal.getTotalAmt()) + "\n";
		text += "공급가액\t\t\t" + String.format("%,8d", UserModePanel.salesTotal.getSupplyAmt()) + "\n";
		text += "부가가치세\t\t\t" + String.format("%,8d", UserModePanel.salesTotal.getVat()) + "\n";
		text += "=========================================\n";
		text += "결제방법\t\t\t" + String.format("%s", UserModePanel.salesTotal.getPayMethod()) + "\n";
		
		if(UserModePanel.salesTotal.getTotalAmt() != 0) {
			text += "카드사명\t\t\t" + String.format("%s", UserModePanel.salesTotal.getCardName()) + "\n";
			text += "카드번호\t\t\t" + String.format("%s", UserModePanel.salesTotal.getCardNo()) + "\n";
			text += "할부개월\t\t\t" + String.format("%s", UserModePanel.salesTotal.getCardQuota()) + "\n";
			text += "승인번호\t\t\t" + String.format("%s", UserModePanel.salesTotal.getAuthCode()) + "\n";
		}
		text += "=========================================\n";
		
		if(!UserModePanel.salesTotal.getMemberId().equals(" ")) {
			text += "[포인트 적립]\n";
			text += "적립포인트\t\t\t" + String.format("%,8d", UserModePanel.salesTotal.getRewardPts()) + "\n";
			text += "누적포인트\t\t\t" + String.format("%,8d", UserModePanel.salesTotal.getTotalPts()) + "\n";
			text += "=========================================\n";
		}
		text += "\n\n";
		
		// 영수증용 텍스트 영역의 높이값을 설정하기 위한 라인수 계산
		for(char c : text.toCharArray()) {
			if(c == '\n') receiptTextLine++;
		}
		receiptTextLine++;
		
		return text;
	}
	
	/* 메소드명: toTransDtTemplate
	 * 파라미터: String transDt
	 * 		  transDt: 거래 일시
	 * 반환값: String
	 * 기능 설명: YYYYMMDDHHMMss 형식의 문자열을 받아 YYYY-MM-DD HH:MM:ss 형식으로 변환하여 반환
	 */
	public String toTransDtTemplate(String transDt) {
		return transDt.substring(0, 4) + "-" + transDt.substring(4, 6) + "-" + transDt.substring(6, 8)
				+ " " + transDt.substring(8, 10) + ":" + transDt.substring(10, 12) + ":" + transDt.substring(12, 14);
	}
}
