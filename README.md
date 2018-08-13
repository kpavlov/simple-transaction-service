Simple Transaction Service 
=======================
Simple RESTful​ ​API​ ​for​ ​money transfers​ ​between​ ​accounts.

[![Build Status](https://travis-ci.org/kpavlov/simple-transaction-service.svg?branch=master)](https://travis-ci.org/kpavlov/simple-transaction-service)
[![codecov](https://codecov.io/gh/kpavlov/simple-transaction-service/branch/master/graph/badge.svg)](https://codecov.io/gh/kpavlov/simple-transaction-service)

## Simplifications

Following assumptions were made for the sake of simplicity:
 - All accounts are using the same currency. Currency conversion is not supported. Currency is ommited in APIs and model.
 - Transaction history is not required at the moment. So, there is only **create** operation for transacitons and no **get** operations.
 - Money transfer is synchronous operation. Delayed and distributed (to third-party banks) transactions are not supported.
