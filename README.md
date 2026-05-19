# RemitlyStockMarket
## Created by Hubert Sanocki,

A simplified Spring Boot stock market app with:
- bank stock management
- wallet buy/sell transactions
- global error handling
- transaction logging
- a high-availability Docker setup with multiple app instances behind Nginx

## Requirements

- Docker/Docker Desktop

## Run with Docker

Start the app on a local port 8080:

```powershell
docker compose up --build
```

## Shut down the app

```bash
docker compose down
```

## Access via Swagger UI
Open your browser and navigate to:  
```
http://localhost:8080/swagger-ui.html
```