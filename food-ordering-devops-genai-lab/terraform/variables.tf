variable "aws_region" {
  description = "AWS region for the practice EKS cluster"
  type        = string
  default     = "ap-south-1"
}

variable "cluster_name" {
  description = "Name of the EKS practice cluster"
  type        = string
  default     = "food-ordering-practice"
}

variable "kubernetes_version" {
  description = "Kubernetes version supported by EKS in the selected region"
  type        = string
  default     = "1.31"
}
