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

## Frameworks/Libraries

* JDK 8+
* Kotlin 1.2 (1.2.60)
* Jersey2
* Jackson
* Open API 2.0 (Swagger)
* Maven 3
* JUnit 5
* Kotlin coroutines in unit test
* Rest-Assured

## Implementation notes

The implementation is eventually consistent.

Since we're living in _eventually consistent_ world - it is totally OK (IMHO).

> What we see with our eyes -- has happened at the moment 
the light has reflected from the surface we're looking for.
We see stars on the sky which are no longer exist!
>
> When we do HTTP (TCP) call we may receive out-of-date state because of TCP delays 
and unpredictable order of request processing on server (we can't control which request will be processed first).

The money transfer operation consists of 3 steps:

1. Holding transferred amount in debit account (`Account.hold(amount)`): 
   subtract the amount from available balance and add to hold amounts.
   This operation (method) is `synchronized`.
   Once the transferred amount is held -- the whole operation will complete eventually.
   
2. Adding transferred amount to credit account. 
   This method is not synchronized. 
   Money will become visible on credit amount eventually. 
     
3. Clearing: removing transferred amount from held amount 
   on debit account (`Account.withdrawHeldFunds(amount)`). 
   This method is not synchronized. 
   To be strict, this implementation does not require keeping track of held amounts 
   since held amount is not reported to the client, but I want to keep it for clarity. 

**Why not use two locks, one per account?** Because it is hard to guarantee lock ordering.
If strong consistency is the requirement, then I would prefer to execute transactions one at a time, 
e.g. by using [LMAX Disruptor](https://lmax-exchange.github.io/disruptor) 
or by submitting tasks to `Executors.newSingleThreadExecutor()`.

## Building and Running Service

To build and run server execute:

    mvn clean verify -B
    java -jar target/simple-transaction-service-exec.jar
    
or just run:

    ./build-and-run.sh

## Using API
    
You may run `./transfer-money.sh` script to test the API.    
    
1. Create account:
    
        curl -v --request POST \
            --data '{"initialBalance": 1005.00}' \
            --header "Content-Type: application/json" \
            http://localhost:8080/accounts
        
2. Get account details:

        curl -v "http://localhost:8080/accounts/$ACCOUNT_ID"
    
3. Transfer money:
    
        TRANSFER_REQUEST="
        {
            \"amount\": 15.00,
            \"debitAccount\":\"$DEBIT_ACCOUNT_ID\",
            \"creditAccount\":\"$CREDIT_ACCOUNT_ID\"
        }"
        curl -d "$TRANSFER_REQUEST" \
             -H "Content-Type: application/json" \
            http://localhost:8080/transactions

    
   