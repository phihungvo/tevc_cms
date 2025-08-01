# Sử dụng node làm base image
FROM node:18 AS build

WORKDIR /app

# Copy mã nguồn ứng dụng vào container
COPY . .

# Inject biến môi trường trước khi build
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Cài đặt dependencies và build dự án
RUN npm install && npm run build

# Sử dụng nginx để phục vụ các file static
FROM nginx:alpine

# Copy các file build từ node vào nginx
COPY --from=build /app/build /usr/share/nginx/html

# Copy cấu hình nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf
