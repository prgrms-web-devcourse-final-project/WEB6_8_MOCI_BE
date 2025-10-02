# ============================================
# RDS MySQL 인스턴스
# ============================================

# DB Subnet Group (RDS를 여러 AZ에 분산)
resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-db-subnet-group"
  subnet_ids = aws_subnet.private[*].id

  tags = {
    Name = "${var.project_name}-db-subnet-group"
  }
}

# RDS Parameter Group (MySQL 8.0 설정)
resource "aws_db_parameter_group" "main" {
  name   = "${var.project_name}-mysql8-params"
  family = "mysql8.0"

  parameter {
    name  = "character_set_server"
    value = "utf8mb4"
  }

  parameter {
    name  = "collation_server"
    value = "utf8mb4_unicode_ci"
  }

  parameter {
    name  = "time_zone"
    value = "Asia/Seoul"
  }

  parameter {
    name  = "max_connections"
    value = "100"
  }

  tags = {
    Name = "${var.project_name}-mysql8-params"
  }
}

# RDS 인스턴스
resource "aws_db_instance" "main" {
  identifier = "${var.project_name}-mysql"

  # 엔진 설정
  engine         = "mysql"
  engine_version = "8.0.39"  # 최신 안정 버전
  instance_class = var.db_instance_class

  # 스토리지 설정
  allocated_storage     = var.db_allocated_storage
  max_allocated_storage = 100 # 자동 스케일링 최대값
  storage_type          = "gp3"
  storage_encrypted     = true

  # 데이터베이스 설정
  db_name  = var.db_name
  username = var.db_username
  password = var.db_password
  port     = 3306

  # 네트워크 설정
  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = false # 보안: 인터넷에서 직접 접근 불가

  # 파라미터 그룹
  parameter_group_name = aws_db_parameter_group.main.name

  # 백업 설정
  backup_retention_period = 7    # 7일간 백업 보관
  backup_window           = "03:00-04:00" # UTC 기준 (한국 시간 12:00-13:00)
  maintenance_window      = "mon:04:00-mon:05:00"

  # 고가용성 설정 (프리티어에서는 단일 AZ)
  multi_az = false # ⚠️ true로 설정하면 프리티어 초과!

  # 모니터링
  enabled_cloudwatch_logs_exports = ["error", "general", "slowquery"]
  monitoring_interval             = 60 # CloudWatch 모니터링 (60초 간격)
  monitoring_role_arn             = aws_iam_role.rds_monitoring.arn

  # 삭제 보호 (운영 환경에서는 true 권장)
  deletion_protection = false # 테스트 중에는 false
  skip_final_snapshot = true  # 삭제 시 최종 스냅샷 생략 (테스트용)

  # 태그
  tags = {
    Name = "${var.project_name}-mysql"
  }

  # RDS는 생성에 시간이 오래 걸림
  timeouts {
    create = "40m"
    update = "80m"
    delete = "40m"
  }
}

# ============================================
# IAM Role for RDS Enhanced Monitoring
# ============================================

resource "aws_iam_role" "rds_monitoring" {
  name = "${var.project_name}-rds-monitoring-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "monitoring.rds.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "${var.project_name}-rds-monitoring-role"
  }
}

resource "aws_iam_role_policy_attachment" "rds_monitoring" {
  role       = aws_iam_role.rds_monitoring.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonRDSEnhancedMonitoringRole"
}
