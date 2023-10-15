# Сервис для поддержки банковских счетов

Доступные API:
- POST /api/accounts - создание банковского счета. Для создания необходимо передать имя пользователя и пин-код состоящий из 4х цифр.
- GET /api/accounts - подучение списка всех счетов (имя пользователя и баланс счета).
- PUT /api/accounts/{accountName}/deposit?amount={amount} - API пополнения банковского счета.
- PUT /api/accounts/{accountName}/withdraw?amount={amount}&pinCode={pinCode} - API снятия денег с банковского счета.
- POST /api/accounts/{sourceAccountName}/transfer/{targetAccountName}?amount={amount} - API перевода денег между счетами.
