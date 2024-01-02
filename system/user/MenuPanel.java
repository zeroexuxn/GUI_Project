package system.user;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.data.CategoryType;
import system.data.MenuType;
import system.data.OrderedMenuType;
import system.main.KioskFrame;
import system.main.Main;
import system.user.custom.CustomButton;
import system.user.custom.CustomColor;
import system.user.custom.CustomLabel;
import system.user.dialog.InputGoMainDialog;
import system.user.dialog.MenuAddFailDialog;

// 2023-08-09: 

public class MenuPanel extends JPanel {
	// 접근 편의성을 위해 UserModePanel에서 생성한 맵, 리스트 객체를 받아옴
	private HashMap<String, String> categoryMap = UserModePanel.categoryMap;
	private ArrayList<Integer> firstIdxByCatList = UserModePanel.firstIdxByCatList;
	private ArrayList<MenuType> menuList = UserModePanel.menuList;
	private ArrayList<OrderedMenuType> cart = UserModePanel.cart;
	
	private CustomButton btnMain;							// 메인 화면으로 돌아가기 버튼
	private String iconMainSrc = "src/img/icon_Main.png";	// 메인 화면으로 돌아가기 버튼 이미지 경로
	
	private int curCatIdx = 0;		// 현재 가리키고 있는 카테고리의 인덱스 (0 ~ 실제 메뉴가 존재하는 카테고리의 개수)
	private int curCatPage = 0;		// 현재 펼쳐진 카테고리 페이지 (하나의 카테고리 페이지에는 최소 1개, 최대 4개의 메뉴를 보여줌)
	private int curMenuPage = 0;	// 현재 펼쳐진 메뉴 페이지 (하나의 메뉴 페이지에는 최소 1개, 최대 12개의 메뉴를 보여줌)
	
	private CustomButton btnPreCatPage, btnNextCatPage;		// 카테고리 페이지 이동(<, >) 버튼
	private CustomButton[] btnCategories;					// 카테고리 버튼
	
	private CustomButton btnPreMenuPage, btnNextMenuPage;	// 메뉴 페이지 이동(<, >) 버튼
	private CustomButton[] btnMenus;							// 메뉴 버튼
	private String iconMenuDir = "src/img/icon_menu/";		// 메뉴 이미지 디렉터리
	
	private int viewIndex = 0;	// 화면에 보여지는 주문 목록 내 메뉴 중 가장 처음에 있는 메뉴의 인덱스
	
	private CustomButton[] btnDeleteMenu, btnDecreaseQty, btnIncreaseQty;	// 주문 목록 내 메뉴 삭제, 수량 변경(-, +) 버튼
	private CustomLabel[] labelMenuName, labelQuantity, labelTotalPrice;		// 주문 목록 내 메뉴명, 주문 수량, 금액 레이블
	private CustomButton btnScrollUp, btnScrollDown;							// 주문 목록 스크롤(▲, ▼) 버튼
	
	private CustomButton btnCartClear, btnPay;	// 전체 삭제, 결제 버튼
	private CustomLabel labelTotalQuantity;		// 총 주문 수량 레이블
	
	// 생성자
	public MenuPanel() {
		this.setLayout(null);
		
		// ===== 메인 화면으로 돌아가기 버튼
		btnMain = new CustomButton(new ImageIcon(iconMainSrc));
		btnMain.toBlack(0);
		btnMain.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
			 * 반환값: 없음
			 * 기능 설명: 주문 정보를 모두 초기화한 후 메인 화면을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				//===============[여기 채우기]==============
				if(cart.size() == 0) {
					System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문) :: 메인 화면으로 돌아갑니다.");
					
					setVisible(false);
					UserModePanel.selectHereOrTakeoutPanel.setVisible(true);
					KioskFrame.userModePanel.setVisible(false);
					KioskFrame.userModePanel.initialize();
					OrderedMenuType.reset();
					KioskFrame.mainPanel.setVisible(true);
					Main.currentMode = 'M';
					
				} else {
					new InputGoMainDialog();
				}
				
