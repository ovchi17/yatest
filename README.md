# Проект автотестов для сервиса petstore

---

## Стек
- Java 17
- Maven
- JUnit5
- RestAssured
- AssertJ
- Lombok
- Allure


## Запуск проекта

### Для Linux и Mac
В терминале последовательно выполните данные команды:
- git clone git@github.com:ovchi17/yatest.git
- cd yatest
- mvn clean test

Также можно открыть отчет в браузере командой: mvn allure:serve

### Для Windows
В командной строке последовательно выполните данные команды:
- git clone git@github.com:ovchi17/yatest.git
- cd yatest
- mvn clean test

Также можно открыть отчет в браузере командой: mvn allure:serve

---

### Примечание
Негативные тесты не реализованы, так как демо-сервер Petstore не валидирует запросы. Например, при отправке POST /pet возвращает 200 даже при пустом теле