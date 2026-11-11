output "mongodb_uri" {
  description = "Cadena de conexion lista para la variable MONGODB_URI"
  sensitive   = true
  value = replace(
    "${mongodbatlas_advanced_cluster.this.connection_strings.standard_srv}/franquicias?retryWrites=true&w=majority",
    "mongodb+srv://",
    "mongodb+srv://${var.db_username}:${var.db_password}@"
  )
}
