package system.user.custom;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;

public class CustomLabel extends JLabel {
	public static String fontBMJUA = "배달의민족 주아";
	public static String fontPretendard = "Pretendard SemiBold";	// default
	
	public CustomLabel() {
		super();
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}
	
	public CustomLabel(Icon img) {
		super(img);
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}

	public CustomLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}

	public CustomLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}

	public CustomLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}

	public CustomLabel(String text) {
		super(text);
		this.setForeground(CustomColor.KIOSK_BLACK);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}
	
	// ============= 폰트 크기를 매개변수로 받는 생성자
	public CustomLabel(float fontSize) {
		super();
		this.setForeground(CustomColor.KIOSK_BLACK);
		this.setFont(new Font(fontPretendard, Font.PLAIN, (int)fontSize));
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}
	
	public CustomLabel(String text, float fontSize) {
		super(text);
		this.setForeground(CustomColor.KIOSK_BLACK);
		this.setFont(new Font(fontPretendard, Font.PLAIN, (int)fontSize));
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}

	public CustomLabel(String text, int horizontalAlignment, float fontSize) {
		super(text, horizontalAlignment);
		this.setForeground(CustomColor.KIOSK_BLACK);
		this.setFont(new Font(fontPretendard, Font.PLAIN, (int)fontSize));
		//this.setBorder(BorderFactory.createLineBorder(Color.black));	// 레이블 위치를 확인하기 위한 테두리
	}
	// ===============
	
	/* 메소드명: toYellow
	 * 파라미터: int fontSize (레이블 폰트 사이즈)
	 * 		  char option (레이블 디자인 옵션): 'D'(Default), 'T'(Text Field)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 폰트 크기가 적용된 노란색 배경의 레이블로 디자인을 변경한다.
	 */
	public void toYellow(float fontSize, char option) {
		if(option == 'T') this.setFont(new Font(fontBMJUA, Font.PLAIN, (int)fontSize));
		else this.setFont(new Font(fontPretendard, Font.PLAIN, (int)fontSize));
		
		this.setOpaque(true);
		this.setBackground(CustomColor.KIOSK_YELLOW);
	}
	
	/* 메소드명: toWhite
	 * 파라미터: int fontSize (레이블 폰트 사이즈)
	 * 반환값: 없음
	 * 기능 설명: 전달받은 폰트 크기가 적용된 하얀색 배경의 레이블로 디자인을 변경한다.
	 */
	public void toWhite(int fontSize) {
		this.setFont(new Font(fontPretendard, Font.PLAIN, fontSize));
		
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
	}
}
