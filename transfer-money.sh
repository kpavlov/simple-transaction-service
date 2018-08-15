#!/usr/bin/env bash

BASE_URL="http://localhost:8080"

# Create debit account

DEBIT_ACCOUNT_ID=`curl -v -d '{"initialBalance": 1005.42}' \
     -H "Content-Type: application/json" \
    "$BASE_URL/accounts" | jq -c -r '.id'`

echo "Debit account created: $DEBIT_ACCOUNT_ID"

curl -v "$BASE_URL/accounts/$DEBIT_ACCOUNT_ID" | jq -C

# Create credit account

CREDIT_ACCOUNT_ID=`curl -v -d '{"initialBalance": 10}' \
     -H "Content-Type: application/json" \
    "$BASE_URL/accounts" | jq -c -r '.id'`

echo "Credit account created: $CREDIT_ACCOUNT_ID"

curl -v "$BASE_URL/accounts/$CREDIT_ACCOUNT_ID" | jq -C

echo "Transferring money"

TRANSFER_REQUEST="
{
    \"amount\": 15.00,
    \"debitAccount\":\"$DEBIT_ACCOUNT_ID\",
    \"creditAccount\":\"$CREDIT_ACCOUNT_ID\"
}"

curl -v -d "$TRANSFER_REQUEST" \
     -H "Content-Type: application/json" \
    "$BASE_URL/transactions"

echo "Debit account balance now:"
curl -v "$BASE_URL/accounts/$DEBIT_ACCOUNT_ID" | jq -C


echo "Credit account balance now:"
curl -v "$BASE_URL/accounts/$CREDIT_ACCOUNT_ID" | jq -C



