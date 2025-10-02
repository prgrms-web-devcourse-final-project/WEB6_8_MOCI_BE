# ============================================
# EC2 인스턴스
# ============================================

# 최신 Ubuntu 22.04 AMI 찾기
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical (Ubuntu 공식)

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# Elastic IP (고정 IP, 도메인 연결용)
resource "aws_eip" "main" {
  domain = "vpc"

  tags = {
    Name = "${var.project_name}-eip"
  }
}

# Elastic IP를 EC2에 연결
resource "aws_eip_association" "main" {
  instance_id   = aws_instance.main.id
  allocation_id = aws_eip.main.id
}

# EC2 인스턴스
resource "aws_instance" "main" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.ec2_instance_type
  key_name      = var.ec2_key_name

  subnet_id                   = aws_subnet.public[0].id
  vpc_security_group_ids      = [aws_security_group.ec2.id]
  associate_public_ip_address = true
  iam_instance_profile        = aws_iam_instance_profile.ec2_profile.name

  # 루트 볼륨 설정
  root_block_device {
    volume_type           = "gp3"
    volume_size           = 20
    delete_on_termination = true

    tags = {
      Name = "${var.project_name}-root-volume"
    }
  }

  # User Data: 인스턴스 시작 시 자동 실행
  user_data = <<-EOF
              #!/bin/bash
              set -e
              
              # 시스템 업데이트
              apt-get update
              apt-get upgrade -y
              
              # 필수 패키지 설치
              apt-get install -y \
                curl \
                wget \
                git \
                vim \
                unzip \
                ca-certificates \
                gnupg \
                lsb-release
              
              # Docker 설치
              mkdir -p /etc/apt/keyrings
              curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
              echo \
                "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
                $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
              
              apt-get update
              apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
              
              # Docker 서비스 시작 및 자동 시작 설정
              systemctl start docker
              systemctl enable docker
              
              # ubuntu 사용자에게 Docker 권한 부여
              usermod -aG docker ubuntu
              
              # 애플리케이션 디렉토리 생성
              mkdir -p /home/ubuntu/app
              chown ubuntu:ubuntu /home/ubuntu/app
              
              # 타임존 설정
              timedatectl set-timezone Asia/Seoul
              
              # 완료 로그
              echo "EC2 초기 설정 완료: $(date)" > /home/ubuntu/setup_complete.log
              EOF

  tags = {
    Name = "${var.project_name}-backend-server"
  }
}

# SSH 키 페어 (AWS에 이미 등록된 키 사용)
# ⚠️ AWS 콘솔에서 수동으로 생성한 키를 참조
# data 블록은 기존 리소스를 가져오는 용도
data "aws_key_pair" "main" {
  key_name = var.ec2_key_name
}
