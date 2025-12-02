ARG NODE_VERSION=25-alpine
ARG NGINX_VERSION=alpine3.22

# ===================
# STAGE 1: Build
# ===================
FROM node:${NODE_VERSION} AS builder

WORKDIR /app

COPY package.json package-lock.json ./

# install required dependencies
RUN --mount=type=cache,target=/root/.npm npm ci

COPY . .

RUN npm run build

# ===================
# STAGE 2: Prepare Nginx to serve static App
# ===================

FROM nginxinc/nginx-unprivileged:${NGINX_VERSION} AS runner

USER nginx

COPY nginx.conf /etc/nginx/nginx.conf

COPY --chown=nginx:nginx --from=builder /app/dist/*/browser /usr/share/nginx/html

EXPOSE 4200

ENTRYPOINT ["nginx", "-c", "/etc/nginx/nginx.conf"]
CMD ["-g", "daemon off;"]