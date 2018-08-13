package com.github.kpavlov.txservice.it

import com.github.kpavlov.txservice.domain.AccountId
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import java.math.BigDecimal

object TestClient {

    init {
        RestAssured.baseURI = System.getProperty("sut.url", "http://localhost:8080")
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    fun getAccountDetails(accountId: AccountId): AccountDetails {
        return RestAssured
                .given()
                .log().uri()
                .get("/accounts/{accountId}", accountId)
                .then()
                .log().body()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().body().`as`(AccountDetails::class.java)
    }

    fun createAccount(initialBalance: BigDecimal): AccountId {
        val requestSpecification = RestAssured
                .given()

        val req = with(CreateAccountRequest()) {
            this.initialBalance = initialBalance
            this
        }

        val extract = requestSpecification
                .log().uri()
                .log().body()
                .body(req)
                .contentType(ContentType.JSON)
                .post("/accounts")
                .then()
                .log().headers()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()

        val result = extract.body().`as`(AccountDetails::class.java)

        return result.id
    }

    fun <T> createTransaction(amount: BigDecimal,
                              debitAccountId: AccountId,
                              creditAccountId: AccountId,
                              expectedHttpStatus: Int = HttpStatus.SC_CREATED,
                              expectedResponse: Class<T>): T {
        val requestSpecification = RestAssured
                .given()

        val req = with(CreateTransactionRequest()) {
            this.amount = amount
            this.debitAccount = debitAccountId
            this.creditAccount = creditAccountId
            this
        }

        val response = requestSpecification
                .log().uri()
                .log().body()
                .body(req)
                .contentType(ContentType.JSON)
                .post("/transactions")
                .then()
                .log().headers()
                .statusCode(expectedHttpStatus)
                .extract()
                .body().`as`(expectedResponse)

        return response
    }
}