# stockDemo
Real time stock price update with Websockets and YahooFinance api on Play Scala

## To Run
You must have sbt 1.2.8 and node package manager

To build the react assets and push them to the ```public``` directory to be served by Play
```cd ./ui && npm run build```

To run the play application
```cd ../ && sbt run```

The application will be available on http://localhost:9000

## Inner workings

The application utilizes a websocket at http://localhost:9000/ws to stream the stock data from the YahooFinance API.


An actor fetches the stockdata and streams it to the websocket connection.

Events sent from the page are parsed as JSON and expect a JSON payload of
```json
   {
      "symbol": "symbol"
      "action": "add | remove"
   } 
```
Events are streamed out in the format:

```json
  {
    "symbol1": ["symbol": "symbol", "name": "name", "price": "price"],
    "symbol2": ["symbol": "symbol", "name": "name", "price": "price"]
  }
```
    
