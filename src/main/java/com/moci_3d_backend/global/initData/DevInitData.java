package com.moci_3d_backend.global.initData;

import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.repository.ArchiveRequestRepository;
import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevInitData {
    @Autowired
    @Lazy
    private DevInitData self;

    private final UserRepository userRepository;
    private final ArchiveRequestRepository archiveRequestRepository;
    private final PublicArchiveRepository publicArchiveRepository;
    private final AiChatRoomRepository aiChatRoomRepository;
    private final AiChatRoomService aiChatRoomService;

    @Bean
    ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            self.memberInit();
            self.archiveRequestInit();
            self.publicArchiveInit();
            self.aiRoomInit();
        };
    }

    @Transactional
    public void memberInit() {
        if (userRepository.count() > 0) {
            return;
        }

        // === 테스트 데이터 10개 생성 ===

        // 1. 관리자 계정
        User admin = new User();
        admin.setUserId("01012345678");
        admin.setLoginType("PHONE");
        admin.setPassword("admin123");
        admin.setName("관리자");
        admin.setEmail("admin@example.com");
        admin.setRole(User.UserRole.ADMIN);
        admin.setDigitalLevel(5);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        userRepository.save(admin);

        // 2. 멘토 계정 1
        User mentor1 = new User();
        mentor1.setUserId("01023456789");
        mentor1.setLoginType("PHONE");
        mentor1.setPassword("mentor123");
        mentor1.setName("김철수");
        mentor1.setEmail("kim@example.com");
        mentor1.setRole(User.UserRole.MENTOR);
        mentor1.setDigitalLevel(4);
        mentor1.setCreatedAt(LocalDateTime.now());
        mentor1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(mentor1);

        // 3. 멘토 계정 2
        User mentor2 = new User();
        mentor2.setUserId("01034567890");
        mentor2.setLoginType("PHONE");
        mentor2.setPassword("mentor123");
        mentor2.setName("이영희");
        mentor2.setEmail("lee@example.com");
        mentor2.setRole(User.UserRole.MENTOR);
        mentor2.setDigitalLevel(3);
        mentor2.setCreatedAt(LocalDateTime.now());
        mentor2.setUpdatedAt(LocalDateTime.now());
        userRepository.save(mentor2);

        // 4. 일반 사용자 1
        User user1 = new User();
        user1.setUserId("01045678901");
        user1.setLoginType("PHONE");
        user1.setPassword("user123");
        user1.setName("박민수");
        user1.setEmail("park@example.com");
        user1.setRole(User.UserRole.USER);
        user1.setDigitalLevel(5);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user1);

        // 5. 일반 사용자 2
        User user2 = new User();
        user2.setUserId("01056789012");
        user2.setLoginType("PHONE");
        user2.setPassword("pass123");
        user2.setName("최민수");
        user2.setEmail("choi@example.com");
        user2.setRole(User.UserRole.USER);
        user2.setDigitalLevel(2);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user2);

        // 6. 일반 사용자 3
        User user3 = new User();
        user3.setUserId("01067890123");
        user3.setLoginType("PHONE");
        user3.setPassword("qwer123");
        user3.setName("정수진");
        user3.setEmail("jung@example.com");
        user3.setRole(User.UserRole.USER);
        user3.setDigitalLevel(3);
        user3.setCreatedAt(LocalDateTime.now());
        user3.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user3);

        // 7. 일반 사용자 4
        User user4 = new User();
        user4.setUserId("01078901234");
        user4.setLoginType("PHONE");
        user4.setPassword("asdf123");
        user4.setName("한지영");
        user4.setEmail("han@example.com");
        user4.setRole(User.UserRole.USER);
        user4.setDigitalLevel(1);
        user4.setCreatedAt(LocalDateTime.now());
        user4.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user4);

        // 8. 일반 사용자 5
        User user5 = new User();
        user5.setUserId("01089012345");
        user5.setLoginType("PHONE");
        user5.setPassword("zxcv123");
        user5.setName("윤태호");
        user5.setEmail("yoon@example.com");
        user5.setRole(User.UserRole.USER);
        user5.setDigitalLevel(4);
        user5.setCreatedAt(LocalDateTime.now());
        user5.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user5);

        // 9. 일반 사용자 6
        User user6 = new User();
        user6.setUserId("01090123456");
        user6.setLoginType("PHONE");
        user6.setPassword("123456");
        user6.setName("강미래");
        user6.setEmail("kang@example.com");
        user6.setRole(User.UserRole.USER);
        user6.setDigitalLevel(2);
        user6.setCreatedAt(LocalDateTime.now());
        user6.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user6);

        // 10. 일반 사용자 7
        User user7 = new User();
        user7.setUserId("01012345670");
        user7.setLoginType("PHONE");
        user7.setPassword("password");
        user7.setName("송하늘");
        user7.setEmail("song@example.com");
        user7.setRole(User.UserRole.USER);
        user7.setDigitalLevel(0);
        user7.setCreatedAt(LocalDateTime.now());
        user7.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user7);
    }

    @Transactional
    public void archiveRequestInit() {
        if (archiveRequestRepository.count() > 0) {
            return;
        }

        // 멘토 사용자들 조회 (ID 2, 3번)
        User mentor1 = userRepository.findById(2L).orElse(null);
        User mentor2 = userRepository.findById(3L).orElse(null);

        if (mentor1 == null || mentor2 == null) {
            return; // 멘토 사용자가 없으면 요청글 생성 중단
        }

        // === 자료 요청 예시 데이터 생성 ===

        // 1. 카카오톡 사용법 요청 (mentor1 작성)
        ArchiveRequest request1 = ArchiveRequest.builder()
                .user(mentor1)
                .title("카카오톡 기본 사용법 자료 요청")
                .description("안녕하세요. 시니어분들을 위한 카카오톡 기본 사용법에 대한 교육 자료가 필요합니다.\n\n" +
                        "특히 다음과 같은 내용이 포함되었으면 좋겠습니다:\n" +
                        "- 카카오톡 설치 방법\n" +
                        "- 친구 추가하는 방법\n" +
                        "- 메시지 보내기와 받기\n" +
                        "- 사진 전송하는 방법\n" +
                        "- 카카오톡 설정 변경하기\n\n" +
                        "초보자도 쉽게 따라할 수 있는 스크린샷이 많이 포함된 자료를 부탁드립니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request1);

        // 2. KTX 예매 방법 요청 (mentor2 작성)
        ArchiveRequest request2 = ArchiveRequest.builder()
                .user(mentor2)
                .title("KTX 온라인 예매 방법 가이드 요청")
                .description("KTX 온라인 예매 시스템 사용법에 대한 상세한 가이드가 필요합니다.\n\n" +
                        "포함되었으면 하는 내용:\n" +
                        "- 코레일톡 앱 설치 및 회원가입\n" +
                        "- 기차표 검색하기\n" +
                        "- 좌석 선택 및 예매하기\n" +
                        "- 결제 방법 (카드, 간편결제 등)\n" +
                        "- 예매 확인 및 취소 방법\n" +
                        "- QR코드로 승차하는 방법\n\n" +
                        "특히 60대 이상 어르신들도 쉽게 이해할 수 있도록 단계별로 자세히 설명된 자료를 요청드립니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request2);

        // 3. 배달의민족 사용법 요청 (mentor1 작성)
        ArchiveRequest request3 = ArchiveRequest.builder()
                .user(mentor1)
                .title("배달의민족 앱 사용법 교육자료 요청")
                .description("배달음식 주문을 위한 배달의민족 앱 사용법 자료가 필요합니다.\n\n" +
                        "다음 내용을 포함해 주세요:\n" +
                        "- 배달의민족 앱 다운로드 및 설치\n" +
                        "- 회원가입 및 주소 등록\n" +
                        "- 음식점 검색 및 선택\n" +
                        "- 메뉴 선택 및 옵션 추가\n" +
                        "- 결제 방법 설정 (신용카드, 네이버페이 등)\n" +
                        "- 주문 확인 및 배달 상태 확인\n" +
                        "- 리뷰 작성하기\n\n" +
                        "디지털에 익숙하지 않은 어르신들도 따라할 수 있는 친절한 설명서를 부탁드립니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request3);

        // 4. 유튜브 시청법 요청 (mentor2 작성)
        ArchiveRequest request4 = ArchiveRequest.builder()
                .user(mentor2)
                .title("유튜브 기본 사용법 가이드 요청")
                .description("시니어층을 위한 유튜브 사용법 교육자료를 요청합니다.\n\n" +
                        "필요한 내용:\n" +
                        "- 유튜브 앱 설치 및 기본 화면 설명\n" +
                        "- 영상 검색하는 방법\n" +
                        "- 영상 재생, 일시정지, 음량 조절\n" +
                        "- 자막 설정하기\n" +
                        "- 구독하기와 좋아요 누르기\n" +
                        "- 재생목록 만들기\n" +
                        "- 시청 기록 확인하기\n\n" +
                        "특히 건강, 요리, 뉴스 등 어르신들이 관심 있어 할 만한 채널 추천도 함께 포함해 주시면 좋겠습니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request4);

        // 5. 쿠팡 온라인 쇼핑 요청 (mentor1 작성)
        ArchiveRequest request5 = ArchiveRequest.builder()
                .user(mentor1)
                .title("쿠팡 온라인 쇼핑 사용법 자료 요청")
                .description("온라인 쇼핑몰 쿠팡 사용법에 대한 교육자료가 필요합니다.\n\n" +
                        "포함 내용:\n" +
                        "- 쿠팡 앱 설치 및 회원가입\n" +
                        "- 상품 검색 및 필터 사용법\n" +
                        "- 상품 상세정보 확인 (리뷰, 평점 등)\n" +
                        "- 장바구니 담기 및 주문하기\n" +
                        "- 배송지 등록 및 변경\n" +
                        "- 결제 방법 설정 (카드, 무통장입금 등)\n" +
                        "- 주문 확인 및 배송 추적\n" +
                        "- 반품/교환 신청 방법\n\n" +
                        "온라인 쇼핑이 처음인 분들도 안전하게 이용할 수 있는 가이드를 부탁드립니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request5);

        // 6. 시외버스 예매 요청 (mentor2 작성) 
        ArchiveRequest request6 = ArchiveRequest.builder()
                .user(mentor2)
                .title("시외버스 온라인 예매 방법 가이드 요청")
                .description("시외버스 터미널 방문 없이 온라인으로 버스표를 예매하는 방법에 대한 자료가 필요합니다.\n\n" +
                        "다음 내용을 포함해 주세요:\n" +
                        "- 고속버스통합예매 사이트 접속 및 회원가입\n" +
                        "- 출발지/도착지 선택 및 날짜/시간 설정\n" +
                        "- 버스편 선택 및 좌석 지정\n" +
                        "- 승객 정보 입력\n" +
                        "- 온라인 결제 (카드결제, 간편결제)\n" +
                        "- 예매 확인서 출력 또는 모바일 티켓\n" +
                        "- 예매 취소 및 변경 방법\n\n" +
                        "컴퓨터와 스마트폰 양쪽 모두에서 사용할 수 있는 방법을 알려주시면 더욱 좋겠습니다.")
                .status(RequestStatus.PENDING)
                .build();
        archiveRequestRepository.save(request6);
    }

    @Transactional
    public void publicArchiveInit() {
        if (publicArchiveRepository.count() > 0) {
            return;
        }

        // 관리자 사용자 조회 (ID 1번)
        User admin = userRepository.findById(1L).orElse(null);
        if (admin == null) {
            return; // 관리자가 없으면 교육 자료실 생성 중단
        }

        // === 교육 자료실 예시 데이터 생성 (3개) ===

        // 1. 카카오톡 사용법 가이드
        PublicArchive archive1 = new PublicArchive();
        archive1.setTitle("카카오톡 기본 사용법 완벽 가이드");
        archive1.setDescription("시니어를 위한 카카오톡 사용법을 단계별로 설명한 교육 자료입니다.\n\n" +
                "📱 포함 내용:\n" +
                "• 카카오톡 앱 설치 및 회원가입 방법\n" +
                "• 친구 추가하기 (QR코드, 전화번호, ID 검색)\n" +
                "• 메시지 보내기와 받기\n" +
                "• 사진, 동영상 전송하는 방법\n" +
                "• 음성메시지 보내기\n" +
                "• 그룹 채팅방 만들기\n" +
                "• 카카오톡 설정 변경하기\n" +
                "• 알림 설정 및 차단 기능\n\n" +
                "💡 특징:\n" +
                "- 큰 글씨와 스크린샷으로 쉽게 이해\n" +
                "- 단계별 상세 설명\n" +
                "- 자주 묻는 질문(FAQ) 포함");
        archive1.setCategory(ArchiveCategory.KAKAO_TALK);
        archive1.setSubCategory("기본 사용법");
        archive1.setUploadedBy(admin);
        archive1.setFileUploads(Collections.emptyList()); // 파일 없음
        publicArchiveRepository.save(archive1);

        // 2. KTX 온라인 예매 가이드
        PublicArchive archive2 = new PublicArchive();
        archive2.setTitle("KTX 온라인 예매 쉽게 하기");
        archive2.setDescription("집에서 편리하게 KTX 기차표를 예매하는 방법을 알려드립니다.\n\n" +
                "🚄 주요 내용:\n" +
                "• 코레일톡 앱 다운로드 및 설치\n" +
                "• 회원가입과 본인인증 과정\n" +
                "• 출발역과 도착역 선택하기\n" +
                "• 날짜와 시간 설정하기\n" +
                "• 좌석 종류별 특징 (일반실, 특실)\n" +
                "• 온라인 결제 방법 (카드, 간편결제)\n" +
                "• 예매 확인 및 모바일 승차권 사용법\n" +
                "• 예매 취소와 변경 방법\n\n" +
                "💳 결제 방법:\n" +
                "- 신용카드, 체크카드\n" +
                "- 카카오페이, 네이버페이\n" +
                "- 페이코, 삼성페이\n\n" +
                "❗ 주의사항과 유용한 팁도 함께 제공합니다.");
        archive2.setCategory(ArchiveCategory.KTX);
        archive2.setSubCategory("온라인 예매");
        archive2.setUploadedBy(admin);
        archive2.setFileUploads(Collections.emptyList()); // 파일 없음
        publicArchiveRepository.save(archive2);

        // 3. 유튜브 시청 방법 가이드
        PublicArchive archive3 = new PublicArchive();
        archive3.setTitle("유튜브로 즐기는 동영상 세상");
        archive3.setDescription("유튜브를 활용해서 다양한 영상을 시청하고 즐기는 방법을 소개합니다.\n\n" +
                "📺 학습 내용:\n" +
                "• 유튜브 앱 설치 및 첫 화면 익히기\n" +
                "• 영상 검색하는 다양한 방법\n" +
                "• 재생, 일시정지, 되감기 조작법\n" +
                "• 음량 조절과 화면 크기 변경\n" +
                "• 자막 켜기/끄기 설정\n" +
                "• 좋아하는 채널 구독하기\n" +
                "• 재생목록 만들고 관리하기\n" +
                "• 시청 기록 확인하기\n\n" +
                "🎯 추천 채널 소개:\n" +
                "• 건강 정보 채널\n" +
                "• 요리 레시피 채널\n" +
                "• 뉴스 및 시사 채널\n" +
                "• 여행 정보 채널\n" +
                "• 취미 활동 채널\n\n" +
                "📱 스마트폰과 태블릿 양쪽 사용법 모두 설명드립니다.");
        archive3.setCategory(ArchiveCategory.YOUTUBE);
        archive3.setSubCategory("기본 시청법");
        archive3.setUploadedBy(admin);
        archive3.setFileUploads(Collections.emptyList()); // 파일 없음
        publicArchiveRepository.save(archive3);
    }

    @Transactional
    public void aiRoomInit() {
        if (aiChatRoomRepository.count() > 0) {
            return;
        }

        aiChatRoomService.create("샘플 AI 채팅방1", "카카오톡 사용법");
        aiChatRoomService.create("샘플 AI 채팅방2", "유투브 사용법");
        aiChatRoomService.create("샘플 AI 채팅방3", "KTX 온라인 예매");

    }

}