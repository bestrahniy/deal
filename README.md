# deal servise

to run:
 - docker-compose up -d
 - go to lint http://localhost:8081/swagger-ui/index.html#/deal-controllers/saveDeal
 - in search line write: /api-docs
 - in method put deal/save paste:

{
  "description": "Кредит №12",
  "agreementNumber": "12/24",
  "agreementDate": "2025-06-30",
  "agreementStartDt": "2025-06-30T12:11:34.169Z",
  "availabilityDate": "2026-06-30",
  "typeId": "CREDIT",
  "sums": [
  {
      "sum": 10000.0,
      "currencyId": "RUB",
      "main": true
    }
  ],
  "contractors": [
    {
      "contractorId": "CTR-001",
      "name": "ООО Ромашка",
      "inn": "7700000000",
      "main": true
    }
  ]
}
