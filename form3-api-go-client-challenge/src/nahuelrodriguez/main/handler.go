package main

import (
	"log"
	"net/http"
)

func Handle(w http.ResponseWriter, r *http.Request) {
	log.Printf("Received request with HTTP method: '%s'", r.Method)
	switch r.Method {
	case http.MethodGet:
		Fetch(w, r)
	case http.MethodPost:
		Create(w, r)
	case http.MethodDelete:
		Delete(w, r)
	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}
