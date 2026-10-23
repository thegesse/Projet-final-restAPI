#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/v1"
FAILED_CURLS=0
SUCCEEDED_CURLS=0

request() {
  local req_id="$1"
  local expected="$2"
  local method="$3"
  local path="$4"
  shift 4

  local actual
  actual=$(curl -s -o /dev/null -w '%{http_code}' \
    -X "$method" "$@" "$BASE_URL$path")

  if [ "$actual" = "$expected" ]; then
    printf '%s PASS expected=%s actual=%s\n' "$req_id" "$expected" "$actual"
    SUCCEEDED_CURLS=$((SUCCEEDED_CURLS + 1))
  else
    printf '%s FAIL expected=%s actual=%s\n' "$req_id" "$expected" "$actual"
    FAILED_CURLS=$((FAILED_CURLS + 1))
  fi
}

# Auteurs: R01–R12
request R01 200 GET "/auteurs"
request R02 200 GET "/auteurs/1"
request R03 404 GET "/auteurs/99"
request R04 201 POST "/auteurs" \
  -H "Content-Type: application/json" \
  -d '{"nom":"Vernes","prenom":"Jules","nationalite":"Française"}'
request R05 400 POST "/auteurs" \
  -H "Content-Type: application/json" \
  -d '{"nom":"","prenom":"Jules"}'
request R06 200 PUT "/auteurs/6" \
  -H "Content-Type: application/json" \
  -d '{"nom":"Verne","prenom":"Jules","nationalite":"Française"}'
request R07 404 PUT "/auteurs/99" \
  -H "Content-Type: application/json" \
  -d '{"nom":"Verne","prenom":"Jules","nationalite":"Française"}'
request R08 409 DELETE "/auteurs/1"
request R09 204 DELETE "/auteurs/5"
request R10 404 DELETE "/auteurs/5"
request R11 200 GET "/auteurs/1/livres"
request R12 404 GET "/auteurs/99/livres"

# Livres: R13–R28
request R13 200 GET "/livres?page=0&size=3&sort=prix,desc"
request R14 200 GET "/livres/1"
request R15 404 GET "/livres/99"
request R16 400 GET "/livres/abc"
request R17 201 POST "/livres" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Vingt Mille Lieues sous les mers","isbn":"978-2-253-00631-2","prix":7.9,"anneePublication":1870,"stock":2,"auteurId":6}'
request R18 409 POST "/livres" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Vingt Mille Lieues sous les mers","isbn":"978-2-253-00631-2","prix":7.9,"anneePublication":1870,"stock":2,"auteurId":6}'
request R19 400 POST "/livres" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Livre fantôme","isbn":"978-2-0000-0000-1","prix":10.0,"anneePublication":2020,"stock":1,"auteurId":99}'
request R20 400 POST "/livres" \
  -H "Content-Type: application/json" \
  -d '{"titre":"","isbn":"978-2-0000-0000-2","prix":-3,"stock":1,"auteurId":1}'
request R21 200 PUT "/livres/9" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Vingt Mille Lieues sous les mers","isbn":"978-2-253-00631-2","prix":8.5,"anneePublication":1870,"stock":2,"auteurId":6}'
request R22 409 PUT "/livres/9" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Vingt Mille Lieues sous les mers","isbn":"978-2-07-040850-4","prix":8.5,"anneePublication":1870,"stock":2,"auteurId":6}'
request R23 404 PUT "/livres/99" \
  -H "Content-Type: application/json" \
  -d '{"titre":"Livre inconnu","isbn":"978-2-0000-0000-3","prix":10.0,"anneePublication":2020,"stock":1,"auteurId":1}'
request R24 200 GET "/livres/recherche?titre=harry"
request R25 400 GET "/livres/recherche"
request R26 200 GET "/livres/disponibles"
request R27 400 PATCH "/livres/8/stock?quantite=0"
request R28 404 PATCH "/livres/99/stock?quantite=2"

# Emprunts et fin du scénario: R29–R46
request R29 409 POST "/emprunts" \
  -H "Content-Type: application/json" \
  -d '{"livreId":8,"emprunteur":"Alice Durand","emailEmprunteur":"alice.durand@email.com"}'
request R30 400 POST "/emprunts" \
  -H "Content-Type: application/json" \
  -d '{"livreId":99,"emprunteur":"Alice Durand","emailEmprunteur":"alice.durand@email.com"}'
request R31 400 POST "/emprunts" \
  -H "Content-Type: application/json" \
  -d '{"livreId":2,"emprunteur":"Alice Durand","emailEmprunteur":"alice.durand"}'
request R32 201 POST "/emprunts" \
  -H "Content-Type: application/json" \
  -d '{"livreId":2,"emprunteur":"Alice Durand","emailEmprunteur":"alice.durand@email.com"}'
request R33 200 GET "/livres/2"
request R34 200 GET "/emprunts/4"
request R35 404 GET "/emprunts/99"
request R36 200 GET "/emprunts/en-cours"
request R37 200 GET "/emprunts/en-retard"
request R38 200 PATCH "/emprunts/2/retour"
request R39 409 PATCH "/emprunts/2/retour"
request R40 404 PATCH "/emprunts/99/retour"
request R41 200 PATCH "/livres/8/stock?quantite=2"
request R42 201 POST "/emprunts" \
  -H "Content-Type: application/json" \
  -d '{"livreId":8,"emprunteur":"Paul Morel","emailEmprunteur":"paul.morel@email.com"}'
request R43 200 GET "/emprunts"
request R44 204 DELETE "/livres/9"
request R45 404 DELETE "/livres/9"
request R46 404 GET "/inexistant"

printf '\nSucceeded: %s | Failed: %s\n' "$SUCCEEDED_CURLS" "$FAILED_CURLS"

