# syntax=docker/dockerfile:1

FROM golang:1.17.4-alpine

WORKDIR /app

COPY go.mod src/nahuelrodriguez/main/*.go ./

RUN go build -o form3-api-client *.go

EXPOSE 8081

CMD [ "./form3-api-client" ]
