# Proyecto de Atlas donde vive el cluster
resource "mongodbatlas_project" "this" {
  name   = "franquicias-api"
  org_id = var.org_id
}

# Cluster gratuito M0 (TENANT) sobre AWS
resource "mongodbatlas_advanced_cluster" "this" {
  project_id   = mongodbatlas_project.this.id
  name         = "franquicias-cluster"
  cluster_type = "REPLICASET"

  replication_specs = [
    {
      region_configs = [
        {
          provider_name         = "TENANT"
          backing_provider_name = "AWS"
          region_name           = "US_EAST_1"
          priority              = 7
          electable_specs = {
            instance_size = "M0"
          }
        }
      ]
    }
  ]
}

# Usuario de la API: solo lectura/escritura sobre su base (minimo privilegio)
resource "mongodbatlas_database_user" "app" {
  project_id         = mongodbatlas_project.this.id
  username           = var.db_username
  password           = var.db_password
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = "franquicias"
  }
}

# IPs con acceso al cluster
resource "mongodbatlas_project_ip_access_list" "app" {
  project_id = mongodbatlas_project.this.id
  cidr_block = var.allowed_cidr
  comment    = "Acceso de la API"
}
