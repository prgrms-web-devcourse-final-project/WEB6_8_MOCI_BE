package com.moci_3d_backend.global.initData;

import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.repository.ArchiveRequestRepository;
import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
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

    @Bean
    ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            self.memberInit();
            self.archiveRequestInit();
            self.publicArchiveInit();
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

        // === 카카오톡 ===
        // 1. 카카오톡 설치 및 가입
        PublicArchive kakao1 = new PublicArchive();
        kakao1.setTitle("카카오톡 설치 및 가입하기");
        kakao1.setDescription("스마트폰에 카카오톡 앱을 설치하고 회원가입하는 방법을 단계별로 안내합니다.");
        kakao1.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao1.setSubCategory("설치와 가입");
        kakao1.setUploadedBy(admin);
        kakao1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao1);

        // 2. 친구 추가
        PublicArchive kakao2 = new PublicArchive();
        kakao2.setTitle("카카오톡 친구 추가하기");
        kakao2.setDescription("전화번호, QR코드, ID로 친구를 추가하는 다양한 방법을 설명합니다.");
        kakao2.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao2.setSubCategory("친구 관리");
        kakao2.setUploadedBy(admin);
        kakao2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao2);

        // 3. 메시지 보내기
        PublicArchive kakao3 = new PublicArchive();
        kakao3.setTitle("카카오톡 메시지 보내고 받기");
        kakao3.setDescription("친구에게 텍스트 메시지를 보내고 확인하는 기본 방법을 알려드립니다.");
        kakao3.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao3.setSubCategory("메시지");
        kakao3.setUploadedBy(admin);
        kakao3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao3);

        // 4. 사진 및 동영상 전송
        PublicArchive kakao4 = new PublicArchive();
        kakao4.setTitle("카카오톡으로 사진과 동영상 보내기");
        kakao4.setDescription("스마트폰에 저장된 사진이나 동영상을 친구에게 보내는 방법을 설명합니다.");
        kakao4.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao4.setSubCategory("메시지");
        kakao4.setUploadedBy(admin);
        kakao4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao4);

        // === KTX 예매 ===
        // 1. 코레일톡 설치 및 회원가입
        PublicArchive ktx1 = new PublicArchive();
        ktx1.setTitle("코레일톡 앱 설치 및 회원가입");
        ktx1.setDescription("KTX 예매를 위한 코레일톡 앱을 설치하고 회원가입하는 과정을 안내합니다.");
        ktx1.setCategory(ArchiveCategory.KTX);
        ktx1.setSubCategory("설치와 가입");
        ktx1.setUploadedBy(admin);
        ktx1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx1);

        // 2. 기차표 예매하기
        PublicArchive ktx2 = new PublicArchive();
        ktx2.setTitle("KTX 기차표 예매하기");
        ktx2.setDescription("코레일톡 앱으로 출발지와 도착지를 선택하고 원하는 시간의 기차표를 예매하는 방법을 설명합니다.");
        ktx2.setCategory(ArchiveCategory.KTX);
        ktx2.setSubCategory("예매");
        ktx2.setUploadedBy(admin);
        ktx2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx2);

        // 3. 결제하기
        PublicArchive ktx3 = new PublicArchive();
        ktx3.setTitle("KTX 예매 결제하기");
        ktx3.setDescription("예매한 기차표를 신용카드나 간편결제로 결제하는 방법을 안내합니다.");
        ktx3.setCategory(ArchiveCategory.KTX);
        ktx3.setSubCategory("예매");
        ktx3.setUploadedBy(admin);
        ktx3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx3);

        // 4. 모바일 승차권 확인
        PublicArchive ktx4 = new PublicArchive();
        ktx4.setTitle("KTX 모바일 승차권 확인 및 사용법");
        ktx4.setDescription("결제가 완료된 모바일 승차권을 확인하고, 기차에 탑승할 때 사용하는 방법을 설명합니다.");
        ktx4.setCategory(ArchiveCategory.KTX);
        ktx4.setSubCategory("승차권");
        ktx4.setUploadedBy(admin);
        ktx4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx4);


        // === 유튜브 ===
        // 1. 유튜브 설치 및 둘러보기
        PublicArchive youtube1 = new PublicArchive();
        youtube1.setTitle("유튜브 앱 설치 및 둘러보기");
        youtube1.setDescription("유튜브 앱을 설치하고, 첫 화면의 다양한 메뉴들을 살펴보는 방법을 안내합니다.");
        youtube1.setCategory(ArchiveCategory.YOUTUBE);
        youtube1.setSubCategory("기본 사용법");
        youtube1.setUploadedBy(admin);
        youtube1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube1);

        // 2. 동영상 검색하기
        PublicArchive youtube2 = new PublicArchive();
        youtube2.setTitle("유튜브에서 동영상 검색하기");
        youtube2.setDescription("보고 싶은 동영상을 키워드로 검색하고 찾는 방법을 설명합니다.");
        youtube2.setCategory(ArchiveCategory.YOUTUBE);
        youtube2.setSubCategory("검색");
        youtube2.setUploadedBy(admin);
        youtube2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube2);

        // 3. 동영상 재생 및 제어
        PublicArchive youtube3 = new PublicArchive();
        youtube3.setTitle("유튜브 동영상 재생 및 제어하기");
        youtube3.setDescription("동영상을 재생하고, 일시정지, 빨리감기, 음량 조절 등 재생을 제어하는 방법을 알려드립니다.");
        youtube3.setCategory(ArchiveCategory.YOUTUBE);
        youtube3.setSubCategory("재생");
        youtube3.setUploadedBy(admin);
        youtube3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube3);

        // 4. 채널 구독과 좋아요
        PublicArchive youtube4 = new PublicArchive();
        youtube4.setTitle("유튜브 채널 구독과 좋아요");
        youtube4.setDescription("마음에 드는 채널을 구독하고, 재미있게 본 동영상에 '좋아요'를 누르는 방법을 설명합니다.");
        youtube4.setCategory(ArchiveCategory.YOUTUBE);
        youtube4.setSubCategory("구독과 좋아요");
        youtube4.setUploadedBy(admin);
        youtube4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube4);

        // === 시외버스 예매 ===
        // 1. 시외버스 예매 앱 설치 및 가입
        PublicArchive bus1 = new PublicArchive();
        bus1.setTitle("시외버스 예매 앱 설치 및 가입");
        bus1.setDescription("시외버스 예매를 위한 '버스타고' 앱을 설치하고 회원가입하는 방법을 안내합니다.");
        bus1.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus1.setSubCategory("설치와 가입");
        bus1.setUploadedBy(admin);
        bus1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus1);

        // 2. 시외버스 노선 및 시간표 조회
        PublicArchive bus2 = new PublicArchive();
        bus2.setTitle("시외버스 노선 및 시간표 조회");
        bus2.setDescription("가고 싶은 지역의 버스 노선과 시간표를 확인하는 방법을 설명합니다.");
        bus2.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus2.setSubCategory("조회");
        bus2.setUploadedBy(admin);
        bus2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus2);

        // 3. 시외버스 좌석 선택 및 예매
        PublicArchive bus3 = new PublicArchive();
        bus3.setTitle("시외버스 좌석 선택 및 예매");
        bus3.setDescription("원하는 버스편의 좌석을 선택하고 예매하는 과정을 안내합니다.");
        bus3.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus3.setSubCategory("예매");
        bus3.setUploadedBy(admin);
        bus3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus3);

        // === 배달의민족 ===
        // 1. 배달의민족 앱 설치 및 주소 설정
        PublicArchive baemin1 = new PublicArchive();
        baemin1.setTitle("배달의민족 앱 설치 및 주소 설정");
        baemin1.setDescription("배달의민족 앱을 설치하고, 음식을 받을 주소를 설정하는 방법을 설명합니다.");
        baemin1.setCategory(ArchiveCategory.BAEMIN);
        baemin1.setSubCategory("설치와 설정");
        baemin1.setUploadedBy(admin);
        baemin1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin1);

        // 2. 배달의민족 음식점 및 메뉴 탐색
        PublicArchive baemin2 = new PublicArchive();
        baemin2.setTitle("배달의민족 음식점 및 메뉴 탐색");
        baemin2.setDescription("주변 음식점을 찾아보고, 원하는 메뉴를 선택하는 방법을 안내합니다.");
        baemin2.setCategory(ArchiveCategory.BAEMIN);
        baemin2.setSubCategory("탐색");
        baemin2.setUploadedBy(admin);
        baemin2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin2);

        // 3. 배달의민족 주문 및 결제
        PublicArchive baemin3 = new PublicArchive();
        baemin3.setTitle("배달의민족 주문 및 결제");
        baemin3.setDescription("선택한 메뉴를 주문하고, 다양한 방법으로 결제하는 과정을 설명합니다.");
        baemin3.setCategory(ArchiveCategory.BAEMIN);
        baemin3.setSubCategory("주문과 결제");
        baemin3.setUploadedBy(admin);
        baemin3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin3);

        // === 쿠팡 ===
        // 1. 쿠팡 앱 설치 및 회원가입
        PublicArchive coupang1 = new PublicArchive();
        coupang1.setTitle("쿠팡 앱 설치 및 회원가입");
        coupang1.setDescription("쿠팡 앱을 설치하고 로켓배송을 이용하기 위해 회원가입하는 방법을 안내합니다.");
        coupang1.setCategory(ArchiveCategory.COUPANG);
        coupang1.setSubCategory("설치와 가입");
        coupang1.setUploadedBy(admin);
        coupang1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang1);

        // 2. 쿠팡 상품 검색 및 상세 정보 확인
        PublicArchive coupang2 = new PublicArchive();
        coupang2.setTitle("쿠팡 상품 검색 및 상세 정보 확인");
        coupang2.setDescription("사고 싶은 물건을 검색하고, 상품의 상세 정보와 다른 사람들의 후기를 확인하는 방법을 설명합니다.");
        coupang2.setCategory(ArchiveCategory.COUPANG);
        coupang2.setSubCategory("검색");
        coupang2.setUploadedBy(admin);
        coupang2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang2);

        // 3. 쿠팡 장바구니 담기 및 주문
        PublicArchive coupang3 = new PublicArchive();
        coupang3.setTitle("쿠팡 장바구니 담기 및 주문");
        coupang3.setDescription("원하는 상품을 장바구니에 담고, 배송지와 결제 정보를 입력하여 주문하는 과정을 안내합니다.");
        coupang3.setCategory(ArchiveCategory.COUPANG);
        coupang3.setSubCategory("주문");
        coupang3.setUploadedBy(admin);
        coupang3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang3);
    }

}