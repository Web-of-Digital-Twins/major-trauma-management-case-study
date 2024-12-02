FROM alpine:3.14
WORKDIR /app
COPY . .
COPY createThing.sh /app/createThing.sh
RUN apk --no-cache add curl
RUN chmod +x /app/createThing.sh

CMD ["/app/createThing.sh"]