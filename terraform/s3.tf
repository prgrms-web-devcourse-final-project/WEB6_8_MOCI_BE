# ============================================
# S3 버킷 (파일 저장용)
# ============================================

resource "aws_s3_bucket" "main" {
  bucket = "${var.s3_bucket_name}-${var.environment}"

  tags = {
    Name = "${var.project_name}-files"
  }
}

# 버킷 버전 관리
resource "aws_s3_bucket_versioning" "main" {
  bucket = aws_s3_bucket.main.id

  versioning_configuration {
    status = "Enabled" # 파일 버전 관리 활성화 (실수로 삭제 방지)
  }
}

# 버킷 암호화
resource "aws_s3_bucket_server_side_encryption_configuration" "main" {
  bucket = aws_s3_bucket.main.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

# 퍼블릭 액세스 차단 (보안)
resource "aws_s3_bucket_public_access_block" "main" {
  bucket = aws_s3_bucket.main.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# CORS 설정 (프론트엔드에서 직접 업로드/다운로드 시 필요)
resource "aws_s3_bucket_cors_configuration" "main" {
  bucket = aws_s3_bucket.main.id

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["GET", "PUT", "POST", "DELETE", "HEAD"]
    allowed_origins = [
      "http://localhost:3000",           # 로컬 프론트엔드
      "https://www.moci.oa.gg",         # 운영 프론트엔드
      "https://api.moci.oa.gg"          # 백엔드
    ]
    expose_headers  = ["ETag"]
    max_age_seconds = 3000
  }
}

# 수명 주기 정책 (오래된 파일 자동 삭제 - 선택사항)
resource "aws_s3_bucket_lifecycle_configuration" "main" {
  bucket = aws_s3_bucket.main.id

  rule {
    id     = "delete-old-versions"
    status = "Enabled"

    filter {} # 빈 필터 = 모든 객체에 적용

    noncurrent_version_expiration {
      noncurrent_days = 90 # 90일 지난 이전 버전 삭제
    }
  }

  rule {
    id     = "move-to-ia"
    status = "Enabled"

    filter {} # 빈 필터 = 모든 객체에 적용

    transition {
      days          = 90
      storage_class = "STANDARD_IA" # 90일 후 저렴한 스토리지로 이동
    }
  }
}

# ============================================
# IAM Policy for EC2 to access S3
# ============================================

# EC2가 S3에 접근할 수 있는 IAM 역할
resource "aws_iam_role" "ec2_s3_role" {
  name = "${var.project_name}-ec2-s3-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "${var.project_name}-ec2-s3-role"
  }
}

# S3 접근 정책
resource "aws_iam_role_policy" "ec2_s3_policy" {
  name = "${var.project_name}-ec2-s3-policy"
  role = aws_iam_role.ec2_s3_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:ListBucket",
          "s3:GetBucketLocation"
        ]
        Resource = aws_s3_bucket.main.arn
      },
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject",
          "s3:PutObjectAcl"
        ]
        Resource = "${aws_s3_bucket.main.arn}/*"
      }
    ]
  })
}

# EC2 인스턴스 프로파일
resource "aws_iam_instance_profile" "ec2_profile" {
  name = "${var.project_name}-ec2-profile"
  role = aws_iam_role.ec2_s3_role.name

  tags = {
    Name = "${var.project_name}-ec2-profile"
  }
}
