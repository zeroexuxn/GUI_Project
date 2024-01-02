package system.user.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class CustomButton extends JButton {
	private static String fontBMJUA = "배달의민족 주아"; // default
	private static String fontPretendard = "Pretendard SemiBold";
	
	public CustomButton() {
		super();
		this.setBorder(null);
		this.setFocusPainted(false);
	}
	
	public CustomButton(Icon icon) {
		super(null, icon);
		this.setBorder(null);
		this.setFocusPainted(false);
	}

	public CustomButton(String text, Icon icon) {
		super(text, icon);
		this.setBorder(null);
		this.setFocusPainted(false);
	}

	public CustomButton(String text) {
		super(text);
		this.setBorder(null);
		this.setFocusPainted(false);
	}
	
	/* 메소드명: toGreen
	 * 파라미터: int fontSize (버튼 폰트 사이즈)
	 * 반환값: 없음
	 * 기능 설명: 초록색 배경에 흰색 글씨로 디자인을 변경한다.
	 */
	public void toGreen(int fontSize) {
		this.setFont(new Font(fontBMJUA, Font.PLAIN, fontSize));
		
		this.setBackground(CustomColor.KIOSK_GREEN);
		this.setForeground(Color.WHITE);
	}
	
	/* 메소드명: toWhite
	 * 파라미터: int fontSize (버튼 폰트 사이즈)
	 * 		  char option (버튼 디자인 옵션): 'D'(Default), 'M'(Menu)
	 * 반환값: 없음
	 * 기능 설명: 흰색 배경에 검은색 글씨로 디자인을 변경한다.
	 */
	public void toWhite(int fontSize, char option) {
		if(option == 'M') this.setFont(new Font(fontPretendard, Font.PLAIN, fontSize));
		else this.setFont(new Font(fontBMJUA, Font.PLAIN, fontSize));
		
		this.setVerticalTextPosition(JButton.BOTTOM);		// 수직 기준 텍스트 위치: 하단
		this.setHorizontalTextPosition(JButton.CENTER);		// 수평 기준 텍스트 위치: 중앙
		
		this.setBackground(Color.WHITE);
		this.setForeground(CustomColor.KIOSK_BLACK);
		this.setBorder(BorderFactory.createLineBorder(CustomColor.KIOSK_GRAY_217));	// 테두리
	}
	
	/* 메소드명: toBlack
	 * 파라미터: int fontSize (버튼 폰트 사이즈)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 폰트 크기를 적용하고, 검은색 배경에 흰색 글씨로 디자인을 변경한다.
	 */
	public void toBlack(int fontSize) {
		this.setFont(new Font(fontBMJUA, Font.PLAIN, fontSize));
		
		this.setBackground(CustomColor.KIOSK_BLACK);
		this.setForeground(Color.WHITE);
	}
	
	/* 메소드명: toGray242
	 * 파라미터: int fontSize (버튼 폰트 사이즈)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 폰트 크기를 적용하고, 회색 배경(242, 242, 242)에 검은색 글씨로 디자인을 변경한다.
	 * 		   (카테고리 버튼 선택 상태)
	 */
	public void toGray242(int fontSize) {
		this.setFont(new Font("배달의민족 주아", Font.PLAIN, fontSize));
		
		this.setBackground(CustomColor.KIOSK_GRAY_242);
		this.setForeground(CustomColor.KIOSK_BLACK);
	}

	/* 메소드명: toGray217
	 * 파라미터: int fontSize (버튼 폰트 사이즈)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 폰트 크기를 적용하고, 회색 배경(217, 217, 217)에 검은색 글씨로 디자인을 변경한다.
	 * 		   (전체 삭제, 결제 버튼 비활성화 표시)
	 */
	public void toGray217(int fontSize) {
		this.setFont(new Font("배달의민족 주아", Font.PLAIN, fontSize));
		
		this.setBackground(CustomColor.KIOSK_GRAY_217);
		this.setForeground(CustomColor.KIOSK_BLACK);
	}
	
	/* 메소드명: toScrollDesign
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: 스크롤 버튼 디자인을 적용한다.
	 */
	public void toScrollDesign() {
		this.setFont(new Font("Pretendard SemiBold", Font.PLAIN, 15));
		
		this.setMargin(new Insets(0, 0, 0, 0));
		
		this.setBackground(CustomColor.KIOSK_GRAY_217);
		this.setForeground(CustomColor.KIOSK_GREEN);
	}
}