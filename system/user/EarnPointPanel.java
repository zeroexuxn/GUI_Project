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

import system.data.MemberType;
import system.data.OrderedMenuType;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;
import system.user.dialog.InputBirthInfoDialog;

public class EarnPointPanel extends JPanel {
	private String birth;				// 생일 정보
	
	private CustomLabel labelInput;		// 입력 레이블
	
	private CustomButton[] btnKey;		// 키패드: 숫자키, 지우기(<), 입력 버튼
	private CustomButton btnNotEarn;		// 미적립 버튼
	
	// 생성자
	public EarnPointPanel() {
		this.setLayout(null);
		
		// ===== 상단 안내 메시지
		CustomLabel labelTitle = new CustomLabel("포인트 적립을 위해 휴대전화 번호를 입력해주세요.", JLabel.CENTER);
		labelTitle.toYellow(25, 'D');
		labelTitle.setBounds(0, 60, 720, 80);
		this.add(labelTitle);
		
		// ===== 입력, 입력 예시 레이블
		labelInput = new CustomLabel("010", JLabel.CENTER);
		labelInput.toYellow(40, 'T');
		labelInput.setBounds(140, 220, 440, 70);
		this.add(labelInput);
		
		CustomLabel labelInputEx = new CustomLabel("예시) 01012345678", JLabel.CENTER, 20.0f);
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
			btnKey[i].addActionListener(new searchMemberInfoListener());
			this.add(btnKey[i]);
		}
		
		// ===== 미적립 버튼
		btnNotEarn = new CustomButton("미적립");
		btnNotEarn.toGreen(25);
		btnNotEarn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// ===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트 적립) :: 데이터 처리 단계로 넘어갑니다.");
				
				setVisible(false);
				UserModePanel.updateDataProcess.updateData("");
				//=======================================
				
			}
		});
		btnNotEarn.setBounds(520, 900, 160, 60);
		this.add(btnNotEarn);
		
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
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 
	 */
	public void initialize() {
		// ===== 입력, 입력 예시 레이블
		labelInput.setText("010");
		
		// ===== 키패드
		btnKey[11].setEnabled(false);
		
	}
	
	/* 메소드명: searchMemberInfo
	 * 파라미터: String searchId (생일 정보를 조회할 회원 ID)
	 * 반환값: MemberType member (회원 조회에 성공한 경우 해당 회원의 정보(회원 ID, 보유 포인트, 생일 정보)가 담긴 MemberType 객체)
	 * 기능 설명: 전달받은 회원 ID를 이용해 회원 목록에서 회원을 조회한 후 해당 회원의 정보가 담긴 MemberType 객체를 반환한다.
	 */
	private MemberType searchMemberInfo(String searchId) {
		MemberType member = new MemberType(searchId, 0, "0000");
		BufferedReader in;	// 입력 스트림
		
		try {
			System.out.println("회원 목록을 읽기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 회원 파일이 없는 경우
			System.out.println("회원 파일이 존재하지 않습니다.");
			return null;
		}
		
		String s;						// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;					// 구분자로 분리한 문자열이 저장될 임시 배열

		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				// 회원 조회에 성공하면 해당 회원의 정보를 저장
				if(tmp[MemberType.INDEX_MEMBER_ID].equals(searchId)) {
					member.setPoint(Integer.parseInt(tmp[MemberType.INDEX_POINT]));
					member.setbirth(tmp[MemberType.INDEX_BIRTH]);
					break;
				}
			}
			
			in.close();	// 스트림 닫기
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return null;
		}

		return member;
	}

	// searchMemberInfo - btnKey 버튼 클릭 이벤트 처리
	public class searchMemberInfoListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			String searchId;								// 조회할 회원 아이디
			int length = labelInput.getText().length();		// 입력 레이블에 입력된 텍스트의 길이
			
			if(btn.getText().equals("입력")) {
				searchId = labelInput.getText();

				MemberType member = searchMemberInfo(searchId);
				birth = member.getbirth();
				
				if(birth.equals("0000")) {							// 회원인데 생일 정보가 없거나, 비회원인 경우
					birth = new InputBirthInfoDialog().getBirth();	// 다이얼로그에서 입력된 생일 정보를 받아옴
				}

				if(!birth.equals("CANCEL")) {	// 포인트 적립을 취소한 경우를 제외하고
					
					//===============[여기 채우기]==============
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(포인트 적립) :: 포인트 적립이 완료되어 데이터 처리 단계로 넘어갑니다.");
					
					UserModePanel.salesTotal.setMemberId(searchId);
					
					int rewardPoints = (int)(OrderedMenuType.getTOTAL_AMT() * 0.01);

					UserModePanel.salesTotal.setRewardPts(rewardPoints);						// 적립할 포인트
					UserModePanel.salesTotal.setTotalPts(member.getPoint() + rewardPoints);		// 누적 포인트
					
					setVisible(false);
					UserModePanel.updateDataProcess.updateData(birth);
					//=======================================
					
				}
				
			}
			
			// 지우기 버튼 (<)이 클릭되었고, 기본값 외의 데이터가 입력되어 있는 경우
			if(btn.getText().equals("<") && labelInput.getText().length() > 3) {
				// 마지막 문자를 지운 값으로 입력 레이블의 내용 초기화
				String str = labelInput.getText();
				labelInput.setText(str.substring(0,str.length()-1));
				
				btnKey[11].setEnabled(false);	// 입력 버튼 비활성화	 (지우기 버튼이 클릭되면 입력 레이블의 내용은 항상 limitMax 이므로)
			}
			
			if(length < 11) {		// 입력해야 할 값이 남아 있는 상태
				if(!(btn.getText().equals("입력") || btn.getText().equals("<"))) {			// 클릭된 버튼이 숫자키인 경우
					labelInput.setText(labelInput.getText() + btn.getText());				// 입력 레이블의 내용 끝에 숫자 추가
					
					// 입력 레이블의 길이가 limitMax 미만이면 입력 버튼 비활성화, 아니면 입력 버튼 활성화
					if(labelInput.getText().length() < 11) btnKey[11].setEnabled(false);
					else btnKey[11].setEnabled(true);
				}
			}
		}
	}
	
	
	
	
}
