package system.admin.data;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import system.data.CategoryType;
import system.data.MenuType;

public class MenuList {
	private Scanner sc=null;
	
	//생성자
	public MenuList(Scanner sc) {
		this.sc=sc;
	}
	
	/* 메소드명: printWholeMenu
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일의 내용 전체 출력
	 */
	public void printWholeMenu() {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		
		//파일을 읽어올 수 있는동안
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				//tmp 배열에 저장된 값들을 MenuType 열거형의 생성자에 전달
				MenuType menu = new MenuType(tmp[MenuType.INDEX_CATEGORY_ID], tmp[MenuType.INDEX_MENU_NO], tmp[MenuType.INDEX_NAME],
						Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]), tmp[MenuType.INDEX_ENABLE]);
				//생성된 menu를 menuList에 저장
				menuList.add(menu);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		//출력
		System.out.println("현재 저장되어 있는 메뉴 리스트를 출력합니다.");
		System.out.println("(메뉴 ID는 카테고리 ID와 메뉴 순번 항목을 결합한 문자열입니다.)");
		System.out.println("(ex > 카테고리 ID가 CFF이고 메뉴 순번이 01인 아메리카노(ICE)의 메뉴 ID는 CFF01입니다.");
		System.out.println("==================================================================================");
		System.out.println("  카테고리ID         메뉴 순번                 메뉴명           가격     재고    활성화 여부");
		System.out.println("==================================================================================");
		for (int i = 0; i < menuList.size(); i++) {
			menuList.get(i).printAdminMenu();
		}
		System.out.println("==================================================================================");
		
	}
	//카테고리 전체출력 메소드
	public void printWholeCategories() {
		ArrayList<CategoryType> categoryList=new ArrayList<CategoryType>();
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(CategoryType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("카테고리 파일을 찾을 수 없습니다.");
			return;
		}
		
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리
				CategoryType category=new CategoryType(tmp[CategoryType.INDEX_CATEGORY_ID], tmp[CategoryType.INDEX_NAME], tmp[CategoryType.INDEX_ENABLE]);
				categoryList.add(category);
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		
		System.out.println("현재 저장되어 있는 카테고리 리스트를 출력합니다.");
		System.out.println("=================================");
		System.out.println("  카테고리ID     카테고리명   활성화 여부");
		System.out.println("=================================");
		for (int i=0; i<categoryList.size(); i++) {
			categoryList.get(i).printCategory();
		}
		System.out.println("=================================");
	}
	
	
	/* 메소드명: searchMenuById
	 * 파라미터: searchedId: 찾을 메뉴의 메뉴 ID (카테고리 ID+메뉴 순번)
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일에서 
	 * 		   searchedId와 메뉴 ID(카테고리 ID+메뉴 순번)가 동일한 메뉴 찾아서 출력하기
	 */
	public void searchMenuById(String searchedId) {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		boolean found=false;// searchedId와 메뉴 ID(카테고리 ID+메뉴 순번)가 동일한 메뉴를 발견했는지 확인하는 변수
		String s;
		String[] tmp;
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {
				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if ((tmp[MenuType.INDEX_CATEGORY_ID]+tmp[MenuType.INDEX_MENU_NO]).equals(searchedId)) {//searchedId와 메뉴 ID(카테고리 ID+메뉴 순번)가 동일한 메뉴 발견하면
					found=true;
					//MenuType 객체 생성
					MenuType menu = new MenuType(tmp[MenuType.INDEX_CATEGORY_ID], tmp[MenuType.INDEX_MENU_NO], tmp[MenuType.INDEX_NAME],
							Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]), tmp[MenuType.INDEX_ENABLE]);

					menuList.add(menu);//menuList에 위에 생성된 MenuType 객체 추가
				}

			}
			in.close();
			if (found==false) {//searchedId와 메뉴 ID(카테고리 ID+메뉴 순번)가 동일한 메뉴 발견하지 못하면
				System.out.printf("메뉴 ID가 %s인 메뉴를 찾을 수 없습니다.\n",searchedId);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//searchedId와 메뉴 ID(카테고리 ID+메뉴 순번)가 동일한 메뉴를 발견했으면 출력 시작
			System.out.printf("메뉴 ID가 %s인 메뉴 목록입니다.\n",searchedId);
			System.out.println("==================================================================================");
			System.out.println("  카테고리ID         메뉴 순번                 메뉴명           가격     재고    활성화 여부");
			System.out.println("==================================================================================");
			for (int i = 0; i < menuList.size(); i++) {
				menuList.get(i).printAdminMenu();
			}
			System.out.println("==================================================================================");
		}
	}
	/* 메소드명: searchMenuByCategory
	 * 파라미터: String searchedCi (찾을 메뉴의 카테고리 ID)
	 * 반환값: 없음
	 * 기능: 메뉴 목록 중 카테고리ID가 searchedCi와 동일한 메뉴 전체 출력
	 */
	public void searchMenuByCategory(String searchedCi) {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;			
		String[] tmp;
		boolean found=false;//카테고리ID가 searchedCi와 같은 메뉴를 발견했는지 확인할 변수
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				if (tmp[MenuType.INDEX_CATEGORY_ID].equals(searchedCi)) {//카테고리ID가 searchedCi와 같은 메뉴를 발견하면
					found=true;
					MenuType menu=new MenuType(tmp[MenuType.INDEX_CATEGORY_ID], tmp[MenuType.INDEX_MENU_NO], tmp[MenuType.INDEX_NAME],
							Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]), tmp[MenuType.INDEX_ENABLE]);

					menuList.add(menu);
				}
			}
			in.close();
			if (found==false) {//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견하지 못하면
				System.out.printf("카테고리ID가 %s인 메뉴를 찾을 수 없습니다.\n",searchedCi);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견하면 출력 시작
			System.out.printf("카테고리ID가 %s인 메뉴 목록입니다.\n",searchedCi);
			System.out.println("==================================================================================");
			System.out.println("  카테고리ID         메뉴 순번                 메뉴명           가격     재고    활성화 여부");
			System.out.println("==================================================================================");
			for (int i = 0; i < menuList.size(); i++) {
				menuList.get(i).printAdminMenu();
			}
			System.out.println("==================================================================================");
		}
	}
	
	/* 메소드명: searchMenuByStock
	 * 파라미터: int searchedStockMin (찾을 재고량 범위의 최솟값), int searchedStockMax (찾을 재고량 범위의 최댓값)
	 * 반환값: 없음
	 * 기능 설명: MenuType 클래스에 지정된 경로에 존재하는 src//files//datas//menu.txt파일에서 
	 * 		   재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴 출력
	 */
	public void searchMenuByStock(int searchedStockMin, int searchedStockMax) {
		ArrayList<MenuType> menuList=new ArrayList<MenuType>();//MenuType 객체들을 담을 수 있는 동적 배열을 생성
		BufferedReader in;	// 입력 스트림
		String s;
		String[] tmp;
		boolean found=false;//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견했는지 확인할 변수 
		
		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}
		try {
			while ((s = in.readLine()) != null) {

				tmp = s.split("\\|"); // 읽어온 문자열을 구분자로 분리

				int stockInt = Integer.parseInt(tmp[MenuType.INDEX_STOCK]);

				if (searchedStockMin<=stockInt && searchedStockMax>=stockInt) {//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견하면
					found=true;
					MenuType menu = new MenuType(tmp[MenuType.INDEX_CATEGORY_ID], tmp[MenuType.INDEX_MENU_NO], tmp[MenuType.INDEX_NAME],
							Integer.parseInt(tmp[MenuType.INDEX_PRICE]), Integer.parseInt(tmp[MenuType.INDEX_STOCK]), tmp[MenuType.INDEX_ENABLE]);

					menuList.add(menu);
				}
			}
			in.close();
			if (found==false) {//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견하지 못하면
				System.out.printf("재고가 %d이상 %d 이하인 메뉴를 찾을 수 없습니다.\n",searchedStockMin, searchedStockMax);
				System.out.println();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		if (found==true) {//재고가 searchedStockMin 이상 searchedStockMax 이하인 메뉴를 발견하면 출력 시작
			System.out.printf("현재 재고가 %d 이상 %d 이하인 상품 목록입니다.\n",searchedStockMin, searchedStockMax);
			System.out.println("==================================================================================");
			System.out.println("  카테고리ID         메뉴 순번                 메뉴명           가격     재고    활성화 여부");
			System.out.println("==================================================================================");
			for (int i = 0; i < menuList.size(); i++) {
				menuList.get(i).printAdminMenu();
			}
			System.out.println("==================================================================================");
		}
	}
	/* 메소드명: editMenuInfo
	 * 파라미터: String 객체 editMenuId (정보를 수정하고자 하는 메뉴의 메뉴ID (카테고리ID+메뉴 순번)
	 * 반환값: 없음
	 * 기능: 카테고리ID와 메뉴 순번을 합친 문자열 메뉴ID가 파라미터 editMenuId와 같은 메뉴를 찾아서 메뉴 정보 수정.
	 * 	   수정 가능한 값은 메뉴 이름, 메뉴 가격, 재고 현황 세가지.
	 * 	   수정하고자 하는 각 값에 아무 값도 입력하지 않고 엔터만 누를 경우 변경되지 않음.
	 * 	   잘못된 값 (ex)가격 혹은 재고 현황을 0미만의 값으로 변경하려고 할 때) 입력하는 경우에도 변경되지 않음.
	 */
	public void editMenuInfo(String editMenuId) {
		BufferedReader in;	//입력 스트림
		PrintWriter out;	//출력 스트림
		String s;			//입력 스트림에 지정된 경로에 있는 파일 내용 읽어올 변수
		String[] tmp;		//입력 스트림에 지정된 경로에 있는 파일 내용 "|" 기준으로 나눠서 한 문단씩 저장할 배열 
		boolean found = false;//메뉴 정보를 수정하고자 하는 메뉴를 발견했는지 확인할 변수
		boolean modify = false;//변경값이 존재하는지 확인하고자 하는 변수

		System.out.println("메뉴 정보를 수정합니다.");

		try {
		    in = new BufferedReader(new FileReader(MenuType.FILE_NAME));
		    out = new PrintWriter(new FileWriter(MenuType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
		    System.out.println("메뉴 파일을 찾을 수 없습니다.");
		    return;
		} catch (IOException e) {
		    System.out.println("입출력 오류가 발생했습니다.");
		    return;
		}

		try {
		    while ((s = in.readLine()) != null) {//s가 파일을 읽을 수 있는 동안
		        tmp = s.split("\\|");//tmp 배열에 읽어온 내용 |기준으로 각 인덱스에 나눠서 담기
		        if ((tmp[MenuType.INDEX_CATEGORY_ID]+tmp[MenuType.INDEX_MENU_NO]).equals(editMenuId)) {//editMenuId와 같은 메뉴ID 가진 메뉴 찾으면
		            found = true;
		            //변경할 값 입력받기
		            System.out.println("현재 메뉴 정보: " + s);
		            System.out.println("유효하지 않은 값을 입력할 시에는 메뉴 정보가 변경되지 않습니다. 정확히 입력해주세요.");
		            System.out.println();
		            System.out.println("새로운 메뉴명 입력 (변경하지 않을 경우 그대로 엔터)");
		            System.out.print(" > ");
		            String newMenuName=sc.nextLine();

		            System.out.println("\n새로운 가격 입력 (변경하지 않을 경우 그대로 엔터)");
		            System.out.print(" > ");
		            String newPrice=sc.nextLine();

		            System.out.println("\n새로운 재고 수량 입력 (변경하지 않을 경우 그대로 엔터)");
		            System.out.print(" > ");
		            String newStock=sc.nextLine();

		            // 메뉴 정보 변경 여부 확인
		            if (!newMenuName.isEmpty() || !newPrice.isEmpty() || !newStock.isEmpty()) {//변경하고자 하는 세 값에 모두 빈 값이 들어간 경우가 아니면
		                modify = true;//변경되는 값이 존재함
		                if (!newMenuName.isEmpty()) {
		                    tmp[MenuType.INDEX_NAME]=newMenuName;
		                }
		                if (!newPrice.isEmpty()) {
		                    try {
		                        int priceUpdate = Integer.parseInt(newPrice);
		                        if (priceUpdate < 0) {
		                            System.out.println("가격은 양의 정수로만 입력 가능합니다.");
		                        } else {
		                            tmp[MenuType.INDEX_PRICE] = priceUpdate + ""; //문자열로 출력해주기 위해 ""도 같이 넣기
		                        }
		                    } catch (NumberFormatException e) {
		                        System.out.println("가격은 숫자로만 입력 가능합니다.");
		                    }
		                }

		                if (!newStock.isEmpty()) {
		                    try {
		                        int stockUpdate = Integer.parseInt(newStock);
		                        if (stockUpdate < 0) {
		                            System.out.println("재고 수량은 양의 정수로만 입력 가능합니다.");
		                        } else {
		                            tmp[MenuType.INDEX_STOCK] = stockUpdate + "";//문자열로 출력해주기 위해 ""도 같이 넣기
		                        }
		                    } catch (NumberFormatException e) {
		                        System.out.println("재고 수량은 숫자로민 입력 가능합니다.");
		                    }
		                }

		                s = String.join("|", tmp);//tmp 배열의 내용 인덱스 사이마다 | 넣으면서 문자열로 저장
		                System.out.println("수정된 메뉴 정보: " + s);//수정된 정보 문자열로 출력해주기
		            }
		        }

		        out.println(s);//파일에 쓰기
		    }

		    in.close();
		    out.close();

		    if (!found) {//수정하고자 하는 메뉴를 찾지 못했으면
		        System.out.printf("메뉴 ID가 %s인 메뉴를 찾을 수 없습니다.\n", editMenuId);
		    } else if (!modify) {//변경점이 아무것도 없으면
		        System.out.println("변경할 내용이 없습니다.");
		    }
		} catch (Exception e) {
		    System.out.println("입출력 오류가 발생하였습니다.");
		    return;
		}
		//파일 바꾸기
		File file = new File(MenuType.FILE_NAME);
		file.delete();

		file = new File(MenuType.TMP_FILE_NAME);
		file.renameTo(new File(MenuType.FILE_NAME));
	}
	//카테고리별로 마지막 메뉴 순번을 찾는 메소드 (메뉴 추가 시 카테고리의 마지막 메뉴 순번+1로 메뉴 순번이 생성되기에 마지막 메뉴 순번을 찾아야함)
	private int findLastMenuNo(String categoryId) {
	    int lastMenuNo=0;//마지막 메뉴 순번

	    try (BufferedReader in=new BufferedReader(new FileReader(MenuType.FILE_NAME))) {
	        String s;
	        String[] tmp;

	        while ((s=in.readLine()) != null) {//파일을 읽어올 수 있는동안
	            tmp=s.split("\\|");
	            if (tmp[MenuType.INDEX_CATEGORY_ID].equals(categoryId)) {
	                int menuNo=Integer.parseInt(tmp[MenuType.INDEX_MENU_NO]);
	                if (menuNo>lastMenuNo) {//읽어온 메뉴 순번이 lastMenuNo보다 크면
	                    lastMenuNo=menuNo;	//lastMenuNo에 메뉴 순번 저장
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("입출력 오류가 발생했습니다.");
	    }
	    
	    return lastMenuNo;
	}
	/* 메소드명: addMenu
	 * 파라미터: 없음
	 * 반환값: 없음 
	 * 기능: 새 메뉴 추가.
	 * 	   1.메뉴 추가 시 카테고리 ID를 입력받아 어떤 카테고리의 메뉴를 추가할지 선택
	 * 	   2.추가하고자 하는 카테고리에 존재하는 메뉴들 중 가장 마지막 메뉴 순번을 findLastMenuNo 메소드로 찾아오기 
	 * 	   3.생성되는 신규 메뉴의 순번은 찾아온 기존의 마지막 메뉴 순번에 +1
	 * 	   4.그 외 신규 메뉴명, 가격, 재고 현황 입력받기 (enable 칼럼의 기본값은 Y)
	 * 	
	 */
	public void addMenu() {
		ArrayList<CategoryType> categoryList=new ArrayList<CategoryType>();	//카테고리 타입 저장할 동적 배열
		
		BufferedReader inMenu;		//메뉴 파일 입력 스트림
		BufferedReader inCategory; //카테고리 파일 입력 스트림
		PrintWriter out;	//출력 스트림 (메뉴 파일에 신규 메뉴를 추가하기 때문에 출력 스트림은 메뉴 파일의 출력 스트림 하나만 존재)
		
		//새로 추가할 메뉴의 정보 담아둘 배열
		String[] newMenuData=new String[6];
		
		//카테고리 ID 입력
		System.out.println("추가하실 메뉴의 카테고리ID를 입력하세요.");
		while(true) {
			System.out.print(" > ");
			newMenuData[MenuType.INDEX_CATEGORY_ID] = sc.nextLine();
			if (Pattern.matches("^[a-zA-Z]+$", newMenuData[MenuType.INDEX_CATEGORY_ID])==true) {
				break;
			}else {
				System.out.println("카테고리ID는 영어로만 구성되어 있어야 합니다. 다시 입력해주세요.");
			}
		}
	    //마지막 메뉴 순번에 +1 해서 새로운 메뉴 순번 정하기
	    int lastMenuNo=findLastMenuNo(newMenuData[MenuType.INDEX_CATEGORY_ID]);
	    int newMenuNo=lastMenuNo+1;
	    newMenuData[MenuType.INDEX_MENU_NO]=String.format("%02d", newMenuNo);
		
		//메뉴명 입력
		System.out.println("추가하실 메뉴의 메뉴명을 입력하세요.");
		System.out.print(" > ");
		newMenuData[MenuType.INDEX_NAME]=sc.nextLine();
		
		//재고 입력
		System.out.println("추가하실 메뉴의 재고 현황을 입력하세요.");
		int addMenuStock=0;
		while(true) {
			System.out.print(" > ");
			try {
				addMenuStock=Integer.parseInt(sc.nextLine());
				if (addMenuStock<0 || addMenuStock>500) {
					System.out.println("재고 현황은 0이상 500이하의 정수만 입력 가능합니다. 다시 입력해주세요.");
				}else break;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println("재고 현황은 숫자만 입력 가능합니다. 다시 입력해주세요.");
			}
			
		}
		newMenuData[MenuType.INDEX_STOCK]=addMenuStock+"";
		newMenuData[MenuType.INDEX_ENABLE]="Y";

		//가격 입력
		System.out.println("추가하실 메뉴의 가격을 입력하세요.");
		int addMenuPrice=0;
		while(true) {
			System.out.print(" > ");
			try {
				addMenuPrice=Integer.parseInt(sc.nextLine());
				if (addMenuPrice<0 || addMenuPrice>50000) {
					System.out.println("가격은 0이상 50000이하의 정수만 입력 가능합니다. 다시 입력해주세요.");
				}else break;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println("가격은 숫자만 입력 가능합니다. 다시 입력해주세요.");
			}
			
		}
		newMenuData[MenuType.INDEX_PRICE]=addMenuPrice+"";
		
		String str=String.join("|", newMenuData);//추가할 메뉴의 정보 menu파일 양식에 맞게 문자열로 저장할 변수

		String sMenu;		//메뉴 파일의 각 요소(카테고리ID, 메뉴 순번, 메뉴명 등...) 문자열로 저장할 변수
		String[] tmp;		//각 인덱스마다 s에 저장된 메뉴 파일의 각 요소 저장할 배열 (tmp[0]에는 카테고리ID, tmp[1]에는 메뉴 순번 .....)
		
		String sCategory;	//카테고리 파일 읽어올 변수
		
		boolean found=false;


		try {
			inMenu=new BufferedReader(new FileReader(MenuType.FILE_NAME));
			inCategory=new BufferedReader(new FileReader(CategoryType.FILE_NAME));
			out=new PrintWriter(new FileWriter(MenuType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		
		try {
			while ((sCategory=inCategory.readLine())!=null) {
				tmp=sCategory.split("\\|");
				CategoryType category=new CategoryType(tmp[CategoryType.INDEX_CATEGORY_ID], tmp[CategoryType.INDEX_NAME], tmp[CategoryType.INDEX_ENABLE]);
				categoryList.add(category);
			}
			
			
			while((sMenu=inMenu.readLine()) != null) {
				tmp=sMenu.split("\\|");
	            
				//해당 카테고리를 가진 행이 나타났으면
				for (CategoryType category : categoryList) {
					if (category.getCategoryId().equals(newMenuData[MenuType.INDEX_CATEGORY_ID])) {
						if (category.getEnable().equals("N")) {
							System.out.println("비활성화된 카테고리에는 메뉴를 추가할 수 없습니다. 카테고리 활성화 여부를 먼저 확인해주세요.");
							inMenu.close();
							inCategory.close();
							out.close();
							return;
						}
						found=true;
						break;
					}
				}
				

				out.println(sMenu);

			}
			if (found) {
				out.println(str);//추가된 문자열 menu파일에 쓰기
				System.out.println(str);
				System.out.println("메뉴가 추가되었습니다.");
			} else {
				System.out.printf("카테고리가 %s인 메뉴를 찾지 못했습니다. 메뉴를 추가할 수 없습니다.\n", newMenuData[MenuType.INDEX_CATEGORY_ID]);
				System.out.println();
			}
			inMenu.close();
			inCategory.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		File file=new File(MenuType.FILE_NAME);
        file.delete();

        file=new File(MenuType.TMP_FILE_NAME);
        file.renameTo(new File(MenuType.FILE_NAME));
	}
	/* 메소드명: toggleMenuEnable
	 * 파라마터: String toggleMenuId (활성화 여부를 변경할 메뉴의 메뉴ID (카테고리ID+메뉴 순번))
	 * 반환값: 없음
	 * 기능: 카테고리ID+메뉴 순번이 toggleMenuId와 같은 메뉴의 활성화 여부 변경
	 * 	    활성화 여부는 Y 혹은 N밖에 존재하지 않으므로 만약 변경하고자 하는 메뉴의 활성화 여부가 Y면 N으로, N이면 Y로 변경
	 */
	public void toggleMenuEnable(String toggleMenuId) {
		// TODO Auto-generated method stub
		
		
		BufferedReader in;

	    PrintWriter out;
	    String s;
	   
	    String[] tmp;
	    
	    boolean found=false;

		try {
			in=new BufferedReader(new FileReader(MenuType.FILE_NAME));

			out=new PrintWriter(new FileWriter(MenuType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		
		
	    try {
	        while (( s=in.readLine()) != null) {
	            tmp=s.split("\\|");

	            //카테고리ID+메뉴 순번과 deleteMenuId 비교해서 활성화 여부를 변경할 메뉴 찾기
	            if ((tmp[MenuType.INDEX_CATEGORY_ID]+tmp[MenuType.INDEX_MENU_NO]).equals(toggleMenuId)) {
	                found=true;//활성화를 변경할 메뉴를 찾음
	                if (tmp[MenuType.INDEX_ENABLE].equals("Y")) {
						tmp[MenuType.INDEX_ENABLE]="N"; //활성화 여부를 N으로 변경
					}else tmp[MenuType.INDEX_ENABLE]="Y";
	                
	                s = String.join("|", tmp); //변경된 값을 다시 문자열로 조합
	            }

	            out.println(s);
	        }

	        in.close();

	        out.close();

	        if (found) {
	            //활성화 여부를 변경한 메뉴가 있었다면 기존 파일 삭제 후 임시 파일을 원본 파일로 변경
	            File file=new File(MenuType.FILE_NAME);
	            file.delete();

	            file=new File(MenuType.TMP_FILE_NAME);
	            file.renameTo(new File(MenuType.FILE_NAME));
	            System.out.println("메뉴의 활성화 여부가 변경되었습니다.");
	        } else {
	            System.out.println("해당 메뉴ID를 가지고 있는 메뉴가 존재하지 않습니다.");
	        }
	    } catch (IOException e) {
	        System.out.println("입출력 오류가 발생하였습니다.");
	    }
	
		
	}
	/* 메소드명: addCategory
	 * 파라미터: 없음
	 * 반환값: 없음 
	 * 기능: 카테고리 파일에 신규 카테고리 추가하기
	 * 	   카테고리 ID와 카테고리 명을 입력받아서 추가. 활성화 여부는 기본값 Y로 설정.
	 * 	   이미 존재하는 카테고리(동일한 카테고리ID 혹은 동일한 카테고리명)는 추가 불가.
	 */
	public void addCategory() {
		BufferedReader in;//입력 스트림
		PrintWriter out;//출력 스트림
		
		//새로 추가할 카테고리의 정보를 담아둘 배열
		String newCategoryData[]=new String[3];
		
		//카테고리 ID 입력
		System.out.println("추가하실 카테고리의 카테고리ID를 입력하세요.");
		while(true) {
			System.out.print(" > ");
			newCategoryData[CategoryType.INDEX_CATEGORY_ID]=sc.nextLine();
			if (Pattern.matches("^[a-zA-Z]+$", newCategoryData[CategoryType.INDEX_CATEGORY_ID])==true) {
				break;
			}else {
				System.out.println("카테고리ID는 영어로만 구성되어 있어야 합니다. 다시 입력해주세요.");
			}
		}
		
		System.out.println("추가하실 카테고리의 카테고리명을 입력하세요.");
		System.out.print(" > ");
		newCategoryData[CategoryType.INDEX_NAME]=sc.nextLine();
		newCategoryData[CategoryType.INDEX_ENABLE]="Y";
		
		String str=String.join("|", newCategoryData);
		
		//읽고 쓰기 시작
		String s;
		String[] category;
							
		try {
			in=new BufferedReader(new FileReader(CategoryType.FILE_NAME));
			out=new PrintWriter(new FileWriter(CategoryType.TMP_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			System.out.println("메뉴 파일을 찾을 수 없습니다.");
			return;
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		
		try {
			while((s=in.readLine())!=null) {//CategoryType.FILE_NAME (카테고리파일)을 읽어올동안
				category=s.split("\\|");
			
				//해당 카테고리ID를 가진 행이 나타나면
				if(category[CategoryType.INDEX_CATEGORY_ID].equals(newCategoryData[CategoryType.INDEX_CATEGORY_ID])) {
					System.out.println("이미 존재하는 카테고리입니다. 추가 작업을 시행하지 않습니다.");
					in.close();
					out.close();
					return;
				}
				//해당 카테고리명을 가진 행이 나타나면
				if (category[CategoryType.INDEX_NAME].equals(newCategoryData[CategoryType.INDEX_NAME])) {
					System.out.println("이미 존재하는 카테고리명입니다. 추가 작업을 시행하지 않습니다.");
					in.close();
					out.close();
					return;
				}
				out.println(s);
			}
			out.println(str);
			System.out.println("카테고리가 추가되었습니다.");
			in.close();   
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("입출력 오류가 발생했습니다.");
			return;
		}
		File file=new File(CategoryType.FILE_NAME);
        file.delete();

        file=new File(CategoryType.TMP_FILE_NAME);
        file.renameTo(new File(CategoryType.FILE_NAME));
	}
	
	/* 메소드명: toggleCategoryEnable
	 * 파라미터: String toggleCategoryId(활성화 여부를 변경할 카테고리의 카테고리ID)
	 * 반환값: 없음
	 * 기능: 활성화 여부를 변경하고자 하는 메뉴의 카테고리ID를 전달받아 활성화 여부 바꾸기.
	 * 	   존재하지 않는 카테고리 전달받을 시 시행 X
	 */
	public void toggleCategoryEnable(String toggleCategoryId) {
	    BufferedReader in;	//입력 스트림
	    PrintWriter out;	//출력 스트림

	    String s;

	    try {
	        in=new BufferedReader(new FileReader(CategoryType.FILE_NAME));
	        out=new PrintWriter(new FileWriter(CategoryType.TMP_FILE_NAME));

	        boolean categoryFound=false;//deleteCategoryId와 같은 카테고리ID를 가진 카테고리를 발견했는지 확인할 변수

	        while ((s=in.readLine())!=null) {
	            String[] category=s.split("\\|");
	            if (toggleCategoryId.equals(category[CategoryType.INDEX_CATEGORY_ID])) {//활성화 여부를 변경하고자 하는 카테고리를 찾으면 
	                categoryFound=true;
	                if (category[CategoryType.INDEX_ENABLE].equals("Y")) {
						category[CategoryType.INDEX_ENABLE]="N"; //카테고리의 활성화 여부 반대로 변경
					}else category[CategoryType.INDEX_ENABLE]="Y";
	                
	            }
	            out.println(String.join("|", category));
	        }

	        if (!categoryFound) {
	            System.out.println("해당 입력값은 존재하지 않는 카테고리ID입니다.");
	            in.close();
	            out.close();
	            return;
	        }

	        in.close();
	        out.close();

	        File categoryFile=new File(CategoryType.FILE_NAME);
	        categoryFile.delete();

	        File tmpCategoryFile=new File(CategoryType.TMP_FILE_NAME);
	        tmpCategoryFile.renameTo(new File(CategoryType.FILE_NAME));

	        System.out.println("카테고리의 사용 가능 여부가 변경되었습니다.");
	    } catch (IOException e) {
	        System.out.println("입출력 오류가 발생했습니다.");
	    }
	}
}
	