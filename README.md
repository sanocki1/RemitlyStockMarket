# RemitlyStockMarket
## Created by Hubert Sanocki,

A simplified Spring Boot stock market app with:
- bank stock management
- wallet buy/sell transactions
- global error handling
- transaction logging
- a high-availability Docker setup with multiple app instances behind Nginx

## Requirements

- Docker Desktop
- Java 21 JDK only if you want to run it locally without Docker
- Docker Compose

## Run with Docker

Start the app on a custom local port (XXXX):

Windows:
```powershell
$env:APP_PORT=XXXX; docker compose up --build
```
Linux/Mac:
```bash
APP_PORT=XXXX docker compose up --build
```