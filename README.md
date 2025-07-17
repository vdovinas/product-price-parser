# Start on local environment

1. start containers
- docker-compose up -d

2. apply migrations (optional)
- run plugin 'flyway:migrate' or 'flyway:clean + flyway:migrate' for full rebuild

### api description (swagger)
http://localhost:8080/product-price-parser/api/swagger-ui.html