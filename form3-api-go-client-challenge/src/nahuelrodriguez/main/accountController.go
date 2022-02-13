package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"
)

const url = "http://accountapi:8080/v1/organisation/accounts"
const apiErrorMsg = "Error calling API Server: "

func Create(w http.ResponseWriter, r *http.Request) {
	log.Printf("Creating account")

	var unsafeAccountData Account
	err := json.NewDecoder(r.Body).Decode(&unsafeAccountData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	safeAccountData, err := json.Marshal(unsafeAccountData)
	onErrorLogPanic(err, "Error parsing unsafe data into a JSON object: ")

	resp, err := http.Post(url, "application/json", bytes.NewBuffer(safeAccountData))
	onErrorLogPanic(err, apiErrorMsg)
	body, err := io.ReadAll(resp.Body)
	closeResponseReader(resp)
	w.Header().Add("Content-Type", "application/vnd.api+json")
	w.WriteHeader(resp.StatusCode)
	writeToResponseBody(w, err, body)
}

func Fetch(w http.ResponseWriter, r *http.Request) {
	accountId := strings.TrimPrefix(r.URL.Path, "/accounts/")
	log.Printf("Fetching account with ID '" + accountId + "'")

	resp, err := http.Get(url + "/" + accountId)
	onErrorLogPanic(err, apiErrorMsg)
	body, err := io.ReadAll(resp.Body)
	closeResponseReader(resp)
	w.Header().Add("Content-Type", "application/vnd.api+json")
	writeToResponseBody(w, err, body)
}

func Delete(w http.ResponseWriter, r *http.Request) {
	accountId := strings.TrimPrefix(r.URL.Path, "/accounts/")
	version := r.URL.Query().Get("version")
	log.Printf(fmt.Sprintf("Deleting account with ID: '%s' and version: '%s'", accountId, version))

	req, err := http.NewRequest("DELETE", fmt.Sprintf("%s/%s?version=%s", url, accountId, version), nil)
	onErrorLogPanic(err, "Error creating the DELETE request!")

	client := &http.Client{}
	resp, err := client.Do(req)
	onErrorLogPanic(err, apiErrorMsg)
	_, err = io.ReadAll(resp.Body)
	closeResponseReader(resp)
	onErrorLogPanic(err, apiErrorMsg)
	w.WriteHeader(http.StatusNoContent)
}

func onErrorLogPanic(err error, msg string) {
	if err != nil {
		log.Panic(msg, err)
	}
}

func closeResponseReader(resp *http.Response) {
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		onErrorLogPanic(err, "Error closing response reader: ")
	}(resp.Body)
}

func writeToResponseBody(w http.ResponseWriter, err error, body []byte) {
	_, err = fmt.Fprintf(w, string(body))
	onErrorLogPanic(err, "Error writing to response body: ")
}
