# Terraform으로 MOCI 3D 인프라 구축

이 디렉토리는 AWS 인프라를 Terraform으로 관리합니다.

## 📋 생성되는 리소스

- **VPC**: 10.0.0.0/16
- **Subnets**: Public 2개, Private 2개
- **EC2**: t3.micro (Ubuntu 22.04, Docker 자동 설치)
- **RDS**: MySQL 8.0 (db.t3.micro, 단일 AZ)
- **S3**: 파일 저장용 버킷
- **Elastic IP**: EC2 고정 IP (도메인 연결용)
- **Security Groups**: EC2용, RDS용

## 🚀 사용 방법

### 1. 사전 준비

#### 1-1. Terraform 설치 확인
```bash
terraform --version
```

#### 1-2. AWS CLI 설정 확인
```bash
aws configure list
aws sts get-caller-identity
```

#### 1-3. SSH 키 페어 생성
```bash
# 키 페어 생성
ssh-keygen -t rsa -b 4096 -f ~/.ssh/moci-3d-ec2-key -C "moci-3d-key"

# 권한 설정
chmod 400 ~/.ssh/moci-3d-ec2-key
chmod 644 ~/.ssh/moci-3d-ec2-key.pub
```

### 2. 변수 파일 설정

```bash
# 예시 파일을 복사
cp terraform.tfvars.example terraform.tfvars

# 편집기로 열기
vim terraform.tfvars
```

**필수 수정 항목:**
- `my_ip`: 본인의 IP 주소 (https://www.whatismyip.com/)
- `db_password`: 강력한 비밀번호 (최소 8자, 영문+숫자+특수문자)

### 3. Terraform 초기화

```bash
cd terraform
terraform init
```

### 4. 실행 계획 확인

```bash
terraform plan
```

이 명령어는 실제로 생성될 리소스를 미리 보여줍니다. (약 30개의 리소스)

### 5. 인프라 생성

```bash
terraform apply
```

- 실행 전 계획을 다시 보여줍니다.
- `yes`를 입력하면 실제로 생성됩니다.
- **소요 시간: 약 10-15분** (RDS 생성이 오래 걸림)

### 6. 출력 값 확인

```bash
terraform output
```

생성된 리소스의 정보를 확인할 수 있습니다:
- EC2 퍼블릭 IP
- RDS 엔드포인트
- S3 버킷 이름
- SSH 접속 명령어
- .env 파일 템플릿

### 7. SSH 접속

```bash
# Terraform output에서 확인한 명령어 사용
ssh -i ~/.ssh/moci-3d-ec2-key.pem ubuntu@<EC2_PUBLIC_IP>
```

## 🔄 인프라 업데이트

코드를 수정한 후:

```bash
terraform plan   # 변경 사항 확인
terraform apply  # 적용
```

## 🗑️ 인프라 삭제

**⚠️ 주의: 모든 리소스가 삭제됩니다!**

```bash
terraform destroy
```

## 📂 파일 구조

```
terraform/
├── .gitignore                # Git에서 제외할 파일
├── README.md                 # 이 파일
├── provider.tf               # Terraform 및 Provider 설정
├── variables.tf              # 변수 정의
├── terraform.tfvars.example  # 변수 값 예시
├── terraform.tfvars          # 실제 변수 값 (Git 제외)
├── vpc.tf                    # VPC, Subnet, Route Table
├── security_groups.tf        # Security Group
├── ec2.tf                    # EC2 인스턴스
├── rds.tf                    # RDS MySQL
├── s3.tf                     # S3 버킷
└── outputs.tf                # 출력 값

생성되는 파일 (Git 제외):
├── .terraform/               # Terraform 플러그인
├── .terraform.lock.hcl       # 의존성 잠금
├── terraform.tfstate         # 현재 상태
└── terraform.tfstate.backup  # 이전 상태
```

## 🔐 보안 주의사항

**절대 Git에 커밋하면 안 되는 파일:**
- `terraform.tfvars` (DB 비밀번호 등 포함)
- `*.tfstate` (리소스 상세 정보 포함)
- `*.pem` (SSH 키)

이 파일들은 `.gitignore`에 포함되어 있습니다.

## 💰 비용 관리

**프리티어 범위 내 설정:**
- EC2: t3.micro (월 750시간 무료)
- RDS: db.t3.micro, 단일 AZ (월 750시간 무료)
- S3: 5GB 저장소 무료
- EBS: 30GB 무료

**⚠️ 프리티어 초과 시 비용 발생!**

## 🐛 문제 해결

### EC2 접속 실패
```bash
# 키 권한 확인
chmod 400 ~/.ssh/moci-3d-ec2-key.pem

# Security Group에 내 IP가 등록되었는지 확인
terraform output
```

### RDS 생성 실패
- DB 비밀번호가 AWS 요구사항을 만족하는지 확인 (최소 8자)
- VPC 설정이 올바른지 확인

### S3 버킷 이름 충돌
- S3 버킷 이름은 전역적으로 고유해야 함
- `terraform.tfvars`에서 `s3_bucket_name` 수정

## 📚 다음 단계

1. **EC2 접속 확인**
2. **애플리케이션 배포** (Phase B)
3. **GitHub Actions CI/CD 설정** (Phase C)
4. **도메인 연결 및 HTTPS** (Phase D)
