package system.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import system.data.CouponType;
import system.data.MemberType;
import system.data.MenuType;
import system.data.OrderedMenuType;
import system.data.SalesDetailType;
import system.data.SalesTotalType;

public class UpdateDataProcess {
	private ArrayList<MenuType> menuList = UserModePanel.menuList;
	private ArrayList<OrderedMenuType> cart = UserModePanel.cart;
	
	private SalesTotalType salesTotal = UserModePanel.salesTotal;
	private ArrayList<SalesDetailType> salesDetailList = UserModePanel.salesDetailList;
	
	public void updateData(String birth) {
		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(데이터 처리) :: 데이터 처리를 진행합니다.");
		
		// 상세 매출 리스트 업데이트
		for(OrderedMenuType orderedMenu : cart) {
			String orderNo = salesTotal.getOrderNo();
			String type = "M";
			String id = orderedMenu.getCategoryId() + orderedMenu.getMenuNo();
			String name = orderedMenu.getName();
			int price = orderedMenu.getPrice();
			int quantity = orderedMenu.getQuantity();
			
			salesDetailList.add(new SalesDetailType(orderNo, type, id, name, price, quantity));
		}

		// ===== 메뉴(menu.txt)
		updateMenuStock();
		
		// ===== 회원(member.txt): 포인트 사용, 쿠폰(Coupon.txt) + 상세 매출 리스트에 할인 내용 반영
		String discountId = salesTotal.getDiscountId();									// 사용한 회원 혹은 쿠폰 ID
		int discountAmt = OrderedMenuType.getTOTAL_AMT() - salesTotal.getTotalAmt();	// 실제 할인 금액
		
		if(!discountId.equals(" ")) {	// 포인트 혹은 쿠폰을 사용한 경우
			if(discountId.length() == 11) {
				updateMemberPoint(discountId, discountAmt);	// 포인트
				salesDetailList.add(new SalesDetailType(salesTotal.getOrderNo(), "P", discountId, "포인트 사용", -discountAmt, 1));
				
			} else {
				updateCouponUsed(discountId);	// 쿠폰
				salesDetailList.add(new SalesDetailType(salesTotal.getOrderNo(), "C", discountId, "쿠폰 할인", -discountAmt, 1));
			}

			// 포인트나 쿠폰으로 일부 결제한 경우 통합 매출 데이터에서 discountId 값을 초기화 (전액 결제한 경우에만 discountId 정보를 남김)
			if(!salesTotal.getPayMethod().equals("포인트") && !salesTotal.getPayMethod().equals("쿠폰"))
				salesTotal.setDiscountId(" ");
		}

		// ===== 통합 매출(sales_total(YYYYMMDD).txt)
		updateSalesTotal();
		
		// ===== 상세 매출(sales_detail(YYYYMMDD).txt)
		updateSalesDetail();
		
		// ===== 회원(member.txt): 포인트 적립
		if(!salesTotal.getMemberId().equals(" "))
			updateMemberInfo(salesTotal.getMemberId(), salesTotal.getTotalPts(), birth);

		System.out.println(Calendar.getInstance().getTime().toString() + ":: 사용자 모드(데이터 처리) :: 데이터 처리가 완료되어 영수증 출력 여부 선택 단계로 넘어갑니다.");
		
		UserModePanel.selectPrintReceiptPanel.setVisible(true);
	}

