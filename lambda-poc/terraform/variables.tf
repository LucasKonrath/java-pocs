variable "region" {
  description = "AWS region to deploy to"
  type        = string
  default     = "us-east-1"
}

variable "function_name" {
  description = "Lambda function name"
  type        = string
  default     = "lambda-poc-hello"
}

variable "handler" {
  description = "Fully qualified handler in the form package.Class::method"
  type        = string
  default     = "org.example.lambdapoc.HelloLambdaHandler::handleRequest"
}

variable "runtime" {
  description = "Lambda runtime"
  type        = string
  default     = "java17"
}

variable "architecture" {
  description = "CPU architecture for Lambda (x86_64 or arm64)"
  type        = string
  default     = "x86_64"
  validation {
    condition     = contains(["x86_64", "arm64"], var.architecture)
    error_message = "architecture must be one of: x86_64, arm64"
  }
}

variable "memory_size" {
  description = "Lambda memory size in MB"
  type        = number
  default     = 512
}

variable "timeout" {
  description = "Lambda timeout in seconds"
  type        = number
  default     = 10
}

variable "artifact_path" {
  description = "Path to the shaded JAR file built by Gradle (relative to Terraform working dir)"
  type        = string
  default     = "../build/libs/lambda-poc-0.0.1-SNAPSHOT-all.jar"
}

variable "environment" {
  description = "Environment variables for the Lambda function"
  type        = map(string)
  default     = {}
}

variable "tags" {
  description = "Tags to apply to created resources"
  type        = map(string)
  default = {
    Project   = "lambda-poc"
    ManagedBy = "terraform"
  }
}

variable "log_retention_days" {
  description = "CloudWatch Logs retention in days"
  type        = number
  default     = 7
}

