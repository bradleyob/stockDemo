import React, {Component} from 'react';
import './App.css';
import StockList from './StockList'
import StockUpdateContainer from './StockUpdateContainer'

const URL = 'ws://localhost:9000/ws'

class App extends Component {
    constructor(props) {
        super(props)
        this.state = {
            stockData: [],
            currentItem: {symbol: ""},
        }
    }
    ws = new WebSocket(URL)

    componentDidMount() {
        this.ws.onopen = () => {
            console.log("Got our websocket connection")
        }

        this.ws.onmessage = event =>{
            const stockData = JSON.parse(event.data)
            this.updateStocks(stockData)
            this.render(<StockUpdateContainer stockData={this.state.stockData} removeStock={this.removeStock}/>)
        }

        this.ws.onclose = () => {
            console.log("Websocket connection closed")
            this.setState({
                ws: new WebSocket(URL)
            })
        }
    }

    addStock = e => {
        e.preventDefault()
        const stockSymbol = this.state.currentItem
        if (stockSymbol.text !== '') {
            this.ws.send(JSON.stringify(stockSymbol))
        }
    }

    updateStocks = stocks => {
        this.setState(state => ({stockData: stocks}))
    }

    handleInput = e => {
        const itemText = e.target.value
        const currentItem = { symbol: itemText, action: "add"}
        this.setState({ currentItem, })
    }

    removeStock = stock => {
        console.log("Removing " + stock)
        this.ws.send(JSON.stringify({"symbol": stock, "action": "remove"}))
    }

    inputElement = React.createRef()

    render() {
        return (
            <div className="App">
               <StockList
                   addStock={this.addStock}
                   inputElement={this.inputElement}
                   handleInput={this.handleInput}
                   currentItem={this.state.currentItem}
               />
               <StockUpdateContainer stockData={this.state.stockData} removeStock={this.removeStock}/>
            </div>
        )
    }
}

export default App;
