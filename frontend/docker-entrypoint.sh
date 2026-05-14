#!/bin/sh
set -e
# Docker Compose : pas de PORT → 80. Cloud Run : PORT=8080 (non-root ne peut pas binder 80).
PORT="${PORT:-80}"
# Docker Compose (réseau interne) : http://backend:8080
# Cloud Run / GCP : https://<service-backend>-xx.a.run.app  (sans slash final)
BACKEND_BASE="${BACKEND_BASE:-http://backend:8080}"
sed -e "s|___BACKEND_BASE___|${BACKEND_BASE}|g" -e "s|___PORT___|${PORT}|g" \
  /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf
exec nginx -g "daemon off;"