	/* 메소드명: updateMenuStock
	 * 파라미터: 없음
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 메뉴 파일(menu.txt)에 저장된 메뉴의 재고량을 업데이트한다.
	 */
	private boolean updateMenuStock() {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		String s;
		String[] tmp;
		
		// 입출력 스트림 생성
		try {
			System.out.println("메뉴 목록을 읽고 쓰기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MenuType.FILE_NAME));	// 파일 내용을 읽어오기 위한 스트림
			out = new PrintWriter(new FileWriter(MenuType.TMP_FILE_NAME));	// 출력 스트림 생성
			
		} catch (FileNotFoundException e) {
			System.out.println("메뉴 파일이 존재하지 않습니다.");
			return false;
			
		}  catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		//파일을 읽어올 수 있는 동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				
				// 비교할 MenuType 객체 생성
				String categoryId = tmp[MenuType.INDEX_CATEGORY_ID];
				String menuNo = tmp[MenuType.INDEX_MENU_NO];
				
				for(int i=0; i<cart.size(); i++) {
					if(cart.get(i).getCategoryId().equals(categoryId) && cart.get(i).getMenuNo().equals(menuNo)) {
						int oriStock = Integer.parseInt(tmp[MenuType.INDEX_STOCK]);		// 원래 재고량
						tmp[MenuType.INDEX_STOCK] = oriStock - cart.get(i).getQuantity() + "";
						
						// 변경 전 내용을 출력하고, 변경 후의 내용을 세팅한 후 출력
						System.out.print(s + " ===> ");
						
						s = String.join("|", tmp);
						System.out.print(s + "\n");
						
						break;
					}
				}
				out.println(s);
			}
			
			// 스트림 닫기
			in.close();
			out.close();
			
		} catch (Exception e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(MenuType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(MenuType.TMP_FILE_NAME);
		file.renameTo(new File(MenuType.FILE_NAME));
		
		System.out.println(MenuType.FILE_NAME + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}

	/* 메소드명: updateMemberPoint
	 * 파라미터: String memberId (포인트를 사용한 회원 ID)
	 * 		  int usePointAmt (사용한 포인트 금액)
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 회원 파일(member.txt)에서 전달받은 회원 ID로 회원 조회 후 누적 포인트 금액을 업데이트한다.
	 */
	private boolean updateMemberPoint(String memberId, int usePointAmt) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		// 입출력 스트림 생성
		try {
			System.out.println("회원 목록을 읽고 쓰기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));		// 파일 내용을 읽어오기 위한 스트림
			out = new PrintWriter(new FileWriter(MemberType.TMP_FILE_NAME));	// 출력 스트림 생성

		} catch (FileNotFoundException e) {		// 회원 파일이 없는 경우
			System.out.println("회원 파일이 존재하지 않습니다.");
			return false;
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		String s;		// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;	// 구분자로 분리한 문자열이 저장될 임시 배열

		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					int newPointAmt = Integer.parseInt(tmp[MemberType.INDEX_POINT]) - usePointAmt;
					tmp[MemberType.INDEX_POINT] = newPointAmt + "";
					
					// 변경 전 내용을 출력하고, 변경 후의 내용을 세팅한 후 출력
					System.out.print(s + " ===> ");
					
					s = String.join("|", tmp);
					System.out.print(s + "\n");
				}
				
				out.println(s);
			}
			
			// 스트림 닫기
			in.close();
			out.close();
			
		} catch (Exception e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(MemberType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(MemberType.TMP_FILE_NAME);
		file.renameTo(new File(MemberType.FILE_NAME));
		
		System.out.println(MemberType.FILE_NAME + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}

	/* 메소드명: updateCouponUsed
	 * 파라미터: String  couponId (사용한 쿠폰 ID)
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 쿠폰 파일(coupon.txt)에서 전달받은 쿠폰 ID로 쿠폰 조회 후 사용 여부를 업데이트한다.
	 */
	private boolean updateCouponUsed(String couponId) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		// 입출력 스트림 생성
		try {
			System.out.println("쿠폰 목록을 읽고 쓰기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(CouponType.FILE_NAME));		// 파일 내용을 읽어오기 위한 스트림
			out = new PrintWriter(new FileWriter(CouponType.TMP_FILE_NAME));	// 출력 스트림 생성

		} catch (FileNotFoundException e) {		// 쿠폰 파일이 없는 경우
			System.out.println("쿠폰 파일이 존재하지 않습니다.");
			return false;
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		String s;		// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;	// 구분자로 분리한 문자열이 저장될 임시 배열
		
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리
				
				if(tmp[CouponType.INDEX_COUPON_ID].equals(couponId)) {
					tmp[CouponType.INDEX_USED] = "Y";
					
					// 변경 전 내용을 출력하고, 변경 후의 내용을 세팅한 후 출력
					System.out.print(s + " ===> ");
					
					s = String.join("|", tmp);
					System.out.print(s + "\n");
				}
				
				out.println(s);
			}

			// 스트림 닫기
			in.close();
			out.close();
			
		} catch (Exception e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
			
		// 구버전 파일 삭제
		File file = new File(CouponType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(CouponType.TMP_FILE_NAME);
		file.renameTo(new File(CouponType.FILE_NAME));
		
		System.out.println(CouponType.FILE_NAME + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}

	/* 메소드명: updateMemberInfo
	 * 파라미터: 
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 회원 파일(member.txt)에서 전달받은 회원 ID로 회원 조회 후 누적 포인트 금액 및 생일 정보를 업데이트한다.
	 */
	private boolean updateMemberInfo(String memberId, int totalPoints, String birth) {
		BufferedReader in;	// 입력 스트림
		PrintWriter out;	// 출력 스트림
		
		// 입출력 스트림 생성
		try {
			System.out.println("회원 목록을 읽고 쓰기 위한 입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(MemberType.FILE_NAME));		// 파일 내용을 읽어오기 위한 스트림
			out = new PrintWriter(new FileWriter(MemberType.TMP_FILE_NAME));	// 출력 스트림 생성

		} catch (FileNotFoundException e) {		// 회원 파일이 없는 경우
			System.out.println("회원 파일이 존재하지 않습니다.");
			return false;
			
		} catch (IOException e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		String s;					// 파일에서 읽어온 한 줄이 저장될 변수
		String[] tmp;				// 구분자로 분리한 문자열이 저장될 임시 배열
		boolean isUpdate = false;	// 신규 등록 여부

		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");	// 구분자(|)를 기준으로 읽어온 문자열 분리

				if(tmp[MemberType.INDEX_MEMBER_ID].equals(memberId)) {
					isUpdate = true;
					
					tmp[MemberType.INDEX_POINT] = totalPoints + "";
					tmp[MemberType.INDEX_BIRTH] = birth;

					// 변경 전 내용을 출력하고, 변경 후의 내용을 세팅한 후 출력
					System.out.print(s + " ===> ");
					
					s = String.join("|", tmp);
					System.out.print(s + "\n");
				}
				
				out.println(s);
			}
		
			// 신규 등록 케이스
			if(!isUpdate) out.println(memberId + "|" + totalPoints + "|" + birth);
			
			// 스트림 닫기
			in.close();
			out.close();
			
		} catch (Exception e) {
			System.out.println("데이터 읽기에 실패하였습니다.");
			return false;
		}
		
		// 구버전 파일 삭제
		File file = new File(MemberType.FILE_NAME);
		file.delete();

		// 새로운 파일명을 설정
		file = new File(MemberType.TMP_FILE_NAME);
		file.renameTo(new File(MemberType.FILE_NAME));

		System.out.println(MemberType.FILE_NAME + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}
	
	/* 메소드명: updateSalesTotal
	 * 파라미터: 없음
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 통합 매출 내역 파일(salesTotal(YYYYMMDD).txt)에 통합 매출 데이터를 추가하여 업데이트한다.
	 */
	private boolean updateSalesTotal() {
		try {
			System.out.println("통합 매출 데이터를 쓰기 위한 입력 스트림을 생성중...");
			System.out.println("   " + salesTotal.getFileName());
			
			PrintWriter out = new PrintWriter(new FileWriter(salesTotal.getFileName(), true));
			
			out.println(salesTotal.toDataTemplate());					// 통합 매출 데이터 작성
			System.out.println("[add]" + salesTotal.toDataTemplate()); 	// 추가된 내용 출력
			
			out.close();	// 스트림 닫기
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.println(salesTotal.getFileName() + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}

	/* 메소드명: updateSalesDetail
	 * 파라미터: 없음
	 * 반환값: boolean (true: 데이터 업데이트 성공, false: 데이터 업데이트 실패)
	 * 기능 설명: 상세 매출 내역 파일(salesTotal(YYYYMMDD).txt)에 상세 매출 데이터를 추가하여 업데이트한다.
	 */
	private boolean updateSalesDetail() {
		try {
			System.out.println("상세 매출 데이터를 쓰기 위한 입력 스트림을 생성중...");
			
			PrintWriter out = new PrintWriter(new FileWriter(salesDetailList.get(0).getFileName(), true));
			
			for(SalesDetailType salesDetail : salesDetailList) {	// 추가해야할 상세 매출 데이터가 있는 동안 작성
				out.println(salesDetail.toDataTemplate());
				System.out.println("[add]" + salesDetail.toDataTemplate()); 	// 추가된 내용 출력
			}
				
			out.close();	// 스트림 닫기
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.println(salesDetailList.get(0).getFileName() + " 파일의 업데이트가 완료되었습니다.");
		return true;
	}

}
