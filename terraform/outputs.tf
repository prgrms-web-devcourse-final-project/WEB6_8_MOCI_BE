# ============================================
# 출력 값 (배포 후 필요한 정보)
# ============================================

output "ec2_public_ip" {
  description = "EC2 퍼블릭 IP (Elastic IP)"
  value       = aws_eip.main.public_ip
}

output "ec2_instance_id" {
  description = "EC2 인스턴스 ID"
  value       = aws_instance.main.id
}

output "rds_endpoint" {
  description = "RDS 엔드포인트 (호스트:포트)"
  value       = aws_db_instance.main.endpoint
}

output "rds_address" {
  description = "RDS 호스트 주소만"
  value       = aws_db_instance.main.address
}

output "rds_port" {
  description = "RDS 포트"
  value       = aws_db_instance.main.port
}

output "db_name" {
  description = "데이터베이스 이름"
  value       = aws_db_instance.main.db_name
}

output "db_username" {
  description = "데이터베이스 사용자명"
  value       = aws_db_instance.main.username
  sensitive   = true
}

output "s3_bucket_name" {
  description = "S3 버킷 이름"
  value       = aws_s3_bucket.main.id
}

output "s3_bucket_arn" {
  description = "S3 버킷 ARN"
  value       = aws_s3_bucket.main.arn
}

output "vpc_id" {
  description = "VPC ID"
  value       = aws_vpc.main.id
}

# ============================================
# SSH 접속 명령어 (편의성)
# ============================================

output "ssh_command" {
  description = "SSH 접속 명령어"
  value       = "ssh -i ~/.ssh/${var.ec2_key_name}.pem ubuntu@${aws_eip.main.public_ip}"
}

# ============================================
# .env 파일에 들어갈 내용
# ============================================

output "env_file_template" {
  description = "EC2에 생성할 .env 파일 내용"
  value       = <<-EOT
    # ========================================
    # Spring Profile
    # ========================================
    SPRING_PROFILES_ACTIVE=prod
    
    # ========================================
    # Database (RDS MySQL)
    # ========================================
    DB_HOST=${aws_db_instance.main.address}
    DB_PORT=${aws_db_instance.main.port}
    DB_NAME=${aws_db_instance.main.db_name}
    DB_USERNAME=${aws_db_instance.main.username}
    DB_PASSWORD=${var.db_password}
    
    # ========================================
    # JPA Settings
    # ========================================
    JPA_DDL_AUTO=update
    
    # ========================================
    # AWS S3
    # ========================================
    S3_BUCKET_NAME=${aws_s3_bucket.main.id}
    AWS_REGION=${var.aws_region}
    FILE_STORAGE_TYPE=s3

    # ========================================
    # 아래 값들은 수동으로 추가하세요
    # ========================================
    # JWT_SECRET_KEY=<your-jwt-secret>
    # GEMINI_API_KEY=<your-gemini-key>
    # SPRING__SECURITY__OAUTH2__CLIENT__REGISTRATION__KAKAO__CLIENT_ID=<your-kakao-id>
    # SPRING__SECURITY__OAUTH2__CLIENT__REGISTRATION__NAVER__CLIENT_ID=<your-naver-id>
    # SPRING__SECURITY__OAUTH2__CLIENT__REGISTRATION__NAVER__CLIENT_SECRET=<your-naver-secret>
    # SPRING__SECURITY__OAUTH2__CLIENT__REGISTRATION__GOOGLE__CLIENT_ID=<your-google-id>
    # SPRING__SECURITY__OAUTH2__CLIENT__REGISTRATION__GOOGLE__CLIENT_SECRET=<your-google-secret>
    # GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent
    
    # ========================================
    # Domain & CORS
    # ========================================
    COOKIE_DOMAIN=moci.oa.gg
    FRONT_URL=https://www.moci.oa.gg
    BACK_URL=https://api.moci.oa.gg
  EOT
  sensitive   = true
}
