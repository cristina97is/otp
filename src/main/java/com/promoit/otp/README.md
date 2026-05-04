# OTP Service — Promo IT

## Описание

OTP Service — backend-приложение для защиты пользовательских операций с помощью одноразовых (OTP) кодов.

Проект реализован в рамках задания Promo IT.  
Сервис позволяет:

- регистрировать пользователей;
- выполнять авторизацию;
- генерировать OTP-коды;
- отправлять их через разные каналы;
- подтверждать операции с помощью OTP.

---

## Стек технологий

- Java 17  
- Spring Boot (Spring MVC)  
- Maven  
- PostgreSQL 17  
- JDBC  
- JWT (аутентификация)  
- Docker / Docker Compose  
- Jakarta Mail  
- Telegram Bot API  
- SMPP / SMPPsim  
- SLF4J (логирование)

---

## Основной функционал

- регистрация пользователей  
- авторизация (JWT)  
- роли: `ADMIN`, `USER`  
- запрет регистрации второго администратора  
- генерация OTP-кодов  
- валидация OTP-кодов  
- статусы OTP:
  - ACTIVE  
  - EXPIRED  
  - USED  
- автоматическое истечение OTP  
- настройка длины и TTL OTP  
- отправка OTP через:
  - Telegram  
  - Email  
  - SMS (SMPP emulator)  
- сохранение OTP в файл  
- логирование всех операций  
- разграничение доступа по ролям  

---

## Структура проекта

src/main/java/com/promoit/otp
├── controller
├── service
├── dao
├── model
├── security
├── notification
└── scheduler

---

## База данных

Используется PostgreSQL 17.

Таблицы:

- users  
- otp_config  
- otp_codes  

---

## Быстрый запуск

docker compose up --build

API: http://localhost:8081

---

## API

POST /api/auth/register
POST /api/auth/login
POST /api/user/otp/generate
POST /api/user/otp/validate

---


## Telegram интеграция

В проекте реализована автоматическая отправка OTP-кодов через Telegram.

Используется бот:
@otp_service_chris_bot

Интеграция уже настроена и работает без дополнительной конфигурации.

После генерации OTP-кода сервис автоматически отправляет сообщение пользователю в Telegram.

Пример сообщения:

"user, your confirmation code is: 166766"

Используется внешний API — Telegram Bot API.
## Пример использования Telegram

POST /api/user/otp/generate

{
"operationId": "tg_test",
"sendEmail": false,
"sendSms": false,
"sendTelegram": true,
"saveToFile": false
}

Результат:
OTP-код автоматически приходит в Telegram-бота. 

При необходимости параметры бота могут быть изменены в:

src/main/resources/telegram.properties

---
## Email

Настройки находятся в:

src/main/resources/email.properties

Пример:

email.username=your_email
email.password=app_password
email.from=your_email

mail.smtp.host=smtp
mail.smtp.port=587
---
## SMS / SMPP

Используется SMPP emulator (например SMPPsim)

Файл:

src/main/resources/sms.properties

smpp.host=localhost
smpp.port=2775
smpp.system_id=smppclient1
smpp.password=password
__
## Как протестировать
зарегистрировать пользователя
выполнить логин
получить token
сгенерировать OTP
получить код (файл или Telegram)
проверить OTP
---
## Логи

логируются:
запросы API
генерация OTP
отправка OTP
ошибки
истечение кодов
---
## Итог

Проект реализует сервис OTP:

регистрация
авторизация
генерация OTP
валидация OTP
разные каналы доставки
логирование
роли пользователей