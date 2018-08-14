package com.github.kpavlov.txservice.it

import com.github.kpavlov.txservice.domain.AccountId
import com.github.kpavlov.txservice.ws.model.AccountDetails
import com.github.kpavlov.txservice.ws.model.CreateAccountRequest
import com.github.kpavlov.txservice.ws.model.CreateTransactionRequest
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
                              expectedHttpStatus: Int = HttpStatus.SC_OK,
                              expectedResponse: Class<T>? = null): T {
        val requestSpecification = RestAssured
                .given()

        val req = with(CreateTransactionRequest()) {
            this.amount = amount
            this.debitAccount = debitAccountId
            this.creditAccount = creditAccountId
            this
        }

        val validatableResponse = requestSpecification
                .log().uri()
                .log().body()
                .body(req)
                .contentType(ContentType.JSON)
                .post("/transactions")
                .then()
                .log().headers()
                .statusCode(expectedHttpStatus)

        return if (expectedResponse !== null) {
            validatableResponse
                    .log().body()
                    .extract()
                    .body().`as`(expectedResponse)
        } else {
            null as T
        }
    }
}