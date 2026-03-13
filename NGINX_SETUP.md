# Nginx Integration Setup Guide

This guide explains how to integrate and use nginx as a reverse proxy for your e-commerce Spring Boot application.

## Architecture

```
Client Requests
      ↓
   Nginx (Port 80/443)
      ↓
Spring Boot App (Port 8080)
      ↓
Database (H2)
```

## Prerequisites

- Docker and Docker Compose installed
- Docker running on your system (for Windows: Docker Desktop)

## Quick Start

### 1. Build and Start Services

```bash
docker-compose up -d
```

This command will:
- Build a Docker image for your Spring Boot application
- Start nginx container
- Start Spring Boot application container
- Create a shared network between containers

### 2. Access the Application

- **Main App**: http://localhost
- **H2 Console**: http://localhost/h2-console
- **API Endpoints**: http://localhost/api/*

### 3. Stop Services

```bash
docker-compose down
```

## Configuration

### Nginx Configuration Files

- **Location**: `./nginx/nginx.conf`
- **Key Features**:
  - Reverse proxy to Spring Boot application
  - Gzip compression for better performance
  - Static file caching (CSS, JS, images)
  - API route configuration
  - Request timeout handling
  - WebSocket support (Upgrade headers)

### Nginx Features Included

#### 1. Reverse Proxy
Routes all requests from port 80 to the Spring Boot application running on port 8080.

#### 2. Static File Caching
Static files (CSS, JS, images) are cached for 30 days with appropriate headers.

#### 3. Gzip Compression
Reduces bandwidth usage by compressing text-based responses.

#### 4. Request Headers
Forwards client IP and protocol information to Spring Boot:
- `X-Real-IP`: Client's real IP address
- `X-Forwarded-For`: Chain of IP addresses
- `X-Forwarded-Proto`: Original protocol (HTTP/HTTPS)

#### 5. Timeouts
Configured to handle long-running requests (60 seconds).

## Docker Services

### Spring Boot Application
- **Container Name**: ecommerce-app
- **Port**: 8080 (internal)
- **Health Check**: Checks `/health` endpoint every 30 seconds
- **Memory**: Limited to 512MB max, 256MB min

### Nginx
- **Container Name**: ecommerce-nginx
- **Port**: 80 (external)
- **Health Check**: Checks reverse proxy connectivity every 30 seconds

## Useful Docker Commands

```bash
# View running containers
docker ps

# View logs
docker logs ecommerce-app
docker logs ecommerce-nginx

# View combined logs
docker-compose logs -f

# Restart a service
docker-compose restart spring-app
docker-compose restart nginx

# Rebuild the Spring Boot image
docker-compose build --no-cache spring-app

# Enter container shell (for debugging)
docker exec -it ecommerce-app sh
docker exec -it ecommerce-nginx sh

# View container resource usage
docker stats

# Stop all services
docker-compose stop

# Remove all containers and networks
docker-compose down -v
```

## Development vs Production

### For Development
- Current setup is suitable for local development
- Nginx serves as a single entry point
- No SSL/TLS required

### For Production
1. **Enable HTTPS**:
   - Uncomment HTTPS server block in `nginx.conf`
   - Configure SSL certificates
   - Redirect HTTP to HTTPS

2. **Environment Configuration**:
   - Update `docker-compose.yml` environment variables
   - Configure production database (not H2)
   - Set appropriate Java memory limits

3. **Security**:
   - Add rate limiting in nginx
   - Configure CORS headers
   - Use environment-specific secrets

4. **Monitoring**:
   - Enable detailed logging
   - Set up log aggregation
   - Configure monitoring tools (Prometheus, Grafana)

## SSL/TLS Configuration

To enable HTTPS:

1. **Generate or obtain SSL certificates**:
   ```bash
   # Example: Self-signed for testing
   mkdir -p nginx/certs
   openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
     -keyout nginx/certs/key.pem \
     -out nginx/certs/cert.pem
   ```

2. **Update `nginx.conf`**:
   - Uncomment the HTTPS server block
   - Update `server_name` to your domain
   - Ensure certificate paths are correct

3. **Update `docker-compose.yml`**:
   - Uncomment port `443:443`
   - Uncomment volume mapping for certs

4. **Restart services**:
   ```bash
   docker-compose restart nginx
   ```

## Performance Tuning

### Nginx Optimizations
- Worker processes set to `auto` for optimal CPU utilization
- Connection pooling with upstream keepalive
- Gzip compression enabled
- Static file caching

### Spring Boot Optimizations
- Java heap memory: `-Xmx512m -Xms256m`
- H2 database runs in-memory (fast but not persistent)

### Further Improvements
1. Use a persistent database (PostgreSQL, MySQL)
2. Implement Redis caching layer
3. Add load balancing across multiple app instances
4. Configure CDN for static assets
5. Enable compression at Java level

## Troubleshooting

### Container won't start
```bash
# Check logs
docker logs ecommerce-app
docker logs ecommerce-nginx

# Rebuild and restart
docker-compose down
docker-compose up -d --build
```

### Cannot reach application
- Ensure Docker is running
- Check if ports 80 and 8080 are available
- Verify no firewall blocking
- Restart Docker: `docker-compose restart`

### Health checks failing
- Verify Spring Boot app is running: `docker logs ecommerce-app`
- Check if `/health` endpoint exists
- Increase `start_period` in docker-compose.yml if startup is slow

### Nginx showing 503 Bad Gateway
- Verify Spring Boot container is running: `docker ps`
- Check Spring Boot logs for errors
- Ensure containers are on same network: `docker network ls`

### Port already in use
```bash
# Find what's using port 80
# Windows: netstat -ano | findstr :80
# Linux/Mac: lsof -i :80

# Kill the process or use different port in docker-compose.yml
```

## Next Steps

1. **Database Persistence**: Migrate from H2 to PostgreSQL/MySQL for production
2. **Load Balancing**: Set up multiple Spring Boot instances behind nginx
3. **CI/CD Integration**: Automate building and deploying Docker images
4. **Monitoring**: Add container monitoring and application metrics
5. **Security**: Configure nginx authentication, rate limiting, WAF

## Additional Resources

- [Nginx Documentation](https://nginx.org/en/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
