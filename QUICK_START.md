# Быстрый запуск

## 1. Запуск

```bash
docker compose up --build
```

После запуска приложение доступно здесь:

```text
http://localhost:8080
```

PostgreSQL 17 поднимается автоматически:

```text
host: localhost
port: 5432
database: otp_service
user: postgres
password: postgres
```

## 2. Проверка через файл

Коды будут сохраняться в файл:

```text
otp-codes.txt
```

## 3. Готовые запросы

Открой файл:

```text
requests.http
```

И выполни запросы по порядку:

1. Register admin
2. Register user
3. Login user
4. Generate OTP to file
5. Validate OTP
6. Login admin
7. Admin endpoints

