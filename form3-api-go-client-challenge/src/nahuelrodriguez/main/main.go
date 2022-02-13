package main

import (
	"log"
	"net/http"
	"strconv"
)

func main() {
	port := 8081
	log.Print("Starting a HTTP request listener on port: ", port)
	http.HandleFunc("/accounts", Handle)
	http.HandleFunc("/accounts/", Handle)
	log.Fatal(http.ListenAndServe(":"+strconv.Itoa(port), nil))
}