				//=======================================
			}
		});
		btnMain.setBounds(0, 0, 40, 40);
		this.add(btnMain);
		
		// ===== 카테고리 페이지 이동 버튼 (<, >)
		btnPreCatPage = new CustomButton("<");
		btnPreCatPage.toGreen(25);
		btnPreCatPage.addActionListener(new categoryLoadListener());
		btnPreCatPage.setBounds(60, 85, 20, 30);
		this.add(btnPreCatPage);
		
		btnNextCatPage = new CustomButton(">");
		btnNextCatPage.toGreen(25);
		btnNextCatPage.addActionListener(new categoryLoadListener());
		btnNextCatPage.setBounds(640, 85, 20, 30);
		this.add(btnNextCatPage);
		
		// ===== 카테고리 버튼
		btnCategories = new CustomButton[4];
		for(int i=0; i<4; i++) {
			btnCategories[i] = new CustomButton();
			btnCategories[i].addActionListener(new menuLoadListener());
			int x = 90 + 140 * i;	// X 좌표값
			btnCategories[i].setBounds(x, 80, 120, 40);
			this.add(btnCategories[i]);
		}
		
		// ===== 메뉴 페이지 이동 버튼 (<, >)
		btnPreMenuPage = new CustomButton("<");
		btnPreMenuPage.toBlack(25);
		btnPreMenuPage.addActionListener(new menuLoadListener());
		btnPreMenuPage.setBounds(20, 400, 20, 40);
		this.add(btnPreMenuPage);
		
		btnNextMenuPage = new CustomButton(">");
		btnNextMenuPage.toBlack(25);
		btnNextMenuPage.addActionListener(new menuLoadListener());
		btnNextMenuPage.setBounds(680, 400, 20, 40);
		this.add(btnNextMenuPage);
		
		// ===== 메뉴 버튼
		btnMenus = new CustomButton[12];
		for(int i=0; i<12; i++) {
			btnMenus[i] = new CustomButton();
			btnMenus[i].toWhite(14, 'M');
			btnMenus[i].addActionListener(new updateCartListener());
			
			int x = 70 + 150 * (i % 4);
			int y = 140 + 200 * (i / 4);
			btnMenus[i].setBounds(x, y, 130, 160);
			this.add(btnMenus[i]);
		}
		
		// ===== 주문 목록
		JPanel panelCartView = new JPanel();
		panelCartView.setBounds(20, 740, 480, 240);
		panelCartView.setBackground(Color.WHITE);
		panelCartView.setLayout(null);
		this.add(panelCartView);
		
		btnDeleteMenu = new CustomButton[5];
		labelMenuName = new CustomLabel[5];
		btnDecreaseQty = new CustomButton[5];
		labelQuantity = new CustomLabel[5];
		btnIncreaseQty = new CustomButton[5];
		labelTotalPrice = new CustomLabel[5];
		
		for(int i=0; i<5; i++) {
			btnDeleteMenu[i] = new CustomButton("X");
			btnDeleteMenu[i].toGreen(20);
			btnDeleteMenu[i].addActionListener(new updateCartListener());
			btnDeleteMenu[i].setBounds(20, 20 + 40 * i, 30, 30);
			panelCartView.add(btnDeleteMenu[i]);
			
			labelMenuName[i] = new CustomLabel(15.0f);
			labelMenuName[i].setBounds(55, 20 + 40 * i, 220, 30);
			panelCartView.add(labelMenuName[i]);
			
			btnDecreaseQty[i] = new CustomButton("-");
			btnDecreaseQty[i].toGreen(20);
			btnDecreaseQty[i].addActionListener(new updateCartListener());
			btnDecreaseQty[i].setBounds(285, 20 + 40 * i, 30, 30);
			panelCartView.add(btnDecreaseQty[i]);
			
			labelQuantity[i] = new CustomLabel("", JLabel.CENTER, 15.0f);
			labelQuantity[i].setBounds(315, 20 + 40 * i, 30, 30);
			panelCartView.add(labelQuantity[i]);
			
			btnIncreaseQty[i] = new CustomButton("+");
			btnIncreaseQty[i].toGreen(20);
			btnIncreaseQty[i].addActionListener(new updateCartListener());
			btnIncreaseQty[i].setBounds(345, 20 + 40 * i, 30, 30);
			panelCartView.add(btnIncreaseQty[i]);
			
			labelTotalPrice[i] = new CustomLabel("", JLabel.RIGHT, 15.0f);
			labelTotalPrice[i].setBounds(380, 20 + 40 * i, 90, 30);
			panelCartView.add(labelTotalPrice[i]);
		}
		
		// ===== 주문 목록 스크롤
		JPanel panelScroll = new JPanel();
		panelScroll.setBackground(CustomColor.KIOSK_GRAY_217);
		panelScroll.setLayout(null);
		panelScroll.setBounds(500, 740, 20, 240);
		this.add(panelScroll);
		
		btnScrollUp = new CustomButton("▲");
		btnScrollUp.toScrollDesign();
		btnScrollUp.addActionListener(new updateCartListener());
		btnScrollUp.setBounds(0, 0, 20, 20);
		panelScroll.add(btnScrollUp);
		
		btnScrollDown = new CustomButton("▼");
		btnScrollDown.toScrollDesign();
		btnScrollDown.addActionListener(new updateCartListener());
		btnScrollDown.setBounds(0, 220, 20, 20);
		panelScroll.add(btnScrollDown);
		
		// ===== 이미지 라벨
		CustomLabel labelImg = new CustomLabel(new ImageIcon("src/img/ordering.gif"));
		labelImg.setBounds(540, 740, 80, 100);
		this.add(labelImg);
				
		// ===== 전체 삭제
		btnCartClear = new CustomButton("<html><div style=\"text-align:center\">전체<br>삭제</div></html>");
		btnCartClear.toGray217(20);
		btnCartClear.addActionListener(new updateCartListener());
		btnCartClear.setBounds(620, 740, 80, 100);
		this.add(btnCartClear);
		
		// ===== 총 주문 수량
		labelTotalQuantity = new CustomLabel("주문 수량: " + OrderedMenuType.getTOTAL_QUANTITY(), JLabel.CENTER);
		labelTotalQuantity.toWhite(20);
		labelTotalQuantity.setBounds(540, 850, 160, 60);
		this.add(labelTotalQuantity);
		
		// ===== 결제 버튼
		btnPay = new CustomButton("<html><div style=\"text-align:center\">" + String.format("%,d", OrderedMenuType.getTOTAL_AMT()) + "원<br>결제하기</div></html>");
		btnPay.toGray217(20);
		btnPay.addActionListener(new ActionListener() {
			/* 메소드명: actionPerformed
			 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
			 * 반환값: 없음
			 * 기능 설명: 결제 버튼이 클릭되면 포인트/쿠폰 사용 여부를 입력받는 패널을 띄운다.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				//===============[여기 채우기]==============
				System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(주문) :: 포인트/쿠폰 여부 선택 단계로 진입합니다.");
				
				setVisible(false);
				UserModePanel.selectPointOrCouponPanel.setVisible(true);
				//=======================================
			}
		});
		btnPay.setBounds(540, 920, 160, 60);
		this.add(btnPay);
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
	
	/* 메소드명: isAnyMenuExists
	 * 파라미터: 없음
	 * 반환값: boolean (true: 출력할 메뉴 있음, false: 출력할 메뉴 없음)
	 * 기능 설명: 사용자가 보게 될 화면에 출력될 수 있는 메뉴가 있는지 없는지 검사한다.
	 */
	public boolean isAnyMenuExists() {
		loadCategoryList();		// 카테고리 목록 불러오기
		loadMenuList();			// 메뉴 목록 불러오기
		
		// 활성화 상태의 메뉴가 없는 경우 false 반환
		if(categoryMap.size() == 0 || menuList.size() == 0) {
			return false;
		}
		
		return true;
	}
	
	/* 메소드명: initialize
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 새로운 주문 프로세스 진행을 위해 주문 처리에 필요한 정보와 컴포넌트의 내용 및 설정을 초기화한다.
	 */
	public void initialize() {
		curCatIdx = 0;
		curCatPage = 0;
		curMenuPage = 0;
		viewIndex = 0;
		
		// ===== 카테고리 페이지 이동 버튼 (<, >)
		btnPreCatPage.setVisible(false);										// 새로운 주문 시에는 첫번째 카테고리를 가리키므로 < 버튼 미표시

		if(firstIdxByCatList.size() <= 4) btnNextCatPage.setVisible(false);		// 출력될 카테고리 개수가 4개 이하면 > 버튼 미표시
		else btnNextCatPage.setVisible(true);

		// ===== 카테고리 버튼
		for(int i=0; i<4; i++) {
			// 카테고리별 첫번째 메뉴의 인덱스를 이용하여 카테고리명으로 버튼 내용 설정
			int firstIdxByCat = firstIdxByCatList.get(i);
			String categoryId = menuList.get(firstIdxByCat).getCategoryId();
			String categoryName = categoryMap.get(categoryId);
			btnCategories[i].setText(categoryName);
			
			if(i == curCatIdx) btnCategories[i].toGray242(20);
			else btnCategories[i].toGreen(20);
		}
		
		// ===== 카테고리 버튼, 메뉴 페이지 이동 버튼 (<, >), 메뉴 버튼
		loadMenuOnBtn();
		
		// ===== 주문 목록
		for(int i=0; i<5; i++) {
			btnDeleteMenu[i].setVisible(false);
			labelMenuName[i].setVisible(false);
			btnDecreaseQty[i].setVisible(false);
			labelQuantity[i].setVisible(false);
			btnIncreaseQty[i].setVisible(false);
			labelTotalPrice[i].setVisible(false);
		}
		
		// ===== 주문 목록 스크롤
		btnScrollUp.setEnabled(false);
		btnScrollDown.setEnabled(false);
		
		// ===== 전체 삭제
		btnCartClear.toGray217(20);
		btnCartClear.setEnabled(false);

		// ===== 총 주문 수량
		labelTotalQuantity.setText("주문 수량: " + OrderedMenuType.getTOTAL_QUANTITY());

		// ===== 결제 버튼
		btnPay.setText("<html><div style=\"text-align:center\">" + String.format("%,d", OrderedMenuType.getTOTAL_AMT()) + "원<br>결제하기</div></html>");
		btnPay.toGray217(20);
		btnPay.setEnabled(false);
	}

	/* 메소드명: loadCategoryList
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: category.txt 파일에서 데이터를 읽어와 파라미터로 받은 맵에 {카테고리 ID: 카테고리명}으로 매핑하여 저장한다.
	 */
	private void loadCategoryList() {
		BufferedReader in;	// 입력 스트림
		try {
			System.out.println("카테고리 목록을 읽기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(CategoryType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			System.out.println("카테고리 파일이 존재하지 않습니다.");
			return;
		}
		
		String s;
		String[] tmp;
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				// 활성화 상태인 카테고리만 맵에 저장
				if(tmp[CategoryType.INDEX_ENABLE].equals("Y")) {
					String categoryId = tmp[CategoryType.INDEX_CATEGORY_ID];
					String name = tmp[CategoryType.INDEX_NAME];
					categoryMap.put(categoryId, name);
				}
			}
			
			in.close();		// 스트림 닫기
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
		}
	}

	/* 메소드명: loadMenuList
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: menu.txt 파일에서 데이터를 읽어와 MenuType의 객체를 생성하여 menuList에 저장하고, 각 카테고리의 첫번재 메뉴의 실제 인덱스는 firstIdxByCatList에 저장한다.
	 */
	private void loadMenuList() {
		BufferedReader in;	// 입력 스트림
		try {
			System.out.println("메뉴 목록을 읽기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MenuType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {		// 메뉴 파일이 없는 경우
			System.out.println("메뉴 파일이 존재하지 않습니다.");
			return;
		}
		
		String s;					// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;				// 구분자로 분리한 문자열이 저장될 임시 배열
		String preCategoryId = "";	// 이전 카테고리 ID
		int index = 0;				// 새로운 카테고리 ID가 등장할 때의 인덱스값
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리

				String categoryId = tmp[MenuType.INDEX_CATEGORY_ID];	// 카테고리 ID
				String menuNo = tmp[MenuType.INDEX_MENU_NO];
				String name = tmp[MenuType.INDEX_NAME];
				int price = Integer.parseInt(tmp[MenuType.INDEX_PRICE]);
				int stock = Integer.parseInt(tmp[MenuType.INDEX_STOCK]);
				String enabled = tmp[MenuType.INDEX_ENABLE];
				
				// 활성화 카테고리에 속한 메뉴 중에서 활성화 상태인 메뉴만 메뉴 리스트에 추가
				if(categoryMap.get(categoryId) != null && enabled.equals("Y")) {
					MenuType menu = new MenuType(categoryId, menuNo, name, price, stock, enabled);
					menuList.add(menu);
					
					if(!categoryId.equals(preCategoryId)) { // 읽어온 카테고리 ID가 이전에 등장한 적이 없는 카테고리 ID라면
						firstIdxByCatList.add(index);		// 현재 인덱스 값을 카테고리별 첫번째 메뉴의 인덱스 목록에 추가
						preCategoryId = categoryId;			// 카테고리 ID를 저장
					}
					
					index++;
				}	
			}
			
			in.close();		// 스트림 닫기
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
		}
	}

	// categoryLoadListener - btnPreCatPage, btnNextCatPage 버튼 클릭 이벤트 처리
	public class categoryLoadListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 카테고리 페이지 이동 버튼이 클릭될 때마다 이동된 카테고리 페이지의 카테고리를 보여준다.
		 * 		   (카테고리 페이지를 이동할 경우, 가장 마지막에 선택된 카테고리에 포커스가 유지된다.)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			// 현재 카테고리 페이지에 선택한 카테고리가 있는 경우 이전/다음 페이지의 카테고리 표시를 위해 미선택 상태로 표시
			if( ((curCatPage * 4) <= curCatIdx) && ((curCatPage * 4 + 3) >= curCatIdx))
				btnCategories[curCatIdx % 4].toGreen(20);
			
			if(btn == btnPreCatPage) curCatPage--;	// < 버튼 클릭 시, 현재 카테고리 페이지는 -1
			else curCatPage++;						// > 버튼 클릭 시, 현재 카테고리 페이지는 +1
			
			// 넘어간 페이지에 선택한 카테고리가 있는 경우 선택 상태로 표시
			if( ((curCatPage * 4) <= curCatIdx) && ((curCatPage * 4 + 3) >= curCatIdx))
				btnCategories[curCatIdx % 4].toGray242(20);
			
			for(int i=0; i<4; i++) {
				// 내용이 세팅되어야 하는 카테고리가 남아있는 동안 카테고리명으로 카테고리 버튼의 텍스트를 채우고, 화면에 보여줌
				if((curCatPage * 4 + i) < firstIdxByCatList.size()) {
					int firstIdxByCat = firstIdxByCatList.get(curCatPage * 4 + i);
					String categoryId = menuList.get(firstIdxByCat).getCategoryId();
					String categoryName = categoryMap.get(categoryId);
					btnCategories[i].setText(categoryName);
					btnCategories[i].setVisible(true);

				} else { // 내용이 세팅되어야 하는 카테고리가 없다면 화면에 보여주지 않음
					btnCategories[i].setVisible(false);
				}
			}
			
			// 현재 카테고리 페이지가 0번째 페이지라면 < 버튼을 보여주지 않고, 0번째 페이지가 아니면 보여줌
			if(curCatPage == 0) btnPreCatPage.setVisible(false);	
			else btnPreCatPage.setVisible(true);
			
			// 현재 카테고리 페이지에 보이는 카테고리 다음의 카테고리가 없다면 > 버튼을 보여주지 않고, 그렇지 않으면 보여줌
			if( ((curCatPage + 1) * 4) >= firstIdxByCatList.size()) btnNextCatPage.setVisible(false);
			else btnNextCatPage.setVisible(true);
		}
	}

	// menuLoadListener - btnPreMenuPage, btnNextMenuPabe, btnCategories 버튼 클릭 이벤트 처리
	public class menuLoadListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 1) 카테고리 버튼이 클릭된 경우 해당 카테고리의 메뉴 목록을 보여준다.
		 * 		   2) 메뉴 페이지 이동 버튼이 클릭된 경우 해당되는 이전/다음 페이지의 메뉴 목록을 보여준다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체
			
			if(btn == btnPreMenuPage) curMenuPage--;		// 메뉴 페이지 이동 버튼(<) 클릭 시, 현재 메뉴 페이지 -1
			else if(btn == btnNextMenuPage) curMenuPage++;	// 메뉴 페이지 이동 버튼(>) 클릭 시, 현재 메뉴 페이지 +1
			else {	// 카테고리 버튼이 눌린 경우
				// 현재 메뉴 페이지는 0으로 초기화하고, 선택되어 있던 카테고리 버튼은 미선택 상태로 표시
				curMenuPage = 0;
				btnCategories[curCatIdx % 4].toGreen(20);
				
				for(int i=0; i<4; i++) {	
					if(btn == btnCategories[i]) {
						// 클릭된 카테고리 버튼의 실제 카테고리 인덱스 값을 계산하고, 클릭된 버튼은 선택 상태로 표시
						curCatIdx = curCatPage * 4 + i;				// 실제 카테고리 인덱스 = 현재 보고 있는 카테고리 페이지 * 4 + 카테고리 버튼 순서
						btnCategories[i].toGray242(20);
						break;
					}
				}
			}
			
			loadMenuOnBtn(); // 메뉴 목록을 불러와 세팅
		}
	}
	
	/* 메소드명: loadMenuOnBtn
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 현재 선택되어 있는 카테고리와 보고 있는 메뉴 페이지에 맞는 메뉴 목록을 불러온다.
	 */
	public void loadMenuOnBtn() {
		// 현재 선택된 카테고리, 보고 있는 메뉴 페이지 정보를 이용하여 표시되어야할 메뉴의 실제 인덱스 범위와 개수를 계산
		int startIndex = curMenuPage * 12 + firstIdxByCatList.get(curCatIdx);	
		int endIndex = ((curCatIdx + 1) == firstIdxByCatList.size())? menuList.size(): firstIdxByCatList.get(curCatIdx + 1);
		int menuCnt = endIndex - startIndex;
		
		// ===== 메뉴 페이지 이동 버튼 (<, >)
		// 현재 메뉴 페이지가 0번째 페이지라면 < 버튼을 보여주지 않고, 0번째 페이지가 아니면 보여줌
		if(curMenuPage == 0) btnPreMenuPage.setVisible(false);
		else btnPreMenuPage.setVisible(true);

		// 표시되어야할 메뉴의 개수가 12개 이하면 > 버튼을 보여주지 않고, 12개를 넘으면 보여줌
		if(menuCnt <= 12) btnNextMenuPage.setVisible(false);
		else btnNextMenuPage.setVisible(true);
		
		for(int i=0; i<12; i++) {
			if(i < menuCnt) {	// 표시되어야할 메뉴가 있는 경우
				String strMenu = "<html><div style=\"text-align:center\">";
				
				int index = startIndex + i;
				String name = menuList.get(index).getName();	// 메뉴명
				int price = menuList.get(index).getPrice();		// 가격
				int stock = menuList.get(index).getStock();		// 재고

				strMenu += name + "<br>";
				
				// 품절(재고 == 0) 여부에 따라 품절 혹은 금액으로 설정
				if(stock == 0) {
					strMenu += "품절";
					btnMenus[i].setEnabled(false);
				} else {
					strMenu += "<font color=#046240>" + String.format("%,d", price) + "원</font>";
					btnMenus[i].setEnabled(true);
				}
				strMenu += "</div></html>";
				
				btnMenus[i].setText(strMenu);
				
				// 메뉴 아이콘 이미지가 있는 경우엔 해당 이미지로, 없을 경우엔 기본 이미지로 설정
				String imgSrc = iconMenuDir + menuList.get(index).getCategoryId() + menuList.get(index).getMenuNo() + ".jpg";
				if(!new File(imgSrc).exists()) imgSrc = iconMenuDir + "default.png";
				btnMenus[i].setIcon(new ImageIcon(imgSrc));
				btnMenus[i].setDisabledIcon(new ImageIcon(imgSrc));
				
				btnMenus[i].setVisible(true);	// 화면에 표시
				
			} else { // 표시되어야할 메뉴가 없다면 화면에 표시하지 않음
				btnMenus[i].setVisible(false);
			}
		}
	}
	
	// updateCartListener - btnCarClear, btnDeleteMenu, btnDecreaseQty, btnIncreaseQty, btnScrollUp, btnScrollDown 버튼 클릭 이벤트 처리
	public class updateCartListener implements ActionListener {
		/* 메소드명: actionPerformed
		 * 파라미터: ActionEvent e (액션 이벤트가 발생한 객체)
		 * 반환값: 없음
		 * 기능 설명: 1) 메뉴 전체 삭제 버튼이 클릭된 경우 주문 목록을 초기화한다.
		 * 		   2) 단일 메뉴 삭제 버튼(x)이 클릭된 경우 주문 목록에서 해당 메뉴를 삭제한다.
		 * 		   3) 메뉴 수량 변경 버튼(-, +)이 클릭된 경우 주문 목록 정보를 업데이트하고 주문 수량이 0이 되는 경우엔 주문 목록에서 해당 메뉴를 삭제한다.
		 *		   4) 주문 목록 스크롤 버튼(▲, ▼)이 클릭된 경우 주문 목록에서 이전/다음 메뉴를 1개씩 보여준다.
		 *		   5) 메뉴가 선택된 경우 해당 메뉴를 주문 목록에 추가한다.
		 *		   6) 주문 목록 정보에 변경이 발생할 때마다 총 주문 수량, 결제 금액 정보가 변경되고, 주문 수량이 0일 때는 전체 삭제, 결제 버튼을 비활성화 처리한다.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			CustomButton btn = (CustomButton)e.getSource();	// 이벤트가 발생한 KioskButton 객체

			if(btn == btnCartClear) {	// 전체 삭제 버튼
				for(OrderedMenuType om : cart)	// 주문했던 수량만큼 메뉴 재고 증가
					menuList.get(om.getRealMenuIndex()).updateUserMenuStock(-om.getQuantity());
				
				cart.clear();				// 주문 목록 내용 삭제
				OrderedMenuType.reset();	// 총 주문 수량, 총 금액 초기화
				viewIndex = 0;
				
				loadMenuOnBtn();			// 주문 목록 내 메뉴의 수량 변경으로 화면에 표시되어야 하는 메뉴 버튼에 변동 사항(품절 여부)이 발생한 경우 반영
				
			} else if(btn.getText().equals("X") || btn.getText().equals("-") || btn.getText().equals("+")) {	// 단일 메뉴 삭제, 수량 감소, 수량 증가
				// 총 주문 수량이 50인 경우 메뉴 추가 불가
				if(btn.getText().equals("+") && OrderedMenuType.getTOTAL_QUANTITY() == 50) {
					new MenuAddFailDialog();
					return;
				}
				
				// 실제 주문 목록에서의 인덱스 계산
				int realCartindex = viewIndex;
				for(int i=0; i<5; i++) {
					if(btn == btnDeleteMenu[i] || btn == btnDecreaseQty[i] || btn == btnIncreaseQty[i]) {
						realCartindex += i;
						break;
					}
				}
				
				// 눌린 버튼의 종류에 따라 수량 변동량을 계산하고 적용한다.
				int difference = 0;
				if(btn.getText().charAt(0) == 'X') difference = -cart.get(realCartindex).getQuantity();
				else if(btn.getText().charAt(0) == '-') difference = -1;
				else difference = 1;
				
				cart.get(realCartindex).updateQuantity(difference);
				menuList.get(cart.get(realCartindex).getRealMenuIndex()).updateUserMenuStock(difference);
				
				// 주문 수량이 0이 되면 해당 메뉴를 주문 목록에서 삭제한다.
				if(cart.get(realCartindex).getQuantity() == 0) cart.remove(realCartindex);
	
				// 주문 목록 내 메뉴 삭제로 인덱스 변동이 생기는 경우 보여지는 주문 목록에 변화를 주기 위해 viewIndex 값을 계산
				if(cart.size() <= 5) viewIndex = 0;
				else if(viewIndex + 5 > cart.size()) viewIndex --;
				
				loadMenuOnBtn();	// 주문 목록 내 메뉴의 수량 변경으로 화면에 표시되어야 하는 메뉴 버튼에 변동 사항(품절 여부)이 발생한 경우 반영
			} 
			else if(btn == btnScrollUp) viewIndex--;
			else if(btn == btnScrollDown) viewIndex++;
			else {	// 주문할 메뉴의 버튼이 눌린 경우
				// 총 주문 수량이 50인 경우 메뉴 추가 불가
				if(OrderedMenuType.getTOTAL_QUANTITY() == 50) {
					new MenuAddFailDialog();
					return;
				}
				
				int realMenuIndex = -1;
				int i ;
				for(i=0; i<12; i++) {
					if(btn == btnMenus[i]) {
						// 선택된 메뉴의 menuList에서의 인덱스
						// 현재 보고 있는 카테고리의 첫번째 메뉴의 인덱스 + (현재 보고 있는 메뉴 페이지 * 한 페이지당 표시되는 메뉴 개수) + 현재 보고 있는 메뉴 페이지에서 선택된 버튼의 순서
						realMenuIndex = firstIdxByCatList.get(curCatIdx) + curMenuPage * btnMenus.length + i;
						break;
					}
				}
				
				// 이미 주문 목록에 있는 메뉴인지 검사한 후 있으면 수량 증가, 없으면 새로 추가
				boolean isAlreadyAdd = false;
				for(OrderedMenuType om : cart) {
					String addMenuId = menuList.get(realMenuIndex).getCategoryId() + menuList.get(realMenuIndex).getMenuNo();
					String orderedMenuId = om.getCategoryId() + om.getMenuNo();
					
					if(orderedMenuId.equals(addMenuId)) {
						om.updateQuantity(1);
						isAlreadyAdd = true;
						break;
					}
				}
				if(!isAlreadyAdd) cart.add(new OrderedMenuType(realMenuIndex, menuList.get(realMenuIndex), 1));
				
				menuList.get(realMenuIndex).updateUserMenuStock(1);		// 메뉴 목록에서 주문된 수량만큼 재고 감소
				if(menuList.get(realMenuIndex).getStock() == 0) {		// 남은 재고가 0이면 품절 처리
					String txt = "<html><div style=\"text-align:center\">" + menuList.get(realMenuIndex).getName() + "<br>품절</div></html>";
					btnMenus[i].setText(txt);
					btnMenus[i].setEnabled(false);
				}

				// 주문 목록 내 메뉴 삭제로 인덱스 변동이 생기는 경우 보여지는 주문 목록에 변화를 주기 위해 viewIndex 값을 계산
				if(cart.size() <= 5) viewIndex = 0;
				else viewIndex = cart.size() - 5;
			}

			// 변동된 주문 목록에 따라 주문 수량, 결제 금액 업데이트
			labelTotalQuantity.setText("주문 수량: " + OrderedMenuType.getTOTAL_QUANTITY());
			btnPay.setText("<html><div style=\"text-align:center\">" + String.format("%,d", OrderedMenuType.getTOTAL_AMT()) + "원<br>결제하기</div></html>");
			
			// 주문 목록이 없는 경우 전체 삭제, 결제 버튼을 비활성화 처리하고, 아닌 경우에는 활성화 처리
			if(cart.size() == 0) {
				btnCartClear.setEnabled(false);
				btnCartClear.toGray217(20);

				btnPay.setEnabled(false);
				btnPay.toGray217(20);
				
			} else {
				btnCartClear.setEnabled(true);
				btnCartClear.toBlack(20);
				
				btnPay.setEnabled(true);
				btnPay.toGreen(20);
			}
			
			updateCartView();	// 실제 보여지는 주문 목록 업데이트
		}
	}
	
	/* 메소드명: updateCartView
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 화면에 실제 보여지는 주문 목록에 변동 사항이 생길 때마다 업데이트된 내용으로 보여준다.
	 * 		   (변동 사항의 종류: 주문 목록이 초기화된 경우, 주문 목록에서 메뉴가 삭제된 경우, 스크롤 버튼(▲, ▼)이 클릭되어 이전/다음 내용을 보여줘야 하는 경우, 주문 목록에 메뉴가 추가된 경우)
	 */
	public void updateCartView() {
		for(int i=0; i<5; i++) {
			// 출력할 내용이 있는 경우 (ex. 전체 주문 메뉴가 3개일 때, 0~2번째 줄에는 주문 메뉴의 내용이 출력되어야 함)
			if(viewIndex+i < cart.size()) {
				btnDeleteMenu[i].setVisible(true);
				labelMenuName[i].setVisible(true);
				btnDecreaseQty[i].setVisible(true);
				labelQuantity[i].setVisible(true);
				btnIncreaseQty[i].setVisible(true);
				labelTotalPrice[i].setVisible(true);
				
				labelMenuName[i].setText(cart.get(viewIndex+i).getName());
				labelQuantity[i].setText(cart.get(viewIndex+i).getQuantity() + "");
				labelTotalPrice[i].setText(String.format("%,d원", cart.get(viewIndex+i).getTotalPrice()));
				
				int realIndex = cart.get(viewIndex+i).getRealMenuIndex();

				if(menuList.get(realIndex).getStock() == 0) {
					btnIncreaseQty[i].setEnabled(false);
					btnIncreaseQty[i].toGray217(20);
				} else {
					btnIncreaseQty[i].setEnabled(true);
					btnIncreaseQty[i].toGreen(20);
				}
					
				
			// 주문 목록이 없거나 출력할 내용이 없는 경우 (ex. 전체 주문 메뉴가 3개일 때, 3~4번째 줄에는 내용이 없음)
			} else if (cart.size() == 0 || viewIndex+i >= cart.size()){
				btnDeleteMenu[i].setVisible(false);
				labelMenuName[i].setVisible(false);
				btnDecreaseQty[i].setVisible(false);
				labelQuantity[i].setVisible(false);
				btnIncreaseQty[i].setVisible(false);
				labelTotalPrice[i].setVisible(false);
			}
		}
		
		// 현재 보여지는 주문 목록 기준으로 이전 내용이 없다면 ▲ 버튼을 비활성화하고, 아니라면 활성화
		if(viewIndex == 0) btnScrollUp.setEnabled(false);
		else btnScrollUp.setEnabled(true);
		
		// 현재 보여지는 주문 목록 기준으로 다음 내용이 없다면 ▼ 버튼을 비활성화하고, 아니라면 활성화
		if(viewIndex+5 < cart.size()) btnScrollDown.setEnabled(true);
		else btnScrollDown.setEnabled(false);
	}
	
}
