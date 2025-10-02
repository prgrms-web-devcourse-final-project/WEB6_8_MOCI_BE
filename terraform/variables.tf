# ============================================
# 변수 정의
# ============================================

variable "aws_region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "environment" {
  description = "환경 (dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "project_name" {
  description = "프로젝트 이름"
  type        = string
  default     = "moci-3d"
}

# ============================================
# VPC 설정
# ============================================

variable "vpc_cidr" {
  description = "VPC CIDR 블록"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "가용 영역 목록"
  type        = list(string)
  default     = ["ap-northeast-2a", "ap-northeast-2c"]
}

# ============================================
# EC2 설정
# ============================================

variable "ec2_instance_type" {
  description = "EC2 인스턴스 타입"
  type        = string
  default     = "t3.micro"
}

variable "ec2_key_name" {
  description = "EC2 SSH 키 페어 이름"
  type        = string
  default     = "moci-3d-ec2-key"
}

variable "my_ip" {
  description = "SSH 접속을 허용할 내 IP 주소 (CIDR 형식)"
  type        = string
  # 예: "123.456.789.0/32"
  # 실제 값은 terraform.tfvars에서 설정
}

# ============================================
# RDS 설정
# ============================================

variable "db_instance_class" {
  description = "RDS 인스턴스 클래스"
  type        = string
  default     = "db.t3.micro"
}

variable "db_name" {
  description = "데이터베이스 이름"
  type        = string
  default     = "moci3d"
}

variable "db_username" {
  description = "데이터베이스 마스터 사용자명"
  type        = string
  default     = "admin"
}

variable "db_password" {
  description = "데이터베이스 마스터 비밀번호"
  type        = string
  sensitive   = true
  # 실제 값은 terraform.tfvars에서 설정
}

variable "db_allocated_storage" {
  description = "RDS 스토리지 크기 (GB)"
  type        = number
  default     = 20
}

# ============================================
# S3 설정
# ============================================

variable "s3_bucket_name" {
  description = "S3 버킷 이름 (전역적으로 고유해야 함)"
  type        = string
  default     = "moci-3d-files"
}
