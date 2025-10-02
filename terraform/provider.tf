# ============================================
# Terraform 및 Provider 설정
# ============================================

terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # 백엔드 설정 (선택사항: 팀 작업 시 S3 사용)
  # backend "s3" {
  #   bucket = "moci-3d-terraform-state"
  #   key    = "prod/terraform.tfstate"
  #   region = "ap-northeast-2"
  # }
}

# AWS Provider 설정
provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "MOCI-3D"
      Environment = var.environment
      ManagedBy   = "Terraform"
    }
  }
}
