#!/bin/sh
set -e
# Docker Compose (réseau interne) : http://backend:8080
# Cloud Run / GCP : https://<service-backend>-xx.a.run.app  (sans slash final)
BACKEND_BASE="${BACKEND_BASE:-http://backend:8080}"
sed "s|___BACKEND_BASE___|${BACKEND_BASE}|g" /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf
exec nginx -g "daemon off;"
