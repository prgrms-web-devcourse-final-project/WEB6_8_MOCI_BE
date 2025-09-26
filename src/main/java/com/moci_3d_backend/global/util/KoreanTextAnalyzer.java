package com.moci_3d_backend.global.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * KOMORAN을 사용한 한국어 텍스트 형태소 분석 유틸리티
 * 검색 키워드에서 의미 있는 명사를 추출하여 검색 품질을 향상시킵니다.
 */
public class KoreanTextAnalyzer {
    
    private static final Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    
    /**
     * 텍스트에서 명사만 추출 (검색용)
     * 
     * @param text 분석할 텍스트
     * @return 추출된 명사 리스트 (2글자 이상만 포함)
     */
    public static List<String> extractNouns(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        KomoranResult result = komoran.analyze(text);
        List<Token> tokens = result.getTokenList();
        
        return tokens.stream()
                .filter(token -> isNoun(token.getPos()))  // 명사만 필터링
                .map(Token::getMorph)  // 형태소(단어) 추출
                .filter(morph -> morph.length() >= 2)  // 2글자 이상만
                .distinct()  // 중복 제거
                .collect(Collectors.toList());
    }
    
    /**
     * 품사가 명사인지 확인
     * 
     * KOMORAN 명사 품사 태그:
     * - NNG: 일반 명사 (친구, 방법)
     * - NNP: 고유 명사 (카카오톡, 서울)
     * - NNB: 의존 명사 (것, 수)
     */
    private static boolean isNoun(String pos) {
        return pos.startsWith("NN");  // NNG, NNP, NNB 등 모든 명사
    }
    
    /**
     * 텍스트에서 명사와 동사 어근 추출 (선택적 사용)
     * 
     * @param text 분석할 텍스트
     * @return 추출된 명사/동사 어근 리스트
     */
    public static List<String> extractKeywords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        KomoranResult result = komoran.analyze(text);
        List<Token> tokens = result.getTokenList();
        
        return tokens.stream()
                .filter(token -> isNoun(token.getPos()) || isVerb(token.getPos()))
                .map(Token::getMorph)
                .filter(morph -> morph.length() >= 2)
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 품사가 동사인지 확인
     * 
     * KOMORAN 동사 품사 태그:
     * - VV: 동사 (추가하다, 보내다)
     */
    private static boolean isVerb(String pos) {
        return pos.equals("VV");
    }
    
    /**
     * 형태소 분석 결과 출력 (디버깅용)
     */
    public static void printAnalysis(String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        
        KomoranResult result = komoran.analyze(text);
        System.out.println("=== 형태소 분석 결과 ===");
        System.out.println("입력: " + text);
        System.out.println("명사: " + result.getNouns());
        System.out.println("전체: " + result.getPlainText());
        
        result.getTokenList().forEach(token -> 
            System.out.println(token.getMorph() + "/" + token.getPos())
        );
    }
}
