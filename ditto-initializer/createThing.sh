#!/bin/sh

curl -X 'PUT' \
  "${DITTO_URL}/api/2/things/wodt%3Ahealthcareuser" \
  -H 'accept: application/json' \
  -H 'Authorization: Basic '$(echo -n ${DITTO_USERNAME}:${DITTO_PASSWORD} | base64)'' \
  -H 'Content-Type: application/json' \
  -d '{
    "definition": "https://gist.githubusercontent.com/AndreaGiulianelli/81f3bb75ee863aa51c244cc133a1a5ea/raw/e53018f2cc21d3579806f716edca13561923e0f8/healthcareUserDittoThingTM.tm.jsonld",
    "attributes": {
      "taxId": "DOEJHN85L15H123K",
      "name": "John",
      "surname": "Doe",
      "birthday": "1985-07-15"
    },
    "features": {
    }
  }'