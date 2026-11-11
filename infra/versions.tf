terraform {
  required_version = ">= 1.5"

  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 2.2"
    }
  }
}

provider "mongodbatlas" {
  client_id     = var.client_id
  client_secret = var.client_secret
}
