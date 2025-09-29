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
import com.moci_3d_backend.global.util.PasswordUtil;
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
@Profile("!prod") // 테스트 환경에서도 실행되도록 변경
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
        admin.setPassword(PasswordUtil.encode("admin123"));
        admin.setRefreshToken(admin.getUserId());
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
        mentor1.setPassword(PasswordUtil.encode("mentor123"));
        mentor1.setRefreshToken(mentor1.getUserId());
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
        mentor2.setPassword(PasswordUtil.encode("mentor123"));
        mentor2.setRefreshToken(mentor2.getUserId());
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
        user1.setPassword(PasswordUtil.encode("user123"));
        user1.setRefreshToken(user1.getUserId());
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
        user2.setPassword(PasswordUtil.encode("pass123"));
        user2.setRefreshToken(user2.getUserId());
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
        user3.setPassword(PasswordUtil.encode("qwer123"));
        user3.setRefreshToken(user3.getUserId());
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
        user4.setPassword(PasswordUtil.encode("asdf123"));
        user4.setRefreshToken(user4.getUserId());
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
        user5.setPassword(PasswordUtil.encode("zxcv123"));
        user5.setRefreshToken(user5.getUserId());
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
        user6.setPassword(PasswordUtil.encode("123456"));
        user6.setRefreshToken(user6.getUserId());
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
        user7.setPassword(PasswordUtil.encode("password"));
        user7.setRefreshToken(user7.getUserId());
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
        kakao1.setDescription("스마트폰에 카카오톡 앱을 설치하고 회원가입하는 방법을 단계별로 안내합니다. 구글 플레이스토어 또는 애플 앱스토어에서 '카카오톡'을 검색하여 설치를 시작하세요. 설치가 완료되면 앱을 열고 전화번호 인증을 통해 간단하게 가입할 수 있습니다. 프로필 사진과 이름을 설정하여 친구들에게 나를 알려보세요.");
        kakao1.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao1.setSubCategory("설치와 가입");
        kakao1.setUploadedBy(admin);
        kakao1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao1);

        // 2. 친구 추가
        PublicArchive kakao2 = new PublicArchive();
        kakao2.setTitle("카카오톡 친구 추가하기");
        kakao2.setDescription("전화번호, QR코드, ID로 친구를 추가하는 다양한 방법을 설명합니다. 연락처에 저장된 친구는 자동으로 목록에 추가됩니다. 친구의 QR코드를 스캔하거나, 카카오톡 ID를 검색하여 새로운 친구를 추가할 수 있습니다. 단체 채팅방에서 모르는 사람을 친구로 추가할 수도 있습니다.");
        kakao2.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao2.setSubCategory("친구 관리");
        kakao2.setUploadedBy(admin);
        kakao2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao2);

        // 3. 메시지 보내기
        PublicArchive kakao3 = new PublicArchive();
        kakao3.setTitle("카카오톡 메시지 보내고 받기");
        kakao3.setDescription("친구에게 텍스트 메시지를 보내고 확인하는 기본 방법을 알려드립니다. 친구 목록에서 대화할 상대를 선택하고, 하단의 입력창에 메시지를 작성한 후 보내기 버튼을 누르세요. 친구에게 온 메시지는 채팅 목록에 표시되며, 클릭하여 내용을 확인할 수 있습니다. 이모티콘을 사용하여 감정을 표현해보세요.");
        kakao3.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao3.setSubCategory("메시지");
        kakao3.setUploadedBy(admin);
        kakao3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao3);

        // 4. 사진 및 동영상 전송
        PublicArchive kakao4 = new PublicArchive();
        kakao4.setTitle("카카오톡으로 사진과 동영상 보내기");
        kakao4.setDescription("스마트폰에 저장된 사진이나 동영상을 친구에게 보내는 방법을 설명합니다. 채팅방 하단의 '+' 버튼을 누르고 '앨범'을 선택하세요. 갤러리에서 보내고 싶은 사진이나 동영상을 선택한 후 전송 버튼을 누르면 됩니다. 여러 장을 한 번에 보낼 수도 있습니다.");
        kakao4.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao4.setSubCategory("메시지");
        kakao4.setUploadedBy(admin);
        kakao4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao4);

        // === KTX 예매 ===
        // 1. 코레일톡 설치 및 회원가입
        PublicArchive ktx1 = new PublicArchive();
        ktx1.setTitle("코레일톡 앱 설치 및 회원가입");
        ktx1.setDescription("KTX 예매를 위한 코레일톡 앱을 설치하고 회원가입하는 과정을 안내합니다. '코레일톡' 앱을 다운로드 받은 후, 회원가입 버튼을 눌러 약관에 동의하고 정보를 입력하세요. 자주 사용하는 신용카드를 등록해두면 다음 결제부터 더 편리합니다.");
        ktx1.setCategory(ArchiveCategory.KTX);
        ktx1.setSubCategory("설치와 가입");
        ktx1.setUploadedBy(admin);
        ktx1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx1);

        // 2. 기차표 예매하기
        PublicArchive ktx2 = new PublicArchive();
        ktx2.setTitle("KTX 기차표 예매하기");
        ktx2.setDescription("코레일톡 앱으로 출발지와 도착지를 선택하고 원하는 시간의 기차표를 예매하는 방법을 설명합니다. '편도' 또는 '왕복'을 선택하고, 출발역, 도착역, 가는 날을 달력에서 선택하세요. 승객 연령 및 인원 수를 정확히 입력해야 합니다.");
        ktx2.setCategory(ArchiveCategory.KTX);
        ktx2.setSubCategory("예매");
        ktx2.setUploadedBy(admin);
        ktx2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx2);

        // 3. 결제하기
        PublicArchive ktx3 = new PublicArchive();
        ktx3.setTitle("KTX 예매 결제하기");
        ktx3.setDescription("예매한 기차표를 신용카드나 간편결제로 결제하는 방법을 안내합니다. 좌석까지 선택한 후 '예매' 버튼을 누르면 결제 화면으로 넘어갑니다. 등록된 카드를 사용하거나, 다른 결제수단을 선택하여 결제를 완료하세요. 할인 쿠폰이 있다면 잊지 말고 적용하세요.");
        ktx3.setCategory(ArchiveCategory.KTX);
        ktx3.setSubCategory("예매");
        ktx3.setUploadedBy(admin);
        ktx3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx3);

        // 4. 모바일 승차권 확인
        PublicArchive ktx4 = new PublicArchive();
        ktx4.setTitle("KTX 모바일 승차권 확인 및 사용법");
        ktx4.setDescription("결제가 완료된 모바일 승차권을 확인하고, 기차에 탑승할 때 사용하는 방법을 설명합니다. 예매 내역은 '승차권 확인' 메뉴에서 볼 수 있습니다. 기차에 탑승할 때는 이 모바일 승차권의 QR코드를 승무원에게 보여주면 됩니다. 종이로 출력할 필요가 없어 편리합니다.");
        ktx4.setCategory(ArchiveCategory.KTX);
        ktx4.setSubCategory("승차권");
        ktx4.setUploadedBy(admin);
        ktx4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(ktx4);


        // === 유튜브 ===
        // 1. 유튜브 설치 및 둘러보기
        PublicArchive youtube1 = new PublicArchive();
        youtube1.setTitle("유튜브 앱 설치 및 둘러보기");
        youtube1.setDescription("유튜브 앱을 설치하고, 첫 화면의 다양한 메뉴들을 살펴보는 방법을 안내합니다. 앱을 열면 추천 동영상, 구독 채널의 새 동영상 등이 표시됩니다. 하단의 '홈', '탐색', '구독', '보관함' 메뉴를 통해 다양한 기능에 접근할 수 있습니다.");
        youtube1.setCategory(ArchiveCategory.YOUTUBE);
        youtube1.setSubCategory("기본 사용법");
        youtube1.setUploadedBy(admin);
        youtube1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube1);

        // 2. 동영상 검색하기
        PublicArchive youtube2 = new PublicArchive();
        youtube2.setTitle("유튜브에서 동영상 검색하기");
        youtube2.setDescription("보고 싶은 동영상을 키워드로 검색하고 찾는 방법을 설명합니다. 상단의 돋보기 아이콘을 누르고, 가수 이름, 노래 제목, 유튜버 이름 등 원하는 검색어를 입력하세요. 필터 기능을 사용하면 업로드 날짜, 길이, 관련성 등으로 검색 결과를 정렬할 수 있습니다.");
        youtube2.setCategory(ArchiveCategory.YOUTUBE);
        youtube2.setSubCategory("검색");
        youtube2.setUploadedBy(admin);
        youtube2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube2);

        // 3. 동영상 재생 및 제어
        PublicArchive youtube3 = new PublicArchive();
        youtube3.setTitle("유튜브 동영상 재생 및 제어하기");
        youtube3.setDescription("동영상을 재생하고, 일시정지, 빨리감기, 음량 조절 등 재생을 제어하는 방법을 알려드립니다. 화면을 두 번 탭하여 10초 앞뒤로 이동할 수 있습니다. 전체 화면으로 보려면 화면 오른쪽 하단의 사각형 아이콘을 누르세요. 자막이 있는 영상은 'CC' 버튼으로 자막을 켜고 끌 수 있습니다.");
        youtube3.setCategory(ArchiveCategory.YOUTUBE);
        youtube3.setSubCategory("재생");
        youtube3.setUploadedBy(admin);
        youtube3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube3);

        // 4. 채널 구독과 좋아요
        PublicArchive youtube4 = new PublicArchive();
        youtube4.setTitle("유튜브 채널 구독과 좋아요");
        youtube4.setDescription("마음에 드는 채널을 구독하고, 재미있게 본 동영상에 '좋아요'를 누르는 방법을 설명합니다. 채널을 구독하면 해당 채널의 새 영상이 '구독' 탭에 표시됩니다. '좋아요'를 누른 동영상은 '보관함'의 '좋아요 표시한 동영상' 목록에서 다시 볼 수 있습니다.");
        youtube4.setCategory(ArchiveCategory.YOUTUBE);
        youtube4.setSubCategory("구독과 좋아요");
        youtube4.setUploadedBy(admin);
        youtube4.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(youtube4);

        // === 시외버스 예매 ===
        // 1. 시외버스 예매 앱 설치 및 가입
        PublicArchive bus1 = new PublicArchive();
        bus1.setTitle("시외버스 예매 앱 설치 및 가입");
        bus1.setDescription("시외버스 예매를 위한 '버스타고' 또는 '시외버스 티머니' 앱을 설치하고 회원가입하는 방법을 안내합니다. 앱 스토어에서 검색하여 설치 후, 휴대폰 인증 등을 통해 간편하게 가입할 수 있습니다.");
        bus1.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus1.setSubCategory("설치와 가입");
        bus1.setUploadedBy(admin);
        bus1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus1);

        // 2. 시외버스 노선 및 시간표 조회
        PublicArchive bus2 = new PublicArchive();
        bus2.setTitle("시외버스 노선 및 시간표 조회");
        bus2.setDescription("가고 싶은 지역의 버스 노선과 시간표를 확인하는 방법을 설명합니다. 출발지와 도착지를 선택하고 날짜를 지정하면, 해당 날짜에 운행하는 버스 목록과 출발 시간, 잔여 좌석 등을 확인할 수 있습니다.");
        bus2.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus2.setSubCategory("조회");
        bus2.setUploadedBy(admin);
        bus2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus2);

        // 3. 시외버스 좌석 선택 및 예매
        PublicArchive bus3 = new PublicArchive();
        bus3.setTitle("시외버스 좌석 선택 및 예매");
        bus3.setDescription("원하는 버스편의 좌석을 선택하고 예매하는 과정을 안내합니다. 버스 좌석 배치도를 보고 원하는 자리를 선택하세요. 창가, 복도 등 선호하는 위치를 고를 수 있습니다. 성인, 아동 등 탑승 인원을 정확히 선택해야 합니다.");
        bus3.setCategory(ArchiveCategory.INTERCITY_BUS);
        bus3.setSubCategory("예매");
        bus3.setUploadedBy(admin);
        bus3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(bus3);

        // === 배달의민족 ===
        // 1. 배달의민족 앱 설치 및 주소 설정
        PublicArchive baemin1 = new PublicArchive();
        baemin1.setTitle("배달의민족 앱 설치 및 주소 설정");
        baemin1.setDescription("배달의민족 앱을 설치하고, 음식을 받을 주소를 설정하는 방법을 설명합니다. 정확한 주소를 입력해야 배달이 가능하니, 아파트 동, 호수까지 상세하게 입력해주세요. 회사나 집 등 여러 주소를 등록해놓고 선택할 수 있습니다.");
        baemin1.setCategory(ArchiveCategory.BAEMIN);
        baemin1.setSubCategory("설치와 설정");
        baemin1.setUploadedBy(admin);
        baemin1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin1);

        // 2. 배달의민족 음식점 및 메뉴 탐색
        PublicArchive baemin2 = new PublicArchive();
        baemin2.setTitle("배달의민족 음식점 및 메뉴 탐색");
        baemin2.setDescription("주변 음식점을 찾아보고, 원하는 메뉴를 선택하는 방법을 안내합니다. 한식, 중식, 치킨 등 카테고리별로 보거나, 검색창에 직접 음식 이름을 입력하여 찾을 수 있습니다. '최소주문금액'과 '배달팁'을 확인하는 것이 좋습니다.");
        baemin2.setCategory(ArchiveCategory.BAEMIN);
        baemin2.setSubCategory("탐색");
        baemin2.setUploadedBy(admin);
        baemin2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin2);

        // 3. 배달의민족 주문 및 결제
        PublicArchive baemin3 = new PublicArchive();
        baemin3.setTitle("배달의민족 주문 및 결제");
        baemin3.setDescription("선택한 메뉴를 주문하고, 다양한 방법으로 결제하는 과정을 설명합니다. 메뉴와 옵션을 모두 선택했다면 '장바구니'에 담아 주문하세요. '만나서 결제' 또는 '앱에서 바로 결제' 등 다양한 결제 방법을 지원합니다.");
        baemin3.setCategory(ArchiveCategory.BAEMIN);
        baemin3.setSubCategory("주문과 결제");
        baemin3.setUploadedBy(admin);
        baemin3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(baemin3);

        // === 쿠팡 ===
        // 1. 쿠팡 앱 설치 및 회원가입
        PublicArchive coupang1 = new PublicArchive();
        coupang1.setTitle("쿠팡 앱 설치 및 회원가입");
        coupang1.setDescription("쿠팡 앱을 설치하고 로켓배송을 이용하기 위해 회원가입하는 방법을 안내합니다. 회원가입 시 로켓와우 멤버십에 가입하면 무료 배송, 무료 반품 등 더 많은 혜택을 누릴 수 있습니다.");
        coupang1.setCategory(ArchiveCategory.COUPANG);
        coupang1.setSubCategory("설치와 가입");
        coupang1.setUploadedBy(admin);
        coupang1.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang1);

        // 2. 쿠팡 상품 검색 및 상세 정보 확인
        PublicArchive coupang2 = new PublicArchive();
        coupang2.setTitle("쿠팡 상품 검색 및 상세 정보 확인");
        coupang2.setDescription("사고 싶은 물건을 검색하고, 상품의 상세 정보와 다른 사람들의 후기를 확인하는 방법을 설명합니다. 다른 구매자들이 남긴 리뷰와 별점은 상품을 선택하는 데 큰 도움이 됩니다. '상품 문의'를 통해 궁금한 점을 판매자에게 직접 물어볼 수도 있습니다.");
        coupang2.setCategory(ArchiveCategory.COUPANG);
        coupang2.setSubCategory("검색");
        coupang2.setUploadedBy(admin);
        coupang2.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang2);

        // 3. 쿠팡 장바구니 담기 및 주문
        PublicArchive coupang3 = new PublicArchive();
        coupang3.setTitle("쿠팡 장바구니 담기 및 주문");
        coupang3.setDescription("원하는 상품을 장바구니에 담고, 배송지와 결제 정보를 입력하여 주문하는 과정을 안내합니다. 여러 상품을 한 번에 주문할 수 있으며, '로켓배송' 상품은 다음 날 바로 받아볼 수 있습니다. 최종 결제 전에 주소와 연락처가 맞는지 다시 한번 확인하세요.");
        coupang3.setCategory(ArchiveCategory.COUPANG);
        coupang3.setSubCategory("주문");
        coupang3.setUploadedBy(admin);
        coupang3.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(coupang3);

        // === 카카오톡 추가 기능 ===
        // 5. 보이스톡 / 페이스톡
        PublicArchive kakao5 = new PublicArchive();
        kakao5.setTitle("카카오톡 보이스톡/페이스톡 사용하기");
        kakao5.setDescription("데이터를 사용하여 친구와 무료로 음성통화(보이스톡) 또는 영상통화(페이스톡)하는 방법을 안내합니다. 친구 프로필이나 채팅방에서 전화기 아이콘을 눌러 시작할 수 있습니다.");
        kakao5.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao5.setSubCategory("통화 기능");
        kakao5.setUploadedBy(admin);
        kakao5.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao5);

        // 6. 프로필 꾸미기
        PublicArchive kakao6 = new PublicArchive();
        kakao6.setTitle("카카오톡 프로필 꾸미기");
        kakao6.setDescription("프로필 사진과 배경사진, 상태메시지를 변경하여 나를 표현하는 방법을 설명합니다. 친구들에게 내 소식을 알리고 개성을 표현해보세요.");
        kakao6.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao6.setSubCategory("프로필");
        kakao6.setUploadedBy(admin);
        kakao6.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao6);

        // 7. 친구 관리 (차단, 숨김)
        PublicArchive kakao7 = new PublicArchive();
        kakao7.setTitle("카카오톡 친구 관리하기 (차단, 숨김)");
        kakao7.setDescription("원치 않는 메시지를 받지 않도록 친구를 차단하거나, 친구 목록에서 보이지 않게 숨기는 방법을 안내합니다. 설정 메뉴에서 관리할 수 있습니다.");
        kakao7.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao7.setSubCategory("친구 관리");
        kakao7.setUploadedBy(admin);
        kakao7.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao7);

        // 8. 이모티콘 사용법
        PublicArchive kakao8 = new PublicArchive();
        kakao8.setTitle("카카오톡 이모티콘 구매하고 사용하기");
        kakao8.setDescription("대화를 더 재미있게 만들어주는 이모티콘을 이모티콘 스토어에서 구매하거나 친구에게 선물하는 방법을 설명합니다. 다양한 이모티콘으로 감정을 표현해보세요.");
        kakao8.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao8.setSubCategory("메시지");
        kakao8.setUploadedBy(admin);
        kakao8.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao8);

        // 9. 카카오톡 채널
        PublicArchive kakao9 = new PublicArchive();
        kakao9.setTitle("카카오톡 채널 추가하고 소식 받기");
        kakao9.setDescription("좋아하는 브랜드나 언론사의 카카오톡 채널을 추가하고, 할인 정보나 새로운 소식을 메시지로 받아보는 방법을 안내합니다.");
        kakao9.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao9.setSubCategory("편의 기능");
        kakao9.setUploadedBy(admin);
        kakao9.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao9);

        // 10. 카카오페이 송금
        PublicArchive kakao10 = new PublicArchive();
        kakao10.setTitle("카카오페이로 송금하고 결제하기");
        kakao10.setDescription("은행 앱 없이 카카오톡에서 친구에게 간편하게 돈을 보내거나, 온라인 쇼핑몰에서 결제하는 방법을 설명합니다. 카카오페이에 계좌를 연결하여 사용할 수 있습니다.");
        kakao10.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao10.setSubCategory("금융");
        kakao10.setUploadedBy(admin);
        kakao10.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao10);

        // 11. 선물하기
        PublicArchive kakao11 = new PublicArchive();
        kakao11.setTitle("카카오톡으로 선물 보내기");
        kakao11.setDescription("친구의 생일이나 특별한 날, 커피 쿠폰이나 케이크 등 다양한 선물을 보내는 방법을 안내합니다. 주소를 몰라도 친구가 직접 입력하게 할 수 있어 편리합니다.");
        kakao11.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao11.setSubCategory("편의 기능");
        kakao11.setUploadedBy(admin);
        kakao11.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao11);

        // 12. 오픈채팅
        PublicArchive kakao12 = new PublicArchive();
        kakao12.setTitle("카카오톡 오픈채팅 참여하기");
        kakao12.setDescription("나와 같은 관심사를 가진 사람들과 익명으로 대화할 수 있는 오픈채팅방에 참여하는 방법을 설명합니다. 검색을 통해 원하는 주제의 채팅방을 찾을 수 있습니다.");
        kakao12.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao12.setSubCategory("소셜");
        kakao12.setUploadedBy(admin);
        kakao12.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao12);

        // 13. 대화 백업
        PublicArchive kakao13 = new PublicArchive();
        kakao13.setTitle("카카오톡 대화 내용 백업하고 복원하기");
        kakao13.setDescription("휴대폰을 바꾸거나 앱을 다시 설치할 때를 대비하여, 친구들과 나눈 대화 내용을 안전하게 저장(백업)하고 다시 불러오는(복원) 방법을 안내합니다.");
        kakao13.setCategory(ArchiveCategory.KAKAO_TALK);
        kakao13.setSubCategory("설정");
        kakao13.setUploadedBy(admin);
        kakao13.setFileUploads(Collections.emptyList());
        publicArchiveRepository.save(kakao13);
    }

    @Transactional
    public void aiRoomInit() {
        if (aiChatRoomRepository.count() > 0) {
            return;
        }

        aiChatRoomService.create("샘플 AI 채팅방1");
        aiChatRoomService.create("샘플 AI 채팅방2");
        aiChatRoomService.create("샘플 AI 채팅방3");

    }

}