variable "client_id" {
  description = "Client ID de la Service Account de Atlas"
  type        = string
}

variable "client_secret" {
  description = "Client Secret de la Service Account de Atlas"
  type        = string
  sensitive   = true
}

variable "org_id" {
  description = "ID de la organizacion de Atlas"
  type        = string
}

variable "db_username" {
  description = "Usuario de la base de datos que usara la API"
  type        = string
  default     = "franquicias_app"
}

variable "db_password" {
  description = "Contrasena del usuario de la base de datos (solo letras y numeros)"
  type        = string
  sensitive   = true
}

variable "allowed_cidr" {
  description = "Rango de IPs que pueden conectarse al cluster"
  type        = string
  default     = "0.0.0.0/0"
}
